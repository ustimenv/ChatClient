package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.StringTokenizer;

public class RegisterActivity extends Activity implements MessageResultReceiver.Receiver
{
	Button 	 sendButton;
	EditText usernameBox;
	EditText passwordBox;
	TextView registrationFeedback;
	
	MessageResultReceiver 	receiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);
		
		sendButton = findViewById(R.id.register_send_button);
		usernameBox = findViewById(R.id.register_username_input_box);
		passwordBox = findViewById(R.id.register_password_input_box);
		registrationFeedback = findViewById(R.id.register_registration_feedback);
		
		sendButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initReceiver();
				Toast.makeText(getBaseContext(), "Registering...", Toast.LENGTH_LONG).show();
				new SendMessageAsync(setupSSL()).execute("2", usernameBox.getText().toString(), passwordBox.getText().toString());
			}
		});
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle result)
	{
		String message = result.getString("message");
		StringTokenizer st = new StringTokenizer(message, "#");
		char flag = st.nextToken().charAt(0);

		switch(flag)
		{
			case '3':						//REGISTRATION_ACK
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);	//now the client has to log in, be it with the newly registered credentials or old
				intent.putExtra("assignedClientID", -1);
				finish();
				startActivity(intent);
				break;
			case '4':						//REGISTRATION_NAK
				registrationFeedback.setText("Username taken, try another!");
				initReceiver();
				break;

		}
	}
	private SSLContext setupSSL()
	{
		try {
			char[] password= "123456".toCharArray();
			KeyStore ksTrust = KeyStore.getInstance("BKS");
			ksTrust.load(getApplicationContext().getResources().openRawResource(R.raw.cert_1), password);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			tmf.init(ksTrust);

			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
			return sslContext;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private void initReceiver()
	{
		receiver = new MessageResultReceiver(new Handler());
		receiver.setReceiver(this);
		Intent intent = new Intent(this, ReceiverService.class);
		intent.putExtra("receiver", receiver);
		startService(intent);
	}
	
}

