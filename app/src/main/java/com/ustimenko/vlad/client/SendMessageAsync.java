package com.ustimenko.vlad.client;
import android.os.AsyncTask;
import android.util.Base64;


import javax.crypto.Cipher;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import static javax.crypto.Cipher.PUBLIC_KEY;

public class SendMessageAsync extends AsyncTask<String, Void, Void> {
	private SSLSocketFactory socketFactory;
	private boolean encode = false;
	
	SendMessageAsync(SSLContext sslContext)
	{
		this.socketFactory = (SSLSocketFactory) sslContext.getSocketFactory();
	}
	
	SendMessageAsync(SSLContext sslContext, boolean encode)
	{
		this.socketFactory = (SSLSocketFactory) sslContext.getSocketFactory();
		this.encode = encode;
	}
	
	@Override
	protected Void doInBackground(String... args)
	{
		final int SERVER_PORT = 50000;
		final String SERVER_IP = "192.168.1.136"; //CHANGE
		StringBuilder message = new StringBuilder();
		for (String s : args) {
			message.append(s).append('#');
		}
		String payload = message.toString();
		if(this.encode)
		{
			payload = payload.replaceAll("#", "");
			payload = encode(payload);
		} else
		{
			payload = message.toString();
		}
		
		try (SSLSocket socket = (SSLSocket) this.socketFactory.createSocket(InetAddress.getByName(SERVER_IP), SERVER_PORT);
			 PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))))
		{
			writer.write(payload);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private String encode(String message)
	{
		try {
			KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
			keyStore.load(null);
			
//			KeyStore.Entry entry = keyStore.getEntry("my_key_1", null);
			
			PublicKey publicKey = keyStore.getCertificate("mykey2").getPublicKey();
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] bytes = cipher.doFinal(message.getBytes());
			return new String(bytes);
//			return Base64.encodeToString(bytes, Base64.DEFAULT);
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
