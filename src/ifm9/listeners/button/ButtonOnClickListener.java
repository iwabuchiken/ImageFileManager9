package ifm9.listeners.button;

import ifm9.items.TI;
import ifm9.main.ImageActv;
import ifm9.main.MainActv;
import ifm9.main.R;
import ifm9.main.TNActv;
import ifm9.utils.CONS;
import ifm9.utils.Methods;
import ifm9.utils.Tags;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

//	@Override
	public void onClick(View v) {
//		//
		Tags.ButtonTags tag = (Tags.ButtonTags) v.getTag();
//
		vib.vibrate(CONS.vibLength_click);
		
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
			
			vib.vibrate(CONS.vibLength_click);
			
			int numOfGroups = TNActv.tiList.size() / lv.getChildCount();
			
			int indexOfLastChild = lv.getChildCount() * numOfGroups;
			
			lv.setSelection(indexOfLastChild);

			break;// case thumb_activity_ib_bottom 
			
		case thumb_activity_ib_top://--------------------------------------------
			
			vib.vibrate(CONS.vibLength_click);
			
			lv.setSelection(0);
			
			break;// thumb_activity_ib_top

		case image_activity_prev://----------------------------------------------------
			
			image_activity_prev();
			
			
			break;// case image_activity_prev
			
		case image_activity_next://----------------------------------------------------

			image_activity_next();
			
			break;// case image_activity_next
			
		default:
			break;
		}//switch (tag)
		
	}//public void onClick(View v)

	private void image_activity_next() {
		/*********************************
		 * 1. Get prefs => current position
		 * 2. No more prev?
		 * 
		 * 3. Get the previous item in the ti list
		 * 4. New image file path
		 * 
		 * 5. Show the previous image
		 * 
		 * 6. Set new pref value
		 * 
		 * 7. Update the file name label
		 * 
		 * 8. Save history
		 * 
		 *********************************/
		SharedPreferences prefs = actv.getSharedPreferences(
					MainActv.prefName_tnActv,
					actv.MODE_PRIVATE);
		
 
		//Methods.PrefenceLabels.thumb_actv.name()
		
		//int savedPosition = prefs.getInt("chosen_list_item", -1);
		int savedPosition = prefs.getInt(
							MainActv.prefName_tnActv_current_image_position,
							-1);
		
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "savedPosition=" + savedPosition);
		
		/*********************************
		 * 2. No more prev?
		 *********************************/
//		if (savedPosition == 0) {
		if (savedPosition >= TNActv.tiList.size() - 1) {
			
			// debug
			Toast.makeText(actv, "No next images", Toast.LENGTH_SHORT).show();
			
			return;
			
		}//if (savedPosition == 0)

		/*********************************
		 * 3. Get the previous item in the ti list
		 *********************************/
		TI ti_prev = TNActv.tiList.get(savedPosition + 1);
//		
//		
		/*********************************
		 * 4. New image file path
		 *********************************/
		String image_file_path_new = ti_prev.getFile_path();
	
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "image_file_path_new=" + image_file_path_new);
//		
		/*********************************
		 * 5. Show the next image
		 *********************************/
		ImageActv.bm = BitmapFactory.decodeFile(image_file_path_new);
		
		ImageActv.LL.removeView(ImageActv.v);
		
		ImageActv.v.setImageBitmap(ImageActv.bm);
		
		ImageActv.LL.addView(ImageActv.v);
//		
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "New image added");
//		
		/*********************************
		 * 6. Set new pref value
		 *********************************/
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(MainActv.prefName_tnActv_current_image_position, savedPosition + 1);
		editor.commit();

		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs set: " + (savedPosition + 1));

		/*********************************
		 * 7. Update the file name label
		 *********************************/
		ImageActv.tv_file_name.setText(ti_prev.getFile_name());

		/*********************************
		 * 8. Save history
		 *********************************/
		boolean res = Methods.record_history(actv, ti_prev);
		
	}//private void image_activity_next()

	private void image_activity_prev() {
		/*********************************
		 * 1. Get prefs => current position
		 * 2. No more prev?
		 * 
		 * 3. Get the previous item in the ti list
		 * 4. New image file path
		 * 
		 * 5. Show the previous image
		 * 
		 * 6. Set new pref value
		 * 
		 * 7. Update the file name label
		 * 
		 * 8. Save history
		 *********************************/
		SharedPreferences prefs = actv.getSharedPreferences(
					MainActv.prefName_tnActv,
					actv.MODE_PRIVATE);
		
 
		//Methods.PrefenceLabels.thumb_actv.name()
		
		//int savedPosition = prefs.getInt("chosen_list_item", -1);
		int savedPosition = prefs.getInt(
							MainActv.prefName_tnActv_current_image_position,
							-1);
		
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "savedPosition=" + savedPosition);
		
		/*********************************
		 * 2. No more prev?
		 *********************************/
		if (savedPosition == 0) {
			
			// debug
			Toast.makeText(actv, "No previous images", Toast.LENGTH_SHORT).show();
			
			return;
			
		}//if (savedPosition == 0)

		/*********************************
		 * 3. Get the previous item in the ti list
		 *********************************/
		TI ti_prev = TNActv.tiList.get(savedPosition - 1);
		
		
		/*********************************
		 * 4. New image file path
		 *********************************/
		String image_file_path_new = ti_prev.getFile_path();
	
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "image_file_path_new=" + image_file_path_new);
		
		/*********************************
		 * 5. Show the previous image
		 *********************************/
		ImageActv.bm = BitmapFactory.decodeFile(image_file_path_new);
		
		ImageActv.LL.removeView(ImageActv.v);
		
		ImageActv.v.setImageBitmap(ImageActv.bm);
		
		ImageActv.LL.addView(ImageActv.v);
		
		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "New image added");
		
		/*********************************
		 * 6. Set new pref value
		 *********************************/
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(MainActv.prefName_tnActv_current_image_position, savedPosition - 1);
		editor.commit();

		// Log
		Log.d("ButtonOnClickListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs set: " + (savedPosition - 1));
		
		/*********************************
		 * 7. Update the file name label
		 *********************************/
		ImageActv.tv_file_name.setText(ti_prev.getFile_name());
		
		/*********************************
		 * 8. Save history
		 *********************************/
		boolean res = Methods.record_history(actv, ti_prev);
		
	}//private void image_activity_prev()

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

}//public class ButtonOnClickListener implements OnClickListener
