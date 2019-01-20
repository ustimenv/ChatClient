package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaDataSource;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatList extends Activity implements MessageResultReceiver.Receiver
{
	Button addNewConvo;
	LinearLayout l;
	FragmentManager fragmentManager;
	EditText chatName;
	String chatNameStr;
	HashMap <String, ArrayList<Message>> inbox = new HashMap<String, ArrayList<Message>>();
	int assignedClientID;
	public MessageResultReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_list_screen);
		
		l = findViewById(R.id.chat_list_screen);
		addNewConvo = findViewById(R.id.addConversation);
		chatName = findViewById(R.id.newConversationName);
		
		receiver = new MessageResultReceiver(new Handler());
		receiver.setReceiver(this);
		startIntent();
		
		Intent initiatingIntent = getIntent();
		assignedClientID = initiatingIntent.getIntExtra("assignedClientID", -2);
		Toast.makeText(getBaseContext(),"ID:" + assignedClientID,Toast.LENGTH_LONG).show();
		
		
		addNewConvo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Toast.makeText(getBaseContext(),"Adding chat",Toast.LENGTH_LONG).show();
				chatNameStr = chatName.getText().toString();
				addConvoView();
			}
		});
	}
	
	private void addConvoView()
	{
		Button newChat = new Button(this);
		newChat.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		
		newChat.setText(chatNameStr);
		
		
		newChat.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v2)
			{
				Intent intent = new Intent(ChatList.this, Chat.class);
				intent.putExtra("assignedClientID", assignedClientID);
				intent.putExtra("chatName", chatNameStr);
				startActivity(intent);
			}
		});
		l.addView(newChat);
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle result)
	{
		Log.i("1", "Received result");
		Toast.makeText(getBaseContext(),"_" + result.getString("message"),Toast.LENGTH_LONG).show();
		startIntent();
		Intent broadcastIntent = new Intent("messageReceived");
		broadcastIntent.putExtra("message", result.getString("message"));
		LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
		
	}
	private void startIntent()
	{
		Intent intent = new Intent(this, ReceiverService.class);
		intent.putExtra("receiver", receiver);
		startService(intent);
	}
}
