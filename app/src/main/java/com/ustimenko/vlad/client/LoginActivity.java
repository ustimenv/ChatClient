package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.*;

public class LoginActivity extends BaseActivity
{
	Button sendButton;
	EditText usernameBox;
	EditText passwordBox;
	TextView loginMessageBox;
	Button registerButton;
	private int assignedClientID = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		generateAndStoreKeypair();
		sendButton = findViewById(R.id.login_send_button);
		usernameBox = findViewById(R.id.login_username_input_box);
		passwordBox = findViewById(R.id.login_password_input_box);
		loginMessageBox = findViewById(R.id.login_login_feedback);
		registerButton = findViewById(R.id.login_register_button);
		
//		prefs = getApplicationContext().getSharedPreferences("ChatPreferences", MODE_PRIVATE);
		
		
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				initReceiver();                                        //start listening for the response
				Toast.makeText(getBaseContext(), "Logging in...", Toast.LENGTH_LONG).show();
				new SendMessageAsync(LoginActivity.super.sslContext)
						.execute("1", usernameBox.getText().toString(), passwordBox.getText().toString(), String.valueOf(assignedClientID));
			}
		});
		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Toast.makeText(getBaseContext(), "Lets register!", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
//		assignedClientID = prefs.getInt("ID", -1);                        //retrieve previous session's login details
		assignedClientID = getIntent().getIntExtra("assignedClientID", assignedClientID);        //if the client had previosouly registered as a new user, overwrite the shared preferences field
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle result)
	{
		String message = result.getString("message");
		Log.d("1", "Message" + message + ">>");
		
		switch (message.charAt(0)) {
			case '1':                                                            //login successful
				loginMessageBox.setText(String.valueOf(assignedClientID));
				Intent intent = new Intent(LoginActivity.this, Home.class);
				intent.putExtra("assignedClientID", assignedClientID);
				finish();
				startActivity(intent);
				break;
			
			case '2':    //LOGIN_NAK
				initReceiver();
//				loginMessageBox.setText("Attempts left:" + message.charAt(2));
				break;
			case '4':                                                            //TODO registration unsuccessful
				break;
			default:
				Log.i("1", "Unrecognisable flag " + message.charAt(0));
		}
	}
	
	private void generateAndStoreKeypair()
	{
		final String myKeyAlias = "mykey2";
		try
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(
					KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
			
			kpg.initialize(new KeyGenParameterSpec.Builder(
					myKeyAlias,
					KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
//					.setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
					.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
					.setKeySize(2048)
					.build());
			
			KeyPair keyPair = kpg.generateKeyPair();
			
		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e)
		{
			e.printStackTrace();
		}
	}
}