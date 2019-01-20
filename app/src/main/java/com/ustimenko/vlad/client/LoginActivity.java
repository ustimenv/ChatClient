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
	private int						assignedClientID = -1;
	
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
		
		assignedClientID = getIntent().getIntExtra("assignedClientID", 0);
		
		sendButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initReceiver();				//start listening for the response
				Toast.makeText(getBaseContext(), "Logging in...", Toast.LENGTH_LONG).show();
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
		Log.d("1", "Message"+message+">>");
		
		switch(message.charAt(0))
		{
			case '1':															//login successful
				loginMessageBox.setText(String.valueOf(assignedClientID));
				Intent intent = new Intent(LoginActivity.this, ChatList.class);
				Log.i("1", "In login ID:"+String.valueOf(assignedClientID));
				intent.putExtra("assignedClientID", assignedClientID);
				finish();
				startActivity(intent);
				break;
			case '2':															//login unsuccessful, continue listening in login activity
				initReceiver();
				loginMessageBox.setText("Attempts left:" + message.charAt(2));
				break;
			case '4':															//TODO registration unsuccessful
				break;
			default:
				Log.i("1", "Unrecognisable flag " + message.charAt(0));
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
