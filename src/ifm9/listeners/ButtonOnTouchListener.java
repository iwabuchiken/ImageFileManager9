package ifm9.listeners;

import ifm9.main.R;
import ifm9.utils.Methods;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class ButtonOnTouchListener implements OnTouchListener {

	/*----------------------------
	 * Fields
		----------------------------*/
	//
	Activity actv;

	//
	Vibrator vib;
	
	public ButtonOnTouchListener(Activity actv) {
		//
		this.actv = actv;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
	}

//	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		Methods.ButtonTags tag = (Methods.ButtonTags) v.getTag();
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			
			switch (tag) {
			
			case ib_up://----------------------------------------------------
				
				ImageButton ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_up_disenabled);
				
//				v.setBackgroundColor(Color.GRAY);
				
				break;// case ib_up
				
			case thumb_activity_ib_bottom://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_thumb_bottom_50x50_disenabled);
						
				break;// case thumb_activity_ib_bottom

			case thumb_activity_ib_top://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_thumb_top_50x50_disenabled);
				
				break;// case thumb_activity_ib_top
				
			}//switch (tag)
			
			break;//case MotionEvent.ACTION_DOWN:
			
			
		case MotionEvent.ACTION_UP:
			switch (tag) {
			case ib_up://----------------------------------------------------
				
				ImageButton ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_up);
				
//				v.setBackgroundColor(Color.WHITE);
				
				break;// case ib_up
				
			case thumb_activity_ib_bottom://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_thumb_bottom_50x50);

				break;// case thumb_activity_ib_bottom
				
			case thumb_activity_ib_top://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_thumb_top_50x50);
				
				break;// case thumb_activity_ib_top
				
			}//switch (tag)
			
			break;//case MotionEvent.ACTION_UP:
		}//switch (event.getActionMasked())
		return false;
	}

}
