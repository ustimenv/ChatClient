package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.StringTokenizer;

public class RegisterActivity extends Activity implements MessageResultReceiver.Receiver
{
	Button 	 sendButton;
	EditText usernameBox;
	EditText passwordBox;
	TextView registrationFeedback;
	
	public MessageResultReceiver 	receiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);
		
		sendButton = findViewById(R.id.sendButton);
		usernameBox = findViewById(R.id.usernameBox);
		passwordBox = findViewById(R.id.passwordBox);
		registrationFeedback = findViewById(R.id.registrationFeedback);
		
		sendButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initReceiver();
				Toast.makeText(getBaseContext(), "Registering...", Toast.LENGTH_LONG).show();
				new SendMessageAsync().execute("2", usernameBox.getText().toString(), passwordBox.getText().toString());
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle result)
	{
		String message = result.getString("message");
		//TODO registration denied,should the name be taken
//		switch(message.charAt(0))
//		{
//			case '3'://registration successful
//		}
		StringTokenizer st = new StringTokenizer(message, "#");
		st.nextToken();
		
		int assignedClientID = Integer.valueOf(st.nextToken());
		
		Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
		intent.putExtra("assignedClientID", assignedClientID);
		finish();
		startActivity(intent);
		Log.d("1", "Message"+message+">>");
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

