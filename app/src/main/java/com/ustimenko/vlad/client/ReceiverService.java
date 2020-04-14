package com.ustimenko.vlad.client;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;


public class ReceiverService extends IntentService
{
	public ReceiverService() {
		super("ReceiverService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		System.setProperty("javax.net.debug", "ssl");
		
		

		Socket s = null;
		String message = "$";
		ServerSocket serverSocket = null;
		InputStream is = null;
		ResultReceiver receiver = intent.getParcelableExtra("receiver");  //send back the data to the correct activity
		boolean decodingRequired = intent.getBooleanExtra("decodingRequired", false);
		try
		{
			serverSocket = new ServerSocket(8080);
			s = serverSocket.accept();
			message = inputStreamToString(s.getInputStream());        //extract text message from received stream
			
			if(decodingRequired)
				message = decode(message);
			
			Bundle bundle = new Bundle();
			bundle.putString("message", message);
			receiver.send(0, bundle);
		} catch (Exception e)
		{
			Log.i("1", e.getMessage());
		}
		finally
		{
			try
			{
				s.close();
				serverSocket.close();
				is.close();
			}catch(Exception e){Log.i("1", e.getMessage());}
		}
	}
	
	public String inputStreamToString(InputStream is) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String tmp ="";
		while((tmp=br.readLine()) != null)
		{
			sb.append(tmp);
		}
		return sb.toString();
	}
	
	private String decode(String message)
	{
		try
		{
			KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
			keyStore.load(null);
			KeyStore.Entry entry = keyStore.getEntry("mykey2", null);
			
			PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			

			byte[] bytes = cipher.doFinal(Base64.decode(message.getBytes(), Base64.URL_SAFE)); //TODO fix broken Base64
			return new String(bytes);
//			return Base64.encodeToString(bytes, Base64.DEFAULT);
		
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
}
