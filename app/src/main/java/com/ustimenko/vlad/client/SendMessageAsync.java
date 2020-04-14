package com.ustimenko.vlad.client;
import android.os.AsyncTask;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;

public class SendMessageAsync extends AsyncTask<String, Void, Void>
{
	private SSLSocketFactory socketFactory;
	
	SendMessageAsync(SSLContext sslContext)
	{
		this.socketFactory = (SSLSocketFactory) sslContext.getSocketFactory();
	}
	
	@Override
	protected Void doInBackground(String... args)
	{
		final int SERVER_PORT = 50000;
		final String SERVER_IP = "192.168.1.136"; //CHANGE
		StringBuilder message = new StringBuilder();
		for(String s : args)
		{
			message.append(s).append('#');
		}
		String payload = message.toString();
		
		try(SSLSocket socket = (SSLSocket) this.socketFactory.createSocket(InetAddress.getByName(SERVER_IP), SERVER_PORT);
			PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))))
		{
			writer.write(payload);
			writer.flush();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

