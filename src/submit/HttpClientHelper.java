package submit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

import org.apache.commons.httpclient.CircularRedirectException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpClientHelper {

	public static boolean downloadFile(String url, String pathToSave, HttpClient client){
	
		GetMethod get = new GetMethod(url);
		try {
			client.executeMethod(get);

			FileOutputStream out = new FileOutputStream(new File(pathToSave));

			InputStream in = get.getResponseBodyAsStream();
			byte[] buffer = new byte[10000];
			int len;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			in.close();
			out.close();
			get.releaseConnection();
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (CircularRedirectException e) {
			return false;
		} catch (HttpException e) {
			return false;
		} catch (SocketException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally{
			get.releaseConnection();
		}
	}

	public static StringBuffer getPageContentAsHttpGet(String url, HttpClient client)
			throws HttpException, IOException {
	
		GetMethod getMethod = new GetMethod(url);

		StringBuffer inputString = new StringBuffer();
		String line;
		int statusCode = 0;

		boolean exception = false;
		
		while(!exception){
			try {
				statusCode = client.executeMethod(getMethod);
				if (statusCode != -1) {
					try {
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(
										getMethod.getResponseBodyAsStream(), "UTF-8"));
						while ((line = bufferedReader.readLine()) != null) {
							inputString.append(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					getMethod.releaseConnection();
				}
				exception = true;
			} catch (CircularRedirectException e) {
			
			} catch (SocketException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} finally{
				getMethod.releaseConnection();
			}
		}
		return inputString;
	}

	public static StringBuffer getPageContentAsHttpPost(String url, HttpClient client)
			throws HttpException, IOException {
		client.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.9) Gecko/20100824 AskTbFXTV5/3.8.0.12304 Firefox/3.6.9 ( .NET CLR 3.5.30729)");
		
		PostMethod postMethod = new PostMethod(url);

		StringBuffer inputString = new StringBuffer();
		String line;

		int statusCode = client.executeMethod(postMethod);
		if (statusCode != -1) {
			try {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(
								postMethod.getResponseBodyAsStream()));
				while ((line = bufferedReader.readLine()) != null) {
					inputString.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			postMethod.releaseConnection();
		}
		return inputString;
	}
}