package com.ustimenko.vlad.client;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SendMessageAsync extends AsyncTask<String, Void, Void>
{
	@Override
	protected Void doInBackground(String... args)
	{
		Socket socket = null;
		PrintWriter writer = null;
		
		try
		{
			StringBuilder message = new StringBuilder();
			for(String s : args)
			{
				message.append(s);
				message.append("#");
			}
			final int SERVER_PORT = 50000;
			final String SERVER_IP = "192.168.1.137";
			socket = new Socket(InetAddress.getByName(SERVER_IP), SERVER_PORT);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			Log.i("1", "Sends from " + socket.getLocalPort() + " to " + socket.getPort());
			writer.write(message.toString());
			writer.flush();
		}catch(Exception e){Log.e("In async", e.getMessage());}
		finally
		{
			try
			{
				socket.close();
				writer.close();
			}catch(Exception e){Log.e("Error closing socket", e.getMessage());}
		}
		return null;
	}
}

