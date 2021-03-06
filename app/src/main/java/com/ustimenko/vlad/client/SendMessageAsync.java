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
//		final String SERVER_IP = "192.168.0.192";
//		final String SERVER_IP = "10.1.19.47";
		final String SERVER_IP = "192.168.43.16";
//		final String SERVER_IP = "169.254.9.190";
		final int SERVER_PORT = 50000;
		
		try(Socket socket = new Socket(InetAddress.getByName(SERVER_IP), SERVER_PORT);
			PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))))
		{
			StringBuilder message = new StringBuilder();
			for(String s : args)
			{
				message.append(s).append('#');
			}
			Log.i("1", "Sends from " + socket.getLocalPort() + " to " + socket.getPort());
			writer.write(message.toString());
			writer.flush();
		}catch(Exception e){Log.e("In async", e.getMessage());}
		return null;
	}
}

