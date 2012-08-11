package ifm9.utils;

import ifm9.items.ThumbnailItem;
import ifm9.listeners.DialogButtonOnClickListener;
import ifm9.listeners.DialogButtonOnTouchListener;
import ifm9.main.MainActv;
import ifm9.main.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
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

import org.apache.commons.lang.StringUtils;

public class Methods {

	static int counter;		// Used => sortFileList()
	
	
	/****************************************
	 * Enums
	 ****************************************/
	public static enum DialogTags {
		// Generics
		dlg_generic_dismiss, dlg_generic_dismiss_second_dialog,
		
		
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
		
		
	}//public static enum DialogTags
	
	public static enum ButtonTags {
		// MainActivity.java
		ib_up,
		
		// DBAdminActivity.java
		db_manager_activity_create_table, db_manager_activity_drop_table, 
		db_manager_activity_register_patterns,
		
		// thumb_activity.xml
		thumb_activity_ib_back, thumb_activity_ib_bottom, thumb_activity_ib_top,
		
		// image_activity.xml
		image_activity_back,
		
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
		
		Methods.update_prefs_currentPath(actv, MainActv.dirPath_current);
		
		/*----------------------------
		 * 2. Refresh list view
			----------------------------*/
		refreshListView(actv);
		
		/*----------------------------
		 * 3. "Up" button => Enable
			----------------------------*/
		ImageButton ib = (ImageButton) actv.findViewById(R.id.v1_bt_up);
		
		if (!ib.isEnabled()) {
			
			ib.setEnabled(true);
			
			ib.setImageResource(R.drawable.ifm8_up);
			
			
		}//if (!ib.isEnabled())
		
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
		 * 5. Update image buttons
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
			
		}
		
		/*----------------------------
		 * 5. Notify adapter of changes
			----------------------------*/
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
		update_image_buttons(actv, currentPath);
		
		
	}//private static void refreshListView()

	private static void update_image_buttons(Activity actv, String currentPath) {
		
		ImageButton ib = (ImageButton) actv.findViewById(R.id.v1_bt_up);
		
		if (currentPath.equals(MainActv.dirPath_base)) {
			
			ib.setImageResource(R.drawable.ifm8_up_disenabled);
			ib.setEnabled(false);
			
		} else {//if (currentPath.equals(MainActv.dirPath_base))

			ib.setImageResource(R.drawable.ifm8_up);
			ib.setEnabled(true);
			
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
		
		tv.setText(getCurrentPathLabel(actv));
		
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
	public static String getCurrentPathLabel(Activity actv) {
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
		 * 4. Refresh list
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
		 * 5. Update path view
			----------------------------*/
		Methods.updatePathLabel(actv);
		
	}//public static void upDir(Activity actv)

}//public class Methods
