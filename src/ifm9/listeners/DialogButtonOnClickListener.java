package ifm9.listeners;

import ifm9.utils.Methods;
import android.app.Activity;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class DialogButtonOnClickListener implements OnClickListener {
	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;
	Dialog dlg;
	Dialog dlg2;		//=> Used in dlg_input_empty_btn_XXX
	Dialog dlg3;
	
	//
	Vibrator vib;
	
	// Used in => Methods.dlg_addMemo(Activity actv, long file_id, String tableName)
	long file_id;
	String tableName;
	
	public DialogButtonOnClickListener(Activity actv, Dialog dlg) {
		//
		this.actv = actv;
		this.dlg = dlg;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public DialogButtonOnClickListener(Activity actv, Dialog dlg1,
			Dialog dlg2) {
		//
		this.actv = actv;
		this.dlg = dlg1;
		this.dlg2 = dlg2;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public DialogButtonOnClickListener(Activity actv, Dialog dlg1,
			Dialog dlg2, Dialog dlg3) {
		//
		this.actv = actv;
		this.dlg = dlg1;
		this.dlg2 = dlg2;
		this.dlg3 = dlg3;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public DialogButtonOnClickListener(Activity actv, Dialog dlg, long file_id, String tableName) {
		// 
		this.actv = actv;
		this.dlg = dlg;
		
		this.tableName = tableName;
		
		this.file_id = file_id;
		
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		
	}//public DialogButtonOnClickListener(Activity actv, Dialog dlg, long file_id, String tableName)

	@Override
	public void onClick(View v) {
		//
		Methods.DialogTags tag_name = (Methods.DialogTags) v.getTag();

		//
		switch (tag_name) {
		
		case dlg_generic_dismiss://------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			dlg.dismiss();
			
			break;

		case dlg_generic_dismiss_second_dialog: // ----------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			dlg2.dismiss();
			
			break;// case dlg_generic_dismiss_second_dialog

		case dlg_generic_dismiss_third_dialog://------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			dlg3.dismiss();
			
			break;

		case dlg_create_folder_cancel://---------------------------------------------
			
			dlg.dismiss();
			
			break;// case dlg_create_folder_cancel

		case dlg_create_folder_ok://--------------------------------------------------
			
			Methods.dlg_isEmpty(actv, dlg);
			
			break;// case dlg_create_folder_ok

		case dlg_input_empty_reenter://----------------------------------------------
			
			dlg2.dismiss();
			
			break;// case dlg_input_empty_reenter

		case dlg_input_empty_cancel://---------------------------------------------
			
			dlg2.dismiss();
			dlg.dismiss();
			
			break;// case dlg_input_empty_cancel

		case dlg_confirm_create_folder_cancel://---------------------------------------------
			
			dlg2.dismiss();
			
			break;

		// dlg_confirm_create_folder.xml
		case dlg_confirm_create_folder_ok://---------------------------------------------
			
			Methods.createFolder(actv, dlg, dlg2);
			
			break;

		case dlg_confirm_remove_folder_cancel://---------------------------------------------
			
			dlg.dismiss();
			
			break;// case dlg_confirm_remove_folder_cancel

		// dlg_confirm_remove_folder.xml
		case dlg_confirm_remove_folder_ok://---------------------------------------------
			
			Methods.removeFolder(actv, dlg);
			
			break;// case dlg_confirm_remove_folder_ok


		case dlg_confirm_move_files_ok: // ----------------------------------------------------
			
			Methods.moveFiles(actv, dlg, dlg2);
			
			break;

		case dlg_add_memos_bt_add: // ----------------------------------------------------
			
			// Log
			Log.d("DialogButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file_id => " + file_id);
			
			
			// Log
			Log.d("DialogButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Calling => Methods.addMemo()");
			
			vib.vibrate(Methods.vibLength_click);
			
			Methods.addMemo(actv, dlg, file_id, tableName);
			
			break;// case dlg_add_memos_bt_add

		case dlg_register_patterns_register:// ----------------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
//			Methods.dlg_register_patterns_isInputEmpty(actv, dlg);
			Methods.dlg_register_patterns_isInputEmpty(actv, dlg, dlg2);
			
			break;

		case dlg_confirm_delete_patterns_ok:// ----------------------------------------------------
			
			Methods.delete_patterns(actv, dlg, dlg2, dlg3);
			
			break;// case dlg_confirm_delete_patterns_ok
			
		default: // ----------------------------------------------------
			break;
		}//switch (tag_name)
	}//public void onClick(View v)

}//DialogButtonOnClickListener
