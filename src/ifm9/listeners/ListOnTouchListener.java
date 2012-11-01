package ifm9.listeners;

import ifm9.main.MainActv;
import ifm9.main.R;
import ifm9.utils.Methods;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ListOnTouchListener implements OnTouchListener {

	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;

	//
	Vibrator vib;
	
	public ListOnTouchListener(Activity actv) {
		//
		this.actv = actv;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
	}

//	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		Methods.ListTags tag = (Methods.ListTags) v.getTag();
		
		// Log
		Log.d("ListOnTouchListener.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "tag=" + tag.name());
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			
			// Log
			Log.d("ListOnTouchListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "DOWN");
			
			switch (tag) {
			
			case main_list_adapter://---------------------------
				
				TextView tv = (TextView) v.findViewById(R.id.list_row_main_list_tv);
				
				String name = tv.getText().toString();
				
//				if (name.equals(MainActv.listFileName)) {
//					
//					tv.setBackgroundColor(Color.GRAY);
//					
//				} else {//if (name.equals(MainActv.listFileName))
//					
//					
//					
//				}//if (name.equals(MainActv.listFileName))
				
//				v.setBackgroundColor(Color.GRAY);
				tv.setBackgroundColor(Color.GRAY);
				
				break;// case main_list_adapter
			}
			
			break;//case MotionEvent.ACTION_DOWN:
			
			
		case MotionEvent.ACTION_UP:
			
			// Log
			Log.d("ListOnTouchListener.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "UP");
			
			switch (tag) {
			
			case main_list_adapter:
				
				TextView tv = (TextView) v.findViewById(R.id.list_row_main_list_tv);
				
				String name = tv.getText().toString();
				
				if (name.equals(MainActv.listFileName)) {
					
					tv.setBackgroundColor(Color.WHITE);
					
				} else {//if (name.equals(MainActv.listFileName))
					
					tv.setBackgroundColor(Color.BLACK);
					
				}//if (name.equals(MainActv.listFileName))

				
				break;
			}//switch (tag)
			
			break;//case MotionEvent.ACTION_UP:
		}//switch (event.getActionMasked())
		return false;
	}

}
