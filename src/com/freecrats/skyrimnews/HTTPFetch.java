package com.freecrats.skyrimnews;
import java.io.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HTTPFetch {
	public static InputStream getInputStreamFromURL(String url){
		InputStream contentStream = null;
		try{
			HttpClient httpc=new DefaultHttpClient();
			HttpResponse response = httpc.execute(new HttpGet(url));
			contentStream = response.getEntity().getContent();
		}catch(Exception e){
			e.printStackTrace();
		}
		return(contentStream);
	}
	
	public static String getStringFromURL(String url) throws UnsupportedEncodingException{
		BufferedReader br = new BufferedReader(new InputStreamReader(getInputStreamFromURL(url)));
		StringBuffer sb = new StringBuffer();
		
		try{
			String line = null;
			while((line = br.readLine())!=null){
				sb.append(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return(sb.toString());
	}
	
}
