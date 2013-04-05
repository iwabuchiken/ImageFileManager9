package ifm9.utils;


import ifm9.items.TI;
import ifm9.listeners.CustomOnItemLongClickListener;
import ifm9.listeners.dialog.DialogButtonOnClickListener;
import ifm9.listeners.dialog.DialogButtonOnTouchListener;
import ifm9.listeners.dialog.DialogListener;
import ifm9.listeners.dialog.DialogOnItemClickListener;
import ifm9.listeners.dialog.DialogOnItemLongClickListener;
import ifm9.main.FTPActv;
import ifm9.main.MainActv;
import ifm9.main.PrefActv;
import ifm9.main.R;
import ifm9.main.TNActv;
import ifm9.tasks.RefreshDBTask;
import ifm9.tasks.SearchTask;
import ifm9.utils.Tags.DialogTags;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
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
import android.os.Looper;

// Apache
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

// REF=> http://commons.apache.org/net/download_net.cgi
//REF=> http://www.searchman.info/tips/2640.html

//import org.apache.commons.net.ftp.FTPReply;

public class Methods_dlg {

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
		lv.setTag(Tags.DialogItemTags.dlg_move_files);

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
		Dialog dlg = Methods_dlg.dlg_addMemo_1_get_dialog(actv, file_id, tableName);

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
		TI ti = Methods.getData(actv, tableName, file_id);
		
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
			
			res = dbu.createTable(wdb, tableName, CONS.cols_memo_patterns, CONS.col_types_memo_patterns);
			
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
		gv.setTag(Tags.DialogItemTags.dlg_add_memos_gv);
		
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
		boolean result = Methods.insertDataIntoDB(actv, CONS.table_name_memo_patterns, 
								CONS.cols_memo_patterns, new String[]{word, table_name});
		
		/*----------------------------
		 * 4. Dismiss dialog
			----------------------------*/
		if (result == true) {
		
			dlg.dismiss();
			
		} else {//if (result == true)

			// debug
			Toast.makeText(actv, "語句を保管できませんでした", 3000).show();

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
		boolean result = Methods.insertDataIntoDB(actv, CONS.table_name_memo_patterns, 
								CONS.cols_memo_patterns, new String[]{word, table_name});
		
		/*----------------------------
		 * 4. Dismiss dialog
			----------------------------*/
		if (result == true) {
		
			dlg.dismiss();
			dlg2.dismiss();
			
			// debug
			Toast.makeText(actv, "定型句を保管しました", Toast.LENGTH_LONG).show();
			
		} else {//if (result == true)

			// debug
			Toast.makeText(actv, "定型句を保管できませんでした", 3000).show();

		}//if (result == true)
		
		
	}//public static void dlg_register_patterns_isInputEmpty(Activity actv, Dialog dlg)

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
		Dialog dlg = Methods_dlg.dlg_template_cancel(
									actv, R.layout.dlg_db_admin, 
									R.string.dlg_db_admin_title, 
									R.id.dlg_db_admin_bt_cancel, 
									Tags.DialogTags.dlg_generic_dismiss);
		
		/*----------------------------
		 * 2. Prep => List
			----------------------------*/
		String[] choices = {
				actv.getString(R.string.dlg_db_admin_item_backup_db),
				actv.getString(R.string.dlg_db_admin_item_refresh_db),
				actv.getString(R.string.dlg_db_admin_item_set_new_column),
				actv.getString(R.string.dlg_db_admin_item_restore_db)
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
		lv.setTag(Tags.DialogItemTags.dlg_db_admin_lv);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/*----------------------------
		 * 6. Show dialog
			----------------------------*/
		dlg.show();
		
		
	}//public static void dlg_db_activity(Activity actv)


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

	public static void dlg_patterns(Activity actv) {
		/*----------------------------
		 * memo
			----------------------------*/
		Dialog dlg = Methods_dlg.dlg_template_cancel(
													actv, R.layout.dlg_admin_patterns, 
													R.string.dlg_memo_patterns_title, 
													R.id.dlg_admin_patterns_bt_cancel, 
													Tags.DialogTags.dlg_generic_dismiss);
		
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
		lv.setTag(Tags.DialogItemTags.dlg_admin_patterns_lv);
		
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
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss_second_dialog);
		
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
			
			res = dbu.createTable(wdb, tableName, CONS.cols_memo_patterns, CONS.col_types_memo_patterns);
			
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
		gv.setTag(Tags.DialogItemTags.dlg_delete_patterns_gv);
		
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
		btn_ok.setTag(Tags.DialogTags.dlg_confirm_delete_patterns_ok);
		btn_cancel.setTag(Tags.DialogTags.dlg_generic_dismiss_third_dialog);
		
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

	public static
	Dialog dlg_template_okCancel_3Dialogues
	(Activity actv,
			int layoutId, int titleStringId,
			int okButtonId, int cancelButtonId,
			DialogTags okTag, DialogTags cancelTag,
			Dialog dlg1, Dialog dlg2) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg3 = new Dialog(actv);
		
		//
		dlg3.setContentView(layoutId);
		
		// Title
		dlg3.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg3.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg3.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(
				new DialogButtonOnTouchListener(actv, dlg3));
		btn_cancel.setOnTouchListener(
				new DialogButtonOnTouchListener(actv, dlg3));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(
				new DialogButtonOnClickListener(actv, dlg1, dlg2, dlg3));
		btn_cancel.setOnClickListener(
				new DialogButtonOnClickListener(actv, dlg1, dlg2, dlg3));
		
		//
		//dlg.show();
		
		return dlg3;
	
	}//public static Dialog dlg_template_okCancel()

	
	public static void dlg_TNList(Activity actv, TI ti) {
		// TODO Auto-generated method stub
		Dialog dlg = Methods_dlg.dlg_template_cancel(
				actv, R.layout.dlg_db_admin, 
				R.string.generic_tv_menu, 
				R.id.dlg_db_admin_bt_cancel, 
				Tags.DialogTags.dlg_generic_dismiss);

		/*----------------------------
		* 2. Prep => List
		----------------------------*/
		String[] choices = {
				actv.getString(R.string.generic_tv_edit),
				actv.getString(R.string.generic_tv_delete),
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
						//R.layout.dlg_db_admin,
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
		lv.setTag(Tags.DialogItemTags.dlg_tn_list);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg, ti));
		
		/*----------------------------
		* 6. Show dialog
		----------------------------*/
		dlg.show();

	}//public static void dlg_TNList(Activity actv, TI ti)

	public static void
	dlg_confirm_DeleteTI(Activity actv, Dialog dlg1, TI ti) {
		// TODO Auto-generated method stub
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(R.layout.dlg_confirm_remove_folder);
		
		// Title
		dlg2.setTitle(R.string.generic_tv_confirm);
		
		/***************************************
		 * Set: Message
		 ***************************************/
		TextView tvMessage = (TextView) dlg2.findViewById(R.id.dlg_confirm_remove_folder_tv_message);
		
		tvMessage.setText("このアイテムを削除しますか？");
		
		
		/*----------------------------
		 * 2. Set folder name to text view
			----------------------------*/
		TextView tv = (TextView) dlg2.findViewById(R.id.dlg_confirm_remove_folder_tv_table_name);
		
		tv.setText(ti.getFile_name());
		
		/*----------------------------
		 * 3. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_ok = (Button) dlg2.findViewById(R.id.dlg_confirm_remove_folder_btn_ok);
		Button btn_cancel = (Button) dlg2.findViewById(R.id.dlg_confirm_remove_folder_btn_cancel);
		
		//
		btn_ok.setTag(DialogTags.dlg_confirm_delete_ti_ok);
		btn_cancel.setTag(DialogTags.dlg_confirm_delete_ti_cancel);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg1, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg1, dlg2));
		
		/*----------------------------
		 * 4. Add listeners => OnClick
			----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg1, dlg2, ti));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg1, dlg2));
		
		/*----------------------------
		 * 5. Show dialog
			----------------------------*/
		dlg2.show();

		
	}//dlg_confirm_DeleteTI(Activity actv, Dialog dlg1, TI ti)

}//public class Methods

