package com.ustimenko.vlad.client;

import java.util.Date;

public class Message
{
	boolean isOwn;			//is the message from the client himself or from the person he is chatting with
	String name;			//in case !isOwn, the sender's name
	Date date;				//date created or received? TODO
	String text;
	
	public Message(boolean isOwn, String senderName, String text)
	{
		this.isOwn = isOwn;
		this.name = senderName;
		this.text = text;
	}
	
	
}
