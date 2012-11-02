package ifm9.utils;


import ifm9.items.TI;
import ifm9.listeners.CustomOnItemLongClickListener;
import ifm9.listeners.DialogButtonOnClickListener;
import ifm9.listeners.DialogButtonOnTouchListener;
import ifm9.listeners.DialogListener;
import ifm9.listeners.DialogOnItemClickListener;
import ifm9.listeners.DialogOnItemLongClickListener;
import ifm9.main.MainActv;
import ifm9.main.PrefActv;
import ifm9.main.R;
import ifm9.main.TNActv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;

// Apache
import org.apache.commons.lang.StringUtils;

// REF=> http://commons.apache.org/net/download_net.cgi
//REF=> http://www.searchman.info/tips/2640.html

//import org.apache.commons.net.ftp.FTPReply;

public class Methods {

	static int counter;		// Used => sortFileList()
	
	
	/****************************************
	 * Enums
	 ****************************************/
	public static enum DialogTags {
		// Generics
		dlg_generic_dismiss, dlg_generic_dismiss_second_dialog, dlg_generic_dismiss_third_dialog,
		
		
		// dlg_create_folder.xml
		dlg_create_folder_ok, dlg_create_folder_cancel,

		// dlg_input_empty.xml
		dlg_input_empty_reenter, dlg_input_empty_cancel,
		
		// dlg_confirm_create_folder.xml
		dlg_confirm_create_folder_ok, dlg_confirm_create_folder_cancel,

		// dlg_confirm_remove_folder.xml
		dlg_confirm_remove_folder_ok, dlg_confirm_remove_folder_cancel,

		// dlg_drop_table.xml
		dlg_drop_table_btn_cancel, dlg_drop_table,
		
		// dlg_confirm_drop.xml
		dlg_confirm_drop_table_btn_ok, dlg_confirm_drop_table_btn_cancel,
		
		// dlg_add_memos.xml
		dlg_add_memos_bt_add, dlg_add_memos_bt_cancel, dlg_add_memos_bt_patterns,
		dlg_add_memos_gv,

		// dlg_move_files.xml
		dlg_move_files_move, dlg_move_files,
		
		// dlg_confirm_move_files.xml	=> ok, cancel, dlg tag
		dlg_confirm_move_files_ok, dlg_confirm_move_files_cancel, dlg_confirm_move_files,

		// dlg_item_menu.xml
		dlg_item_menu_bt_cancel, dlg_item_menu,

		// dlg_create_table.xml
		dlg_create_table_bt_create,

		// dlg_memo_patterns.xml
		dlg_memo_patterns,
		
		// dlg_register_patterns.xml
		dlg_register_patterns_register,

		// dlg_search.xml
		dlg_search, dlg_search_ok,

		// dlg_admin_patterns.xml

		// dlg_confirm_delete_patterns.xml
		dlg_confirm_delete_patterns_ok,
		
	}//public static enum DialogTags
	
	public static enum DialogItemTags {
		// dlg_moveFiles(Activity actv)
		dlg_move_files,
		
		// dlg_add_memos.xml
		dlg_add_memos_gv,

		// dlg_db_admin.xml
		dlg_db_admin_lv,

		// dlg_admin_patterns.xml
		dlg_admin_patterns_lv,

		// dlg_delete_patterns.xml
		dlg_delete_patterns_gv,
		
	}//public static enum DialogItemTags
	
	
	public static enum ButtonTags {
		// MainActivity.java
		ib_up,
		
		// DBAdminActivity.java
		db_manager_activity_create_table, db_manager_activity_drop_table, 
		db_manager_activity_register_patterns,
		
		// thumb_activity.xml
		thumb_activity_ib_back, thumb_activity_ib_bottom, thumb_activity_ib_top,
		
		// image_activity.xml
		image_activity_back, image_activity_prev, image_activity_next,
		
		// TIListAdapter.java
		tilist_cb,
		
	}//public static enum ButtonTags
	
	public static enum ItemTags {
		
		// MainActivity.java
		dir_list,
		
		// ThumbnailActivity.java
		dir_list_thumb_actv,
		
		// Methods.java
		dir_list_move_files,
		
		// TIListAdapter.java
		tilist_checkbox,
		
		
	}//public static enum ItemTags

	public static enum MoveMode {
		// TIListAdapter.java
		ON, OFF,
		
	}//public static enum MoveMode

	public static enum PrefenceLabels {
		
		CURRENT_PATH,
		
		thumb_actv,
		
		chosen_list_item,
		
	}//public static enum PrefenceLabels

	public static enum ListTags {
		// MainActivity.java
		actv_main_lv,
		
		// Main
		main_list_adapter,
		
	}//public static enum ListTags

	
	/****************************************
	 * Vars
	 ****************************************/
	public static final int vibLength_click = 35;

	static int tempRecordNum = 20;

	/****************************************
	 * Methods
	 ****************************************/
	public static void sortFileList(File[] files) {
		// REF=> http://android-coding.blogspot.jp/2011/10/sort-file-list-in-order-by-implementing.html
		/*----------------------------
		 * 1. Prep => Comparator
		 * 2. Sort
			----------------------------*/
		/*----------------------------
		 * 1. Prep => Comparator
			----------------------------*/
		Comparator<? super File> filecomparator = new Comparator<File>(){
			
			public int compare(File file1, File file2) {
				/*----------------------------
				 * 1. Prep => Directory
				 * 2. Calculate
				 * 3. Return
					----------------------------*/
				
				int pad1=0;
				int pad2=0;
				
				if(file1.isDirectory())pad1=-65536;
				if(file2.isDirectory())pad2=-65536;
				
				int res = pad2-pad1+file1.getName().compareToIgnoreCase(file2.getName());
				
				return res;
			} 
		 };//Comparator<? super File> filecomparator = new Comparator<File>()
		 
		/*----------------------------
		 * 2. Sort
			----------------------------*/
		Arrays.sort(files, filecomparator);

	}//public static void sortFileList(File[] files)

	/****************************************
	 *
	 * 
	 * <Caller> 1. MainActv.onListItemClick()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void enterDir(Activity actv, File newDir) {
		/*----------------------------
		 * Steps
		 * 1. Update the current path
		 * 2. Refresh list view
		 * 3. Update path view
		 * 
		 * 
			----------------------------*/
		/*----------------------------
		 * 1. Update the current path
			----------------------------*/
		
		MainActv.dirPath_current = newDir.getAbsolutePath();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "MainActv.dirPath_current: " + MainActv.dirPath_current);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Calling: Methods.update_prefs_currentPath(actv, MainActv.dirPath_current)");
		
		
		Methods.update_prefs_currentPath(actv, MainActv.dirPath_current);
		
		/*----------------------------
		 * 2. Refresh list view
			----------------------------*/
		refreshListView(actv);
		
		/*----------------------------
		 * 3. "Up" button => Enable
			----------------------------*/
		String currentPath = MainActv.dirPath_current;
		
		Methods.update_image_buttons(actv, currentPath);
		
//		update_image_buttons(actv, currentPath)；
		
//		ImageButton ib = (ImageButton) actv.findViewById(R.id.v1_bt_up);
//		
//		if (!ib.isEnabled()) {
//			
//			ib.setEnabled(true);
//			
//			ib.setImageResource(R.drawable.ifm8_up);
//			
//			
//		}//if (!ib.isEnabled())
		
		/*----------------------------
		 * 3. Update path view
			----------------------------*/
		updatePathLabel(actv);
		
		
	}//public static void enterDir(Activity actv, File newDir)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean update_prefs_currentPath(Activity actv, String newPath) {
		
		SharedPreferences prefs = 
				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putString(MainActv.prefs_current_path, newPath);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean update_prefs_current_path(Activity actv, Strin newPath)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean clear_prefs_currentPath(Activity actv, String newPath) {
		
		SharedPreferences prefs = 
				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Clear
			----------------------------*/
		try {
			
			editor.clear();
			editor.commit();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Prefs cleared");
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean clear_prefs_current_path(Activity actv, Strin newPath)

	
	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static String get_currentPath_from_prefs(Activity actv) {
		
		SharedPreferences prefs = 
				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);

		return prefs.getString(MainActv.prefs_current_path, null);
		
	}//public static String get_currentPath_from_prefs(Activity actv)

	
	/****************************************
	 * 		refreshListView(Activity actv)
	 * 
	 * <Caller> 
	 * 1. Methods.enterDir()
	 * 
	 * <Desc> 
	 * 1. 
	 * 
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void refreshListView(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Get currentPath
		 * 2. Get file array
		 * 3. Sort file array
		 * 
		 * 4. Add file names to list
		 * 5. Notify adapter of changes
		 * 6. Update image buttons
		 * 
		 * 
		 * 1. Get file list
		 * 1-2. Sort list
		 * 2. Clear => ImageFileManager8Activity.file_names
		 * 3. Add file names to => ImageFileManager8Activity.file_names
		 * 4. Notify adapter of changes
		 * 
		 * 
		 * 
		 * 6. Update image buttons
			----------------------------*/
		/*----------------------------
		 * 1. Get currentPath
			----------------------------*/
		String currentPath = Methods.get_currentPath_from_prefs(actv);
		
		if (currentPath == null) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Methods.get_currentPath_from_prefs(actv) => null");
			
			return;
			
		}//if (currentPath == null)
		// 
		/*----------------------------
		 * 2. Get file array
			----------------------------*/
		File currentDir = new File(currentPath);
		
		File[] files = currentDir.listFiles();
		
		/*----------------------------
		 * 3. Sort file array
			----------------------------*/
		sortFileList(files);
		
		/*----------------------------
		 * 4. Add file names to list
			----------------------------*/
		// Reset file_names
		if(MainActv.file_names != null) {
			
			MainActv.file_names.clear();
			
		} else {
			
			MainActv.file_names = new ArrayList<String>();
			
		}
		
		// Add names
		for (File item : files) {
			
			MainActv.file_names.add(item.getName());
//			if (item.getName().equals(MainActv.listFileName)) {
//				
//				String tname = Methods.convert_path_into_table_name(actv);
//				
//				int num_of_entries = Methods.get_num_of_entries(actv, tname);
//				
////				String name = item.getName() + Methods.get_num_of_entries(this, )
//				MainActv.file_names.add(item.getName() + "(" + num_of_entries + ")");
//				
//				// Log
//				Log.d("MainActv.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"Table name=" + Methods.convert_path_into_table_name(actv));
//				
//			} else {//if (item.getName().equals(MainActv.listFileName))
//				
//				MainActv.file_names.add(item.getName());
//				
//			}//if (item.getName().equals(MainActv.listFileName))

			
		}//for (File item : files)
		
		/*----------------------------
		 * 5. Notify adapter of changes
			----------------------------*/
//		if (MainActv.adapter != null) {
		if (MainActv.adapter != null) {
			
			MainActv.adapter.notifyDataSetChanged();
			
		} else {//if (condition)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "MainActv.adapter => null");
			
			// debug
			Toast.makeText(actv, "MainActv.adapter => null", 3000).show();

		}//if (condition)
		
		/*----------------------------
		 * 6. Update image buttons
			----------------------------*/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "actv.getClass().getName(): " + actv.getClass().getName());
		
//		08-17 08:09:32.187: D/Methods.java[522](8638): actv.getClass().getName(): ifm9.main.ImageActv
		// Add memo, then the program refreshes the list in TNActv, using this method.
		// However, the caller, Methods.addMemo() calls this method handing the parameters, in which
		//	the "actv" contains an instance of ImageActv, which does not has a button "Up", apparently
		//	=> Hence, I workaround this null pointer error by adding an if sentence, in which, if the actv
		//		variable is an instance of ImageActv, the method update_image_buttons(actv, currentPath)
		//		will not be executed, as coded below.
//		if (!actv.getClass().getName().equals("ifm9.main.ImageActv")) {
//			
//			update_image_buttons(actv, currentPath);
//			
//		}//if (!actv.getClass().getName().equals("ifm9.main.ImageActv"))
//		update_image_buttons(actv, currentPath);
		
		
	}//private static void refreshListView()

	private static void update_image_buttons(Activity actv, String currentPath) {
		
		ImageButton ib_up = (ImageButton) actv.findViewById(R.id.v1_bt_up);
		
		if (currentPath.equals(MainActv.dirPath_base)) {
			
			ib_up.setImageResource(R.drawable.ifm8_up_disenabled);
			ib_up.setEnabled(false);
			
		} else {//if (currentPath.equals(MainActv.dirPath_base))

			ib_up.setImageResource(R.drawable.ifm8_up);
			ib_up.setEnabled(true);
			
		}//if (currentPath.equals(MainActv.dirPath_base))
		
	}//private static void update_image_buttons()

	/****************************************
	 *
	 * 
	 * <Caller> 1.  Methods.enterDir(this, target)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void updatePathLabel(Activity actv) {
		// 
		TextView tv = (TextView) actv.findViewById(R.id.v1_tv_dir_path);
		
		tv.setText(convert_prefs_into_path_label(actv));
		
	}//public static void updatePathLabel(Activity actv)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.updatePathLabel(Activity actv)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
//	public static String getCurrentPathLabel(Activity actv) {
	public static String convert_prefs_into_path_label(Activity actv) {
		/*----------------------------
		 * 1. Prep => pathArray, currentBaseDirName
		 * 2. Detect loation of "IFM8"
		 * 3. Build path label
			----------------------------*/
		
		String currentPath = Methods.get_currentPath_from_prefs(actv);
		
		String[] pathArray = currentPath.split(File.separator);
		
		String currentBaseDirName = pathArray[pathArray.length - 1];
		
		/*----------------------------
		 * 2. Detect loation of "IFM8"
			----------------------------*/
		int location = -1;
		
		for (int i = 0; i < pathArray.length; i++) {
			if (pathArray[i].equals(MainActv.dirName_base)) {
				location = i;
				break;
			}//if (pathArray[i].equals(ImageFileManager8Activity.baseDirName))
		}//for (int i = 0; i < pathArray.length; i++)
		
		/*----------------------------
		 * 3. Build path label
			----------------------------*/
		//REF=> http://stackoverflow.com/questions/4439595/how-to-create-a-sub-array-from-another-array-in-java
		String[] newPath = Arrays.copyOfRange(pathArray, location, pathArray.length);
		
		String s_newPath = StringUtils.join(newPath, File.separator);
		
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "s_newPath => " + s_newPath);
		
		return s_newPath;
		
	}//public static String getCurrentPathLabel(Activity actv)

	public static String convert_prefs_into_path_label(Activity actv, String path) {
		/*----------------------------
		 * 1. Prep => pathArray, currentBaseDirName
		 * 2. Detect loation of "IFM8"
		 * 3. Build path label
			----------------------------*/
		
		String currentPath = path;
		
		String[] pathArray = currentPath.split(File.separator);
		
		String currentBaseDirName = pathArray[pathArray.length - 1];
		
		/*----------------------------
		 * 2. Detect loation of "IFM8"
			----------------------------*/
		int location = -1;
		
		for (int i = 0; i < pathArray.length; i++) {
			if (pathArray[i].equals(MainActv.dirName_base)) {
				location = i;
				break;
			}//if (pathArray[i].equals(ImageFileManager8Activity.baseDirName))
		}//for (int i = 0; i < pathArray.length; i++)
		
		/*----------------------------
		 * 3. Build path label
			----------------------------*/
		//REF=> http://stackoverflow.com/questions/4439595/how-to-create-a-sub-array-from-another-array-in-java
		String[] newPath = Arrays.copyOfRange(pathArray, location, pathArray.length);
		
		String s_newPath = StringUtils.join(newPath, File.separator);
		
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "s_newPath => " + s_newPath);
		
		return s_newPath;
		
	}//public static String getCurrentPathLabel(Activity actv)

	public static void upDir(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Get the current path from preference
		 * 2. Is the current path "roof"?
		 * 3. Go up the path
		 * 3-2. New path => Equal to base dir path?
		 * 
		 * 4. Refresh list
		 * 4-2. Update buttons
		 * 5. Update path view
			----------------------------*/
		/*----------------------------
		 * 1. Get the current path from preference
			----------------------------*/
		String currentPath = Methods.get_currentPath_from_prefs(actv);
		
		/*----------------------------
		 * 2. Is the current path "roof"?
			----------------------------*/
		if (currentPath.equals(MainActv.dirPath_base)) {
			
			// debug
			Toast.makeText(actv, "トップ・フォルダにいます", 2000).show();
		
			return;
		}//if (ImageFileManager8Activity.currentDirPath == ImageFileManager8Activity.baseDirPath)
		
		/*----------------------------
		 * 3. Update the current path
			----------------------------*/
		Methods.update_prefs_currentPath(actv, new File(currentPath).getParent());
		
//		ImageFileManager8Activity.currentDirPath = 
//						(new File(ImageFileManager8Activity.currentDirPath))
		
//		File f = new File(ImageFileManager8Activity.currentDirPath);
//		
//		ImageFileManager8Activity.currentDirPath = f.getParent();
//		
//		Methods.toastAndLog(actv, "f.getParent() => " + f.getParent(), 3000);
		
//		/*----------------------------
//		 * 3-2. New path => Equal to base dir path?
//			----------------------------*/
//		
//		
		/*----------------------------
		 * 4. Refresh list
			----------------------------*/
		Methods.refreshListView(actv);
		
		/*----------------------------
		 * 4-2. Update buttons
			----------------------------*/
		currentPath = Methods.get_pref(actv, MainActv.prefs_current_path, null);
		
		if (currentPath != null) {
			
			Methods.update_image_buttons(actv, currentPath);
			
		}//if (currentPath != null)
//			Methods.update_image_buttons(actv, currentPath);
		
		/*----------------------------
		 * 5. Update path view
			----------------------------*/
		Methods.updatePathLabel(actv);
		
	}//public static void upDir(Activity actv)

	public static void startThumbnailActivity(Activity actv, String targetFileName) {
		/*----------------------------
		 * Steps
		 * 1. "list.txt"?
		 * 2. If yes, start activity
			----------------------------*/
		if (!targetFileName.equals(MainActv.listFileName)) {
			
			// debug
			Toast.makeText(actv, "list.txt ではありません", 2000).show();
			
			return;
		}//if (!target.getName().equals(ImageFileManager8Activity.listFileName))
		
		/*----------------------------
		 * 2. If yes, start activity
			----------------------------*/
		Intent i = new Intent();
		
		i.setClass(actv, TNActv.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		actv.startActivity(i);
		
	}//public static void startThumbnailActivity(Activity actv, File target)


	public static void start_PrefActv(Activity actv) {
		
		Intent i = new Intent();
		
		i.setClass(actv, PrefActv.class);
		
//		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		actv.startActivity(i);
		
	}//public static void start_PrefActv(Activity actv)

	public static void start_refreshDB(Activity actv) {
		
		RefreshDBTask task_ = new RefreshDBTask(actv);
		
		// debug
		Toast.makeText(actv, "Starting a task...", 2000)
				.show();
		
		task_.execute("Start");

	}//public static void start_refreshDB(Activity actv)

	public static void confirm_quit(Activity actv, int keyCode) {
		
		// TODO 自動生成されたメソッド・スタブ
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			
			AlertDialog.Builder dialog=new AlertDialog.Builder(actv);
			
	        dialog.setTitle("アプリの終了");
	        dialog.setMessage("アプリを終了しますか？");
	        
	        dialog.setPositiveButton("終了",new DialogListener(actv, dialog, 0));
	        dialog.setNegativeButton("キャンセル",new DialogListener(actv, dialog, 1));
	        
	        dialog.create();
	        dialog.show();
			
		}//if (keyCode==KeyEvent.KEYCODE_BACK)
		
	}//public static void confirm_quit(Activity actv, int keyCode)

	/****************************************
	 *
	 * 
	 * <Caller> 
	 * 1. TNActv.set_list()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static List<TI> getAllData(Activity actv, String tableName) {
		/*----------------------------
		 * Steps
		 * 0. Table exists?
		 * 1. DB setup
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
		 * 
		 * 9. Close db
		 * 10. Return value
			----------------------------*/

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "getAllData() => Starts");
		/*----------------------------
		 * 1. DB setup
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*----------------------------
		 * 0. Table exists?
			----------------------------*/
		boolean res = dbu.tableExists(rdb, tableName);
		
		if (res == false) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "getAllData() => Table doesn't exist: " + tableName);
			
			rdb.close();
			
			return null;
			
		}//if (res == false)
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "rdb.getPath(): " + rdb.getPath());
		
		
		
		/*----------------------------
		 * 2. Get data
		 * 		2.1. Get cursor
		 * 		2.2. Add to list
			----------------------------*/
		//
		String sql = "SELECT * FROM " + tableName;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());

		/*----------------------------
		 * 2.2. Add to list
			----------------------------*/
		c.moveToNext();
		
		List<TI> tiList = new ArrayList<TI>();
		
		for (int i = 0; i < c.getCount(); i++) {
//		for (int i = 0; i < c.getCount() / 200; i++) {

			TI ti = new TI(
					c.getLong(1),	// file_id
					c.getString(2),	// file_path
					c.getString(3),	// file_name
					c.getLong(4),	// date_added
//					c.getLong(5)		// date_modified
					c.getLong(5),		// date_modified
					c.getString(6),	// memos
					c.getString(7)	// tags
			);
	
			// Add to the list
			tiList.add(ti);
			
			//
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		
		
		/*----------------------------
		 * 9. Close db
			----------------------------*/
		rdb.close();
		
		/*----------------------------
		 * 10. Return value
			----------------------------*/
		
		
		return tiList;
		
	}//public static List<ThumbnailItem> getAllData

	public static String convert_path_into_table_name(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Get table name => Up to the current path
		 * 2. Add name => Target folder name
			----------------------------*/
		String tableName = null;
		StringBuilder sb = new StringBuilder();

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "convert_prefs_into_path_label(actv): " + convert_prefs_into_path_label(actv));
		
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "MainActv.dbName: " + MainActv.dbName);
		
//		if(convert_prefs_into_path_label(actv).equals(MainActv.dbName)) {
		if(convert_prefs_into_path_label(actv).equals(MainActv.dirName_base)) {
			
			tableName = convert_prefs_into_path_label(actv);
			
		} else {
			
			String[] currentPathArray = convert_prefs_into_path_label(actv).split(File.separator);
			
			if (currentPathArray.length > 1) {
				
				tableName = StringUtils.join(currentPathArray, "__");
				
			} else {//if (currentPathArray.length > 1)
				
				sb.append(currentPathArray[0]);
				
			}//if (currentPathArray.length > 1)
			
		}//if(getCurrentPathLabel(actv).equals(ImageFileManager8Activity.baseDirName))
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "tableName => " + tableName);
		
		
		return tableName;
	}//public static String convert_path_into_table_name(Activity actv)

	public static String convert_path_into_table_name(Activity actv, String newPath) {
		/*----------------------------
		 * Steps
		 * 1. Get table name => Up to the current path
		 * 2. Add name => Target folder name
			----------------------------*/
		String tableName = null;
		StringBuilder sb = new StringBuilder();

			
//		String[] currentPathArray = convert_prefs_into_path_label(actv).split(File.separator);
		String[] currentPathArray = newPath.split(File.separator);
		
		if (currentPathArray.length > 1) {
			
			tableName = StringUtils.join(currentPathArray, "__");
			
		} else {//if (currentPathArray.length > 1)
			
			sb.append(currentPathArray[0]);
			
			tableName = sb.toString();
			
		}//if (currentPathArray.length > 1)
		
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tableName => " + tableName);
		
		
		return tableName;
	}//public static String convert_path_into_table_name(Activity actv)

	public static String convert_filePath_into_table_name(Activity actv, String filePath) {
		
		String temp = Methods.convert_prefs_into_path_label(actv, filePath);
		
		return Methods.convert_path_into_table_name(actv, temp);
		
	}//public static String convert_filePath_into_table_name(Activity actv, String filePath)

	public static String convert_filePath_into_path_label(Activity actv, String filePath) {
		
		String temp = Methods.convert_prefs_into_path_label(actv, filePath);
		
		return temp;
		
//		return Methods.convert_path_into_table_name(actv, temp);
		
	}//public static String convert_filePath_into_path_label(Activity actv, String filePath)

	public static String convert_filePath_into_path_label_no_base(Activity actv, String filePath) {
		
		String temp = Methods.convert_prefs_into_path_label(actv, filePath);
		
		String[] a_temp = temp.split(File.separator);
		
//		String[] a_temp2 = Arrays.copyOfRange(a_temp, 1, a_temp.length - 1);
		String[] a_temp2 = Arrays.copyOfRange(a_temp, 1, a_temp.length);
//		String[] a_temp2 = Arrays.copyOfRange(a_temp, 0, a_temp.length);
		
//		return StringUtils.join(a_temp2, MainActv.tableName_separator);
		return StringUtils.join(a_temp2, File.separator);
		
//		return temp;
		
//		return Methods.convert_path_into_table_name(actv, temp);
		
	}//public static String convert_filePath_into_path_label_no_base(Activity actv, String filePath)

	public static List<String> get_table_list(Activity actv) {
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
						" WHERE type = 'table' ORDER BY name";
		
		Cursor c = null;
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		// Log
		if (c != null) {
			c.moveToFirst();
			
			for (int i = 0; i < c.getCount(); i++) {
				//
				tableList.add(c.getString(0));
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "c.getString(0): " + c.getString(0));
				
				
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)

//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount(): " + c.getCount());
//		
		rdb.close();
		
		return tableList;
	}//public static List<String> get_table_list()

	/****************************************
	 *	refreshMainDB(Activity actv)
	 * 
	 * <Caller> 1. <Desc> 1. <Params> 1.
	 * 
	 * <Return>
	 *  -1		Can't create a table
	 *  0~	Number of items added
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static int refreshMainDB(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Set up DB(writable)
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 3. Execute query for image files

		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return
			----------------------------*/
		/*----------------------------
		 * 1. Set up DB(writable)
			----------------------------*/
		//
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*----------------------------
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 		1. baseDirName
		 * 		2. backupTableName
			----------------------------*/
		boolean res = refreshMainDB_1_set_up_table(wdb, dbu);

		if (res == false) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Can't  create table");
			
			wdb.close();
			
			return -1;
			
		}//if (res == false)
		/*----------------------------
		 * 3. Execute query for image files
			----------------------------*/
		Cursor c = refreshMainDB_2_exec_query(actv, wdb, dbu);
		
		/*----------------------------
		 * 4. Insert data into db
			----------------------------*/
		int numOfItemsAdded;
		
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Query result: 0");
			
			numOfItemsAdded = 0;
			
//			// debug
//			Toast.makeText(actv, "新規のファイルはありません", 2000).show();
			
		} else {//if (c.getCount() < 1)
			
			numOfItemsAdded = refreshMainDB_3_insert_data(actv, wdb, dbu, c);
			
		}//if (c.getCount() < 1)
		
		/*----------------------------
		 * 9. Close db
			----------------------------*/
		wdb.close();
		
		/*----------------------------
		 * 10. Return
			----------------------------*/
		return numOfItemsAdded;
		
	}//public static int refreshMainDB(Activity actv)

	public static boolean refreshMainDB(Activity actv, Dialog dlg) {
		/*----------------------------
		 * Steps
		 * 1. Set up DB(writable)
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 3. Execute query for image files

		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return
			----------------------------*/
		/*----------------------------
		 * 1. Set up DB(writable)
			----------------------------*/
		//
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*----------------------------
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 		1. baseDirName
		 * 		2. backupTableName
			----------------------------*/
		boolean res = refreshMainDB_1_set_up_table(wdb, dbu);
		
		/*----------------------------
		 * 3. Execute query for image files
			----------------------------*/
		Cursor c = refreshMainDB_2_exec_query(actv, wdb, dbu);
		
		/*----------------------------
		 * 4. Insert data into db
			----------------------------*/
		int numOfItemsAdded;
		
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Query result: 0");
			
			numOfItemsAdded = 0;
			
			// debug
			Toast.makeText(actv, "新規のファイルはありません", 2000).show();
			
		} else {//if (c.getCount() < 1)
			
			numOfItemsAdded = refreshMainDB_3_insert_data(actv, wdb, dbu, c);
			
		}//if (c.getCount() < 1)
		
		/*----------------------------
		 * 9. Close db
			----------------------------*/
		wdb.close();
		
		/*----------------------------
		 * 10. Return
			----------------------------*/
		return true;
		
	}//public static boolean refreshMainDB(Activity actv)

	private static int refreshMainDB_3_insert_data(Activity actv, SQLiteDatabase wdb, DBUtils dbu, Cursor c) {
		/*----------------------------
		 * 4. Insert data into db
			----------------------------*/
		int numOfItemsAdded = insertDataIntoDB(actv, MainActv.dirName_base, c);
			
//		int numOfItemsAdded = -1;
		
		/*----------------------------
		 * 5. Update table "refresh_log"
			----------------------------*/
		c.moveToPrevious();
		
		long lastItemDate = c.getLong(3);
		
		updateRefreshLog(actv, wdb, dbu, lastItemDate, numOfItemsAdded);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getLong(3) => " + c.getLong(3));
		

		return numOfItemsAdded;
		
	}//private static int refreshMainDB_3_insert_data(Cursor c)

	private static Cursor refreshMainDB_2_exec_query(Activity actv, SQLiteDatabase wdb, DBUtils dbu) {
		/*----------------------------
		 * 3. Execute query for image files
		 * 		1. ContentResolver
		 * 		2. Uri
		 * 		3. proj
		 * 		4. Last refreshed date
		 * 		5. Execute query
			----------------------------*/
		/*----------------------------
		 * 3.1. ContentResolver, Uri, proj
			----------------------------*/
		ContentResolver cr = actv.getContentResolver();
		
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        
		String[] proj = DBUtils.proj;

		/*----------------------------
		 * 3.4. Last refreshed date
			----------------------------*/
		long lastRefreshedDate = 0;		// Initial value => 0

		boolean result = dbu.tableExists(wdb, MainActv.tableName_refreshLog);
		
		if (result != false) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + MainActv.tableName_refreshLog);
			
			
			// REF=> http://www.accessclub.jp/sql/10.html
			String sql = "SELECT * FROM refresh_log ORDER BY " + android.provider.BaseColumns._ID + " DESC";
			
			Cursor tempC = wdb.rawQuery(sql, null);
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tempC.getCount() => " + tempC.getCount());
	
			if (tempC.getCount() > 0) {
				
				tempC.moveToFirst();
				
				lastRefreshedDate = tempC.getLong(1);
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", 
						"lastRefreshedDate => " + String.valueOf(lastRefreshedDate) +
						" (I will refresh db based on this date!)");
				
			}//if (tempC.getCount() > 0)
		} else {//if (result != false)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + MainActv.tableName_refreshLog);
			
			// Create one
			result = dbu.createTable(
											wdb, 
											MainActv.tableName_refreshLog, 
											DBUtils.cols_refresh_log, 
											DBUtils.col_types_refresh_log);
			
			if (result == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + MainActv.tableName_refreshLog);
				
			} else {//if (result == true)
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + MainActv.tableName_refreshLog);
				
			}//if (result == true)
			
		}//if (result != false)
		
		/*----------------------------
		 * 3.5. Execute query
			----------------------------*/
		// REF=> http://blog.csdn.net/uoyevoli/article/details/4970860
		Cursor c = actv.managedQuery(
											uri, 
											proj,
											MediaStore.Images.Media.DATE_ADDED + " > ?",
											new String[] {String.valueOf(lastRefreshedDate)},
											null);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Last refreshed (in sec): " + String.valueOf(lastRefreshedDate / 1000));

        actv.startManagingCursor(c);
        
        // Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getCount() => " + c.getCount());

		return c;
		
	}//private static Cursor refreshMainDB_2_exec_query()

	/****************************************
	 *	refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)
	 * 
	 * <Caller> 1. <Desc> 1. <Params> 1.
	 * 
	 * <Return>
	 *  false		=> Can't create table
	 * 	true		=> Either (1) New table created, or, (2) Table exists
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static boolean refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu) {
		/*----------------------------
		 * 2-1.1. baseDirName
			----------------------------*/
		String tableName = MainActv.dirName_base;
		boolean result = dbu.tableExists(wdb, tableName);
		
		// If the table doesn't exist, create one
		if (result == false) {

			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			result = 
					dbu.createTable(wdb, tableName, DBUtils.cols, DBUtils.col_types);
			
			if (result == false) {

				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: "+ tableName);
				
				return false;
				
			} else {//if (result == false)
				
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: "+ tableName);
				
				return true;
				
			}//if (result == false)

		} else {//if (result == false)
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: "+ tableName);

			return true;
			
		}//if (result == false)
	}//private static boolean refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)

	public static boolean refreshMainDB_async(Activity actv, AsyncTask asy) {
		/*----------------------------
		 * Steps
		 * 1. Set up DB(writable)
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 3. Execute query for image files

		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return
			----------------------------*/
		/*----------------------------
		 * 1. Set up DB(writable)
			----------------------------*/
		//
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*----------------------------
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 		1. baseDirName
		 * 		2. backupTableName
			----------------------------*/
		boolean res = refreshMainDB_async_1_set_up_table(wdb, dbu);
		
		/*----------------------------
		 * 3. Execute query for image files
			----------------------------*/
		Cursor c = refreshMainDB_async_2_exec_query(actv, wdb, dbu);
		
		/*----------------------------
		 * 4. Insert data into db
			----------------------------*/
		int numOfItemsAdded;
		
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Query result: 0");
			
			numOfItemsAdded = 0;
			
		} else {//if (c.getCount() < 1)
			
			numOfItemsAdded = refreshMainDB_async_3_insert_data(actv, wdb, dbu, c, asy);
			
		}//if (c.getCount() < 1)
		
		/*----------------------------
		 * 9. Close db
			----------------------------*/
		wdb.close();
		
		/*----------------------------
		 * 10. Return
			----------------------------*/
		return true;
		
	}//public static boolean refreshMainDB(Activity actv)

	private static int refreshMainDB_async_3_insert_data(
										Activity actv, SQLiteDatabase wdb, DBUtils dbu, Cursor c, AsyncTask asy) {
		/*----------------------------
		 * 4. Insert data into db
			----------------------------*/
		int numOfItemsAdded = insertDataIntoDB_async(actv, MainActv.dirName_base, c, asy);

//		int numOfItemsAdded = -1;
		
		/*----------------------------
		 * 5. Update table "refresh_log"
			----------------------------*/
		c.moveToPrevious();
		
		long lastItemDate = c.getLong(3);
		
		updateRefreshLog(actv, wdb, dbu, lastItemDate, numOfItemsAdded);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getLong(3) => " + c.getLong(3));
		

		return numOfItemsAdded;
		
	}//private static int refreshMainDB_3_insert_data(Cursor c)

	private static Cursor refreshMainDB_async_2_exec_query(Activity actv, SQLiteDatabase wdb, DBUtils dbu) {
		/*----------------------------
		 * 3. Execute query for image files
		 * 		1. ContentResolver
		 * 		2. Uri
		 * 		3. proj
		 * 		4. Last refreshed date
		 * 		5. Execute query
			----------------------------*/
		/*----------------------------
		 * 3.1. ContentResolver, Uri, proj
			----------------------------*/
		ContentResolver cr = actv.getContentResolver();
		
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        
		String[] proj = DBUtils.proj;

		/*----------------------------
		 * 3.4. Last refreshed date
			----------------------------*/
		long lastRefreshedDate = 0;		// Initial value => 0

		boolean result = dbu.tableExists(wdb, MainActv.tableName_refreshLog);
		
		if (result != false) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + MainActv.tableName_refreshLog);
			
			
			// REF=> http://www.accessclub.jp/sql/10.html
			String sql = "SELECT * FROM refresh_log ORDER BY " + android.provider.BaseColumns._ID + " DESC";
			
			Cursor tempC = wdb.rawQuery(sql, null);
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tempC.getCount() => " + tempC.getCount());
	
			if (tempC.getCount() > 0) {
				
				tempC.moveToFirst();
				
				lastRefreshedDate = tempC.getLong(1);
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", 
						"lastRefreshedDate => " + String.valueOf(lastRefreshedDate) +
						" (I will refresh db based on this date!)");
				
			}//if (tempC.getCount() > 0)
		} else {//if (result != false)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + MainActv.tableName_refreshLog);
			
			// Create one
			result = dbu.createTable(
											wdb, 
											MainActv.tableName_refreshLog, 
											DBUtils.cols_refresh_log, 
											DBUtils.col_types_refresh_log);
			
			if (result == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + MainActv.tableName_refreshLog);
				
			} else {//if (result == true)
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + MainActv.tableName_refreshLog);
				
			}//if (result == true)
			
		}//if (result != false)
		
		/*----------------------------
		 * 3.5. Execute query
			----------------------------*/
		// REF=> http://blog.csdn.net/uoyevoli/article/details/4970860
		Cursor c = actv.managedQuery(
											uri, 
											proj,
											MediaStore.Images.Media.DATE_ADDED + " > ?",
											new String[] {String.valueOf(lastRefreshedDate)},
											null);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Last refreshed (in sec): " + String.valueOf(lastRefreshedDate / 1000));

        actv.startManagingCursor(c);
        
        // Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getCount() => " + c.getCount());

		return c;
		
	}//private static Cursor refreshMainDB_2_exec_query()

	private static boolean refreshMainDB_async_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu) {
		/*----------------------------
		 * 2-1.1. baseDirName
			----------------------------*/
		String tableName = MainActv.dirName_base;
		boolean result = dbu.tableExists(wdb, tableName);
		
		// If the table doesn't exist, create one
		if (result == false) {

			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			result = 
					dbu.createTable(wdb, tableName, DBUtils.cols, DBUtils.col_types);
			
			if (result == false) {

				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: "+ tableName);
				
				return false;
				
			} else {//if (result == false)
				
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: "+ tableName);
				
				return true;
				
			}//if (result == false)

		} else {//if (result == false)
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: "+ tableName);

			return true;
			
		}//if (result == false)
	}//private static boolean refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)

	/****************************************
	 *		insertDataIntoDB()
	 * 
	 * <Caller> 
	 * 1. private static boolean refreshMainDB_3_insert_data(Activity actv, Cursor c)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static int insertDataIntoDB(Activity actv, String tableName, Cursor c) {
		/*----------------------------
		 * Steps
		 * 0. Set up db
		 * 1. Move to first
		 * 2. Set variables
		 * 3. Obtain data
		 * 4. Insert data
		 * 5. Close db
		 * 6. Return => counter
			----------------------------*/
		/*----------------------------
		 * 0. Set up db
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		 * 1. Move to first
			----------------------------*/
		c.moveToFirst();

		/*----------------------------
		 * 2. Set variables
			----------------------------*/
		int counter = 0;
		int counter_failed = 0;
		
		/*----------------------------
		 * 3. Obtain data
			----------------------------*/
		for (int i = 0; i < c.getCount(); i++) {

			String[] values = {
					String.valueOf(c.getLong(0)),
					c.getString(1),
					c.getString(2),
					String.valueOf(c.getLong(3)),
					String.valueOf(c.getLong(4))
			};

			/*----------------------------
			 * 4. Insert data
			 * 		1. Insert data to tableName
			 * 		2. Record result
			 * 		3. Insert data to backupTableName
			 * 		4. Record result
				----------------------------*/
			boolean blResult = 
						dbu.insertData(wdb, tableName, DBUtils.cols_for_insert_data, values);
				
			if (blResult == false) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "i => " + i + "/" + "c.getLong(0) => " + c.getLong(0));
				
				counter_failed += 1;
				
			} else {//if (blResult == false)
				counter += 1;
			}

			//
			c.moveToNext();
			
			if (i % 100 == 0) {
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Done up to: " + i);
				
			}//if (i % 100 == 0)
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "All data inserted: " + counter);
		
		/*----------------------------
		 * 5. Close db
			----------------------------*/
		wdb.close();
		
		/*----------------------------
		 * 6. Return => counter
			----------------------------*/
		//debug
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "counter_failed(sum): " + counter_failed);
		
		return counter;
		
	}//private static int insertDataIntoDB(Activity actv, Cursor c)

	private static boolean insertDataIntoDB(
			Activity actv, String tableName, String[] types, String[] values) {
		/*----------------------------
		* Steps
		* 1. Set up db
		* 2. Insert data
		* 3. Show message
		* 4. Close db
		----------------------------*/
		/*----------------------------
		* 1. Set up db
		----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		* 2. Insert data
		----------------------------*/
		boolean result = dbu.insertData(wdb, tableName, types, values);
		
		/*----------------------------
		* 3. Show message
		----------------------------*/
		if (result == true) {
		
			// debug
			Toast.makeText(actv, "Data stored", 2000).show();
			
			/*----------------------------
			* 4. Close db
			----------------------------*/
			wdb.close();
			
			return true;
			
		} else {//if (result == true)
		
			// debug
			Toast.makeText(actv, "Store data => Failed", 200).show();
			
			/*----------------------------
			* 4. Close db
			----------------------------*/
			wdb.close();
			
			return false;
		
		}//if (result == true)
		
		/*----------------------------
		* 4. Close db
		----------------------------*/
	
	}//private static int insertDataIntoDB()

	
	/****************************************
	 *		insertDataIntoDB()
	 * 
	 * <Caller> 
	 * 1. private static boolean refreshMainDB_3_insert_data(Activity actv, Cursor c)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static int insertDataIntoDB_async(Activity actv, String tableName, Cursor c, AsyncTask asy) {
		/*----------------------------
		 * Steps
		 * 0. Set up db
		 * 1. Move to first
		 * 2. Set variables
		 * 3. Obtain data
		 * 4. Insert data
		 * 5. Close db
		 * 6. Return => counter
			----------------------------*/
		/*----------------------------
		 * 0. Set up db
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		 * 1. Move to first
			----------------------------*/
		c.moveToFirst();

		/*----------------------------
		 * 2. Set variables
			----------------------------*/
		int counter = 0;
		int counter_failed = 0;
		
		/*----------------------------
		 * 3. Obtain data
			----------------------------*/
		for (int i = 0; i < c.getCount(); i++) {

			String[] values = {
					String.valueOf(c.getLong(0)),
					c.getString(1),
					c.getString(2),
					String.valueOf(c.getLong(3)),
					String.valueOf(c.getLong(4))
			};

			/*----------------------------
			 * 4. Insert data
			 * 		1. Insert data to tableName
			 * 		2. Record result
			 * 		3. Insert data to backupTableName
			 * 		4. Record result
				----------------------------*/
			boolean blResult = 
						dbu.insertData(wdb, tableName, DBUtils.cols_for_insert_data, values);
				
			if (blResult == false) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "i => " + i + "/" + "c.getLong(0) => " + c.getLong(0));
				
				counter_failed += 1;
				
			} else {//if (blResult == false)
				counter += 1;
			}

			//
			c.moveToNext();
			
			if (i % 100 == 0) {
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Done up to: " + i);
				
			}//if (i % 100 == 0)
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "All data inserted: " + counter);
		
		/*----------------------------
		 * 5. Close db
			----------------------------*/
		wdb.close();
		
		/*----------------------------
		 * 6. Return => counter
			----------------------------*/
		//debug
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "counter_failed(sum): " + counter_failed);
		
		return counter;
		
	}//private static int insertDataIntoDB(Activity actv, Cursor c)

	private static boolean updateRefreshLog(
				Activity actv, SQLiteDatabase wdb, 
				DBUtils dbu, long lastItemDate, int numOfItemsAdded) {
		/*----------------------------
		* Steps
		* 1. Table exists?
		* 2. If no, create one
		* 2-2. Create table failed => Return
		* 3. Insert data
		----------------------------*/
		/*----------------------------
		 * 1. Table exists?
			----------------------------*/
		String tableName = MainActv.tableName_refreshLog;
		
		if(!dbu.tableExists(wdb, tableName)) {
		
			Log.d("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Table doesn't exitst: " + tableName);
		
			/*----------------------------
			* 2. If no, create one
			----------------------------*/
			if(dbu.createTable(wdb, tableName, 
				DBUtils.cols_refresh_log, DBUtils.col_types_refresh_log)) {
				
				//toastAndLog(actv, "Table created: " + tableName, 3000);
				
				// Log
				Log.d("Methods.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
				.getLineNumber() + "]", "Table created: " + tableName);
			
			} else {//if
				/*----------------------------
				* 2-2. Create table failed => Return
				----------------------------*/
				//toastAndLog(actv, "Create table failed: " + tableName, 3000);
				
				// Log
				Log.d("Methods.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
				.getLineNumber() + "]", "Create table failed: " + tableName);
				
				
				return false;
			
			}//if
		
		} else {//if(dbu.tableExists(wdb, ImageFileManager8Activity.refreshLogTableName))
		
			//toastAndLog(actv, "Table exitsts: " + tableName, 2000);
			
			// Log
			Log.d("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Table exitsts: " + tableName);
		
		
		}//if(dbu.tableExists(wdb, ImageFileManager8Activity.refreshLogTableName))
		
		/*----------------------------
		* 3. Insert data
		----------------------------*/
		try {
			dbu.insertData(
							wdb, 
							tableName, 
							DBUtils.cols_refresh_log, 
							new long[] {lastItemDate, (long) numOfItemsAdded}
			);
			
			return true;
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Insert data failed");
			
			return false;
		}
		
	}//private static boolean updateRefreshLog(SQLiteDatabase wdb, long lastItemDate)

	public static void dlg_createFolder(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. CheckBox
			----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(R.layout.dlg_create_folder);
		
		// Title
		dlg.setTitle(R.string.dlg_create_folder_title);
		
		//
		
		
		/*----------------------------
		 * 2. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_ok = (Button) dlg.findViewById(R.id.dlg_create_folder_bt_ok);
		Button btn_cancel = (Button) dlg.findViewById(R.id.dlg_create_folder_cancel);
		
		//
		btn_ok.setTag(DialogTags.dlg_create_folder_ok);
		btn_cancel.setTag(DialogTags.dlg_create_folder_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		 * 3. Add listeners => OnClick
			----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		/*----------------------------
		 * 4. CheckBox
			----------------------------*/
//		CheckBox cb = (CheckBox) dlg.findViewById(R.id.dlg_create_folder_cb_folder_set);
		
		//
		dlg.show();
		
	}//public static void dlg_createFolder(Activity actv)

	/****************************************
	 * <Caller> DialogButtonOnClickListener.onClick()
	 *  1. 
	 *  
	 *  <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void dlg_isEmpty(Activity actv, Dialog dlg) {
		/*----------------------------
		 * 1. Check if the input exists. If not, show a dialog
		 * 2. If yes, go to Methods.createFolder()
			----------------------------*/
		/*----------------------------
		 * 1. Check if the input exists. If not, show a dialog
			----------------------------*/
		//
		EditText et = (EditText) dlg.findViewById(R.id.dlg_create_folder_et);
		String folderName = et.getText().toString();
		
		//
		if (!folderName.equals("")) {
			/*----------------------------
			 * 2. If yes, go to Methods.createFolder()
				----------------------------*/
			dlg_confirm_createFolder(actv, dlg);
			
			return;
			
		}//if (!folderName.equals(""))
		
		/*----------------------------
		 * If not, show a dialog
			----------------------------*/
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_input_empty);
		
		// Title
		dlg2.setTitle(R.string.dlg_input_empty_title);
		
		//
		
		
		/*----------------------------
		 * 2. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_input_empty_btn_reenter);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_input_empty_btn_cancel);
		
		//
		btn_ok.setTag(DialogTags.dlg_input_empty_reenter);
		btn_cancel.setTag(DialogTags.dlg_input_empty_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/*----------------------------
		 * 3. Add listeners => OnClick
			----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		
		//
		dlg2.show();
		
	}//public static void dlg_isEmpty(Activity actv, Dialog dlg)

	private static void dlg_confirm_createFolder(Activity actv, Dialog dlg) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 2. Set folder name to text view
			----------------------------*/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_confirm_create_folder);
		
		// Title
		dlg2.setTitle(R.string.generic_tv_confirm);
		
		/*----------------------------
		 * 2. Set folder name to text view
			----------------------------*/
		EditText et = (EditText) dlg.findViewById(R.id.dlg_create_folder_et);
		
		TextView tv = (TextView) dlg2.findViewById(R.id.dlg_confirm_create_folder_tv_table_name);
		
		tv.setText(et.getText().toString());
		
		/*----------------------------
		 * 3. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_confirm_create_folder_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_confirm_create_folder_btn_cancel);
		
		//
		btn_ok.setTag(DialogTags.dlg_confirm_create_folder_ok);
		btn_cancel.setTag(DialogTags.dlg_confirm_create_folder_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/*----------------------------
		 * 4. Add listeners => OnClick
			----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		
		/*----------------------------
		 * 5. Show dialog
			----------------------------*/
		dlg2.show();
		
	}//private static void dlg_confirm_createFolder

	public static void createFolder(Activity actv, Dialog dlg, Dialog dlg2) {
		/*----------------------------
		 * Steps
		 * 1. Get folder name from dlg2
		 * 1-1. CheckBox => Checked?
		 * 1-2. Dismiss dlg2
		 * 2. Get current directory path
		 * 3. Create a file object
		 * 4. Create a dir

		 * 5. If successful, dismiss dialog. Otherwise, toast a message
		 * 6. Create a "list.txt"
		 * 6-2. Create a folder set if checked
		 * 
		 * 7. Refresh list view 
		 * 
		 * 8. Create a new table
			----------------------------*/
		File newDir = createFolder_1(actv, dlg, dlg2);
		
		boolean res = createFolder_2(actv, dlg, newDir);
		
		if (res == true) {
			
			createFolder_3(actv, dlg, newDir);
			
		} else {//if (res == true)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "createFolder_2() => false");
			
		}//if (res == true)
		
			
		
	}//public static void createFolder(Activity actv, Dialog dlg, Dialog dlg2)

	public static File createFolder_1(Activity actv, Dialog dlg, Dialog dlg2) {
		/*----------------------------
		 * Steps
		 * 1. Get folder name from dlg2

		 * 1-2. Dismiss dlg2
		 * 2. Get current directory path
		 * 3. Create a file object
		 * 4. Create a dir

		 * 5. If successful, dismiss dialog. Otherwise, toast a message
		 * 6. Create a "list.txt"
		 * 1-1. CheckBox => Checked?
		 * 6-2. Create a folder set if checked
		 * 
		 * 7. Refresh list view 
		 * 
		 * 8. Create a new table
			----------------------------*/
		//
		TextView tv_folderName = (TextView) dlg2.findViewById(R.id.dlg_confirm_create_folder_tv_table_name);
		String folderName = tv_folderName.getText().toString();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Folder name => " + tv_folderName.getText().toString());
		
		/*----------------------------
		 * 1-2. Dismiss dlg2
			----------------------------*/
		dlg2.dismiss();
		
		/*----------------------------
		 * 2. Get current directory path
		 * 3. Create a file object
			----------------------------*/
		String currentDirPath = Methods.get_currentPath_from_prefs(actv);
		
		File newDir = new File(currentDirPath, folderName);
//		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "currentDirPath: " + currentDirPath + " | " + "newDir: " + newDir.getAbsolutePath());
		
		/*----------------------------
		 * 4. Create a file => Use BufferedWriter
			----------------------------*/
		//
		if (newDir.exists()) {
			// debug
			Toast.makeText(actv, "この名前のフォルダはすでにあります！： " + folderName, 3000).show();
			
			return null;
			
		} else {//if (newDir.exists())
			//
			try {
				newDir.mkdir();
				
				/*----------------------------
				 * 5. If successful, dismiss dialog. 
					----------------------------*/
				dlg.dismiss();
				
				// debug
				Toast.makeText(actv, "フォルダを作りました : " + newDir.getAbsolutePath(), 3000).show();
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Folder created => " + newDir.getAbsolutePath());
				
				
			} catch (Exception e) {
				// debug
				Toast.makeText(actv, "フォルダを作れませんでした : " + newDir.getName(), 3000).show();
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "newDir.getName() => " + newDir.getName());
				
				return null;
			}//try
			
		}//if (newDir.exists())
		
		/*----------------------------
		 * 6. Create a "list.txt"
			----------------------------*/
		File listFile = new File(newDir, MainActv.listFileName);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "listFile => " + listFile.getAbsolutePath());
		
		if (listFile.exists()) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "listFile => Exists");
			
			// debug
			Toast.makeText(actv, "list.txt => すでにあります", 3000).show();
			
		} else {//if (listFile.exists())
			try {
				BufferedWriter br = new BufferedWriter(new FileWriter(listFile));
				
				br.close();
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "listFile => Created");
				
//				// debug
//				Toast.makeText(actv, "list.txt => 作成されました", 3000).show();
				
			} catch (IOException e) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create listFile => Failed: " + listFile.getAbsolutePath());
				// debug
				Toast.makeText(actv, "list.txt => 作成できませんでした", 3000).show();
				
				return null;
			}
		}//if (listFile.exists())
		
		return newDir;
		
		/*----------------------------
		 * 6-2. Create a folder set if checked
			----------------------------*/
		
	}//public static File createFolder(Activity actv, Dialog dlg, Dialog dlg2)

	public static boolean createFolder_2(Activity actv, Dialog dlg, File newDir) {
		/*----------------------------
		 * 6-2. Create a folder set if checked
			----------------------------*/
		CheckBox cb = (CheckBox) dlg.findViewById(R.id.dlg_create_folder_cb_folder_set);
		
		boolean checked = cb.isChecked();
		
		if (checked) {
			
			String[] folder_set = {"DO", "DONE", "LATER", "SENT_TO_PC"};
			
			for (String eachFolder : folder_set) {
				/*----------------------------
				 * 1. Create a folder
				 * 2. list.txt
					----------------------------*/
				/*----------------------------
				 * 1. Create a folder
					----------------------------*/
				File f = new File(newDir, eachFolder);
				
				boolean res = f.mkdir();
				
				/*----------------------------
				 * 2. list.txt
					----------------------------*/
				File listFile;
				
				if (res) {
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "Folder set: " + f.getAbsolutePath());
					
					
					listFile = new File(f, MainActv.listFileName);
					
					try {
						BufferedWriter br = new BufferedWriter(new FileWriter(listFile));
						
						br.close();
						
						// Log
						Log.d("Methods.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]", "listFile => Created");
						
						// debug
						Toast.makeText(actv, "list.txt => 作成されました", 3000).show();
						
					} catch (IOException e) {
						// Log
						Log.d("Methods.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]", "Create listFile => Failed: " + listFile.getAbsolutePath());
						// debug
						Toast.makeText(actv, "list.txt => 作成できませんでした", 3000).show();
					}
					
				}//if (res)
				
				
			}//for (String eachFolder : folder_set)
			
		}//if (checked)
		
		return true;
		
		/*----------------------------
		 * 7. Refresh list viewFile 
			----------------------------*/
		
	}//public static boolean createFolder(Activity actv, Dialog dlg, Dialog dlg2)

	public static boolean createFolder_3(Activity actv, Dialog dlg, File newDir) {
		/*----------------------------
		 * 7. Refresh list view
			----------------------------*/
		refreshListView(actv);
		
		/*----------------------------
		 * 8. Create a new table
		 * 		8.1. Build a table name
		 * 		8.2. Create a table
			----------------------------*/
		/*----------------------------
		 * 8.1. Build a table name
			----------------------------*/
		String newPath = newDir.getAbsolutePath();

		String convertedPath = Methods.convert_prefs_into_path_label(actv, newPath);
		
		String tableName = Methods.convert_path_into_table_name(actv, convertedPath);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "New table name => " + tableName);
		

		/*----------------------------
		 * 8.2. Create a table
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = dbu.createTable(wdb, tableName, 
//					dbu.get_cols(), dbu.get_col_types());
							DBUtils.cols, DBUtils.col_types);
		
		wdb.close();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Methods.createFolder() => Done");
		
		return res;
		
	}//public static boolean createFolder(Activity actv, Dialog dlg, Dialog dlg2)

	public static void dlg_removeFolder(Activity actv, String folderName) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 2. Set folder name to text view
			----------------------------*/
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(R.layout.dlg_confirm_remove_folder);
		
		// Title
		dlg.setTitle(R.string.generic_tv_confirm);
		
		/*----------------------------
		 * 2. Set folder name to text view
			----------------------------*/
		TextView tv = (TextView) dlg.findViewById(R.id.dlg_confirm_remove_folder_tv_table_name);
		
		tv.setText(folderName);
		
		/*----------------------------
		 * 3. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_ok = (Button) dlg.findViewById(R.id.dlg_confirm_remove_folder_btn_ok);
		Button btn_cancel = (Button) dlg.findViewById(R.id.dlg_confirm_remove_folder_btn_cancel);
		
		//
		btn_ok.setTag(DialogTags.dlg_confirm_remove_folder_ok);
		btn_cancel.setTag(DialogTags.dlg_confirm_remove_folder_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		 * 4. Add listeners => OnClick
			----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		/*----------------------------
		 * 5. Show dialog
			----------------------------*/
		dlg.show();

		
	}//public static void dlg_removeFolder(Activity actv)

	public static void removeFolder(Activity actv, Dialog dlg) {
		/*----------------------------
		 * Steps
		 * 1. Get folder name
		 * 2. Validate
		 * 3. Remove
		 * 4. Refresh list
		 * 5. Dismiss dialog
		 * 
		 * 6. Drop table
			----------------------------*/
		/*----------------------------
		 * 1. Get folder name
			----------------------------*/
		TextView tv = (TextView) dlg.findViewById(R.id.dlg_confirm_remove_folder_tv_table_name);
		String folderName = tv.getText().toString();
		
		//
		File targetDir = new File(Methods.get_currentPath_from_prefs(actv), folderName);
		
		if (!targetDir.exists()) {
			// debug
			Toast.makeText(actv, "このアイテムは、存在しません", 2000).show();
			
			return;
		}
		
		if (!targetDir.isDirectory()) {
			// debug
			Toast.makeText(actv, "このアイテムは、フォルダではありません", 2000).show();
			
			return;
		}//if (!targetDir.exists() || !targetDir.isDirectory())
		
		/*----------------------------
		 * 3. Remove
			----------------------------*/
		String path = targetDir.getAbsolutePath();
		
		boolean result = deleteDirectory(targetDir);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + result);

		if (result == true) {
			/*----------------------------
			 * 5. Dismiss dialog
				----------------------------*/
			dlg.dismiss();
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Dir => Removed: " + path);
			
			// debug
			Toast.makeText(actv, "削除しました" + path, 3000).show();
		} else {//if (result == true)
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Remove dir => Failed: " + path);
			
			// debug
			Toast.makeText(actv, "削除できませんでした: " + path, 3000).show();
			
			return;
		}//if (result == true)
		
		/*----------------------------
		 * 4. Refresh list
			----------------------------*/
		refreshListView(actv);
		
		/*----------------------------
		 * 6. Drop table
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase db = dbu.getWritableDatabase();
		
//		String tableName = Methods.convertPathIntoTableName(actv, targetDir);
//		String tableName = Methods.convert_path_into_table_name(actv, targetDir.getAbsolutePath());
		String tableName = Methods.convert_prefs_into_path_label(actv, targetDir.getAbsolutePath());
		
		tableName = Methods.convert_path_into_table_name(actv, tableName);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tableName => " + tableName);
		
		dbu.dropTable(actv, db, tableName);

		db.close();
		
		return;
		
	}//public static void removeFolder(Activity actv, Dialog dlg)

	/*----------------------------
	 * deleteDirectory(File target)()
	 * 
	 * 1. REF=> http://www.rgagnon.com/javadetails/java-0483.html
		----------------------------*/
	public static boolean deleteDirectory(File target) {
		
		if(target.exists()) {
			//
			File[] files = target.listFiles();
			
			//
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					
					deleteDirectory(files[i]);
					
				} else {//if (files[i].isDirectory())
					
					String path = files[i].getAbsolutePath();
					
					files[i].delete();
					
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "Removed => " + path);
					
					
				}//if (files[i].isDirectory())
				
			}//for (int i = 0; i < files.length; i++)
			
		}//if(target.exists())
		
		return (target.delete());
	}//public static boolean deleteDirectory(File target)

	public static void sort_tiList(List<TI> tiList) {
		
		Collections.sort(tiList, new Comparator<TI>(){

//			@Override
			public int compare(TI lhs, TI rhs) {
				// TODO 自動生成されたメソッド・スタブ
				
//				return (int) (lhs.getDate_added() - rhs.getDate_added());
				
				return (int) (lhs.getFile_name().compareToIgnoreCase(rhs.getFile_name()));
			}
			
		});//Collections.sort()

	}//public static void sort_tiList(List<ThumbnailItem> tiList)

	public static void sort_tiList_last_viewed_at(List<TI> tiList) {
		
		Collections.sort(tiList, new Comparator<TI>(){

//			@Override
			public int compare(TI ti_1, TI ti_2) {
				/*********************************
				 * memo
				 *********************************/
				long t1 = ti_1.getLast_viewed_at();
				long t2 = ti_2.getLast_viewed_at();
				
//				if (t1 > 0 && t2 > 0) {
				if (t1 > 0 || t2 > 0) {
					
					// REF=> http://stackoverflow.com/questions/4355303/how-can-i-convert-a-long-to-int-in-java
					return (int)(t1 - t2);
					
				} else {//if (t1 == condition)
					
					return ti_1.getFile_name().compareToIgnoreCase(ti_2.getFile_name());
					
				}
				
//				return (int) (ti_1.getDate_added() - rti.getDate_added());
				
//				return (int) (lti.getFile_name().compareToIgnoreCase(rti.getFile_name()));
			}//public int compare(TI lti, TI rti)
			
		});//Collections.sort()

	}//public static void sort_tiList(List<ThumbnailItem> tiList)

	public static boolean set_pref(Activity actv, String pref_name, String value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putString(pref_name, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static String get_pref(Activity actv, String pref_name, String defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * Return
			----------------------------*/
		return prefs.getString(pref_name, defValue);

	}//public static boolean set_pref(String pref_name, String value)

	public static boolean set_pref(Activity actv, String pref_name, int value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putInt(pref_name, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static boolean set_pref(Activity actv, String pref_name, String pref_key, int value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putInt(pref_key, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static int get_pref(Activity actv, String pref_name, int defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * Return
			----------------------------*/
		return prefs.getInt(pref_name, defValue);

	}//public static boolean set_pref(String pref_name, String value)

	public static int get_pref(Activity actv, String pref_name, String pref_key, int defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * Return
			----------------------------*/
		return prefs.getInt(pref_key, defValue);

	}//public static boolean set_pref(String pref_name, String value)

	public static void dlg_moveFiles(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Get generic dialog
		 * 2. Get dir list
		 * 2-1. Set list to the adapter
		 * 3. Set adapter to the list view
		 * 4. Set listener to the view
		 * 
		 * 9. Show dialog
			----------------------------*/
		
		Dialog dlg = dlg_template_cancel(
				// Activity, layout, title
				actv, R.layout.dlg_move_files, R.string.thumb_actv_menu_move_files,
				// Ok button, Cancel button
				R.id.dlg_move_files_bt_cancel,
				// Ok tag, Cancel tag
				DialogTags.dlg_generic_dismiss
							);
		
		/*----------------------------
		 * 2. Get dir list
			----------------------------*/
		File[] files = new File(MainActv.dirPath_base).listFiles(new FileFilter(){

//			@Override
			public boolean accept(File pathname) {
				// TODO 自動生成されたメソッド・スタブ
				
				return pathname.isDirectory();
			}
			
		});//File[] files
		
		TNActv.fileNameList = new ArrayList<String>();
		
//		for (String fileName : fileNames) {
		for (File eachFile : files) {
			
//			fileNameList.add(fileName);
			TNActv.fileNameList.add(eachFile.getName());
			
		}//for (String fileName : fileNames)
		
		Collections.sort(TNActv.fileNameList);
		
		/*----------------------------
		 * 2-1. Set list to the adapter
			----------------------------*/
		TNActv.dirListAdapter = new ArrayAdapter<String>(
												actv,
												android.R.layout.simple_list_item_1,
												TNActv.fileNameList
											);
		
		/*----------------------------
		 * 3. Set adapter to the list view
			----------------------------*/
		//
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_move_files_lv_list);
		
		lv.setAdapter(TNActv.dirListAdapter);
		
		/*----------------------------
		 * 4. Set listener to the view
		 * 		1. onClick
		 * 		2. onLongClick
			----------------------------*/
		lv.setTag(Methods.DialogItemTags.dlg_move_files);

		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/*----------------------------
		 * 4.2. onLongClick
			----------------------------*/
//		lv.setTag(Methods.DialogItemTags.dlg_move_files);
		
		lv.setOnItemLongClickListener(
						new DialogOnItemLongClickListener(
												actv,
												dlg,
												TNActv.dirListAdapter, TNActv.fileNameList));
		
		/*----------------------------
		 * 9. Show dialog
			----------------------------*/
		dlg.show();
		
	}//public static void dlg_moveFiles(Activity actv)

	public static Dialog dlg_template_cancel(Activity actv, int layoutId, int titleStringId,
			int cancelButtonId, DialogTags cancelTag) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_cancel.setTag(cancelTag);
		
		//
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static Dialog dlg_template_okCancel(Activity actv, int layoutId, int titleStringId,
			int okButtonId, int cancelButtonId, DialogTags okTag, DialogTags cancelTag) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static void dlg_confirm_moveFiles(Activity actv, Dialog dlg, String folderPath) {
		/*----------------------------
		 * Steps
		 * 1. Get a confirm dialog
		 * 2. Set a chosen folder name to the view
		 * 9. Show dialog
			----------------------------*/
		/*----------------------------
		* 1. Get a confirm dialog
			* 1. Set up
			* 2. Add listeners => OnTouch
			* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_confirm_move_files);
		
		// Title
		dlg2.setTitle(R.string.generic_tv_confirm);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_confirm_move_files_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_confirm_move_files_btn_cancel);
		
		//
		btn_ok.setTag(DialogTags.dlg_confirm_move_files_ok);
		btn_cancel.setTag(DialogTags.dlg_generic_dismiss_second_dialog);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
				
		/*----------------------------
		 * 2. Set a chosen folder name to the view
			----------------------------*/
		TextView tv_folder_name = (TextView) dlg2.findViewById(R.id.dlg_confirm_move_files_tv_table_name);
		
		tv_folder_name.setText(folderPath);
		
		/*----------------------------
		 * 9. Show dialog
			----------------------------*/
		dlg2.show();
		
	}//public static void dlg_confirm_moveFiles(Activity actv, Dialog dlg)

	public static void moveFiles(Activity actv, Dialog dlg1, Dialog dlg2) {
		/*----------------------------
		 * Steps
		 * 1. Move files
		 * 2. Update the list view
		 * 2-2. Update preference for highlighting a chosen item
		 * 3. Dismiss dialogues
			----------------------------*/
		/*----------------------------
		 * 1. Move files
		 * 		1.1. Prepare toMoveFiles
		 * 		1.2. Get target dir path from dlg2
		 * 		1.3. Insert items in toMoveFiles to the new table
		 * 		1.4. Delete the items from the source table
			----------------------------*/
		List<TI> toMoveFiles = Methods.moveFiles_1_get_toMoveFiles();
		
		/*----------------------------
		 * 1.2. Get target dir path from dlg2
			----------------------------*/
		TextView tv = (TextView) dlg2.findViewById(R.id.dlg_confirm_move_files_tv_table_name);
		
		String folderPath = tv.getText().toString();
		
		File f = new File(MainActv.dirPath_base, folderPath);
		
//		String targetTableName = Methods.convert_path_into_table_name(actv, folderPath);
		String targetTableName = Methods.convert_filePath_into_table_name(actv, f.getAbsolutePath());
		
		String sourceTableName = Methods.convert_path_into_table_name(actv);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "folderPath => " + folderPath);

		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "targetTableName => " + targetTableName);
		
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sourceTableName => " + sourceTableName);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "prefs_current_path: " + Methods.get_pref(actv, MainActv.prefs_current_path, "NO DATA"));
		
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "f.getAbsolutePath(): " + f.getAbsolutePath());
		
		/*----------------------------
		 * 1.3. Insert items in toMoveFiles to the new table
		 * 		1.3.1. Insert data to the new table
			----------------------------*/
		/*----------------------------
		 * 1.3.1. Insert data to the new table
		 * 		1. Set up db
		 * 		2. Table exists?
		 * 		2-2. If no, create one
		 * 		3. Get item from toMoveFiles
		 * 
		 * 		4. Insert data into the new table
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*----------------------------
		 * 1.3.1.2. Table exists?
			----------------------------*/
		boolean res = moveFiles_2_table_exists(actv, wdb, dbu, targetTableName);
		
		if (res == false) {
			
			return;
			
		}//if (res == false)
		
		/*----------------------------
		 * 1.3.1.3. Get item from toMoveFiles
			----------------------------*/
		for (TI ti : toMoveFiles) {
			
			/*----------------------------
			 * 1.3.4. Insert data into the new table
				----------------------------*/
			dbu.insertData(wdb, targetTableName, ti);
			
			deleteItem_fromTable(actv, sourceTableName, ti);
			
		}//for (ThumbnailItem thumbnailItem : toMoveFiles)
		
		
		/*----------------------------
		 * 1.4. Delete the items from the source table
		 * 		1. Delete data from the source table
		 * 		2. Delete the item from tiList
		 * 
		 * 		9. Close db
			----------------------------*/
		/*----------------------------
		 * 1.4.2. Delete the item from tiList
			----------------------------*/
		for (Integer position : TNActv.checkedPositions) {
			
			TNActv.tiList.remove(position);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Removed from tiList at position=" + position);
			
			
		}//for (Integer position : ThumbnailActivity.checkedPositions)
		
		/*----------------------------
		 * 1.4.9. Close wdb
			----------------------------*/
		wdb.close();
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "wdb => Closed");
//		
//		/*----------------------------
//		 * 2. Update the list view
//			----------------------------*/
//		ThumbnailActivity.checkedPositions.clear();
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "checkedPositions => Cleared");
//		
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", 
//				"ThumbnailActivity.checkedPositions.size() => " + 
//				ThumbnailActivity.checkedPositions.size());
//		
//		ThumbnailActivity.aAdapter.notifyDataSetChanged();
//		
////		ThumbnailActivity.bAdapter.notifyDataSetChanged();
//		ThumbnailActivity.bAdapter =
//				new TIListAdapter(
//						actv, 
//						ifm8.main.R.layout.thumb_activity, 
////						ThumbnailActivity.tiList);
//						ThumbnailActivity.tiList,
//						Methods.MoveMode.ON);
//
//		((ListActivity) actv).setListAdapter(ThumbnailActivity.bAdapter);
//		
//		
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "aAdapter => Notified");
//		
//		/*----------------------------
//		 * 2-2. Update preference for highlighting a chosen item
//			----------------------------*/
////		SharedPreferences prefs = 
////				actv.getSharedPreferences(
//////						"thumb_actv", 
////						Methods.PrefenceLabels.thumb_actv.name(),
////						ThumbnailActivity.MODE_PRIVATE);
////
////		SharedPreferences.Editor editor = prefs.edit();
////
////		int savedPosition = prefs.getInt(Methods.PrefenceLabels.chosen_list_item.name(), -1);
//		
		/*----------------------------
		 * 3. Dismiss dialogues
			----------------------------*/
		dlg1.dismiss();
		dlg2.dismiss();
		
	}//public static void moveFiles(Activity actv, Dialog dlg1, Dialog dlg2)

	private static boolean moveFiles_2_table_exists(Activity actv, 
					SQLiteDatabase wdb, DBUtils dbu, String targetTableName) {
		
		boolean result = dbu.tableExists(wdb, targetTableName);
		
		if (result == false) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + targetTableName);
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Now I create one");
			
			/*----------------------------
			 * 1.3.2-2. If no, create one
				----------------------------*/
			result = dbu.createTable(
								wdb, targetTableName, 
								dbu.get_cols(), dbu.get_col_types());
			
			if (result == false) {
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: " + targetTableName);
				
				wdb.close();
				
				return false;
				
			} else {//if (result == false)
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + targetTableName);
				
				return true;
				
			}//if (result == false)
			
		} else {//if (result == true)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + targetTableName);
			
			return true;
			
		}//if (result == true)
	}//private static boolean moveFiles_2_table_exists()

	public static List<TI> moveFiles_1_get_toMoveFiles() {
		/*----------------------------
		 * 1. Move files
		 * 		1.1. Prepare toMoveFiles
		 * 		1.2. Get target dir path from dlg2
		 * 		1.3. Insert items in toMoveFiles to the new table
		 * 		1.4. Delete the items from the source table
			----------------------------*/
		//
		List<TI> toMoveFiles = new ArrayList<TI>();
		
		for (int position : TNActv.checkedPositions) {
			
			toMoveFiles.add(TNActv.tiList.get(position));
			
		}//for (int position : ThumbnailActivity.checkedPositions)
		
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "toMoveFiles.size() => " + toMoveFiles.size());
		
		for (TI thumbnailItem : toMoveFiles) {
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "thumbnailItem.getFile_name() => " + thumbnailItem.getFile_name());
		}
		
		return toMoveFiles;
		
	}//public static List<TI> moveFiles_1_get_toMoveFiles(Activity actv, Dialog dlg1, Dialog dlg2)

	public static void deleteItem_fromTable(Activity actv, String tableName, TI ti) {
		/*----------------------------
		 * 1. db setup
		 * 2. Delete data
		 * 3. Close db
		 * 4. If unsuccesful, toast a message (Not dismiss the dialog)
		 * 4-2. If successful, delete the item from tiList, as well, and,
		 * #4-3. Notify adapter
		 * 5. Dismiss dialog
			----------------------------*/
		
		/*----------------------------
		 * 1. db setup
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		 * 2. Delete data
			----------------------------*/
		boolean result = dbu.deleteData(
							actv,
							wdb, 
//							Methods.convertPathIntoTableName(actv),
							tableName,
							ti.getFileId());
		
		/*----------------------------
		 * 3. Close db
			----------------------------*/
		wdb.close();
		
		/*----------------------------
		 * 4. If unsuccesful, toast a message (Not dismiss the dialog)
			----------------------------*/
		if (result == false) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Data wasn't deleted: " + ti.getFile_name());
			
		} else if (result == true) {//if (result == true)
			/*----------------------------
			 * 4-2. If successful, delete the item from tiList, as well
				----------------------------*/
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Data was deleted: " + ti.getFile_name());

//			ThumbnailActivity.tiList.remove(position);
			
//			// Log
//			Log.d("DialogOnItemClickListener.java"
//					+ "["
//					+ Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "Data removed from tiList => " + ti.getFile_name());
			
//			/*----------------------------
//			 * 4-3. Notify adapter
//				----------------------------*/
//			ThumbnailActivity.aAdapter.notifyDataSetChanged();
			
		}//if (result == true)
		
//		/*----------------------------
//		 * 5. Notify adapter
//			----------------------------*/
//		ThumbnailActivity.aAdapter.notifyDataSetChanged();
//		
//		/*----------------------------
//		 * 5. Dismiss dialog
//			----------------------------*/
//		dlg.dismiss();

	}//public static void deleteItem_fileId(Activity actv, TI ti, int position)

	public static void dlg_addMemo(Activity actv, long file_id, String tableName) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 1-2. Set text to edit text
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. GridView
		 * 
		 * 8. Close db
		 * 9. Show dialog
			----------------------------*/
		Dialog dlg = Methods.dlg_addMemo_1_get_dialog(actv, file_id, tableName);

		/*----------------------------
		 * 4. GridView
		 * 	1. Set up db
		 * 	2. Get cursor
		 * 	3. Get list
		 * 	4. Adapter
		 * 	5. Set adapter to view
		 * 6. Set listener
			----------------------------*/
		dlg = dlg_addMemo_2_set_gridview(actv, dlg);
		
		dlg.show();
		
	}//public static void dlg_addMemo(Activity actv, long file_id, String tableName)

	
	
	public static Dialog dlg_addMemo_1_get_dialog(Activity actv, long file_id, String tableName) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 1-2. Set text to edit text
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. GridView
		 * 
		 * 8. Close db
		 * 9. Show dialog
			----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(R.layout.dlg_add_memos);
		
		// Title
		dlg.setTitle(R.string.dlg_add_memos_tv_title);
		
		/*----------------------------
		 * 1-2. Set text to edit text
			----------------------------*/
		TI ti = getData(actv, tableName, file_id);
		
		EditText et = (EditText) dlg.findViewById(R.id.dlg_add_memos_et_content);
		
		if (ti.getMemo() != null) {
			
			et.setText(ti.getMemo());
			
			et.setSelection(ti.getMemo().length());
			
		} else {//if (ti.getMemo() != null)
			
			et.setSelection(0);
			
		}//if (ti.getMemo() != null)
		
		/*----------------------------
		 * 2. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_add = (Button) dlg.findViewById(R.id.dlg_add_memos_bt_add);
		Button btn_cancel = (Button) dlg.findViewById(R.id.dlg_add_memos_cancel);
		
		Button btn_patterns = (Button) dlg.findViewById(R.id.dlg_add_memos_bt_patterns);
		
		// Tags
		btn_add.setTag(DialogTags.dlg_add_memos_bt_add);
		btn_cancel.setTag(DialogTags.dlg_generic_dismiss);
		
		btn_patterns.setTag(DialogTags.dlg_add_memos_bt_patterns);
		
		//
		btn_add.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		btn_patterns.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		 * 3. Add listeners => OnClick
			----------------------------*/
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file_id => " + file_id);
		
		
		//
//		btn_add.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_add.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, file_id, tableName));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		btn_patterns.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));

		
		return dlg;
		
	}//public static Dialog dlg_addMemo(Activity actv, long file_id, String tableName)

	public static Dialog dlg_addMemo_2_set_gridview(Activity actv, Dialog dlg) {
		/*----------------------------
		 * 4.1. Set up db
			----------------------------*/
		GridView gv = (GridView) dlg.findViewById(R.id.dlg_add_memos_gv);
		
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/*----------------------------
		 * 4.2. Get cursor
		 * 		1. Table exists?
		 * 		2. Get cursor
			----------------------------*/
		/*----------------------------
		 * 4.2.1. Table exists?
			----------------------------*/
		String tableName = MainActv.tableName_memo_patterns;
		
		boolean res = dbu.tableExists(rdb, tableName);
		
		if (res == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);
			
			rdb.close();
			
//			return;
			
		} else {//if (res == false)
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			rdb.close();
			
			SQLiteDatabase wdb = dbu.getWritableDatabase();
			
			res = dbu.createTable(wdb, tableName, DBUtils.cols_memo_patterns, DBUtils.col_types_memo_patterns);
			
			if (res == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + tableName);
				
				wdb.close();
				
			} else {//if (res == true)
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + tableName);
				
				wdb.close();
				
				return dlg;
				
			}//if (res == true)

			
		}//if (res == false)
		
		
		/*----------------------------
		 * 4.2.2. Get cursor
			----------------------------*/
		rdb = dbu.getReadableDatabase();
		
		String sql = "SELECT * FROM " + tableName + " ORDER BY word ASC";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		c.moveToFirst();
		
		/*----------------------------
		 * 4.3. Get list
			----------------------------*/
		List<String> patternList = new ArrayList<String>();
		
		if (c.getCount() > 0) {
			
			for (int i = 0; i < c.getCount(); i++) {
				
				patternList.add(c.getString(1));
				
				c.moveToNext();
				
			}//for (int i = 0; i < patternList.size(); i++)
			
		} else {//if (c.getCount() > 0)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "!c.getCount() > 0");
			
		}//if (c.getCount() > 0)
		
		
		Collections.sort(patternList);

		/*----------------------------
		 * 4.4. Adapter
			----------------------------*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										actv,
										R.layout.add_memo_grid_view,
										patternList
										);
		
		/*----------------------------
		 * 4.5. Set adapter to view
			----------------------------*/
		gv.setAdapter(adapter);
		
		/*----------------------------
		 * 4.6. Set listener
			----------------------------*/
//		gv.setTag(DialogTags.dlg_add_memos_gv);
		gv.setTag(Methods.DialogItemTags.dlg_add_memos_gv);
		
		gv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "GridView setup => Done");
		
		/*----------------------------
		 * 8. Close db
			----------------------------*/
		rdb.close();
		
		/*----------------------------
		 * 9. Show dialog
			----------------------------*/
//		dlg.show();
		
		return dlg;
		
	}//public static Dialog dlg_addMemo(Activity actv, long file_id, String tableName)

	public static TI getData(Activity actv, String tableName, long file_id) {

		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		TI ti = dbu.getData(actv, rdb, tableName, file_id);
		
		rdb.close();
		
		return ti;
		
	}//public ThumbnailItem getData(Activity actv, String tableName, long file_id)

	public static void addMemo(Activity actv, Dialog dlg, long file_id, String tableName) {
		/*----------------------------
		 * Steps
		 * 1. Get tuhumbnail item
		 * 1-2. Get text from edit text
		 * 2. Set memo
		 * 3. Update db
		 * 
		 * 4. Refresh thumbnails list
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		TI ti = dbu.getData(actv, rdb, tableName, file_id);
		
		rdb.close();
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "DB => closed");
//		
//		toastAndLog(actv, ti.getFile_name() + "/" + "memo=" + ti.getMemo(), 2000);
		
		/*----------------------------
		 * 1-2. Get text from edit text
			----------------------------*/
		EditText et = (EditText) dlg.findViewById(R.id.dlg_add_memos_et_content);
		
		/*----------------------------
		 * 2. Set memo
			----------------------------*/
//		ti.setMemo("abcdefg");
//		ti.setMemo("123456");
//		ti.setMemo("WHERE句を省略した場合はテーブルに含まれる全てのデータの指定のカラムの値が指定の値で更新されます。");
		
		ti.setMemo(et.getText().toString());
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "New memo => " + et.getText().toString());
		
		/*----------------------------
		 * 3. Update db
			----------------------------*/
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		boolean result = dbu.updateData_memos(actv, wdb, tableName, ti);
		
		wdb.close();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "wdb => Closed");
		
		if (result) {
			
			dlg.dismiss();

			// debug
			Toast.makeText(actv, "Memo => Stored", 3000).show();

		} else {//if (result)
			
			return;
			
		}//if (result)
		
		/*----------------------------
		 * 4. Refresh thumbnails list
			----------------------------*/
//		refreshTIList(actv);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Calling: Methods.refreshListView(actv)");
				+ "]", "Calling: Methods.refresh_tilist(actv)");
		
		
//		Methods.refreshListView(actv);
		Methods.refresh_tilist(actv);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "TIList => Refreshed");
		
	}//public static void addMemo()

	private static void refresh_tilist(Activity actv) {
		/*----------------------------
		 * 1. Get table name
		 * 2. Clear tiList
		 * 3. Get data to list
		 * 
		 * 4. Sort list
		 * 5. Notify to adapter
			----------------------------*/
		
		String currentPath = Methods.get_currentPath_from_prefs(actv);
		
		String tableName = Methods.convert_filePath_into_table_name(actv, currentPath);

		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "currentPath: " + currentPath);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tableName: " + tableName);
		
		
		/*----------------------------
		 * 2. Clear tiList
			----------------------------*/
		TNActv.tiList.clear();
		
		/*----------------------------
		 * 3. Get data to list
			----------------------------*/
		if (TNActv.long_searchedItems == null) {

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TNActv.long_searchedItems == null");
			
			
//			TNActv.tiList = Methods.getAllData(actv, tableName);
			TNActv.tiList.addAll(Methods.getAllData(actv, tableName));
			
		} else {//if (long_searchedItems == null)

//			tiList = Methods.convert_fileIdArray2tiList(actv, "IFM8", long_searchedItems);
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TNActv.long_searchedItems != null");
			
		}//if (long_searchedItems == null)

		/*----------------------------
		 * 4. Sort list
			----------------------------*/
		Methods.sort_tiList(TNActv.tiList);
		
		
		/*----------------------------
		 * 5. Notify to adapter
			----------------------------*/
		TNActv.aAdapter.notifyDataSetChanged();
		
	}//private static void refresh_tilist(Activity actv)

	public static void dlg_register_patterns(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Dialog
		 * 9. Show
			----------------------------*/
		Dialog dlg = dlg_template_okCancel(
					actv, R.layout.dlg_register_patterns, R.string.dlg_register_patterns_title,
				R.id.dlg_register_patterns_btn_create, R.id.dlg_register_patterns_btn_cancel, 
				DialogTags.dlg_register_patterns_register, DialogTags.dlg_generic_dismiss);
		
		
		/*----------------------------
		 * 9. Show
			----------------------------*/
		dlg.show();
	}//public static void dlg_register_patterns(Activity actv)

	public static void dlg_register_patterns(Activity actv, Dialog dlg) {
		/*----------------------------
		 * Steps
		 * 1. Dialog
		 * 9. Show
			----------------------------*/
//		Dialog dlg2 = dlg_template_okCancel(
//					actv, , ,
//				, , 
//				, );
		
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_register_patterns);
		
		// Title
		dlg2.setTitle(R.string.dlg_register_patterns_title);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_register_patterns_btn_create);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_register_patterns_btn_cancel);
		
		//
		btn_ok.setTag(DialogTags.dlg_register_patterns_register);
		btn_cancel.setTag(DialogTags.dlg_generic_dismiss_second_dialog);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		
		/*----------------------------
		 * 9. Show
			----------------------------*/
		dlg2.show();
		
	}//public static void dlg_register_patterns(Activity actv)

	public static void dlg_register_patterns_isInputEmpty(Activity actv, Dialog dlg) {
		/*----------------------------
		 * Steps
		 * 1. Get views
		 * 2. Prepare data
		 * 3. Register data
		 * 4. Dismiss dialog
			----------------------------*/
		// Get views
		EditText et_word = (EditText) dlg.findViewById(R.id.dlg_register_patterns_et_word);
		EditText et_table_name = 
					(EditText) dlg.findViewById(R.id.dlg_register_patterns_et_table_name);
		
		if (et_word.getText().length() == 0) {
			// debug
			Toast.makeText(actv, "語句を入れてください", 3000).show();
			
			return;
		}// else {//if (et_column_name.getText().length() == 0)
		
		/*----------------------------
		 * 2. Prepare data
			----------------------------*/
		//
		String word = et_word.getText().toString();
		String table_name = et_table_name.getText().toString();
		
		/*----------------------------
		 * 3. Register data
			----------------------------*/
		boolean result = insertDataIntoDB(actv, DBUtils.table_name_memo_patterns, 
								DBUtils.cols_memo_patterns, new String[]{word, table_name});
		
		/*----------------------------
		 * 4. Dismiss dialog
			----------------------------*/
		if (result == true) {
		
			dlg.dismiss();
			
		} else {//if (result == true)

			// debug
			Toast.makeText(actv, "メモを保管できませんでした", 3000).show();

		}//if (result == true)
		
		
	}//public static void dlg_register_patterns_isInputEmpty(Activity actv, Dialog dlg)

	public static void dlg_register_patterns_isInputEmpty(Activity actv, Dialog dlg, Dialog dlg2) {
		/*----------------------------
		 * Steps
		 * 1. Get views
		 * 2. Prepare data
		 * 3. Register data
		 * 4. Dismiss dialog
			----------------------------*/
		// Get views
		EditText et_word = (EditText) dlg2.findViewById(R.id.dlg_register_patterns_et_word);
		EditText et_table_name = 
					(EditText) dlg2.findViewById(R.id.dlg_register_patterns_et_table_name);
		
		if (et_word.getText().length() == 0) {
			// debug
			Toast.makeText(actv, "語句を入れてください", 3000).show();
			
			return;
		}// else {//if (et_column_name.getText().length() == 0)
		
		/*----------------------------
		 * 2. Prepare data
			----------------------------*/
		//
		String word = et_word.getText().toString();
		String table_name = et_table_name.getText().toString();
		
		/*----------------------------
		 * 3. Register data
			----------------------------*/
		boolean result = insertDataIntoDB(actv, DBUtils.table_name_memo_patterns, 
								DBUtils.cols_memo_patterns, new String[]{word, table_name});
		
		/*----------------------------
		 * 4. Dismiss dialog
			----------------------------*/
		if (result == true) {
		
			dlg.dismiss();
			dlg2.dismiss();
			
			// debug
			Toast.makeText(actv, "定型句を保管しました", 3000).show();
			
		} else {//if (result == true)

			// debug
			Toast.makeText(actv, "定型句を保管できませんでした", 3000).show();

		}//if (result == true)
		
		
	}//public static void dlg_register_patterns_isInputEmpty(Activity actv, Dialog dlg)

	public static void add_pattern_to_text(Dialog dlg, int position, String word) {
		
		EditText et = (EditText) dlg.findViewById(R.id.dlg_add_memos_et_content);
		
		String content = et.getText().toString();
		
		content += word + " ";
		
		et.setText(content);
		
		et.setSelection(et.getText().toString().length());
		
	}//public static void add_pattern_to_text(Dialog dlg, int position, String word)

	public static void dlg_db_activity(Activity actv) {
		/*----------------------------
		 * 1. Dialog
		 * 2. Prep => List
		 * 3. Adapter
		 * 4. Set adapter
		 * 
		 * 5. Set listener to list
		 * 6. Show dialog
			----------------------------*/
		Dialog dlg = Methods.dlg_template_cancel(
									actv, R.layout.dlg_db_admin, 
									R.string.dlg_db_admin_title, 
									R.id.dlg_db_admin_bt_cancel, 
									Methods.DialogTags.dlg_generic_dismiss);
		
		/*----------------------------
		 * 2. Prep => List
			----------------------------*/
		String[] choices = {
										actv.getString(R.string.dlg_db_admin_item_backup_db),
										actv.getString(R.string.dlg_db_admin_item_refresh_db)
										};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
			
			list.add(item);
			
		}
		
		/*----------------------------
		 * 3. Adapter
			----------------------------*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actv,
//				R.layout.dlg_db_admin,
				android.R.layout.simple_list_item_1,
				list
				);

		/*----------------------------
		 * 4. Set adapter
			----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_db_admin_lv);
		
		lv.setAdapter(adapter);
		
		/*----------------------------
		 * 5. Set listener to list
			----------------------------*/
		lv.setTag(Methods.DialogItemTags.dlg_db_admin_lv);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/*----------------------------
		 * 6. Show dialog
			----------------------------*/
		dlg.show();
		
		
	}//public static void dlg_db_activity(Activity actv)

	public static void db_backup(Activity actv, Dialog dlg) {
		/*----------------------------
		 * 1. Prep => File names
		 * 2. Prep => Files
		 * 2-2. Folder exists?
		 * 3. Copy
			----------------------------*/
		String time_label = Methods.get_TimeLabel(Methods.getMillSeconds_now());
		
		String db_src = StringUtils.join(new String[]{MainActv.dirPath_db, MainActv.fileName_db}, File.separator);
		
		String db_dst = StringUtils.join(new String[]{MainActv.dirPath_db_backup, MainActv.fileName_db_backup_trunk}, File.separator);
		db_dst = db_dst + "_" + time_label + MainActv.fileName_db_backup_ext;
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "db_src: " + db_src + " * " + "db_dst: " + db_dst);
		
		/*----------------------------
		 * 2. Prep => Files
			----------------------------*/
		File src = new File(db_src);
		File dst = new File(db_dst);
		
		/*----------------------------
		 * 2-2. Folder exists?
			----------------------------*/
		File db_backup = new File(MainActv.dirPath_db_backup);
		
		if (!db_backup.exists()) {
			
			try {
				db_backup.mkdir();
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Folder created: " + db_backup.getAbsolutePath());
			} catch (Exception e) {
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create folder => Failed");
				
				return;
				
			}
			
		} else {//if (!db_backup.exists())
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Folder exists: ");
			
		}//if (!db_backup.exists())
		
		/*----------------------------
		 * 3. Copy
			----------------------------*/
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
			
			// debug
			Toast.makeText(actv, "DB backup => Done", 3000).show();

			dlg.dismiss();
			
		} catch (FileNotFoundException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
		}//try

		
	}//public static void db_backup(Activity actv, Dialog dlg, String item)

	public static void dlg_seratchItem(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Dialog
		 * 9. Show
			----------------------------*/
		Dialog dlg = dlg_template_okCancel(
								actv, R.layout.dlg_search, R.string.dlg_search_title,
				R.id.dlg_search_bt_ok, R.id.dlg_search_cancel, DialogTags.dlg_search_ok, DialogTags.dlg_generic_dismiss);
		
		/*----------------------------
		 * 9. Show
			----------------------------*/
		dlg.show();
		
	}//public static void dlg_seratchItem(Activity actv)

	public static long getMillSeconds(int year, int month, int date) {
		// Calendar
		Calendar cal = Calendar.getInstance();
		
		// Set time
		cal.set(year, month, date);
		
		// Date
		Date d = cal.getTime();
		
		return d.getTime();
		
	}//private long getMillSeconds(int year, int month, int date)

	/****************************************
	 *	getMillSeconds_now()
	 * 
	 * <Caller> 
	 * 1. ButtonOnClickListener # case main_bt_start
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static long getMillSeconds_now() {
		
		Calendar cal = Calendar.getInstance();
		
		return cal.getTime().getTime();
		
	}//private long getMillSeconds_now(int year, int month, int date)

	public static String get_TimeLabel(long millSec) {
		
		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		 
		return sdf1.format(new Date(millSec));
		
	}//public static String get_TimeLabel(long millSec)

	public static void dlg_patterns(Activity actv) {
		/*----------------------------
		 * memo
			----------------------------*/
		Dialog dlg = Methods.dlg_template_cancel(
													actv, R.layout.dlg_admin_patterns, 
													R.string.dlg_memo_patterns_title, 
													R.id.dlg_admin_patterns_bt_cancel, 
													Methods.DialogTags.dlg_generic_dismiss);
		
		/*----------------------------
		 * 2. Prep => List
			----------------------------*/
		String[] choices = {
				actv.getString(R.string.generic_tv_register),
				actv.getString(R.string.generic_tv_edit),
				actv.getString(R.string.generic_tv_delete)
		};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
			
			list.add(item);
			
		}
		
		/*----------------------------
		 * 3. Adapter
			----------------------------*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actv,
//				R.layout.dlg_db_admin,
				android.R.layout.simple_list_item_1,
				list
				);

		/*----------------------------
		 * 4. Set adapter
			----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_admin_patterns_lv);
		
		lv.setAdapter(adapter);

		/*----------------------------
		 * 5. Set listener to list
			----------------------------*/
		lv.setTag(Methods.DialogItemTags.dlg_admin_patterns_lv);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/*----------------------------
		 * 6. Show dialog
			----------------------------*/
		dlg.show();
		
	}//public static void dlg_patterns(Activity actv)

	
	public static void dlg_delete_patterns(Activity actv, Dialog dlg) {
		/*----------------------------
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. Prep => List
		 * 5. Prep => Adapter
		 * 6. Set adapter
		 * 
		 * 7. Show dialog
			----------------------------*/
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_delete_patterns);
		
		// Title
		dlg2.setTitle(R.string.dlg_delete_patterns_title);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_delete_patterns_bt_cancel);
		
		//
		btn_cancel.setTag(Methods.DialogTags.dlg_generic_dismiss_second_dialog);
		
		//
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2));
		
		/*----------------------------
		 * 4. Prep => List
		 * 5. Prep => Adapter
		 * 6. Set adapter
			----------------------------*/
		GridView gv = dlg_delete_patterns_2_grid_view(actv, dlg, dlg2);

		/*----------------------------
		 * 7. Show dialog
			----------------------------*/
		dlg2.show();
		
	}//public static void dlg_delete_patterns(Activity actv, Dialog dlg)

	private static GridView dlg_delete_patterns_2_grid_view(Activity actv, Dialog dlg, Dialog dlg2) {
		/*----------------------------
		 * 1. Set up db
		 * 1-1. Get grid view
		 * 2. Table exists?
		 * 3. Get cursor
		 * 
		 * 4. Get list
		 * 5. Prep => Adapter
		 * 6. Set adapter to view
		 * 
		 * 7. Set listener
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/*----------------------------
		 * 1-1. Get grid view
			----------------------------*/
		GridView gv = (GridView) dlg2.findViewById(R.id.dlg_delete_patterns_gv);
		
		/*----------------------------
		 * 2. Table exists?
			----------------------------*/
		String tableName = MainActv.tableName_memo_patterns;
		
		boolean res = dbu.tableExists(rdb, tableName);
		
		if (res == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);
			
			rdb.close();
			
//			return;
			
		} else {//if (res == false)
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);
			
			rdb.close();
			
			SQLiteDatabase wdb = dbu.getWritableDatabase();
			
			res = dbu.createTable(wdb, tableName, DBUtils.cols_memo_patterns, DBUtils.col_types_memo_patterns);
			
			if (res == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + tableName);
				
				wdb.close();
				
			} else {//if (res == true)
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + tableName);
				
				wdb.close();
				
				return gv;
				
			}//if (res == true)

			
		}//if (res == false)
		
		/*----------------------------
		 * 3. Get cursor
			----------------------------*/
		rdb = dbu.getReadableDatabase();
		
		String sql = "SELECT * FROM " + tableName + " ORDER BY word ASC";
		
		Cursor c = rdb.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		c.moveToFirst();
		
		/*----------------------------
		 * 4. Get list
			----------------------------*/
		List<String> patternList = new ArrayList<String>();
		
		if (c.getCount() > 0) {
			
			for (int i = 0; i < c.getCount(); i++) {
				
				patternList.add(c.getString(1));
				
				c.moveToNext();
				
			}//for (int i = 0; i < patternList.size(); i++)
			
		} else {//if (c.getCount() > 0)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "!c.getCount() > 0");
			
		}//if (c.getCount() > 0)
		
		
		Collections.sort(patternList);

		/*----------------------------
		 * 5. Prep => Adapter
			----------------------------*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										actv,
										R.layout.add_memo_grid_view,
										patternList
										);
		
		/*----------------------------
		 * 6. Set adapter to view
			----------------------------*/
		gv.setAdapter(adapter);
		
		/*----------------------------
		 * 7. Set listener
			----------------------------*/
//		gv.setTag(DialogTags.dlg_add_memos_gv);
		gv.setTag(Methods.DialogItemTags.dlg_delete_patterns_gv);
		
		gv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg, dlg2));
		
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "GridView setup => Done");
		
		/*----------------------------
		 * 8. Close db
			----------------------------*/
		rdb.close();

		return gv;
	}//private static GridView dlg_delete_patterns_2_grid_view(Activity actv, Dialog dlg, Dialog dlg2)

	/****************************************
	 *
	 * 
	 * <Caller> 1. 
	 * 
	 * <Desc> 
	 * 1. dlg	=> 「定型」
	 * 2. dlg2	=> 「定型句　削除」
	 * 3. dlg3	=> 「確認」
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void dlg_confirm_delete_patterns(Activity actv, Dialog dlg,
			Dialog dlg2, String item) {
		/*----------------------------
		 * 1. Set up dialog
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
		 * 
		 * 4. Set pattern name
		 * 5. Show dialog
			----------------------------*/
		/*----------------------------
		 * 1. Set up dialog
			----------------------------*/
		Dialog dlg3 = new Dialog(actv);
		
		//
		dlg3.setContentView(R.layout.dlg_confirm_delete_patterns);
		
		// Title
		dlg3.setTitle(R.string.generic_tv_confirm);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg3.findViewById(R.id.dlg_confirm_delete_patterns_btn_ok);
		Button btn_cancel = (Button) dlg3.findViewById(R.id.dlg_confirm_delete_patterns_btn_cancel);
		
		//
		btn_ok.setTag(Methods.DialogTags.dlg_confirm_delete_patterns_ok);
		btn_cancel.setTag(Methods.DialogTags.dlg_generic_dismiss_third_dialog);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg3));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg3));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2, dlg3));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg, dlg2, dlg3));
		
		/*----------------------------
		 * 4. Set pattern name
			----------------------------*/
		TextView tv = (TextView) dlg3.findViewById(R.id.dlg_confirm_delete_patterns_tv_pattern_name);
		
		tv.setText(item);
		
		/*----------------------------
		 * 5. Show dialog
			----------------------------*/
		dlg3.show();
		
	}//public static void dlg_confirm_delete_patterns()

	public static void delete_patterns(Activity actv, Dialog dlg, Dialog dlg2,
			Dialog dlg3) {
		/*----------------------------
		 * 0. Get pattern name
		 * 1. Set up db
		 * 2. Query
		 * 3. Dismiss dialogues
			----------------------------*/
		/*----------------------------
		 * 0. Get pattern name
			----------------------------*/
		TextView tv = (TextView) dlg3.findViewById(R.id.dlg_confirm_delete_patterns_tv_pattern_name);
		
		String item = tv.getText().toString();
		
		/*----------------------------
		 * 1. Set up db
			----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*----------------------------
		 * 2. Query
			----------------------------*/
		String sql = "DELETE FROM " + MainActv.tableName_memo_patterns +
							" WHERE word='" + item + "'";
		
		try {
			wdb.execSQL(sql);
		
			
			// debug
			Toast.makeText(actv, "Pattern deleted", 3000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Pattern deleted: " + item);

			/*----------------------------
			 * 3. Dismiss dialogues
				----------------------------*/
			dlg3.dismiss();
			dlg2.dismiss();
			dlg.dismiss();
			
		} catch (SQLException e) {
			
			// debug
			Toast.makeText(actv, "パターン削除　=>　できませんでした", 3000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Pattern deletion => Failed:  " + item);
			
		} finally {
			
			wdb.close();
			
		}
		
	}//public static void delete_patterns()

	public static void searchItem(Activity actv, Dialog dlg) {
		/*----------------------------
		 * Steps
		 * 1. Get search words
		 * 2. Format words
		 * 
		 * 2-2. Get table name from current path
		 * 3. Search task
		 * 
		 * 9. Dismiss dialog
			----------------------------*/
		EditText et = (EditText) dlg.findViewById(R.id.dlg_search_et);
		
		String words = et.getText().toString();
		
		if (words.equals("")) {
			
			// debug
			Toast.makeText(actv, "語句を入れてないよ", 2000).show();
			
			return;
			
		}//if (words.equals(""))
		
		/*----------------------------
		 * 2. Format words
			----------------------------*/
		words = words.replace('　', ' ');
		
		String[] a_words = words.split(" ");
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "a_words.length => " + a_words.length);
		
		/*----------------------------
		 * 2-2. Get table name from current path
			----------------------------*/
		String tableName = Methods.convert_path_into_table_name(actv);
		
		/*----------------------------
		 * 3. Search task
			----------------------------*/
		CheckBox cb_all_table = (CheckBox) dlg.findViewById(R.id.dlg_search_cb_all_table);
		
		int search_mode = 0;	// 0 => Specific table (default)
		
		if (cb_all_table.isChecked()) {
			
			search_mode = 1;	// 1 => All tables
			
		}//if (condition)
		
		SearchTask st = new SearchTask(actv, search_mode);
		
//		SearchTask st = new SearchTask(actv);
		
		
		
//		st.execute(a_words);
//		st.execute(a_words, new String[]{"aaa", "bbb", "ccc"});
		st.execute(a_words, new String[]{tableName});
		
		/*----------------------------
		 * 9. Dismiss dialog
			----------------------------*/
		dlg.dismiss();
		
	}//public static void searchItem(Activity actv, Dialog dlg)

	public static List<TI> convert_fileIdArray2tiList(
			Activity actv, String tableName, long[] long_file_id) {
		/*----------------------------
		* Steps
		* 1. DB setup
		* 2. Get ti list
		* 3. Close db
		* 4. Return
		----------------------------*/
		/*----------------------------
		* 1. DB setup
		----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*----------------------------
		* 2. Get ti list
		----------------------------*/
		List<TI> tilist = new ArrayList<TI>();
		
		for (long file_id : long_file_id) {
			
			String sql = "SELECT * FROM " + tableName 
						+ " WHERE file_id = '" + String.valueOf(file_id) + "'";
			
			Cursor c = rdb.rawQuery(sql, null);
			
			if (c.getCount() > 0) {
			
				c.moveToFirst();
				
				tilist.add(Methods.convertCursorToThumbnailItem(c));
				
				c.moveToNext();
				
			}//if (c.getCount() > 0)
		}
		
		// Log
		Log.d("Methods.java" + "["
		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		+ "]", "tilist.size() => " + tilist.size());
		
		/*----------------------------
		* 3. Close db
		----------------------------*/
		rdb.close();
		
		
		/*----------------------------
		* 4. Return
		----------------------------*/
//		return tilist.size() > 0 ? tilist : null;
		return tilist;
	
	}//public static List<ThumbnailItem> convert_fileIdArray2tiList(Activity actv, String tableName, long[] long_file_id)

	public static List<TI> convert_fileIdArray2tiList_all_table(Activity actv,
			long[] long_searchedItems, String[] string_searchedItems_table_names) {
		/*********************************
		 * memo
		 *********************************/
		/*----------------------------
		* Steps
		* 1. DB setup
		* 2. Get ti list
		* 3. Close db
		* 4. Return
		----------------------------*/
		/*----------------------------
		* 1. DB setup
		----------------------------*/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		/*----------------------------
		* 2. Get ti list
		----------------------------*/
		List<TI> tilist = new ArrayList<TI>();
		
		for (int i = 0; i < long_searchedItems.length; i++) {
			
			String sql = "SELECT * FROM " + string_searchedItems_table_names[i] 
						+ " WHERE file_id = '"
						+ String.valueOf(long_searchedItems[i]) + "'";
			
			Cursor c = rdb.rawQuery(sql, null);
			
			if (c.getCount() > 0) {
			
				c.moveToFirst();
				
				tilist.add(Methods.convertCursorToThumbnailItem(c));
				
				c.moveToNext();
				
			}//if (c.getCount() > 0)
			
		}//for (int i = 0; i < long_searchedItems.length; i++)
		
		// Log
		Log.d("Methods.java" + "["
		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		+ "]", "tilist.size() => " + tilist.size());
		
		/*----------------------------
		* 3. Close db
		----------------------------*/
		rdb.close();
		
		
		/*----------------------------
		* 4. Return
		----------------------------*/
		return tilist;
		
	}//public static List<TI> convert_fileIdArray2tiList_all_table()

	/****************************************
	 *
	 * 
	 * <Caller> 1. 
	 * 
	 * <Desc> 
	 * 1. Originally, SearchTask.java was calling this method.
	 * 		But I changed the starategy, ending up not using this method (20120723_145553) 
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static TI convertCursorToThumbnailItem(Cursor c) {
		/*----------------------------
		 * Steps
		 * 1. 
			----------------------------*/
		return new TI(
				c.getLong(1),	// file_id
				c.getString(2),	// file_path
				c.getString(3),	// file_name
				c.getLong(4),	// date_added
//				c.getLong(5)		// date_modified
				c.getLong(5),		// date_modified
				c.getString(6),	// memos
				c.getString(7)	// tags
		);
		
		
	}//public static TI convertCursorToThumbnailItem(Cursor c)

	public static boolean restore_db(Activity actv, String dbName,
				String src, String dst) {
		/*********************************
		 * 1. Setup db
		 * 2. Setup: File paths
		 * 3. Setup: File objects
		 * 4. Copy file
		 * 
		 *********************************/
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		wdb.close();

		/*********************************
		 * 2. Setup: File paths
		 *********************************/
//    	String src = 
//    			"/mnt/sdcard-ext/ShoppingList_backup/shoppinglist_backup_20120906_201402.bk";
//    			"/mnt/sdcard-ext/CR4_backup/cr4_backup_20120907_184555.bk";

    	
//    	String dst =
////    			"/data/data/test.main/databases/shoppinglist.db";
//    			"/data/data/cr4.main/databases/cr4.db";

    	/*********************************
		 * 3. Setup: File objects
		 *********************************/
		File f_src = new File(src);
		File f_dst = new File(dst);

		/*********************************
		 * 4. Copy file
		 *********************************/
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied: " + src);
			
			// debug
			Toast.makeText(actv, "DB restoration => Done", 3000).show();
			
			return true;

		} catch (FileNotFoundException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			return false;
			
		} catch (IOException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			return false;
			
		}//try
		
	}//private boolean restore_db()

	public static void show_history(Activity actv) {
		/*********************************
		 * 1. Set up db
		 * 2. Table exists?
		 * 
		 * 2-2. Number of records more than the set limit?
		 * 
		 * 3. Get all data
		 * 
		 * 3-2. Set pref value => 1
		 * 
		 * 4. Set data to intent
		 * 4-2. Close db
		 * 5. Start activity
		 *********************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting => show_history()");
		
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*********************************
		 * 2. Table exists?
		 *********************************/
		boolean result = dbu.tableExists(wdb, MainActv.tableName_show_history);
		
		if (result == false) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + MainActv.tableName_show_history);
			
			// Create one
			result = dbu.createTable(
											wdb, 
											MainActv.tableName_show_history, 
											MainActv.cols_show_history, 
											MainActv.col_types_show_history);
			
			if (result == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + MainActv.tableName_show_history);
				
			} else {//if (result == true)
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + MainActv.tableName_show_history);
				
				// debug
				Toast.makeText(actv, 
						"Create table failed: " + MainActv.tableName_show_history,
						Toast.LENGTH_SHORT).show();

				wdb.close();
				
				return;
				
			}//if (result == true)
		}//if (result == false)
		
		/*********************************
		 * 2-2. Number of records more than the set limit?
		 *********************************/
		long num_of_records = Methods.get_num_of_entries(actv, MainActv.tableName_show_history);

		SharedPreferences prefs =
				actv.getSharedPreferences(
						actv.getString(R.string.prefs_shared_prefs_name), 0);
		
		int pref_history_size = 
//				(int) prefs.getInt(actv.getString(R.string.prefs_history_size_key), 0);
				prefs.getin;

//		int pref_history_size = prefs.getString(actv.getString(R.string.prefs_history_size_key), null);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "num_of_records=" + num_of_records);
		
		long start_id_num = 0;
		
		if (num_of_records > pref_history_size) {
			
			start_id_num = (long) (num_of_records - pref_history_size);
			
		} else if(num_of_records <= pref_history_size) {//if (num_of_records > )
			
			start_id_num = 0;
			
		}//if (num_of_records > )
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "start_id_num=" + start_id_num);
		
		/*********************************
		 * 3. Get all data
		 *********************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Table exists: " + MainActv.tableName_show_history);
		
		
		// REF=> http://www.accessclub.jp/sql/10.html
		String sql = "SELECT * FROM " + MainActv.tableName_show_history;
		
		Cursor c = wdb.rawQuery(sql, null);
		
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getCount() => " + c.getCount());

		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "No history data");
			
			// debug
			Toast.makeText(actv, "No history data", Toast.LENGTH_SHORT).show();
			
			wdb.close();
			
			return;
			
		} else if (c.getCount() > 0) {//if (tempC.getCount() > 0)
			
			// debug
			Toast.makeText(actv, 
					"Num of history data: " + c.getCount(),
					Toast.LENGTH_SHORT).show();
			
		}//if (tempC.getCount() > 0)
		
		/*********************************
		 * 3-2. Set pref value => 1
		 *********************************/
		result = Methods.set_pref(
							actv, 
							MainActv.prefName_mainActv, 
							MainActv.prefName_mainActv_history_mode,
							MainActv.HISTORY_MODE_ON);

		if (result == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Pref => Set: " + MainActv.HISTORY_MODE_ON);
			
		} else {//if (result == true)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Set pref => Failed");
			
		}//if (result == true)
		
		
		/*********************************
		 * 4. Set data to intent
		 * 	1. Set up intent
		 * 	2. Get data => File ids
		 * 	3. Get data => Table names
		 * 	4. Put data to intent
		 * 	5. Start activity
		 *********************************/
		Intent i = new Intent();
		
		i.setClass(actv, TNActv.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		c.moveToFirst();
		
		/*********************************
		 * 4.2. Get data => File ids
		 * 4.3. Get data => Table names
		 *********************************/
		int data_length = c.getCount();
		
		long[] file_ids = new long[data_length];
		
		String[] table_names = new String[data_length];
		
		for (int j = 0; j < data_length; j++) {
			
			file_ids[j] = c.getLong(3);
			
			table_names[j] = c.getString(4);
			
			c.moveToNext();
			
		}//for (int j = 0; j < data_length; j++)
		
		/*********************************
		 * 4-2. Close db
		 *********************************/
		wdb.close();
		
		/*********************************
		 * 4.4. Put data to intent
		 *********************************/
		i.putExtra(MainActv.intent_label_file_ids, file_ids);
		
		i.putExtra(MainActv.intent_label_table_names, table_names);
		
		/*********************************
		 * 5. Start activity
		 *********************************/
		actv.startActivity(i);

			
//		} else {//if (result != false)
			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table doesn't exist: " + MainActv.tableName_show_history);
//			
//			// Create one
//			result = dbu.createTable(
//											wdb, 
//											MainActv.tableName_show_history, 
//											MainActv.cols_show_history, 
//											MainActv.col_types_show_history);
//			
//			if (result == true) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Table created: " + MainActv.tableName_show_history);
//				
//			} else {//if (result == true)
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Create table failed: " + MainActv.tableName_show_history);
//				
//				// debug
//				Toast.makeText(actv, 
//						"Create table failed: " + MainActv.tableName_show_history,
//						Toast.LENGTH_SHORT).show();
//
//				return;
//				
//			}//if (result == true)
			
//		}//if (result != false)
		

		
	}//public static void show_history(Activity actv)

	public static void save_history(Activity actv, long fileId,
			String table_name) {
		/*********************************
		 * 1. Build data
		 * 2. Set up db
		 * 
		 * 2-2. Table exists?
		 * 
		 * 3. Insert data
		 * 4. Close db
		 *********************************/
		Object[] data = {fileId, table_name};
		
		/*********************************
		 * 2. Set up db
		 *********************************/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 2-2. Table exists?
		 *********************************/
		boolean result = dbu.tableExists(wdb, MainActv.tableName_show_history);
		
		if (result == false) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + MainActv.tableName_show_history);
			
			// Create one
			result = dbu.createTable(
											wdb, 
											MainActv.tableName_show_history, 
											MainActv.cols_show_history, 
											MainActv.col_types_show_history);
			
			if (result == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + MainActv.tableName_show_history);
				
			} else {//if (result == true)
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table failed: " + MainActv.tableName_show_history);
				
				// debug
				Toast.makeText(actv, 
						"Create table failed: " + MainActv.tableName_show_history,
						Toast.LENGTH_SHORT).show();

				wdb.close();
				
				return;
				
			}//if (result == true)
		}//if (result == false)

		
		/*********************************
		 * 3. Insert data
		 *********************************/
		boolean res = DBUtils.insertData_history(actv, wdb, data);
		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "res=" + res);
		
		if (res == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "History saved: fileId=" + fileId);
			
		} else {//if (res == true)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Save history => Failed: " + fileId);
			
		}//if (res == true)
		
		
		/*********************************
		 * 4. Close db
		 *********************************/
		wdb.close();
		
	}//public static void save_history()

	public static List<TI> get_all_data_history(Activity actv,
			long[] history_file_ids, String[] history_table_names) {
		/*********************************
		 * 1. DB setup
		 * 2. Build list
		 *
		 * 2-2. Close db
		 * 
		 * 3. Sort list
		 * 4. Return list
		 *********************************/
		/*********************************
		 * 1. DB setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/*********************************
		 * 2. Build list
		 *********************************/
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Build list");
		
		List<TI> tiList = dbu.get_all_data_history(
									actv,
									rdb,
									history_file_ids,
									history_table_names);
		
		/*********************************
		 * 2-2. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 3. Sort list
		 *********************************/
		if (tiList != null) {
			
			Methods.sort_tiList(tiList);
			
		}//if (tiList == null)
//		Methods.sort_tiList(tiList);
		
		return tiList;
		
	}//public static List<TI> get_all_data_history()

	public static String[] get_column_list(Activity actv, String dbName, String tableName) {
		/*********************************
		 * 1. Set up db
		 * 2. Cursor null?
		 * 3. Get names
		 * 
		 * 4. Close db
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT * FROM " + tableName;
		
		/*********************************
		 * 2. Cursor null?
		 *********************************/
		Cursor c = null;
		
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 3. Get names
		 *********************************/
		String[] column_names = c.getColumnNames();
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return column_names;
		
//		return null;
	}//public static String[] get_column_list(Activity actv, String tableName)

    public static void drop_table(Activity actv, String dbName, String tableName) {
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = 
				dbu.dropTable(actv, wdb, tableName);
		
		if (res == true) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table dropped: " + tableName);
		} else {//if (res == true)

			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Drop table => Failed: " + tableName);
			
		}//if (res == true)
		

		wdb.close();
		
		
	}//private void drop_table(String tableName)

	public static boolean add_column_to_table(Activity actv, String dbName,
			String tableName, String column_name, String data_type) {
		/*********************************
		 * 1. Column already exists?
		 * 2. db setup
		 * 
		 * 3. Build sql
		 * 4. Exec sql
		 * 
		 * 5. Close db
		 *********************************/
		/*********************************
		 * 1. Column already exists?
		 *********************************/
		String[] cols = Methods.get_column_list(actv, dbName, tableName);
		
		//debug
		for (String col_name : cols) {

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col: " + col_name);
			
		}//for (String col_name : cols)

		
		for (String col_name : cols) {
			
			if (col_name.equals(column_name)) {
				
				// debug
				Toast.makeText(actv, "Column exists: " + column_name, Toast.LENGTH_SHORT).show();
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Column exists: " + column_name);
				
				return false;
				
			}
			
		}//for (String col_name : cols)
		
		// debug
		Toast.makeText(actv, "Column doesn't exist: " + column_name, Toast.LENGTH_SHORT).show();
		
		/*********************************
		 * 2. db setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 3. Build sql
		 *********************************/
		// REF[20121001_140817] => http://stackoverflow.com/questions/8291673/how-to-add-new-column-to-android-sqlite-database
		
		String sql = "ALTER TABLE " + tableName + 
					" ADD COLUMN " + column_name + 
					" " + data_type;
		
		/*********************************
		 * 4. Exec sql
		 *********************************/
		try {
//			db.execSQL(sql);
			wdb.execSQL(sql);
			
			// Log
			Log.d(actv.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Column added => " + column_name);

			/*********************************
			 * 5. Close db
			 *********************************/
			wdb.close();
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d(actv.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			/*********************************
			 * 5. Close db
			 *********************************/
			wdb.close();

			return false;
		}//try

		/*********************************
		 * 5. Close db
		 *********************************/


		
	}//public static boolean add_column_to_table()

	public static List<String> get_table_list(Activity actv, String key_word) {
		/*********************************
		 * 1. Set up db
		 * 2. Query
		 * 
		 * 3. Build list
		 *********************************/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/*********************************
		 * 2. Query
		 *********************************/
		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
						" WHERE type = 'table' ORDER BY name";
		
		Cursor c = null;
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		/*********************************
		 * 3. Build list
		 *********************************/
		// Log
		if (c != null) {
			c.moveToFirst();
			
//			String t_name = c.getString(0);
			
			String reg = "IFM9.*";
			
			Pattern p = Pattern.compile(reg);
			Matcher m;// = p.matcher(t_name);

			
			for (int i = 0; i < c.getCount(); i++) {
				//
				String t_name = c.getString(0);
				
				m = p.matcher(t_name);
				
				if (m.find()) {
					
					tableList.add(c.getString(0));
					
				}//if (variable == condition)
//				tableList.add(c.getString(0));
				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "c.getString(0): " + c.getString(0));
				
				
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)

//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount(): " + c.getCount());
//		
		rdb.close();
		
		return tableList;
	}//public static List<String> get_table_list(Activity actv, String key_word)

	
	public static boolean record_history(Activity actv, TI ti) {
		/*********************************
		 * memo
		 *********************************/
		int current_history_mode = Methods.get_pref(
				actv, 
				MainActv.prefName_mainActv, 
				MainActv.prefName_mainActv_history_mode,
				-1);

		if (current_history_mode == MainActv.HISTORY_MODE_OFF) {
			
			Methods.save_history(
					actv,
					ti.getFileId(),
					Methods.convert_path_into_table_name(actv));
			
			/*********************************
			 * 2-2-a. Update data
			 *********************************/
//			// Log
//			Log.d("Methods.java"
//					+ "["
//					+ Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]",
//					"[onListItemClick] Table name=" + Methods.convert_path_into_table_name(actv));
			
			DBUtils dbu = new DBUtils(actv, MainActv.dbName);
			
			//
			SQLiteDatabase wdb = dbu.getWritableDatabase();

			
			boolean res = DBUtils.updateData_TI_last_viewed_at(
								actv,
								wdb,
								Methods.convert_path_into_table_name(actv),
								ti);
			
			if (res == true) {
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Data updated: " + ti.getFile_name());
			} else {//if (res == true)
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"Update data => Failed: " + ti.getFile_name());
			}//if (res == true)
			
			
			wdb.close();
			
		} else {//if (current_move_mode == MainActv.HISTORY_MODE_OFF)
			
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "History not saved");
			
		}//if (current_move_mode == MainActv.HISTORY_MODE_OFF)

		
		
		return false;
	}//public static boolean record_history(Activity actv, long fileId)


	/*********************************
	 * <Return>
	 * -1	=> Query failed
	 *********************************/
	public static int get_num_of_entries(Activity actv, String table_name) {
		/*********************************
		 * memo
		 *********************************/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		String sql = "SELECT * FROM " + table_name;
		
		Cursor c = null;
		
		try {
			
			c = rdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return -1;
		}
		
		int num_of_entries = c.getCount();
		
		rdb.close();

		return num_of_entries;
		
	}//public static int get_num_of_entries(Activity actv, String table_name)

	public static boolean is_numeric(String str) {
		
		// REF=> http://www.coderanch.com/t/401142/java/java/check-if-String-value-numeric # Hurkpan Potgieter Greenhorn
		String regex = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
		
//		Pattern p = Pattern.compile( "([0-9]*)\\.[0]" );
		Pattern p = Pattern.compile(regex);

		Matcher m = p.matcher(str);
		
		return m.matches(); //TRUE
		
	}//public static boolean is_numeric(String str)
	
}//public class Methods
