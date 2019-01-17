package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.StringTokenizer;

public class LoginActivity extends Activity implements MessageResultReceiver.Receiver
{
	Button 	 						sendButton;
	EditText 						usernameBox;
	EditText 						passwordBox;
	TextView 						loginMessageBox;
	Button 			 				registerButton;
	public MessageResultReceiver 	receiver;
	private long 					assignedClientID;
	
	private void initReceiver()
	{
		Intent intent = new Intent(this, ReceiverService.class);
		intent.putExtra("receiver", receiver);
		startService(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		
		sendButton = findViewById(R.id.sendButton);
		usernameBox = findViewById(R.id.usernameBox);
		passwordBox = findViewById(R.id.passwordBox);
		loginMessageBox = findViewById(R.id.loginMessageBox);
		registerButton = findViewById(R.id.registerButton);
		receiver = new MessageResultReceiver(new Handler());
		receiver.setReceiver(this);
		
		initReceiver();
		
		sendButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Toast.makeText(getBaseContext(), "Sending", Toast.LENGTH_LONG).show();
				new SendMessageAsync().execute("1", usernameBox.getText().toString(), passwordBox.getText().toString(), String.valueOf(assignedClientID));
			}
		});
		registerButton.setOnClickListener(new View.OnClickListener()
		{
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
	public void onReceiveResult(int resultCode, Bundle result)
	{
		String message = result.getString("message");
		switch(message.charAt(0))
		{
			case '1':											//login successful
				Log.i("1", "login acknowledged");
				loginMessageBox.setText(message);
				Intent intent = new Intent(LoginActivity.this, ChatList.class);
				intent.putExtra("assignedClientID", assignedClientID);
				finish();
				startActivity(intent);
				break;
			case '2':											//login unsuccessful, continue listening in login activity
				initReceiver();
				loginMessageBox.setText("Attempts left:" + message.charAt(2));
				break;
			case '3':											//registration message
				Log.i("1", "register received");
				StringTokenizer st = new StringTokenizer(message, "#");
				st.nextToken();
				assignedClientID = Integer.valueOf(st.nextToken());		//will consequently be used to 'sign' messages from this particular client
				initReceiver();
				break;
			default:
				Log.i("1", "Unrecognisable flag " + message.charAt(0));
		}
	}
}
