package ifm9.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ifm9.items.TI;
import ifm9.listeners.ButtonOnClickListener;
import ifm9.listeners.ButtonOnTouchListener;
import ifm9.listeners.CustomOnItemLongClickListener;
import ifm9.listeners.DialogListener;
import ifm9.utils.Methods;
import ifm9.utils.TIListAdapter;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class TNActv extends ListActivity {

	public static Vibrator vib;

	public static List<TI> tiList;

	public static TIListAdapter aAdapter;
	public static TIListAdapter bAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/*----------------------------
		 * Steps
		 * 1. Super
		 * 2. Set content
		 * 3. Basics
		 * 
		 * 4. Set up
		 * 5. Initialize vars
		----------------------------*/
		super.onCreate(savedInstanceState);

		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onCreate()");
		
		//
		setContentView(R.layout.thumb_activity);
		
		/*----------------------------
		 * 3. Basics
			----------------------------*/
		this.setTitle(this.getClass().getName());
		
		vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
		
		/*----------------------------
		 * 4. Set up
			----------------------------*/
//		//debug
//		Methods.update_prefs_currentPath(this, MainActv.dirName_base);
		
		set_listeners();
		
		set_list();

		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Table name: " + Methods.convert_path_into_table_name(this));
		
		/*----------------------------
		 * 5. Initialize vars
			----------------------------*/
		MainActv.checkedPositions = new ArrayList<Integer>();
		
	}//public void onCreate(Bundle savedInstanceState)


	private void set_list() {
		/*----------------------------
		 * 1. Get table name
		 * 2. Prep list
		 * 3. Sort list
		 * 
		 * 4. Prep adapter
		 * 5. Set adapter
			----------------------------*/
		/*----------------------------
		 * 1. Get table name
			----------------------------*/
		String tableName = Methods.convert_path_into_table_name(this);
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tableName: " + tableName);
		
		//debug
//		Methods.getTableList(this);
		
		
		/*----------------------------
		 * 2. Prep list
			----------------------------*/
		tiList = Methods.getAllData(this, tableName);
//		
		if (tiList == null) {
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tiList => null");
			
			show_message_no_data();

			return;
//			// debug
//			Toast.makeText(this, 
//							"このフォルダには、ファイルはありません。他のフォルダから、オプション・メニューの「移動」を使って、もってこれます", 
//							7000).show();
			
		} else {//if (tiList == null)

			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tiList.size(): " + tiList.size());
			
		}//if (tiList == null)
		
		/*----------------------------
		 * 3. Sort list
			----------------------------*/
		Collections.sort(tiList, new Comparator<TI>(){

			@Override
			public int compare(TI lhs, TI rhs) {
				// TODO 自動生成されたメソッド・スタブ
				
//				return (int) (lhs.getDate_added() - rhs.getDate_added());
				
				return (int) (lhs.getFile_name().compareToIgnoreCase(rhs.getFile_name()));
			}
			
		});

		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tiList => Sort done");
		
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "After sort => tiList.size(): " + tiList.size());
		
		/*----------------------------
		 * 4. Prep adapter
			----------------------------*/
		aAdapter = 
				new TIListAdapter(
						this, 
						R.layout.thumb_activity, 
//						ThumbnailActivity.tiList);
						tiList);
		
		/*----------------------------
		 * 5. Set adapter
			----------------------------*/
		setListAdapter(aAdapter);
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (TI item : tiList) {
			
			list.add(item.getFile_name());
			
		}
		
		ArrayAdapter<String> adp = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1,
				list
				);
		
//		setListAdapter(adp);
		
	}//private void set_list()


	private void set_listeners() {
		/*----------------------------
		 * Steps
		 * 1. "Back" button
		 * 2. LongClick
		 * 3. "Bottom"
		 * 4. "Top"
			----------------------------*/
		//
		ImageButton ib_back = (ImageButton) findViewById(R.id.thumb_activity_ib_back);
		
		ib_back.setEnabled(true);
		ib_back.setImageResource(R.drawable.ifm8_thumb_back_50x50);
		
		ib_back.setTag(Methods.ButtonTags.thumb_activity_ib_back);
		
		ib_back.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_back.setOnClickListener(new ButtonOnClickListener(this));
		
		/*----------------------------
		 * 2. LongClick
			----------------------------*/
//		ListView lv = (ListView) findViewById(android.R.layout.activity_list_item);
		ListView lv = this.getListView();
		
		lv.setTag(Methods.ItemTags.dir_list_thumb_actv);
		
		lv.setOnItemLongClickListener(new CustomOnItemLongClickListener(this));
		
		/*----------------------------
		 * 3. "Bottom"
		 * 		1. Set up
		 * 		2. Listeners
			----------------------------*/
		ImageButton bt_bottom = (ImageButton) findViewById(R.id.thumb_activity_ib_toBottom);
		
		bt_bottom.setEnabled(true);
		bt_bottom.setImageResource(R.drawable.ifm8_thumb_bottom_50x50);
		
		// Tag
		bt_bottom.setTag(Methods.ButtonTags.thumb_activity_ib_bottom);
		
		bt_bottom.setOnTouchListener(new ButtonOnTouchListener(this));
		bt_bottom.setOnClickListener(new ButtonOnClickListener(this, lv));
		
		/*----------------------------
		 * 4. "Top"
		 * 		1. Set up
		 * 		2. Listeners
			----------------------------*/
		ImageButton bt_top = (ImageButton) findViewById(R.id.thumb_activity_ib_toTop);
		
		bt_top.setEnabled(true);
		bt_top.setImageResource(R.drawable.ifm8_thumb_top_50x50);
		
		// Tag
		bt_top.setTag(Methods.ButtonTags.thumb_activity_ib_top);
		
		/*----------------------------
		 * 4.2. Listeners
			----------------------------*/
		bt_top.setOnTouchListener(new ButtonOnTouchListener(this));
		bt_top.setOnClickListener(new ButtonOnClickListener(this, lv));
		
		
	}//private void set_listeners()
	

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
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onResume()");
		
	}

	@Override
	protected void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onStart()");
		
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onStop()");

	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onDestroy()");

	}

	@Override
	public void onBackPressed() {
		/*----------------------------
		 * memo
			----------------------------*/
		this.finish();
		
		overridePendingTransition(0, 0);
		
	}//public void onBackPressed()

	/****************************************
	 * method_name(param_type)
	 * 
	 * <Caller> 1. TNActv.set_list()
	 * 
	 * <Desc> 
	 * 1. Click "OK" button, then TNActv will get finished.
	 * 
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public void show_message_no_data() {
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		
        dialog.setTitle("情報");
        dialog.setMessage("このフォルダには、データはありません。他のフォルダから、オプション・メニューの「移動」を使って、もってこれます");
        
        dialog.setPositiveButton("OK",new DialogListener(this, dialog, 0));
        
        dialog.create();
        dialog.show();
		
	}//public void show_message_no_data()

	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		/*----------------------------
		 * Steps
		 * 0. Vibrate
		 * 1. Get item
		 * 2. Intent
		 * 		2.1. Set data
		 * 3. Start intent
			----------------------------*/
		/*----------------------------
		 * 0. Vibrate
			----------------------------*/
		vib.vibrate(Methods.vibLength_click);
		
		if (MainActv.move_mode == false) {
			/*----------------------------
			 * 1. Get item
				----------------------------*/
			TI ti = (TI) lv.getItemAtPosition(position);
			
			/*----------------------------
			 * 2. Intent
			 * 		2.1. Set data
				----------------------------*/
			Intent i = new Intent();
			
			i.setClass(this, ImageActv.class);
			
			i.putExtra("file_id", ti.getFileId());
			i.putExtra("file_path", ti.getFile_path());
			i.putExtra("file_name", ti.getFile_name());
			
			i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			
//			SharedPreferences preference = 
//					getSharedPreferences(
////							"thumb_actv", 
//							Methods.PrefenceLabels.thumb_actv.name(),
//							MODE_PRIVATE);
//
//			SharedPreferences.Editor editor = preference.edit();
//			
////			editor.putInt("chosen_list_item", position);
//			editor.putInt(Methods.PrefenceLabels.chosen_list_item.name(), position);
//			editor.commit();
//
//			// Log
//			Log.d("ThumbnailActivity.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Prefs set");
//			
////			aAdapter.notifyDataSetChanged();
//			
//			// Log
//			Log.d("ThumbnailActivity.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "aAdapter notified");
			
			this.startActivity(i);
			
		} else if (MainActv.move_mode == true) {//if (move_mode == false)
			
			/*----------------------------
			 * CheckBox on, then click on the item, then nothing happens (20120717_221403)
				----------------------------*/
			
			MainActv.checkedPositions.add(position);
			
			if (bAdapter != null) {
				
				bAdapter.notifyDataSetChanged();
				
			}//if (bAdapter != null)
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "New position => " + position +
					" / " + "(length=" + MainActv.checkedPositions.size() + ")");
			
			
		}//if (move_mode == false)
		
		super.onListItemClick(lv, v, position, id);
		
	}//protected void onListItemClick(ListView lv, View v, int position, long id)

}//public class TNActv
