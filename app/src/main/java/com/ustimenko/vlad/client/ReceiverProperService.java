package com.ustimenko.vlad.client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.*;
import java.net.Socket;

public class ReceiverProperService extends Service
{
	
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();
	private Socket socket = null;
	private DataInputStream in;
	private DataOutputStream out;
	
	
	public InputStream getDataInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	public OutputStream getDataOutputStream() throws IOException {
		return socket.getOutputStream();
	}
	
	@Override
	public void onCreate()
	{
		String SERVER_IP = "10.0.2.2";
		int SERVERPORT = 8080;
		try {
			socket = new Socket(SERVER_IP, SERVERPORT);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (Exception ex) {
			Log.e("Erreur","Connexion impossible !");
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		try
		{
			socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	class LocalBinder extends Binder
	{
		ReceiverProperService getService()
		{
			return ReceiverProperService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}
	
}

