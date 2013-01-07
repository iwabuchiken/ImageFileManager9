package ifm9.listeners.button;

import ifm9.main.R;
import ifm9.utils.Methods;
import ifm9.utils.Tags;
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Tags.ButtonTags tag = (Tags.ButtonTags) v.getTag();
		
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

			case image_activity_prev://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_back_disenabled);
				
				break;// case image_activity_prev

			case image_activity_next://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_forward_disenabled);
				
				break;// case image_activity_next
				
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

			case image_activity_prev://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_back);
				
				break;// case image_activity_prev

			case image_activity_next://----------------------------------------------------
				
				ib = (ImageButton) v;
				ib.setImageResource(R.drawable.ifm8_forward);
				
				break;// case image_activity_next
				
			}//switch (tag)
			
			break;//case MotionEvent.ACTION_UP:
		}//switch (event.getActionMasked())
		return false;
	}

}
