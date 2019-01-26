package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.*;
import org.w3c.dom.Text;


public class Chat extends Activity
{
	final String TAG = "qwertf";
	Button sendButton;
	EditText messageBox;
	TextView responseBox;
	long assignedClientID = -100;
	String chatName;		//who the messages sent from this chat will be addressed to
	LinearLayout linearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);
		Intent intent = getIntent();
		assignedClientID = intent.getIntExtra("assignedClientID", -3);		//assigned at the registration stage
		LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("messageReceived"));
	
		chatName = intent.getStringExtra("chatName");
		linearLayout = findViewById(R.id.chatScreen);
		messageBox = findViewById(R.id.messageBox);
		sendButton = findViewById(R.id.sendButton);
		responseBox = findViewById(R.id.responseTextbox);
		responseBox.setText("Waiting");
	
		
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Toast.makeText(getBaseContext(), "Sending"+messageBox.getText().toString(), Toast.LENGTH_LONG).show();
				new SendMessageAsync().execute("5", String.valueOf(assignedClientID), chatName, messageBox.getText().toString());
			}
		});
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent ) {
			String data = intent.getStringExtra("message");
			addNewMsg(data);
		}
	};
	private void addNewMsg(String msgText)
	{
		TextView newMsg= new TextView(this);
		newMsg.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		
		newMsg.setText(msgText);
		linearLayout.addView(newMsg);
	}
	@Override
	protected void onDestroy()
	{
		LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
}
