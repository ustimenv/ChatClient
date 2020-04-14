package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Home extends BaseActivity
{
	private Button postButton;
	private ConstraintLayout layout;
	private EditText tweetInput;
	private LinearLayout tweetsLayout;
	
	private int assignedClientID = -2;
	private MessageResultReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		Intent initiatingIntent = getIntent();
		assignedClientID = initiatingIntent.getIntExtra("assignedClientID", -2);
		Toast.makeText(getBaseContext(),"ID:" + assignedClientID, Toast.LENGTH_LONG).show();
		
		layout = findViewById(R.id.home_layout_prime);
		postButton = findViewById(R.id.home_post_button);
		tweetInput = findViewById(R.id.home_tweet_input);
		tweetsLayout = findViewById(R.id.home_tweets_layout_secondary);
		
		initReceiver();
		
		postButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Toast.makeText(getBaseContext(),"Postin' the tweet!",Toast.LENGTH_LONG).show();
				new SendMessageAsync(Home.super.sslContext).execute("3", tweetInput.getText().toString());
			}
		});
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle result)
	{
		String message = result.getString("message");
		StringTokenizer st = new StringTokenizer(message, "#");
		char flag = st.nextToken().charAt(0);
		
		Toast.makeText(getBaseContext(),"Gotcha! " + flag, Toast.LENGTH_LONG).show();
		addTweetToScreen(st.nextToken());
		super.initReceiver();
	}
	

	private void addTweetToScreen(String content)
	{
		TextView t = new TextView(this);
		t.setLayoutParams(new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.TEXT_ALIGNMENT_CENTER)
		);

		t.setText(content);
		tweetsLayout.addView(t);
	}
	

//	@Override
//	public void onReceiveResult(int resultCode, Bundle result)
//	{
//		Toast.makeText(getBaseContext(),"_" + result.getString("message"),Toast.LENGTH_LONG).show();
//		startIntent();
//		Intent broadcastIntent = new Intent("messageReceived");
//		broadcastIntent.putExtra("message", result.getString("message"));
//		LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
//
//	}
//	private void startIntent()
//	{
//		Intent intent = new Intent(this, ReceiverService.class);
//		intent.putExtra("receiver", receiver);
//		startService(intent);
//	}
}
