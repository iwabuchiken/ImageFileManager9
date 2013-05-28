package ifm9.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import ifm9.items.TI;
import ifm9.main.R;
import ifm9.utils.CONS;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TaskHTTP extends AsyncTask<String, Integer, Integer> {

	Activity actv;
	TI ti;
	
	
	public TaskHTTP(Activity actv, TI ti) {
		
		this.actv	= actv;
		this.ti		= ti;
		
	}

	@Override
	protected Integer doInBackground(String... tags) {
		
		if (tags[0].equals(actv.getString(R.string.http_post_file_name_lollipop))) {
		
			return _doInBackground_Lollipop();
			
		} else {//if (tags[0].equals(actv.getString(R.string.http_post_file_name_lollipop)))
			
			return _doInBackground_Rails();
			
		}//if (tags[0].equals(actv.getString(R.string.http_post_file_name_lollipop)))
		
		
		

		
	}//protected Integer doInBackground(String... arg0)
	
	

	private Integer _doInBackground_Rails() {
		//REF post json: http://stackoverflow.com/questions/6218143/android-post-json-using-http answered Jun 2 '11 at 18:16
		
		// Log
		Log.d("TaskHTTP.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "Starting to post data to remote rails app");
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String url = "http://cosmos-ifm-1.herokuapp.com/images/new";
		
	    //url with the post data
		HttpPost httpPost = new HttpPost(url);

		/***************************************
		 * Json object
		 *
		 ***************************************/
		//REF json object: http://stackoverflow.com/questions/8706046/create-json-in-android answered Jan 2 '12 at 22:42
//		JSONObject joRoot = new JSONObject();
		
		JSONObject joBody = new JSONObject();
//		JSONObject joRoot = new JSONObject();
		
		try {
			
			joBody.put("file_name", ti.getFile_name());
			joBody.put("file_path", ti.getFile_path());
			joBody.put("file_id", ti.getFileId());
			
			joBody.put("table_name", ti.getTable_name());
			
			joBody.put("memos", ti.getMemo());
			joBody.put("tags", ti.getTags());
			
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "ti.getFile_name()=" + ti.getFile_name());
			
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "ti.getTable_name()=" + ti.getTable_name());
			
//			joRoot.put("image_dev", joBody);
//			joRoot.put("image", joBody);
//			joBody.put("file_name", "1_v1.png");
//			joBody.put("table_name", "IFM_Android");
			
		} catch (JSONException e) {
			
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", e.toString());
			
			return null;
			
		}
		
		StringEntity se;
		try {
			
//			se = new StringEntity(joRoot.toString());
//			se = new StringEntity(joBody.toString());
			
			//REF encoging: http://stackoverflow.com/questions/5084462/how-to-send-unicode-characters-in-an-httppost-on-android answered Feb 22 '11 at 22:38
			se = new StringEntity(joBody.toString(), "UTF-8");
			
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "joBody => Set \"UTF-8\"");
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "joBody.toString()=" + joBody.toString());
			
		} catch (UnsupportedEncodingException e) {
			
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", e.toString());
			
			return null;
			
		}
		
		httpPost.setEntity(se);
		
		httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
		
	    /***************************************
		 * Post
		 ***************************************/
	    DefaultHttpClient dhc = new DefaultHttpClient();
	    
		HttpResponse hr = null;
		
		try {
			
			hr = dhc.execute(httpPost);
			
		} catch (ClientProtocolException e) {
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
			
		} catch (IOException e) {
			
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
			
		}
		
		/***************************************
		 * Validate: Return
		 ***************************************/
		if (hr == null) {
			
//			// debug
//			Toast.makeText(actv, "hr == null", 2000).show();
			
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "hr == null");
			
//			return CONS.Task_GetTexts.EXECUTE_POST_NULL;
			return null;
			
		} else {//if (hr == null)
			
			// Log
			Log.d("Task_GetTexts.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Http response => Obtained");

			
//			return null;
			
		}//if (hr == null)

		/***************************************
		 * Status code
		 ***************************************/
		/*********************************
		 * Status code
		 *********************************/
		int status = hr.getStatusLine().getStatusCode();
		
		if (status == CONS.HTTP_Response.CREATED
				|| status == CONS.HTTP_Response.OK) {

			// Log
			Log.d("Task_GetYomi.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "status=" + status);

//			return CONS.HTTP_Response.CREATED;
			
		} else {//if (status == CONS.HTTP_Response.CREATED)
			
			// Log
			Log.d("Task_GetTexts.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "status=" + status);
			
			return CONS.HTTP_Response.NOT_CREATED;
			
		}//if (status == CONS.HTTP_Response.CREATED)
		
		return null;
		
	}//private Integer _doInBackground_Rails()

	private Integer _doInBackground_Lollipop() {
		// TODO Auto-generated method stub
		// REF UrlEncodedFormEntity http://319ring.net/blog/archives/1667
		String param = __doInBackground__1_BuildParam_Get();
//		UrlEncodedFormEntity param = __doInBackground__1_BuildParam();
	
		String url = "http://benfranklin.chips.jp/IFM_CAKE_1/images/add";
		url += "?" + param;
		
		// Log
		Log.d("TaskHTTP.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "url=" + url);
		
		/***************************************
		 * HttpPost
		 ***************************************/
		HttpGet httpGet = new HttpGet(url);
//		HttpPost httpPost = new HttpPost(url);
		
//		httpPost.setHeader("Content-type", "text/html");
		
//		httpPost.setEntity(param);
		
		DefaultHttpClient dhc = new DefaultHttpClient();
		
		HttpResponse hr = null;
		
		try {
			
//			hr = dhc.execute(postRequest);
			hr = dhc.execute(httpGet);
//			hr = dhc.execute(httpPost);
			
		} catch (ClientProtocolException e) {
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
		} catch (IOException e) {
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
		}
		
		
		/***************************************
		 * Validate: Return
		 ***************************************/
		if (hr == null) {
			
//			// debug
//			Toast.makeText(actv, "hr == null", 2000).show();
			
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "hr == null");
			
//			return CONS.Task_GetTexts.EXECUTE_POST_NULL;
			return null;
			
		} else {//if (hr == null)
			
			// Log
			Log.d("Task_GetTexts.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Http response => Obtained");

			
//			return null;
			
		}//if (hr == null)
		

		/*********************************
		 * Status code
		 *********************************/
		int status = hr.getStatusLine().getStatusCode();
		
		if (status == CONS.HTTP_Response.CREATED
				|| status == CONS.HTTP_Response.OK) {

			// Log
			Log.d("Task_GetYomi.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "status=" + status);

//			return CONS.HTTP_Response.CREATED;
			
		} else {//if (status == CONS.HTTP_Response.CREATED)
			
			// Log
			Log.d("Task_GetTexts.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "status=" + status);
			
			return CONS.HTTP_Response.NOT_CREATED;
			
		}//if (status == CONS.HTTP_Response.CREATED)
		
		
		
		return null;
		
	}//private Integer _doInBackground_Lollipop()

	private String __doInBackground__1_BuildParam_Get() {
		// TODO Auto-generated method stub
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		
		params.add(new BasicNameValuePair("data[Image][file_name]", ti.getFile_name()));
//		params.add(new BasicNameValuePair("file_name", ti.getFile_name()));
		
		return URLEncodedUtils.format(params, "utf-8");

	}

	private UrlEncodedFormEntity __doInBackground__1_BuildParam() {
		
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		
//		params.add(new BasicNameValuePair("data[file_name]", ti.getFile_name()));	//=> count($posts) => 0
		params.add(new BasicNameValuePair("file_name", ti.getFile_name()));
		
		try {
			return new UrlEncodedFormEntity(params, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", e.toString());
			
			return null;
		}
//		return URLEncodedUtils.format(params, "utf-8");
		
	}
	

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		// debug
		Toast.makeText(actv, "HTTP task => Done", Toast.LENGTH_LONG).show();
		
		// Log
		Log.d("TaskHTTP.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "HTTP task => Done");
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		// debug
		Toast.makeText(actv, "Starting HTTP task...", Toast.LENGTH_LONG).show();
		
		// Log
		Log.d("TaskHTTP.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "Starting HTTP task...");

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	
}
