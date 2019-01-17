package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.*;
import org.w3c.dom.Text;

public class ChatList extends FragmentActivity
{
	Button addNewConvo;		//
	LinearLayout l;
	FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_list_screen);
		fragmentManager = getSupportFragmentManager();
		
		l = findViewById(R.id.chat_list_screen);
		addNewConvo = findViewById(R.id.addConversation);
		
		
//		chatButton.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Toast.makeText(getBaseContext(), "On to the chat screen", Toast.LENGTH_LONG).show();
//				Intent intent = new Intent(ChatList.this, Chat.class);
//				long assignedClientID = intent.getLongExtra("assignedClientID", -1);
//
//				startActivity(intent);
//			}
//		});
//		addNewConvo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				TextView newChat = addConvoView();
			}
		})
		
	}
	private void addConvoView()
	{
		EditText newChat = new EditText(this);
		newChat.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		
		l.addView(newChat);
	}
}
