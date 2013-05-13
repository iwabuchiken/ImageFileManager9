package ifm9.tasks;

import ifm9.main.MainActv;
import ifm9.utils.Methods;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RefreshDBTask extends AsyncTask<String, Integer, String> {

	//
	Activity actv;
	Dialog dlg;
	
	public RefreshDBTask(Activity actv) {
		
		this.actv = actv;
		
	}//public RefreshDBTask(Activity actv)
	
	public RefreshDBTask(Activity actv, Dialog dlg) {
		// 
		this.actv = actv;
		this.dlg = dlg;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		int result = Methods.refreshMainDB((ListActivity) actv);
//		boolean result = Methods.refreshMainDB((ListActivity) actv);
//		boolean result = Methods.refreshMainDB_async((ListActivity) actv);
//		boolean result = Methods.refreshMainDB_async((ListActivity) actv, this);
		
		// Log
		Log.d("RefreshDBTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + result);
		
		
		if (result > 0) {
			
			return "DB refreshed: " + String.valueOf(result) + " items";
			
		} else if (result == -1){//if (result == true)

			return "Can't create table => " + MainActv.dirName_base;
			
		} else if (result == 0){//if (result == true)
			
			return "No new entry";
			
		} else {//if (result == true)
			
			return "Unknown result";
			
		}//if (result == true)
		
//		return null;
	}//protected String doInBackground(String... params)

	@Override
	protected void onPostExecute(String result) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onPostExecute(result);

		// debug
		Toast.makeText(actv, result, Toast.LENGTH_LONG).show();
		
//		dlg.dismiss();
		
	}//protected void onPostExecute(String result)

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
//		super.onProgressUpdate(values);
		
		// Log
		Log.d("RefreshDBTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Progress: " + values[0]);
		
		
	}//protected void onProgressUpdate(Integer... values)

}
