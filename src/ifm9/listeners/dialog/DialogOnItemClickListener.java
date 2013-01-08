package ifm9.listeners.dialog;

import java.util.List;

import ifm9.main.MainActv;
import ifm9.main.R;
import ifm9.tasks.RefreshDBTask;
import ifm9.tasks.Task_add_table_name;
import ifm9.utils.CONS;
import ifm9.utils.Methods;
import ifm9.utils.Methods_dialog;
import ifm9.utils.Tags;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class DialogOnItemClickListener implements OnItemClickListener {

	//
	Activity actv;
	Dialog dlg;
	Dialog dlg2;
	//
	Vibrator vib;
	
	//
//	Methods.DialogTags dlgTag = null;

	public DialogOnItemClickListener(Activity actv, Dialog dlg) {
		// 
		this.actv = actv;
		this.dlg = dlg;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
		
	}//public DialogOnItemClickListener(Activity actv, Dialog dlg)

	public DialogOnItemClickListener(Activity actv, Dialog dlg, Dialog dlg2) {
		// 
		this.actv = actv;
		this.dlg = dlg;
		this.dlg2 = dlg2;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
		
	}//public DialogOnItemClickListener(Activity actv, Dialog dlg)

//	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		/*----------------------------
		 * Steps
		 * 1. Get tag
		 * 2. Vibrate
		 * 3. Switching
			----------------------------*/
		
		Tags.DialogItemTags tag = (Tags.DialogItemTags) parent.getTag();
//		
		vib.vibrate(CONS.vibLength_click);
		
		/*----------------------------
		 * 3. Switching
			----------------------------*/
		switch (tag) {
		
		case dlg_move_files://----------------------------------------------

			String folderPath = (String) parent.getItemAtPosition(position);
			
			Methods_dialog.dlg_confirm_moveFiles(actv, dlg, folderPath);

//			// debug
//			Toast.makeText(actv, "Move files to: " + folderPath, 2000)
//					.show();
			
			break;// case dlg_move_files

		case dlg_add_memos_gv://----------------------------------------------
			
			String word = (String) parent.getItemAtPosition(position);
			
			Methods.add_pattern_to_text(dlg, position, word);
			
//			String word = (String) parent.getItemAtPosition(position);
//			
//			EditText et = (EditText) dlg.findViewById(R.id.dlg_add_memos_et_content);
//			
//			String content = et.getText().toString();
//			
//			content += word + " ";
//			
//			et.setText(content);
//			
//			et.setSelection(et.getText().toString().length());
			
//			// debug
//			Toast.makeText(actv, word, 2000).show();
			
			break;
			
		case dlg_db_admin_lv://----------------------------------------------
			/*----------------------------
			 * 1. Get chosen item name
			 * 2. Switching
				----------------------------*/
			
			String item = (String) parent.getItemAtPosition(position);
			
//			// debug
//			Toast.makeText(actv, item, 2000).show();
			
			/*----------------------------
			 * 2. Switching
				----------------------------*/
			if (item.equals(actv.getString(R.string.dlg_db_admin_item_backup_db))) {
				
				Methods.db_backup(actv, dlg);
				
			} else if (item.equals(actv.getString(R.string.dlg_db_admin_item_refresh_db))){
				
				RefreshDBTask task_ = new RefreshDBTask(actv, dlg);
				
				// debug
				Toast.makeText(actv, "Starting a task...", 2000)
						.show();
				
				task_.execute("Start");

				dlg.dismiss();
				
//				// Log
//				Log.d("DialogOnItemClickListener.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "DB refresh");
				
				
//				Methods.refreshMainDB(actv, dlg);
			} else if (item.equals(actv.getString(R.string.dlg_db_admin_item_set_new_column))){
				
				//
				dlg_db_admin_item_set_new_column();
				
			} else if (item.equals(actv.getString(R.string.dlg_db_admin_item_restore_db))){
				
				//
				dlg_db_admin_item_restore_db();
				
			}
			
			break;// case dlg_add_memos_gv

		case dlg_admin_patterns_lv://----------------------------------------------
			/*----------------------------
			 * 1. Get chosen item name
			 * 2. Switching
				----------------------------*/
			
			item = (String) parent.getItemAtPosition(position);
			
//			// debug
//			Toast.makeText(actv, item, 2000).show();
			
			/*----------------------------
			 * 2. Switching
				----------------------------*/
			if (item.equals(actv.getString(R.string.generic_tv_register))) {
				
				Methods_dialog.dlg_register_patterns(actv, dlg);
				
			} else if (item.equals(actv.getString(R.string.generic_tv_delete))) {

				Methods_dialog.dlg_delete_patterns(actv, dlg);
				
			} else if (item.equals(actv.getString(R.string.generic_tv_edit))) {
				
			}
			
			break;// case dlg_admin_patterns_lv

		case dlg_delete_patterns_gv://----------------------------------------------
			
			item = (String) parent.getItemAtPosition(position);
			
//			// debug
//			Toast.makeText(actv, item, 2000).show();
			
			Methods_dialog.dlg_confirm_delete_patterns(actv, dlg, dlg2, item);
			
			break;// case dlg_delete_patterns_gv
			
		default:
			break;
		}//switch (tag)
		
	}//public void onItemClick(AdapterView<?> parent, View v, int position, long id)

	private void dlg_db_admin_item_restore_db() {
		
		Methods.restore_db(actv);
		
//		// B28 v-1.2
//		String db_file_name = "ifm9_backup_20121226_125955.bk";
//		
//		Methods.restore_db(actv, db_file_name);
//		
//		dlg.dismiss();
	}

	private void dlg_db_admin_item_set_new_column() {
		// Dismiss dialog
		dlg.dismiss();
		
		Task_add_table_name task = new Task_add_table_name(actv);
		
		task.execute("message");
		
//		// Strings
//		String t_name = "IFM9__TEST";
//		String col_name = "table_name";
//		String data_type = "String";
		
//////////////////////////////////////////////////
//		boolean res = 
//				Methods.update_table_add_new_column(
//						actv, 
//						MainActv.dbName,
//						t_name,
//						col_name,
//						data_type);
//		
//		// debug
//		Toast.makeText(actv, "Add new column => " + res, Toast.LENGTH_SHORT).show();
//		
//////////////////////////////////////////////////
		
//////////////////////////////////////////////////
//		// Get table name list
//		List<String> t_names = Methods.get_table_list(actv, "ifm");
//		
//		// Add a new column
//		boolean res = false;
//		String col_name = "table_name";
//		String data_type = "String";
//		
//		for (String name : t_names) {
//			
//			res = Methods.update_table_add_new_column(
//					actv, 
//					MainActv.dbName,
//					name,
//					col_name,
//					data_type);
//
//			// Log
//			Log.d("DialogOnItemClickListener.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "t_name=" + name + "(res=" + res + ")");
//
//		}
//		
//		// debug
////		Toast.makeText(actv, "Add new column => " + res, Toast.LENGTH_SHORT).show();
		
//////////////////////////////////////////////////
		
		//
		
//////////////////////////////////////////////////
//		// Get table name
////		String tableName = Methods.convert_path_into_table_name(actv);
//		
//		// Get column names
////		String[] col_names = Methods.get_column_list(actv, MainActv.dbName, tableName);
//		String[] col_names = Methods.get_column_list(actv, MainActv.dbName, t_name);
//		
//		// Show each name
//		for (String name : col_names) {
//			
//			// Log
//			Log.d("DialogOnItemClickListener.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col_name=" + name);
//			
//		}
//////////////////////////////////////////////////

	}//private void dlg_db_admin_item_set_new_column()
}
