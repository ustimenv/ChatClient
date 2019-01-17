package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.StringTokenizer;

public class RegisterActivity extends Activity
{
	Button 	 sendButton;
	EditText usernameBox;
	EditText passwordBox;
	TextView registrationFeedback;
	
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
				Toast.makeText(getBaseContext(), "Sending", Toast.LENGTH_LONG).show();
				new SendMessageAsync().execute("2", usernameBox.getText().toString(), passwordBox.getText().toString());
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}
}

