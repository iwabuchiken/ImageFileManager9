package ifm9.listeners;

import ifm9.main.TNActv;
import ifm9.utils.Tags.ItemTags;
import android.app.Activity;
import android.view.View;
import android.view.View.OnLongClickListener;

public class CustomOnLongClickListener implements OnLongClickListener {

	//
	Activity actv;
	
	//
	int position;
	
	//
	ItemTags itemTag;
	
	public CustomOnLongClickListener(Activity actv, int position) {
		
		this.actv = actv;
		this.position = position;
		
	}
	
	public CustomOnLongClickListener(Activity actv, int position,
											ItemTags tag) {
		
		this.actv = actv;
		this.position = position;
		this.itemTag = tag;
		
	}//public CustomOnLongClickListener

//	@Override
	public boolean onLongClick(View v) {
		
		if (itemTag != null && itemTag instanceof ItemTags) {
			
			switch (itemTag) {
			case tilist_checkbox:
				/*----------------------------
				 * Steps
				 * 1. If the list contains the position
				 * 2. If not
					----------------------------*/
				/*----------------------------
				 * 1. If the list contains the position
					----------------------------*/
				if (TNActv.checkedPositions.contains((Integer) position)) {

					TNActv.checkedPositions.clear();
					
//					TNActv.aAdapter.notifyDataSetChanged();
					TNActv.bAdapter.notifyDataSetChanged();
					
				/*----------------------------
				 * 2. If not
					----------------------------*/
				} else {//if (TNActv.checkedPositions.contains((Integer) position))
					
					TNActv.checkedPositions.clear();
					
					for (int i = 0; i < TNActv.tiList.size(); i++) {
						
						TNActv.checkedPositions.add((Integer) i);
						
					}//for (int i = 0; i < TNActv.tiList.size(); i++)
					
//					TNActv.aAdapter.notifyDataSetChanged();
					TNActv.bAdapter.notifyDataSetChanged();
					
				}//if (TNActv.checkedPositions.contains((Integer) position))
				
				
				break;// case tilist_checkbox
			
			}//switch (tag)
			
		}//if (tag != null)
		
		return true;
	}//public boolean onLongClick(View arg0)

}//public class CustomOnLongClickListener implements OnLongClickListener
