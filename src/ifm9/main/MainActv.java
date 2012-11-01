package ifm9.main;

import ifm9.listeners.ButtonOnClickListener;
import ifm9.listeners.ButtonOnTouchListener;
import ifm9.listeners.CustomOnItemLongClickListener;
import ifm9.listeners.DialogListener;
import ifm9.utils.DBUtils;
import ifm9.utils.MainListAdapter;
import ifm9.utils.Methods;
import ifm9.utils.RefreshDBTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class MainActv extends ListActivity {
	
	public static Vibrator vib;

	/*********************************
	 * Intent data labels
	 *********************************/
	public static String intent_label_file_ids = "file_ids";		// Methods.show_history()
	
	public static String intent_label_table_names = "table_names";	// Methods.show_history()

	public static String intent_label_searchedItems_table_names = "string_searchedItems_table_names";
	
	/*********************************
	 * Prefs
	 *********************************/
	private static SharedPreferences prefs;

//	public static String prefs_current_path = "current_path";
	public static String prefs_current_path = "ifm9_master_current_path";
	
	public static String prefName_tnActv = "pref_tn_actv";
	
	public static String prefName_tnActv_current_image_position = "pref_tn_actv_current_image_position";
	
	// MainActv
	// history
	public static String prefName_mainActv = "pref_main_actv";
	
	public static String prefName_mainActv_history_mode = "history_mode";
	
	public static final int HISTORY_MODE_ON = 1;
	
	public static final int HISTORY_MODE_OFF = 0;
	
	/*----------------------------
	 * Paths and names
		----------------------------*/
	public static String dirName_ExternalStorage = "/mnt/sdcard-ext";

//	public static String  dirName_base = "IFM8";
	public static String  dirName_base = "IFM9";
//	public static String  dirName_base = "ifm9";

	public static String dirPath_base = dirName_ExternalStorage + File.separator + dirName_base;

	public static String dirPath_current = null;
	
	/*----------------------------
	 * Others
		----------------------------*/
	// Used => create_list_file()
	public static String listFileName = "list.txt";

	// Used => 
	public static List<String> file_names = null;

//	public static ArrayAdapter<String> adapter = null;
	
	public static MainListAdapter adapter = null;

	public static boolean move_mode = false;
	
	/*----------------------------
	 * DB
		----------------------------*/
//	public static String dbName = "IFM8";
	public static String dbName = "ifm9.db";
	
	// Table names
	public static String tableName_refreshLog = "refresh_log";
	public static String tableName_memo_patterns = "memo_patterns";

	// show_history
	public static String tableName_show_history = "show_history";
	
	public static String[] cols_show_history = {
		"file_id", "table_name"
	};
	
	public static String[] col_types_show_history = {
		"INTEGER", "TEXT"
	};

	public static String tableName_separator = "__";

	// Backup
	public static String dirPath_db = "/data/data/ifm9.main/databases";
	
	public static String fileName_db = "ifm9.db";

	public static String dirPath_db_backup = dirName_ExternalStorage + "/IFM9_backup";
	
	public static String fileName_db_backup_trunk = "ifm9_backup";
	public static String fileName_db_backup_ext = ".bk";

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*----------------------------
		 * 1. super
		 * 2. Set content
		 * 2-2. Set title
		 * 3. Initialize => vib
		 * 
		 *  4. Set list
		 *  5. Set listener => Image buttons
		 *  6. Set path label
		 *  
		 *  7. Initialize preferences
		 *  
		 *  8. Refresh DB
			----------------------------*/
		
        super.onCreate(savedInstanceState);
        
//        debug_b22();
        
//        setup();
        
//        setContentView(R.layout.main);
//        
//        /*----------------------------
//		 * 2-2. Set title
//			----------------------------*/
//		this.setTitle(this.getClass().getName());
//        
//        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
//        
//        /*----------------------------
//		 * 4. Set list
//			----------------------------*/
//        set_initial_dir_list();
//        
//        /*----------------------------
//		 * 5. Set listener => Image buttons
//			----------------------------*/
//		set_listeners();
//		
//		/*----------------------------
//		 * 6. Set path label
//			----------------------------*/
//		Methods.updatePathLabel(this);
//		
//		/*********************************
//		 * 7. Initialize preferences
//		 *********************************/
//		init_prefs();
//		
//		/*********************************
//		 * 8. Refresh DB
//		 *********************************/
//		refresh_db();
		
//		int current_history_mode = Methods.get_pref(
//				this, 
//				MainActv.prefName_mainActv, 
//				MainActv.prefName_mainActv_history_mode,
//				-1);
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "onCreate: current_history_mode=" + current_history_mode);
		
		//debug
//		do_debug();
//		copy_db_file();
//		test_simple_format();
//		restore_db();
//		check_db();
//		show_column_list();
		
        
    }//public void onCreate(Bundle savedInstanceState)

    /*********************************
	 * 
	 * 
	 *********************************/
    private void debug_b22() {
    	
    	setContentView(R.layout.main);
        
        /*----------------------------
		 * 2-2. Set title
			----------------------------*/
		this.setTitle(this.getClass().getName());
        
        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
		
        // REF => http://stackoverflow.com/questions/6579968/how-can-i-get-the-device-name-in-android
        String device_name = android.os.Build.MODEL;
        
        // Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "device_name=" + device_name);
        
	}//private void debug_b22()

	private void setup() {
        setContentView(R.layout.main);
        
        /*----------------------------
		 * 2-2. Set title
			----------------------------*/
		this.setTitle(this.getClass().getName());
        
        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        
        /*----------------------------
		 * 4. Set list
			----------------------------*/
        set_initial_dir_list();
        
        /*----------------------------
		 * 5. Set listener => Image buttons
			----------------------------*/
		set_listeners();
		
		/*----------------------------
		 * 6. Set path label
			----------------------------*/
		Methods.updatePathLabel(this);
		
		/*********************************
		 * 7. Initialize preferences
		 *********************************/
		init_prefs();
		
		/*********************************
		 * 8. Refresh DB
		 *********************************/
		refresh_db();
	}//private void setup()

	private void do_debug() {
		/*********************************
		 * 6. Drop table
		 * 7. Add new col => "last_viewed_at"
		 *********************************/
//		copy_db_file();
//		test_simple_format();
//		restore_db("ifm9_backup_20121001_140224.bk");
//		check_db();
		show_column_list("IFM9__Android");
//		10-01 15:05:54.408: D/MainActv.java[260](14946): New col added to: IFM9__Android

    	/*********************************
		 * 6. Drop table
		 *********************************/
//    	Methods.drop_table(this, MainActv.dbName, MainActv.tableName_show_history);
    	
    	/*********************************
		 * 7. Add new col => "last_viewed_at"
		 *********************************/
//    	add_new_col_last_viewed_at();
    	
    	
	}//private void do_debug()

	private void add_new_col_last_viewed_at() {
		/*********************************
		 * 1. Get table list
		 * 2. Add new col
		 *********************************/
		List<String> table_list = Methods.get_table_list(this, "IFM9");
		
//		//debug
//		for (String name : table_list) {
//			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "t_name=" + name);
//			
//		}//for (String name : table_list)
		
		/*********************************
		 * 2. Add new col
		 *********************************/
		for (String t_name : table_list) {
			
			boolean res = Methods.add_column_to_table(
									this,
									MainActv.dbName,
									t_name,
									"last_viewed_at",
									"INTEGER");
			if (res == true) {
				// Log
				Log.d("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "New col added to: " + t_name);
			} else {//if (res == true)
				// Log
				Log.e("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Add new col => Failed: " + t_name);
			}//if (res == true)
			
			
		}//for (String t_name : table_list)
		
		
//		for (String name : table_list) {
//			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "table name=" + name);
//			
//		}
		
//		String table_5 = table_list.get(5);
//		
//		String[] col_names = Methods.get_column_list(this, MainActv.dbName, table_5);
//		
//		for (String name : col_names) {
//		
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col name=" + name);
//		
//		}
//
//		String table_6 = table_list.get(6);
//		
//		String[] col_names_6 = Methods.get_column_list(this, MainActv.dbName, table_6);
//		
//		for (String name_6 : col_names_6) {
//		
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col name=" + name_6);
//
//		}
		
				
		
	}//private void add_new_col_last_viewed_at()

	private void init_prefs() {
    	/*********************************
		 * 1. history_mode
		 *********************************/
//		int current_history_mode = Methods.get_pref(
//				this, 
//				MainActv.prefName_mainActv, 
//				MainActv.prefName_mainActv_history_mode,
//				-1);
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "onCreate: current_history_mode=" + current_history_mode);
		
		boolean res = Methods.set_pref(
				this, 
				MainActv.prefName_mainActv, 
				MainActv.prefName_mainActv_history_mode,
				MainActv.HISTORY_MODE_OFF);

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", 
				"history_mode set => MainActv.HISTORY_MODE_OFF"
				+ "(" + MainActv.HISTORY_MODE_OFF + ")");
		
	}//private void init_prefs()

	private void show_column_list() {
		/*********************************
		 * memo
		 *********************************/
    	String[] col_names = Methods.get_column_list(this, MainActv.dbName, "IFM9");
    	
    	for (String name : col_names) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col=" + name);
    		
		}//for (String name : col_names)
		
	}

	private void show_column_list(String table_name) {
		/*********************************
		 * memo
		 *********************************/
    	String[] col_names = Methods.get_column_list(this, MainActv.dbName, table_name);
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Table: " + table_name);
		
    	for (String name : col_names) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col=" + name);
    		
		}//for (String name : col_names)
		
	}

	private void check_db() {
    	/*********************************
		 * 1. Clear preferences
		 *********************************/
		prefs = 
				this.getSharedPreferences(prefs_current_path, MODE_PRIVATE);
		
		SharedPreferences.Editor editor = prefs.edit();

		editor.clear();
		editor.commit();
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs cleared");

		
    	String dst = "/data/data/ifm9.main/databases" + MainActv.dbName;
    	
    	File f = new File(dst);
    	
    	File db_dir = new File("/data/data/ifm9.main/databases");
    	
    	for (String name : db_dir.list()) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file name=" + name);
    		
		}//for (String name : db_dir.list())
    	
//    	boolean res = f.exists();
//    	
//    	// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "res=" + res);
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "f.getAbsolutePath(): " + f.getAbsolutePath());
	}

	private void restore_db() {
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: restore_db()");
    	
		String src = "/mnt/sdcard-ext/IFM9_backup/ifm9_backup_20120929_075009.bk";
		String dst = StringUtils.join(new String[]{"/data/data/ifm9.main/databases", MainActv.dbName}, File.separator);
		
//		String dst = "/data/data/ifm9.main/databases" + MainActv.dbName;
		boolean res = Methods.restore_db(this, MainActv.dbName, src, dst);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
		
	}//private void restore_db()

	private void restore_db(String dbFile_name) {
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: restore_db()");
    	
		String src = "/mnt/sdcard-ext/IFM9_backup/" + dbFile_name;
		String dst = StringUtils.join(new String[]{"/data/data/ifm9.main/databases", MainActv.dbName}, File.separator);
		
//		String dst = "/data/data/ifm9.main/databases" + MainActv.dbName;
		boolean res = Methods.restore_db(this, MainActv.dbName, src, dst);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "[restore_db()] res=" + res);
		
	}//private void restore_db()

	private void test_simple_format() {
    	
    	long t = Methods.getMillSeconds_now();
    	
    	String time_label = Methods.get_TimeLabel(t);
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "time_label: " + time_label);
		
    }//private void test_simple_format()
    
	private void set_listeners() {
		/*----------------------------
		 * 1. Get view
		 * 2. Set enables
		 * 
		 * 3. Set listeners to buttons => Click
		 * 4. Set listener => Long click
		 * 
		 * 5. On touch
			----------------------------*/
		
		ImageButton ib_up = (ImageButton) findViewById(R.id.v1_bt_up);
		
		/*----------------------------
		 * 2. Set enables
			----------------------------*/
		String curDirPath = Methods.get_currentPath_from_prefs(this);
		
		if (curDirPath.equals(dirPath_base)) {
			
			ib_up.setEnabled(false);
			
			ib_up.setImageResource(R.drawable.ifm8_up_disenabled);
			
		} else {//if (this.currentDirPath == this.baseDirPath)
		
			ib_up.setEnabled(true);
			
			ib_up.setImageResource(R.drawable.ifm8_up);
		
		}//if (this.currentDirPath == this.baseDirPath)
		
		/*----------------------------
		 * 3. Listeners => Click
			----------------------------*/
		ib_up.setTag(Methods.ButtonTags.ib_up);
		
		ib_up.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_up.setOnClickListener(new ButtonOnClickListener(this));
		
		/*********************************
		 * 5. On touch
		 *********************************/
//		ListView lv = this.getListView();
//		
//		a
//		
		
	}//private void set_listeners()

	private boolean set_initial_dir_list() {
		// TODO 自動生成されたメソッド・スタブ
		set_initial_dir_list_part1();
//		set_initial_dir_list_part2();
		
		return false;
	}//private boolean set_initial_dir_list()
	

	private boolean set_initial_dir_list_part1() {
		/*----------------------------
		 * Steps
		 * 1. Create root dir
		 * 1-2. Create "list.txt"
		 * 2. Set variables => currentDirPath, baseDirPath
		 * 3. Get file list
		 * 3-1. Sort file list
		 * 4. Set list to adapter
		 * 5. Set adapter to list view
		 * 
		 * 6. Set listener to list
		 * 
		 * 9. Return
			----------------------------*/
		/*----------------------------
		 * 1. Create root dir
			----------------------------*/
		File file = create_root_dir();
		
		if (file == null) {
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file == null");
			
			return false;
		}//if (file == null)
		
		/*----------------------------
		 * 1-2. Create "list.txt"
			----------------------------*/
		boolean res = create_list_file(file);
		
		if (res == false) {
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			return false;
		}//if (res == false)

		/*----------------------------
		 * 2. Set variables => currentDirPath, baseDirPath
			----------------------------*/
		init_prefs_current_path();

		/*----------------------------
		 * 3. Get file list
			----------------------------*/
		if (file_names == null) {
			
			init_file_list(file);
			
		}//if (this.file_names == null)
			
		/*----------------------------
		 * 4. Set list to adapter
			----------------------------*/
		res = set_list_to_adapter();
		
		if (res == false)
			return false;
		
		/*----------------------------
		 * 6. Set listener to list
			----------------------------*/
		set_listener_to_list();
		
		/*----------------------------
		 * 9. Return
			----------------------------*/
		return false;
	}//private boolean set_initial_dir_list()

	private void set_listener_to_list() {
		/*********************************
		 * 1. Long click
		 * 
		 * 2. On touch
		 *********************************/
		ListView lv = this.getListView();
		
//		lv.setTag(Methods.ItemTags.dir_list);
		lv.setTag(Methods.ListTags.actv_main_lv);
		
		lv.setOnItemLongClickListener(new CustomOnItemLongClickListener(this));
		
		/*********************************
		 * 2. On touch
		 *********************************/
		
		
	}//private void set_listener_to_list()

	private boolean set_list_to_adapter() {
		
//		adapter = new ArrayAdapter<String>(
//				this,
//				android.R.layout.simple_list_item_1,
//				file_names
//				);
		
		adapter = new MainListAdapter(
				this,
				R.layout.list_row_main_list,
				file_names
				);

		this.setListAdapter(adapter);
		
//		// Log
//		Log.d("MainActv.java" + "["
//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//		+ "]", "adapter => set");
		
		if (adapter == null) {
//			// Log
//			Log.d("MainActv.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "adapter => null");
			
			return false;
			
		} else {//if (adapter == null)
//			// Log
//			Log.d("MainActv.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "adapter => Not null");
			
			return true;
			
		}//if (adapter == null)
		


	}//private boolean set_list_to_adapter()

	private void init_file_list(File file) {
		/*----------------------------
		 * 1. Get file array
		 * 2. Sort the array
		 * 3. Return
			----------------------------*/
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "file path: " + file.getAbsolutePath());
		
		File[] files = null;
		
		String path_in_prefs = Methods.get_currentPath_from_prefs(this);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "path_in_prefs: " + path_in_prefs);
		

		if (path_in_prefs == null) {
			
			files = file.listFiles();
			
		} else {//if (path_in_prefs == null)
			
			files = new File(path_in_prefs).listFiles();
			
		}//if (path_in_prefs == null)
		
		//debug
		if (files == null) {
			
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "files => null");
			
		}//if (files == null)
		
		/*----------------------------
		 * 2. Sort the array
			----------------------------*/
		Methods.sortFileList(files);
		
//		List<String> file_names = new ArrayList<String>();
		file_names = new ArrayList<String>();

		for (File item : files) {
//			if (item.getName().equals(MainActv.listFileName)) {
//				
//				String tname = Methods.convert_path_into_table_name(this);
//				
//				int num_of_entries = Methods.get_num_of_entries(this, tname);
//				
////				String name = item.getName() + Methods.get_num_of_entries(this, )
//				file_names.add(item.getName() + "(" + num_of_entries + ")");
//				
//				// Log
//				Log.d("MainActv.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"Table name=" + Methods.convert_path_into_table_name(this));
//				
//			} else {//if (item.getName().equals(MainActv.listFileName))
//				
//				file_names.add(item.getName());
//				
//			}//if (item.getName().equals(MainActv.listFileName))
			
			file_names.add(item.getName());
		}
				
	}//private void init_file_list(File file)

	private void init_prefs_current_path() {
		/*----------------------------
		 * If the preference already set, then no operation
		 * 
		 * 1. Get preference
		 * 0. Prefs set already?
		 * 2. Get editor
		 * 3. Set value
			----------------------------*/
		
		
		/*----------------------------
		 * 1. Get preference
			----------------------------*/
		prefs = 
				this.getSharedPreferences(prefs_current_path, MODE_PRIVATE);

		/*----------------------------
		 * 0. Prefs set already?
			----------------------------*/
		String temp = prefs.getString(prefs_current_path, null);
		
		if (temp != null && !temp.equals("IFM8")) {
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Prefs already set: " + temp);
			
			return;
			
		}//if (temp == null)
		
		
		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putString(prefs_current_path, dirPath_base);
		
		editor.commit();
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs init => " + prefs_current_path + "/" + dirPath_base);
		
	}//private void init_prefs_current_path()

	private boolean create_list_file(File file) {
		File list_file = new File(file, listFileName);
		
		if (list_file.exists()) {
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "\"list.txt\" => Exists");
			
			return true;
			
		} else {//if (list_file.exists())
			try {
				BufferedWriter br = new BufferedWriter(new FileWriter(list_file));
				br.close();
				
				return true;
				
			} catch (IOException e) {
				// Log
				Log.e("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "BufferedWriter: Exception => " + e.toString());
				
				return false;
			}
		}//if (list_file.exists())
		
	}//private boolean create_list_file()

	private File create_root_dir() {
		String baseDirPath = StringUtils.join(
				new String[]{
						dirName_ExternalStorage, dirName_base},
				File.separator);

		File file = new File(baseDirPath);
		
		if (!file.exists()) {
			try {
				file.mkdir();
				
				// Log
				Log.d("MainActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "Dir created => " + file.getAbsolutePath());
				
				return file;
				
			} catch (Exception e) {
				// Log
				Log.e("MainActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "Exception => " + e.toString());
				
				return null;
			}
			
		} else {//if (file.exists())
			// Log
//			Log.d("MainActv.java"
//			+ "["
//			+ Thread.currentThread().getStackTrace()[2]
//				.getLineNumber() + "]", "Dir exists => " + file.getAbsolutePath());
			
			return file;
		}//if (file.exists())

	}//private void create_root_dir()

	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		/*----------------------------
		 * Steps
		 * 0. Vibrate
		 * 
		 * 1. Get item name
		 * 2. Get file object
		 * 3. Is a directory?
		 * 		=> If yes, update the current path
			----------------------------*/
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onListItemClick()");
		//
		vib.vibrate(Methods.vibLength_click);
		
		String itemName = (String) lv.getItemAtPosition(position);
		
		/*----------------------------
		 * 2. Get file object
			----------------------------*/
		File target = get_file_object(itemName);
		
		/*----------------------------
		 * 3. Is a directory?
			----------------------------*/
		if (!target.exists()) {
			// debug
			Toast.makeText(this, "This item doesn't exist in the directory: " + itemName, 
					2000)
					.show();
			
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"This item doesn't exist in the directory: " + itemName);

			return;
		}//if (!target.exists())
		
		//
		if (target.isDirectory()) {
			
			Methods.enterDir(this, target);
			
//			// debug
//			Toast.makeText(this, "Enter directory: " + itemName, 
//					2000)
//					.show();
			
		} else if (target.isFile()) {//if (target.isDirectory())
			
			Methods.startThumbnailActivity(this, target.getName());
			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "target.getName()=" + target.getName());
			
//			Methods.toastAndLog(this, "This is a file: " + itemName, 2000);
			
//			// debug
//			Toast.makeText(this, "This is a file: " + itemName, 
//					2000)
//					.show();
			
		}//if (target.isDirectory())
		
		
		super.onListItemClick(lv, v, position, id);
	}//protected void onListItemClick(ListView l, View v, int position, long id)

	private File get_file_object(String itemName) {
		/*----------------------------
		 * 1. 
			----------------------------*/
		
		dirPath_current = prefs.getString(prefs_current_path, null);
		
		if (dirPath_current == null) {
			
			init_prefs_current_path();
			
			dirPath_current = prefs.getString(prefs_current_path, null);
			
		}//if (dirPath_current == null)
		
		File target = new File(dirPath_current, itemName);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "dirPath_current: " + dirPath_current);
		
		return target;
	}//private File get_file_object(String itemName)

	@Override
	protected void onDestroy() {
		/*----------------------------
		 * 1. Reconfirm if the user means to quit
		 * 2. super
		 * 3. Clear => prefs
		 * 4. Clear => file_names
			----------------------------*/
		
		super.onDestroy();
		
		/*----------------------------
		 * 3. Clear => prefs
			----------------------------*/
		prefs = 
				this.getSharedPreferences(prefs_current_path, MODE_PRIVATE);
		
		SharedPreferences.Editor editor = prefs.edit();

		editor.clear();
		editor.commit();
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs cleared");
		
		/*----------------------------
		 * 4. Clear => file_names
			----------------------------*/
		file_names = null;
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file_names => Set to null");
		
	}//protected void onDestroy()

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		Methods.confirm_quit(this, keyCode);
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

//		case R.id.main_opt_menu_refresh_db://---------------------------------------
//			/*----------------------------
//			 * Steps
//			 * 1. Vibrate
//			 * 2. Task
//				----------------------------*/
//			
//			vib.vibrate(Methods.vibLength_click);
//			
//			/*----------------------------
//			 * 2. Task
//				----------------------------*/
////			RefreshDBTask task_ = new RefreshDBTask(this);
//			RefreshDBTask task_ = new RefreshDBTask(this);
//			
//			// debug
//			Toast.makeText(this, "Starting a task...", 2000)
//					.show();
//			
//			task_.execute("Start");
//			
//			break;// case R.id.main_opt_menu_refresh_db
		
		case R.id.main_opt_menu_create_folder://----------------------------------
			
			Methods.dlg_createFolder(this);
			
			break;// case R.id.main_opt_menu_create_folder
			
		case R.id.main_opt_menu_db_activity://----------------------------------
			
			Methods.dlg_db_activity(this);
			
			break;// case R.id.main_opt_menu_db_activity

		case R.id.main_opt_menu_search://-----------------------------------------------
			
			Methods.dlg_seratchItem(this);
			
			break;// case R.id.main_opt_menu_search
			
		case R.id.main_opt_menu_preferences://-----------------------------------------------
			
			Methods.start_PrefActv(this);
			
			break;// case R.id.main_opt_menu_search
			
		case R.id.main_opt_menu_history://-----------------------------------------------
			
			Methods.show_history(this);
			
			break;// case R.id.main_opt_menu_history
			
		}//switch (item.getItemId())
		
		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSelected(MenuItem item)

	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();

		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onPause()");

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "prefs: " + Methods.get_currentPath_from_prefs(this));
		
		
	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();

//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "onResume()");
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "prefs: " + Methods.get_currentPath_from_prefs(this));
//		
//				/*----------------------------
//		 * 2. Set enables
//			----------------------------*/
//		ImageButton ib_up = (ImageButton) findViewById(R.id.v1_bt_up);
//		
//		String curDirPath = Methods.get_currentPath_from_prefs(this);
//		
//		if (curDirPath.equals(dirPath_base)) {
//			
//			ib_up.setEnabled(false);
//			
//			ib_up.setImageResource(R.drawable.ifm8_up_disenabled);
//			
//		} else {//if (this.currentDirPath == this.baseDirPath)
//		
//			ib_up.setEnabled(true);
//
//			
//			ib_up.setImageResource(R.drawable.ifm8_up);
//		
//		}//if (this.currentDirPath == this.baseDirPath)
		
	}//protected void onResume()

	private void copy_db_file() {
		/*----------------------------
		 * 1. db setup
		 * 2. Setup files
			----------------------------*/
		
		DBUtils dbu = new DBUtils(this, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "rdb.getPath(): " + rdb.getPath());
		
		String dbPath = rdb.getPath();
		
		rdb.close();
		
		/*----------------------------
		 * 2. Setup files
			----------------------------*/
		
		String dstPath = "/mnt/sdcard-ext";
		
		File src = new File(dbPath);
//		File dst = new File(dstPath);
		File dst = new File(dstPath, src.getName());
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "src: " + src.getAbsolutePath() + "/" + "dst: " + dst.getAbsolutePath());
		
		
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied");
			
		} catch (FileNotFoundException e) {
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
		}//try

	}//copy_db_file()

	@Override
	protected void onStart() {
		
		setup();
		
		/*----------------------------
		 * 1. Refresh DB
			----------------------------*/
//		refresh_db();
//		SharedPreferences prefs =
//							this.getSharedPreferences(this.getString(R.string.prefs_shared_prefs_name), 0);
//		
////		// Log
////		Log.d("MainActv.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "prefs: db_refresh => " + prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false));
//		
//		if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false)) {
//			
//			Methods.start_refreshDB(this);
//			
//		} else {//if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
//			
////			// Log
////			Log.d("MainActv.java" + "["
////					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////					+ "]", "Prefs: db_refresh => " + false);
//			
//		}//if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
		
		super.onStart();
	}//protected void onStart()

	private void refresh_db() {
		SharedPreferences prefs =
				this.getSharedPreferences(this.getString(R.string.prefs_shared_prefs_name), 0);

		//// Log
		//Log.d("MainActv.java" + "["
		//	+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		//	+ "]", "prefs: db_refresh => " + prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false));
		
		if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false)) {
		
			Methods.start_refreshDB(this);
		
		} else {//if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
		
		//// Log
		//Log.d("MainActv.java" + "["
		//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		//		+ "]", "Prefs: db_refresh => " + false);
		
		}//if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
		
	}

}//public class MainActv extends Activity
