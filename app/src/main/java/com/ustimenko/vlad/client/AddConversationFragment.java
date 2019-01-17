package com.ustimenko.vlad.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AddConversationFragment extends Fragment
{
	Button addConvoButton;
	EditText newConvoName;				//username of the other client in the conversation
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.add_conversation_frag, parent, false);
		
		newConvoName = view.findViewById(R.id.newConvoName);
		addConvoButton = view.findViewById(R.id.addConvoButton);
		return view;
	}
	
}
