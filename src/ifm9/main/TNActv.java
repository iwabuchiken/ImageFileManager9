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
import ifm9.utils.DBUtils;
import ifm9.utils.Methods;
import ifm9.utils.TIListAdapter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

	public static boolean move_mode = false;

	/*********************************
	 * Special intent data
	 *********************************/
	public static long[] long_searchedItems; //=> Used in initial_setup()
	
	public static long[] history_file_ids = null;
	
	public static String[] history_table_names = null;
	
	/*********************************
	 * List-related
	 *********************************/
	public static ArrayList<Integer> checkedPositions;

	public static List<String> fileNameList;
	
	public static ArrayAdapter<String> dirListAdapter;
	
	/*----------------------------
	 * Preference names
		----------------------------*/
	public static String tnactv_selected_item = "tnactv_selected_item";

	/*********************************
	 * Views
	 *********************************/
	public static ListView lv_main;
	
	/****************************************
	 * Methods
	 ****************************************/
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
		checkedPositions = new ArrayList<Integer>();

		//debug
//		get_data_from_table_AAA();
		
//		get_tables_from_db();
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "prefs_current_path: " + Methods.get_pref(this, MainActv.prefs_current_path, "NO DATA"));
		
	}//public void onCreate(Bundle savedInstanceState)


	private void get_tables_from_db() {
		
		DBUtils dbu = new DBUtils(this, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "rdb.getPath(): " + rdb.getPath());
		
		
		// REF=> http://stackoverflow.com/questions/82875/how-do-i-list-the-tables-in-a-sqlite-database-file
		String sql = "SELECT * FROM sqlite_master WHERE type='table'";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		startManagingCursor(c);
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Tables: c.getCount()" + c.getCount());
		
		c.moveToFirst();
		
		for (int i = 0; i < c.getCount(); i++) {
			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "name: " + c.getString(1));
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		rdb.close();
		
	}//private void get_tables_from_db()

	private void get_data_from_table_AAA() {
		
		DBUtils dbu = new DBUtils(this, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		String sql = "SELECT * FROM AAA";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		startManagingCursor(c);
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "AAA: c.getCount()" + c.getCount());
		
		rdb.close();
		
	}//private void get_data_from_table_AAA()


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
		tiList = prep_list();
//		tiList = Methods.getAllData(this, tableName);
//		
//		if (tiList == null) {
		if (tiList == null || tiList.size() < 1) {
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
		int current_history_mode = Methods.get_pref(
				this, 
				MainActv.prefName_mainActv, 
				MainActv.prefName_mainActv_history_mode,
				-1);

		if (current_history_mode == MainActv.HISTORY_MODE_OFF) {

			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "MainActv.HISTORY_MODE => OFF");
			
			Collections.sort(tiList, new Comparator<TI>(){

//				@Override
				public int compare(TI lhs, TI rhs) {
					// TODO 自動生成されたメソッド・スタブ
					
//					return (int) (lhs.getDate_added() - rhs.getDate_added());
					
					return (int) (lhs.getFile_name().compareToIgnoreCase(rhs.getFile_name()));
				}
				
			});
			
		} else if (current_history_mode == MainActv.HISTORY_MODE_ON) {

			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "MainActv.HISTORY_MODE => ON");

			Methods.sort_tiList_last_viewed_at(tiList);
			
		}

//		Collections.sort(tiList, new Comparator<TI>(){
//
////			@Override
//			public int compare(TI lhs, TI rhs) {
//				// TODO 自動生成されたメソッド・スタブ
//				
////				return (int) (lhs.getDate_added() - rhs.getDate_added());
//				
//				return (int) (lhs.getFile_name().compareToIgnoreCase(rhs.getFile_name()));
//			}
//			
//		});

		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tiList => Sort done");
		
//		Log.d("TNActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "After sort => tiList.size(): " + tiList.size());
		
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


	private List<TI> prep_list() {
		/*----------------------------
		 * Get ThumbnailItem list
		 * 1. Get intent data
		 * 2. Build tiList
			----------------------------*/
		/*----------------------------
		 * 1. Get intent data
			----------------------------*/
		Intent i = this.getIntent();
		
		long_searchedItems = i.getLongArrayExtra("long_searchedItems");
		
		history_file_ids = i.getLongArrayExtra(MainActv.intent_label_file_ids);
		
		history_table_names = i.getStringArrayExtra(MainActv.intent_label_table_names);
		
		/*----------------------------
		 * 2. Build tiList
			----------------------------*/
		String tableName = Methods.convert_path_into_table_name(this);
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tableName: " + tableName);
		
		
		if (long_searchedItems != null) {

			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "long_searchedItems.length: " + long_searchedItems.length);
			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Calling: Methods.convert_fileIdArray2tiList()");
			
//			tiList = Methods.getAllData(this, tableName);
//			tiList = Methods.convert_fileIdArray2tiList(this, MainActv.dirName_base, long_searchedItems);
			tiList = Methods.convert_fileIdArray2tiList(this, tableName, long_searchedItems);

			
		} else if (history_file_ids != null) {//if (long_searchedItems == null)

			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "history_file_ids != null");
			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"file_ids: length=" + history_file_ids.length + 
					"/" + "history_table_names: length=" + history_table_names.length);
			
			tiList = Methods.get_all_data_history(this, history_file_ids, history_table_names);
			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tiList.size()=" + tiList.size());
			
//			tiList = Methods.convert_fileIdArray2tiList(this, "IFM8", long_searchedItems);
			
		} else {//if (long_searchedItems == null)
			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Calling => Methods.getAllData");
			
			tiList = Methods.getAllData(this, tableName);
			
		}//if (long_searchedItems == null)

		
		
		return tiList;
	}//private List<TI> prep_list()


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
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

	@Override
	protected void onResume() {
		/*********************************
		 * 1. super
		 * 2. Notify adapter
		 * 
		 * 3. Set selection
		 *********************************/
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onResume()");

		if (TNActv.aAdapter != null) {
					
			TNActv.aAdapter.notifyDataSetChanged();
			
		}
//		TNActv.aAdapter.notifyDataSetChanged();

//		/*********************************
//		 * 3. Set selection
//		 *********************************/
//		lv_main = this.getListView();
//		
//		SharedPreferences prefs = this.getSharedPreferences(
//				MainActv.prefName_tnActv,
//				MODE_PRIVATE);
//	
//
//		//Methods.PrefenceLabels.thumb_actv.name()
//		
//		//int savedPosition = prefs.getInt("chosen_list_item", -1);
//		int savedPosition = prefs.getInt(
//							MainActv.prefName_tnActv_current_image_position,
//							-1);
//		
//		int target_position = savedPosition - (lv_main.getChildCount() / 2);
//		
//		if (target_position < 0) {
//			
//			target_position = 0;
//			
//		}//if (target_position == 0)
//
//		// Log
//		Log.d("TNActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "target_position=" + target_position);
//		
//		
//		lv_main.setSelection(target_position);

		
//		// Log
//		Log.d("TNActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "lv_main.getCheckedItemPosition()=" + lv_main.getCheckedItemPosition());
		
	}//protected void onResume()

	@Override
	protected void onStart() {
		/*********************************
		 * 1. super
		 * 2. Set selection
		 *********************************/
		super.onStart();
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onStart()");
		
//		/*********************************
//		 * 2. Set selection
//		 *********************************/
//		lv_main = this.getListView();
//		
//		SharedPreferences prefs = this.getSharedPreferences(
//				MainActv.prefName_tnActv,
//				MODE_PRIVATE);
//	
//
//		//Methods.PrefenceLabels.thumb_actv.name()
//		
//		//int savedPosition = prefs.getInt("chosen_list_item", -1);
//		int savedPosition = prefs.getInt(
//							MainActv.prefName_tnActv_current_image_position,
//							-1);
//		
//		int target_position = savedPosition - (lv_main.getChildCount() / 2);
//		
//		if (target_position < 0) {
//			
//			target_position = 0;
//			
//		}//if (target_position == 0)
//
//		// Log
//		Log.d("TNActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "target_position=" + target_position);
//		
//		
//		lv_main.setSelection(target_position);
		
	}//protected void onStart()

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
		/*********************************
		 * 1. super
		 * 2. move_mode => falsify
		 * 
		 * 3. History mode => Off
		 *********************************/
		
		super.onDestroy();
		
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onDestroy()");
		
		/*----------------------------
		 * 2. move_mode => falsify
			----------------------------*/
		if (move_mode == true) {
			
			move_mode = false;
			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "move_mode => Now false");
			
		}//if (move_mode == true)

		SharedPreferences prefs = 
				this.getSharedPreferences(MainActv.prefName_tnActv, MODE_PRIVATE);
		
		SharedPreferences.Editor editor = prefs.edit();

		editor.clear();
		editor.commit();
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs cleared: " + MainActv.prefName_tnActv);

		/*********************************
		 * 3. History mode => Off
		 *********************************/
		int current_move_mode = Methods.get_pref(
							this, 
							MainActv.prefName_mainActv, 
							MainActv.prefName_mainActv_history_mode,
							-1);
		
		if (current_move_mode == MainActv.HISTORY_MODE_ON) {
			
			boolean result = Methods.set_pref(
					this, 
					MainActv.prefName_mainActv, 
					MainActv.prefName_mainActv_history_mode,
					MainActv.HISTORY_MODE_OFF);

			if (result == true) {
				// Log
				Log.d("TNActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Pref set: " + MainActv.HISTORY_MODE_OFF);
			} else {//if (result == true)
				// Log
				Log.d("TNActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Set pref => Failed");
				
			}//if (result == true)
			
		}//if (current_move_mode == 1)
		
	}//protected void onDestroy()

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
		 * 
		 * 2-2. Record history
		 * 2-2-a. Update data
		 * 
		 * 2-3. Save preferences
		 * 
		 * 3. Start intent
			----------------------------*/
		//debug
//		// Log
//		Log.d("TNActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "v.getId()=" + v.getId());
		
		
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
			
			/*********************************
			 * 2-2. Record history
			 *********************************/
//			// Log
//			Log.d("TNActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table name=" + Methods.convert_path_into_table_name(this));
			int current_history_mode = Methods.get_pref(
					this, 
					MainActv.prefName_mainActv, 
					MainActv.prefName_mainActv_history_mode,
					-1);

			if (current_history_mode == MainActv.HISTORY_MODE_OFF) {
				
				Methods.save_history(
						this,
						ti.getFileId(),
						Methods.convert_path_into_table_name(this));
				
				/*********************************
				 * 2-2-a. Update data
				 *********************************/
//				// Log
//				Log.d("TNActv.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"[onListItemClick] Table name=" + Methods.convert_path_into_table_name(this));
				
				DBUtils dbu = new DBUtils(this, MainActv.dbName);
				
				//
				SQLiteDatabase wdb = dbu.getWritableDatabase();

				
				boolean res = DBUtils.updateData_TI_last_viewed_at(
									this,
									wdb,
									Methods.convert_path_into_table_name(this),
									ti);
				
				if (res == true) {
					// Log
					Log.d("TNActv.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "Data updated: " + ti.getFile_name());
				} else {//if (res == true)
					// Log
					Log.d("TNActv.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"Update data => Failed: " + ti.getFile_name());
				}//if (res == true)
				
				
				wdb.close();
				
			} else {//if (current_move_mode == MainActv.HISTORY_MODE_OFF)
				
				// Log
				Log.d("TNActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "History not saved");
				
			}//if (current_move_mode == MainActv.HISTORY_MODE_OFF)
//			Methods.save_history(this, ti.getFileId(), Methods.convert_path_into_table_name(this));
			
			
			/*********************************
			 * 2-3. Save preferences
			 *********************************/
			SharedPreferences preference = 
					getSharedPreferences(
							MainActv.prefName_tnActv,
							MODE_PRIVATE);

			SharedPreferences.Editor editor = preference.edit();
			
			editor.putInt(MainActv.prefName_tnActv_current_image_position, position);
			editor.commit();

			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Prefs set");
			
//			aAdapter.notifyDataSetChanged();
			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "aAdapter notified");
			
			this.startActivity(i);
			
		} else if (MainActv.move_mode == true) {//if (move_mode == false)
			
			/*----------------------------
			 * CheckBox on, then click on the item, then nothing happens (20120717_221403)
				----------------------------*/
			
			TNActv.checkedPositions.add(position);
			
			if (bAdapter != null) {
				
				bAdapter.notifyDataSetChanged();
				
			}//if (bAdapter != null)
			
			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "New position => " + position +
					" / " + "(length=" + TNActv.checkedPositions.size() + ")");
			
			
		}//if (move_mode == false)
		
		super.onListItemClick(lv, v, position, id);
		
	}//protected void onListItemClick(ListView lv, View v, int position, long id)

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.thumb_actv_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*----------------------------
		 * Steps
		 * 1. R.id.thumb_actv_menu_move_mode
		 * 2. R.id.thumb_actv_menu_move_files
			----------------------------*/
		
		
		case R.id.thumb_actv_menu_move_mode://---------------------------------------
			if (move_mode == true) {
				
				move_mode_true(item);
				
			} else {// move_mode => false
				
				move_mode_false(item);
				
			}//if (move_mode == true)
			
			break;// case R.id.thumb_actv_menu_move_files
		
		case R.id.thumb_actv_menu_move_files:	//------------------------------------------
			
			if (move_mode == false) {
				
				// debug
				Toast.makeText(this, "Move mode is not on", 2000)
						.show();
				
				return false;
				
			} else if (move_mode == true) {
				/*----------------------------
				 * Steps
				 * 1. checkedPositions => Has contents?
				 * 2. If yes, show dialog
					----------------------------*/
				if (checkedPositions.size() < 1) {
					
					// debug
					Toast.makeText(TNActv.this, "No item selected", 2000).show();
					
					return false;
					
				}//if (checkedPositions.size() < 1)
				
				
				/*----------------------------
				 * 2. If yes, show dialog
					----------------------------*/
				Methods.dlg_moveFiles(this);
				
			}//if (move_mode == false)
			
			break;// case R.id.thumb_actv_menu_move_files
			
		}//switch (item.getItemId())
		
		
		
		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSelected(MenuItem item)


	private void move_mode_false(MenuItem item) {
		
		/*----------------------------
		 * Steps: Current mode => false
		 * 1. Set icon => On
		 * 2. move_mode => true
		 * 
		 * 2-1. Set position to preference
		 * 
		 * 3. Update aAdapter
		 * 4. Re-set tiList
			----------------------------*/
		
		item.setIcon(R.drawable.ifm8_thumb_actv_opt_menu_move_mode_on);
		
		move_mode = true;
		
		// Log
		Log.d("TNActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "move_mode => Now true");
		
		/*----------------------------
		 * 2-1. Set position to preference
			----------------------------*/
		// Log
		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "this.getSelectedItemPosition(): " + this.getSelectedItemPosition());

		Log.d("TNActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "this.getSelectedItemId(): " + this.getSelectedItemId());

		/*----------------------------
		 * 4. Re-set tiList
			----------------------------*/
//		String tableName = Methods.convertPathIntoTableName(this);

		String currentPath = Methods.get_currentPath_from_prefs(this);
		
		String tableName = Methods.convert_filePath_into_table_name(this, currentPath);
		
		// Log
		Log.d("TNActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "tableName: " + tableName);
		
		
		//
		tiList.clear();

		// Log
		Log.d("TNActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "tiList => Cleared");

//		Log.d("TNActv.java"
//				+ "["
//				+ Thread.currentThread().getStackTrace()[2]
//						.getLineNumber() + "]", "checkedPositions.size() => " + checkedPositions.size());

		if (long_searchedItems == null) {

			tiList = Methods.getAllData(this, tableName);
			
		} else {//if (long_searchedItems == null)

			// Log
			Log.d("TNActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "long_searchedItems != null");
			
			tiList = Methods.convert_fileIdArray2tiList(this, tableName, long_searchedItems);
			
		}//if (long_searchedItems == null)


		// Log
		Log.d("TNActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "tiList.size() => " + tiList.size());
		
		/*----------------------------
		 * 3. Update aAdapter
			----------------------------*/
		Methods.sort_tiList(tiList);
		
		bAdapter =
				new TIListAdapter(
						this, 
						R.layout.thumb_activity, 
						tiList,
						Methods.MoveMode.ON);

		setListAdapter(bAdapter);

	}//private void move_mode_false(MenuItem item)


	private void move_mode_true(MenuItem item) {
		/*----------------------------
		 * Steps: Current mode => false
		 * 1. Set icon => On
		 * 2. move_mode => false
		 * 2-2. TNActv.checkedPositions => clear()
		 * 
		 * 2-3. Get position from preference
		 * 
		 * 3. Re-set tiList
		 * 4. Update aAdapter
			----------------------------*/
		
		item.setIcon(R.drawable.ifm8_thumb_actv_opt_menu_move_mode_off);
		
		move_mode = false;

		// Log
		Log.d("TNActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "move_mode => Now false");
		/*----------------------------
		 * 2-2. TNActv.checkedPositions => clear()
			----------------------------*/
		TNActv.checkedPositions.clear();
		
		/*----------------------------
		 * 2-3. Get position from preference
			----------------------------*/
		int selected_position = Methods.get_pref(this, tnactv_selected_item, 0);
		
		/*----------------------------
		 * 3. Re-set tiList
			----------------------------*/
//		String tableName = Methods.convertPathIntoTableName(this);
		String currentPath = Methods.get_currentPath_from_prefs(this);
		
		String tableName = Methods.convert_filePath_into_table_name(this, currentPath);


		tiList.clear();

		// Log
		Log.d("TNActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "tiList => Cleared");

		Log.d("TNActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "checkedPositions.size() => " + checkedPositions.size());
		
		if (long_searchedItems == null) {

			tiList.addAll(Methods.getAllData(this, tableName));
			
		} else {//if (long_searchedItems == null)

//			tiList = Methods.getAllData(this, tableName);
//			tiList = Methods.convert_fileIdArray2tiList(this, "IFM8", long_searchedItems);
			
		}//if (long_searchedItems == null)

		// Log
		Log.d("TNActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "tiList.size() => " + tiList.size());
		
		/*----------------------------
		 * 4. Update aAdapter
			----------------------------*/
		Methods.sort_tiList(tiList);
		
		aAdapter = 
				new TIListAdapter(
						this, 
						R.layout.thumb_activity, 
						tiList,
						Methods.MoveMode.OFF);
		
		setListAdapter(aAdapter);
		
		this.setSelection(selected_position);
		
	}//private void move_mode_true()

}//public class TNActv
