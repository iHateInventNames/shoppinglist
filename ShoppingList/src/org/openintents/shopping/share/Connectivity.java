package org.openintents.shopping.share;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class Connectivity extends Service {
	
	private static final Connectivity this_ = new Connectivity();
	
	public static final String SERVER_BASE_URL = "http://OI-SHOPPING-LIST-SYNC.HOSTOI.COM/command.php";

	public static final String RESULT_RESPONSE = "result_code";
	public static final int RESULT_CODE_SUCCESS = 0;
	public static final String RESULT_RESPONSE_TEXT = "result_text";
	public static final String RESULT_RESPONSE_LIST = "list";	
	
	public static final String ADD_COMMAND = "add";
	public static final String UPDATE_ITEM_COMMAND = "update";
	public static final String SYNC_COMMAND = "sync";
	

	private String id;

	public static abstract class JsonProcessor implements Runnable {
		@Override
		public void run() {
			run(result);
		}

		JSONObject result;
		
		public abstract void run(JSONObject result);
	}

	public class HttpTask extends AsyncTask<NameValuePair, Void, JSONObject> {
		Activity context;

		JsonProcessor processResult;

		public HttpTask(Activity context, JsonProcessor processResult) {
			super();
			this.context = context;
			this.processResult = processResult;

			// read instance guid
			if (getId() == null) {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(context);
				if (preferences.contains("id")) {
					String idPref = preferences.getString("id", null);
					setId(idPref);
				} else {
					setId(UUID.randomUUID().toString().replace("-", ""));
					Editor editor = preferences.edit();
					editor.putString("id", getId().toString());
					editor.commit();
				}
			}
		}

		@Override
		protected JSONObject doInBackground(NameValuePair... params) {
			JSONParser jParser = new JSONParser();
			ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.addAll(Arrays.asList(params));
			BasicNameValuePair idPair = new BasicNameValuePair("uuid", getId());
			paramList.add(idPair);
			JSONObject json = jParser.makeHttpRequest(SERVER_BASE_URL, "POST", paramList);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonData) {
			processResult.result = jsonData;
			context.runOnUiThread(processResult);
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void InternalRequest(Activity activity,
			JsonProcessor resultProcessor, NameValuePair... params) {
		new HttpTask(activity, resultProcessor).execute(params);
	}

	public static void Request(Activity activity,
			JsonProcessor resultProcessor, NameValuePair... params) {
		this_.InternalRequest(activity, resultProcessor, params);
	}

	public static String getId() {
		return this_.id;
	}

	public static void setId(String id) {
		this_.id = id;
	}
}
