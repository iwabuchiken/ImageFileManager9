package ifm9.utils;

import ifm9.main.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TaskFTP extends AsyncTask<String, Integer, String> {

	Activity actv;
	
	public TaskFTP(Activity actv) {
		
		this.actv = actv;
		
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		
		int res = MethodsFTP.ftp_connect_disconnect(actv);
		
		// Log
		Log.d("TaskFTP.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
		
		if (res > 0) {
			
			return String.valueOf(res);
			
		} else {//if (res == true)
			
			return String.valueOf(res);
			
		}//if (res == true)
		
		
//		return "done";
	}

	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		
		// debug
		Toast.makeText(actv, result, Toast.LENGTH_SHORT).show();
		
		TextView tv = (TextView) actv.findViewById(R.id.activity_ftp_tv_message);
		
		tv.setText(result);
		
	}//protected void onPostExecute(String result)
	
}
