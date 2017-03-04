package com.pay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SendRequest {
	public static String Post(String urlstr,String data,int timeout) throws IOException{
		URL url = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Accept-Language", "zh-cn");
		conn.setRequestProperty("content-length", String.valueOf(data.length()));
		conn.setConnectTimeout(timeout);
		// post数据
		OutputStream outs = conn.getOutputStream();
		try {
			OutputStreamWriter writer = new OutputStreamWriter(outs,"utf-8");
			writer.write(data);
			writer.flush();
		} finally {
			outs.close();
		}
		// 读取数据
		InputStream in = conn.getInputStream();
		StringBuffer buf = new StringBuffer();
		try {
			InputStreamReader rd = new InputStreamReader(in,"utf-8");
			BufferedReader r = new BufferedReader(rd);
			String line = null;
			do {
				line = r.readLine();
				if (line != null && line.length() > 0){
					buf.append(line);
				}
			} while (line != null);
		} finally {
			in.close();
		}
		return buf.toString();
	}
}
