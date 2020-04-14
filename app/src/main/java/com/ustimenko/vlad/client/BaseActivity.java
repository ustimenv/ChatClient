package com.ustimenko.vlad.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.os.Bundle;
import android.os.Handler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.SecureRandom;

public abstract class BaseActivity extends Activity implements MessageResultReceiver.Receiver
{
	SSLContext sslContext = null;
	MessageResultReceiver receiver = null;
	SharedPreferences prefs = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.sslContext = setupSSL();
	}
	private SSLContext setupSSL()
	{
		try {
			char[] password= "123456".toCharArray();
			KeyStore ksTrust = KeyStore.getInstance("BKS");
			ksTrust.load(getApplicationContext().getResources().openRawResource(R.raw.server_cert_bks), password);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			tmf.init(ksTrust);
			
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
			return sslContext;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	void initReceiver()
	{
		receiver = new MessageResultReceiver(new Handler());
		receiver.setReceiver(this);
		Intent intent = new Intent(this, ReceiverService.class);
		intent.putExtra("receiver", receiver);
		startService(intent);
	}
	
}
