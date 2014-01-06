package org.openintents.shopping.share;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

    public JSONObject makeHttpRequest(String url, String method,
            List<? extends NameValuePair> params) {

		JSONObject jObj = null;
        try { 
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpRequestBase request = null;
            if(method == "POST"){
            	request = new HttpPost(url);
            	BasicHttpParams httpParams = new BasicHttpParams(); 
            	UrlEncodedFormEntity form = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            	form.setContentEncoding(HTTP.UTF_8);
                ((HttpPost)request).setEntity(form);
            	request.setParams(httpParams);
            }else if(method == "GET"){
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                request = new HttpGet(url);
            }           
            
            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = httpEntity.getContent();
            
	
    		String json = "";
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8" /*"iso-8859-1"*/), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            json = sb.toString();
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	      
	      
	        try {
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	
	        
        } catch (Exception e) { 
            e.printStackTrace();
        } 
        
        return jObj;

    }
}