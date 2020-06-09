package com.hybunion.yirongma.payment.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * http数据请求交互 单例方式
 * @author shenshilei
 *
 */
public class MyHttp {
	private static String TAG="MyHttp";
	private static final String CHARSET = HTTP.UTF_8;
	public static HttpClient httpClient;
	public static String cookieStr;
	public static String jsessionID;
	public static HttpClient getHttp(){
		if(null==httpClient){
			 HttpParams params =new BasicHttpParams();
	            // 设置一些基本参数
	            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	            HttpProtocolParams.setContentCharset(params,
                        CHARSET);
	            HttpProtocolParams.setUseExpectContinue(params, true);
	            HttpProtocolParams
	                    .setUserAgent(
                                params,
                                "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                        + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
	            // 超时设置
	            /* 从连接池中取连接的超时时间 */
	            ConnManagerParams.setTimeout(params, 20*1000);
	            /* 连接超时 */
	            HttpConnectionParams.setConnectionTimeout(params, 20*1000);
	            /* 请求超时 */
	            HttpConnectionParams.setSoTimeout(params, 20*1000);

			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg =new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			SSLSocketFactory sslSocketFactory =  SSLSocketFactory
					.getSocketFactory();
			sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			schReg.register(new Scheme("https",sslSocketFactory , 443));
			// 使用线程安全的连接管理来创建HttpClient
	            ClientConnectionManager conMgr =new ThreadSafeClientConnManager(
	                    params, schReg);
	            httpClient= new DefaultHttpClient(conMgr,params);
		}
		return httpClient;
	}
	/**
	 * 通过一个地址获取内容
	 * @param paramString 
	 * @throws java.io.UnsupportedEncodingException
	 * @throws IllegalStateException
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static String httpGet(String paramString) throws IllegalStateException, IOException {
		HttpClient httpClient = getHttp();
	    HttpUriRequest httpUriRequest = new HttpGet(paramString);
	      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((httpClient).execute(httpUriRequest).getEntity().getContent(), "UTF-8"));
	      StringBuilder stringBuilder = new StringBuilder();
	      String str = bufferedReader.readLine();
	      CookieStore mCookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
          List<Cookie> cookies =  mCookieStore.getCookies();
          for (int i = 0; i < cookies.size(); i++) {
               //这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
        	  cookieStr=cookies.get(i).getName()+"="+cookies.get(i).getValue();
          }
	      if (str == null||str.equals("")){
	    	  stringBuilder.append(str);
		      str = bufferedReader.readLine();
		      return null;
	      }else{
		       return str;
	      }

	}
	/**
	 * 传递参数的数据请求
	 * @param paramString
	 * @param paramList
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 * @throws org.json.JSONException
	 */
	public static String httpPost(String paramString, List<NameValuePair> paramList) throws IOException {
	    HttpClient defaultHttpClient = getHttp();
	    HttpUriRequest httpPost = new HttpPost(paramString);
	    if (jsessionID!=null)
		{
	    	httpPost.setHeader("Cookie","JSESSIONID="+jsessionID);
		}
	    if(null!=paramList)
	    	((HttpEntityEnclosingRequestBase) httpPost).setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
	     HttpEntity httpEntity = defaultHttpClient.execute(httpPost).getEntity();
	      if (httpEntity == null)
	        return null;
	      String resultStr = EntityUtils.toString(httpEntity);
	      CookieStore mCookieStore = ((AbstractHttpClient) defaultHttpClient).getCookieStore();
          List<Cookie> cookies =  mCookieStore.getCookies();
          for (int i = 0; i < cookies.size(); i++) {
               //这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
//        	  cookieStr=cookies.get(i).getName()+"="+cookies.get(i).getValue();
        	  if("JSESSIONID".equals(cookies.get(i).getName())){
                  jsessionID = cookies.get(i).getValue();
                  break;
              }
          }
	      return resultStr;
	  }
	/**
	 * 上传文件
	 * @param file 文件
	 * @param url 地址
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 *//*
	public static String postFile(File file,String url) throws ClientProtocolException, IOException {  
        FileBody bin = null;  
        HttpClient httpclient = getHttp();  
        HttpPost httppost = new HttpPost(url);  
        if(file != null) {  
            bin = new FileBody(file);  
        }  
        MultipartEntity reqEntity = new MultipartEntity();  
        reqEntity.addPart("data", bin);  
        httppost.setEntity(reqEntity);  
        Log.i(TAG,"执行: " + httppost.getRequestLine());  
        HttpResponse response = httpclient.execute(httppost);  
        Log.i(TAG,"statusCode is " + response.getStatusLine().getStatusCode());  
        HttpEntity resEntity = response.getEntity();  
        Log.i(TAG,""+response.getStatusLine());  
        if (resEntity != null) {  
        	 String resultStr = EntityUtils.toString(resEntity);
        	 resEntity.consumeContent();  
        	 return resultStr;
        }  
        return null;  
    }  */
	/**
	 * 
	 * @param urlpath
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	 public static String getByUrl( String urlpath,String encoding) throws Exception {
		 URL url = new URL(urlpath);
		 //实例化一个HTTP连接对象conn
		 HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		 //定义请求方式为GET，其中GET的格式需要注意
		 conn.setRequestMethod("GET");
		 //定义请求时间，在ANDROID中最好是不好超过10秒。否则将被系统回收。
		 conn.setConnectTimeout(6*1000);
		 if(conn.getResponseCode()== 200){
			 InputStream inStream = conn.getInputStream();
			 byte[] data = readStream(inStream);
			return new String(data,encoding);
			
		 }	
		 return null;
	 }	
	 
	public static  byte[] readStream(InputStream inStream) throws Exception {
		 //readStream获得了传递进来的输入流
		 //要返回输入流，就需要定义一个输出流。
		 //定义一个字节数组型的输出流，ByteArrayOutputStream
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			//建立一个缓冲区buffer
			byte[] buffer = new byte[1024];
			int len= -1;
			//将输入流不断的读，并放到缓冲区中去。直到读完
			while((len=inStream.read(buffer))!=-1){
				//将缓冲区的数据不断的写到内存中去。边读边写
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			inStream.close();
			//将输出流以字节数组的方式返回
			return outStream.toByteArray();
		}
}
