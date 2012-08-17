package ifm9.listeners;

import ifm9.main.R;
import ifm9.utils.Methods;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		/*----------------------------
		 * Steps
		 * 1. Get tag
		 * 2. Vibrate
		 * 3. Switching
			----------------------------*/
		
		Methods.DialogItemTags tag = (Methods.DialogItemTags) parent.getTag();
//		
		vib.vibrate(Methods.vibLength_click);
		
		/*----------------------------
		 * 3. Switching
			----------------------------*/
		switch (tag) {
		
		case dlg_move_files://----------------------------------------------

			String folderPath = (String) parent.getItemAtPosition(position);
			
			Methods.dlg_confirm_moveFiles(actv, dlg, folderPath);

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

		default:
			break;
		}//switch (tag)
		
	}//public void onItemClick(AdapterView<?> parent, View v, int position, long id)
}
