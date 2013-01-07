package ifm9.main;

import ifm9.tasks.TaskFTP;
import ifm9.utils.Methods;
import ifm9.utils.MethodsFTP;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FTPActv extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/********************************
		 * 
		 ********************************/

		super.onCreate(savedInstanceState);
		
		setup_1();

//		setContentView(R.layout.main);
//
//		this.setTitle(this.getClass().getName());

	}//public void onCreate(Bundle savedInstanceState)

	private void setup_1() {
		/*********************************
		 * memo
		 *********************************/
		setContentView(R.layout.activity_ftp);

		this.setTitle(this.getClass().getName());
		
		TaskFTP task = new TaskFTP(this);
		
		task.execute("ABC");
//		Methods.ftp_connect_disconnect(this);
		
//		MethodsFTP.ftp_connect_disconnect(this);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
	}
	
}//public class FTPActv extends Activity
