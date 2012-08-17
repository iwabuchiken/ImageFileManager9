package ifm9.listeners;

import ifm9.main.TNActv;
import ifm9.utils.Methods;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ButtonOnClickListener implements OnClickListener {
	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;

	//
	Vibrator vib;
	
	//
	int position;
	
	//
	ListView lv;
	
	public ButtonOnClickListener(Activity actv) {
		//
		this.actv = actv;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	public ButtonOnClickListener(Activity actv, int position) {
		//
		this.actv = actv;
		this.position = position;
		
		//
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
		
		
		
	}//public ButtonOnClickListener(Activity actv, int position)

	public ButtonOnClickListener(Activity actv, ListView lv) {
		// 
		this.actv = actv;
		this.lv = lv;
		
		vib = (Vibrator) actv.getSystemService(actv.VIBRATOR_SERVICE);
	}

	@Override
	public void onClick(View v) {
//		//
		Methods.ButtonTags tag = (Methods.ButtonTags) v.getTag();
//
		vib.vibrate(Methods.vibLength_click);
		
		//
		switch (tag) {
		case ib_up://---------------------------------------------------------
			
			Methods.upDir(actv);
			
			break;

		case tilist_cb://------------------------------------------------------------------------------
			/*----------------------------
			 * Steps
			 * 1. If already checked, unlist from ThumbnailActivity.checkedPositions
			 * 2. If not yet, enlist into it
			 * 3. Then, notify to adapter
				----------------------------*/
			/*----------------------------
			 * 1. If already checked, unlist from ThumbnailActivity.checkedPositions
				----------------------------*/
			case_tilist_cb();
			
			/*----------------------------
			 * 3. Then, notify to adapter
				----------------------------*/
			TNActv.aAdapter.notifyDataSetChanged();
			TNActv.bAdapter.notifyDataSetChanged();
			
			break;

		case thumb_activity_ib_bottom: //----------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			int numOfGroups = TNActv.tiList.size() / lv.getChildCount();
			
			int indexOfLastChild = lv.getChildCount() * numOfGroups;
			
			lv.setSelection(indexOfLastChild);

			break;// case thumb_activity_ib_bottom 
			
		case thumb_activity_ib_top://--------------------------------------------
			
			vib.vibrate(Methods.vibLength_click);
			
			lv.setSelection(0);
			
			break;// thumb_activity_ib_top

		default:
			break;
		}//switch (tag)
		
	}//public void onClick(View v)

	private void case_tilist_cb() {
		if (TNActv.checkedPositions.contains((int)position)) {
			// Log
			Log.d("ButtonOnClickListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "position exists => " + position);
			
//			TNActv.checkedPositions.add(position);
//			TNActv.checkedPositions.remove(position);
			TNActv.checkedPositions.remove((Integer) position);
			
			// Log
			Log.d("ButtonOnClickListener.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "position removed => " + position);
			
			
//			// Log
//			Log.d("TNActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "New position => " + position +
//					" / " + "(length=" + TNActv.checkedPositions.size() + ")");
//
//			Log.d("TNActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", 
//					"tiList(position=" + position + ") => " + 
//					TNActv.tiList.get(position).getFile_name());

		} else {//if (TNActv.checkedPositions.contains((int)position))
			/*----------------------------
			 * 2. If not yet, enlist into it
				----------------------------*/
			
			TNActv.checkedPositions.add(position);
			
			// Log
			String temp = "new position added => " + String.valueOf(position) +
					"(size=" + TNActv.checkedPositions.size() + ")" + "[";
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(temp);
			for (int i = 0; i < TNActv.checkedPositions.size(); i++) {
				
				sb.append(TNActv.checkedPositions.get(i) + ",");
				
			}//for (int i = 0; i < TNActv.checkedPositions.size(); i++)
			sb.append("]");
			
			
			Log.d("ButtonOnClickListener.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", sb.toString());
//							.getLineNumber() + "]", "new position added => " + String.valueOf(position) +
//							"(size=" + TNActv.checkedPositions.size() + ")" + "[" +);
			
			
		}//if (TNActv.checkedPositions.contains((int)position))
	}//private void case_tilist_cb()

}
