package ifm9.listeners;

import ifm9.main.MainActv;
import ifm9.utils.Methods;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class DialogOnItemLongClickListener implements OnItemLongClickListener {

	Activity actv;
	static Vibrator vib;

	//
	static Methods.ItemTags itemTag = null;
	
	//
	ArrayAdapter<String> dirListAdapter;	// Used in => case dir_list_move_files
	
	//
	Dialog dlg;	// Used in => case dir_list_move_files
	
	//
	List<String> fileNameList;	// Used in => case dir_list_move_files
	
	/****************************************
	 * Constructor
	 ****************************************/
	public DialogOnItemLongClickListener(Activity actv) {
		// 
		this.actv = actv;
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	/*----------------------------
	 * Used in => case dir_list_move_files
		----------------------------*/
	public DialogOnItemLongClickListener(Activity actv,
			Dialog dlg, ArrayAdapter<String> dirListAdapter, List<String> fileNameList) {
		// 
		this.actv = actv;
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		this.dlg = dlg;
		this.fileNameList = fileNameList;
		this.dirListAdapter = dirListAdapter;
		
	}//public CustomOnItemLongClickListener

	/****************************************
	 * Methods
	 ****************************************/
	@Override
	public boolean onItemLongClick(
										AdapterView<?> parent, View v,
										int position, long id) {
		/*----------------------------
		 * Steps
		 * 1. Get item
		 * 2. Get tag
		 * 3. Vibrate
		 * 
		 * 4. Is the tag null?
		 * 
		 * 5. If no, the switch
			----------------------------*/
		
		//
		Methods.DialogItemTags tag = (Methods.DialogItemTags) parent.getTag();
		
		// Log
		Log.d("DialogOnItemLongClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Tag => " + tag.name());

		//
//		vib.vibrate(400);
		vib.vibrate(40);
		
		switch (tag) {
		
		case dlg_move_files:
			
			/*----------------------------
			 * 0. Get folder name
				----------------------------*/
			String folderName = (String) parent.getItemAtPosition(position);

			// Log
			Log.d("DialogOnItemLongClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "folderName: " + folderName);
			
			/*----------------------------
			 * 5.1.1. Is a directory?
				----------------------------*/
//			String curPath = Methods.get_currentPath_from_prefs(actv);
			
//			File targetFile = new File(curPath, folderName);
			File targetFile = new File(MainActv.dirPath_base, folderName);
			
			if (targetFile.exists() && targetFile.isFile()) {
				// debug
				Toast.makeText(actv, "ファイル", 2000).show();
				
	//			return false;
				return true;		//=> "false" => Then, onClick process starts
				
			}//if (targetFile.exists() && targetFile.isFile())

			// Log
			Log.d("DialogOnItemLongClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "targetFile.getAbsolutePath(): " + targetFile.getAbsolutePath());

			if (!targetFile.exists()) {
				
				// Log
				Log.d("DialogOnItemLongClickListener.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Item doesn't exist: " + targetFile.getAbsolutePath());
				
				return true;
				
			}//if (!targetFile.exists())
			
			
			File[] files = new File(targetFile.getAbsolutePath()).listFiles(new FileFilter(){

				@Override
				public boolean accept(File pathname) {
					// TODO 自動生成されたメソッド・スタブ
					
					return pathname.isDirectory();
				}
				
			});//File[] files
			
			if (files.length < 1) {
				
				AlertDialog.Builder dialog=new AlertDialog.Builder(actv);
				
		        dialog.setTitle("情報");
		        dialog.setMessage("このフォルダには、サブフォルダはありません。");
		        
		        dialog.setNegativeButton("OK",new DialogListener(actv, dialog, 1));
		        
		        dialog.create();
		        dialog.show();
 
				
			} else {//if (files.length < 1)
				
				// Log
				Log.d("DialogOnItemLongClickListener.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "files.length: " + files.length);
				
				fileNameList.clear();
				
				for (File eachFile : files) {
					
//					fileNameList.add(fileName);
//					fileNameList.add(eachFile.getName());
//					fileNameList.add(eachFile.getAbsolutePath());
//					fileNameList.add(Methods.convert_filePath_into_table_name(actv, eachFile.getAbsolutePath()));
//					fileNameList.add(Methods.convert_filePath_into_path_label(actv, eachFile.getAbsolutePath()));
					
					fileNameList.add(Methods.convert_filePath_into_path_label_no_base(actv, eachFile.getAbsolutePath()));
					
				}//for (String fileName : fileNames)
			
				Collections.sort(fileNameList);
	
				dirListAdapter.notifyDataSetChanged();

			}//if (files.length < 1)
			
			/*----------------------------
			 * 5.1.2. If yes, call a method
				----------------------------*/
//			Methods.dlg_removeFolder(actv, folderName);
			

			
			break;
		
		}//switch (tag)
		
		
//		return false;
		return true;
		
	}//public boolean onItemLongClick()

}
