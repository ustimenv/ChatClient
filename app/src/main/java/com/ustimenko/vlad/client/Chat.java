package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class Chat extends Activity implements MessageResultReceiver.Receiver
{
	final String TAG = "qwertf";
	Button sendButton;
	EditText messageBox;
	TextView responseBox;
	long clientID = -100;
	
	public MessageResultReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);
		Intent intent = getIntent();
		clientID = intent.getLongExtra("assignedClientID", -3);		//assigned at the registration stage
		
		messageBox = findViewById(R.id.messageBox);
		sendButton = findViewById(R.id.sendButton);
		responseBox = findViewById(R.id.responseTextbox);
		responseBox.setText("Waiting");
		
		receiver = new MessageResultReceiver(new Handler());
		receiver.setReceiver(this);
		startIntent();
		
		sendButton.setOnClickListener(new View.OnClickListener() {
		@Override
			public void onClick(View v)
			{
				Toast.makeText(getBaseContext(), "Sending"+messageBox.getText().toString(), Toast.LENGTH_LONG).show();
				new SendMessageAsync().execute("4", String.valueOf(clientID), String.valueOf(clientID), messageBox.getText().toString());
			}
		});
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle result)
	{
		Log.i(TAG, "Received result");
		Toast.makeText(getBaseContext(),"_" + result.getString("message"),Toast.LENGTH_LONG).show();
		responseBox.setText(result.getString("message", "defaultValue"));
		startIntent();
	}

	private void startIntent()
	{
		Intent intent = new Intent(this, ReceiverService.class);
		intent.putExtra("receiver", receiver);
		startService(intent);
	}
}
