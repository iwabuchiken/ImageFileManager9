package ifm9.tasks;

import ifm9.main.R;
import ifm9.utils.MethodsFTP;
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
//		
//		// Log
//		Log.d("TaskFTP.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "res=" + res);
		
//		int res = try_1();
		
		if (res > 0) {
			
			return String.valueOf(res);
			
		} else {//if (res == true)
			
			return String.valueOf(res);
			
		}//if (res == true)
		
		
//		return "done";
	}

	private int try_1() {
		
		
		
		return 0;
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
