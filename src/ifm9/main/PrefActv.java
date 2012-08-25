package ifm9.main;

import android.app.ListActivity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PrefActv extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		/*----------------------------
		 * Steps
		 * 1. Super
		 * 2. Set content
		 * 
		 * 3. Set preferences
		----------------------------*/
		super.onCreate(savedInstanceState);

		//
		setContentView(R.layout.main_pref);

		this.setTitle(this.getClass().getName());
		
		/*----------------------------
		 * 3. Set preferences
			----------------------------*/
		getPreferenceManager().setSharedPreferencesName(this.getString(R.string.prefs_shared_prefs_name));
		
		addPreferencesFromResource(R.xml.preferences);
		
	}//public void onCreate(Bundle savedInstanceState)

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		return super.onCreateOptionsMenu(menu);
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

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}
	
}//public class PrefActv extends ListActivity
