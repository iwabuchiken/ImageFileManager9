package ifm9.adapters;

import java.util.List;

import ifm9.items.TI;
import ifm9.listeners.ListOnTouchListener;
import ifm9.main.MainActv;
import ifm9.main.R;
import ifm9.utils.Methods;
import ifm9.utils.Tags;
import ifm9.utils.Tags.ListTags;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainListAdapter extends ArrayAdapter<String> {

	// Context
	Context con;

	// Inflater
	LayoutInflater inflater;

	public MainListAdapter(Context con, int resourceId,
			List<String> list) {
		
		super(con, resourceId, list);
		
		// Context
		this.con = con;

		// Inflater
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}//public MainListAdapter

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*********************************
		 * 1. Setup view
		 * 2. Get item
		 * 
		 * 3. Get view => TextView
		 * 4. Modify text => "list.txt"
		 * 
		 * 5. Set text
		 * 
		 * 6. Set listener => On touch
		 *********************************/
//		// Log
//		Log.d("MainListAdapter.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "getView()");
		
    	View v = null;
    	
    	if (convertView != null) {

    		v = convertView;
    		
		} else {//if (convertView != null)

			v = inflater.inflate(R.layout.list_row_main_list, null);

		}//if (convertView != null)

//    	v = inflater.inflate(R.layout.list_row_main_list, null);
    	
    	TextView tv = (TextView) v.findViewById(R.id.list_row_main_list_tv);
    	
    	/*********************************
		 * 4. Modify text => "list.txt"
		 *********************************/
    	String name = getItem(position);
    	
		if (name.equals(MainActv.listFileName)) {
			/*********************************
			 * 1. Modify text
			 * 2. Change background color
			 *********************************/
			String tname = Methods.convert_path_into_table_name((Activity) con);
			
			int num_of_entries = Methods.get_num_of_entries((Activity) con, tname);
			
	//		String name = name + Methods.get_num_of_entries(this, )
	//		file_names.add(item.getName() + "(" + num_of_entries + ")");
			
			name += "(" + num_of_entries + ")";
			
			// Log
			Log.d("MainActv.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]",
					"Table name=" + tname);
			
			/*********************************
			 * 2. Change background color
			 *********************************/
			tv.setBackgroundColor(Color.WHITE);
			
			tv.setTextColor(Color.BLACK);
			
		} else {//if (name.equals(MainActv.listFileName))
			
			tv.setBackgroundColor(Color.BLACK);
			
			tv.setTextColor(Color.WHITE);
			
		}//if (name.equals(MainActv.listFileName))

    	
    	/*********************************
		 * 5. Set text
		 *********************************/
    	tv.setText(name);
    	
    	/*********************************
		 * 6. Set listener => On touch
		 *********************************/
    	v.setTag(Tags.ListTags.main_list_adapter);
    	
    	v.setOnTouchListener(new ListOnTouchListener((Activity) con));
    	
    	return v;
//		return super.getView(position, convertView, parent);
	}//public View getView

	
}//public class MainListAdapter<T>
