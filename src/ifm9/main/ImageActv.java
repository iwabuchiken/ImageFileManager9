package ifm9.main;

import ifm9.items.*;
import ifm9.listeners.button.ButtonOnClickListener;
import ifm9.listeners.button.ButtonOnTouchListener;
import ifm9.utils.Methods;
import ifm9.utils.Methods_dlg;
import ifm9.utils.Tags;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ImageActv extends Activity {

	//
	public static Vibrator vib;

	//
	long file_id;
	
	//
	public static Bitmap bm;

	/*********************************
	 * Views
	 *********************************/
	public static MyView v;

	public static LinearLayout LL;
	
	public static TextView tv_file_name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/*----------------------------
		 * Steps
		 * 1. Super
		 * 2. Set content
		----------------------------*/
		super.onCreate(savedInstanceState);

		//

//		setContentView(R.layout.image_activity);
		setContentView(R.layout.image_activity_for_myview);

		this.setTitle(this.getClass().getName());
		
		//
		vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
		
		initial_setup();
		
	}//public void onCreate(Bundle savedInstanceState)

	private void initial_setup() {
		/*----------------------------
		 * Steps
		 * 1. Get intent and data
		 * 2. Prepare image
		 * 3. Set image to the view
		 * 
		 * 3-2
		 * 
		 * 4. Set file name to the view
		 * 
		 * 5. Set listeners
			----------------------------*/
		//
		Intent i = getIntent();
		
		String file_path = i.getStringExtra("file_path");
		file_id = i.getLongExtra("file_id", -1);
		String file_name = i.getStringExtra("file_name");
		
		/*----------------------------
		 * 2. Prepare image
			----------------------------*/
		set_image(file_path);
		
		/*----------------------------
		 * 4. Set file name to the view
			----------------------------*/
//		TextView tv_file_name = (TextView) findViewById(R.id.image_activity_tv_message);
		tv_file_name = (TextView) findViewById(R.id.image_activity_tv_message);
		
		tv_file_name.setText(file_name);
		
		/*----------------------------
		 * 5. Set listeners
			----------------------------*/
		set_listeners();
		
		
//		Methods.toastAndLog(this, file_path, 2000);
		
	}//private void initial_setup()

	private void set_image(String file_path) {
		
		// Log
		Log.d("ImageActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "file_path=" + file_path);
		
//		Bitmap bm = BitmapFactory.decodeFile(file_path);
		bm = BitmapFactory.decodeFile(file_path);
		
		
		Bitmap bm_modified = set_image_1_modify_bitmap(bm);
		
		/*----------------------------
		 * 3. Set image to the view
			----------------------------*/
		// MyView
//		MyView v = new MyView(this);
		v = new MyView(this);
		
		// Set image
//		v.setImageBitmap(bm);
		v.setImageBitmap(bm_modified);
		
		//
//		LinearLayout LL = (LinearLayout) findViewById(R.id.image_activity_LL_image);
		LL = (LinearLayout) findViewById(R.id.image_activity_LL_image);
		
		LL.addView(v);
		
	}//private void set_image()
	
	private Bitmap set_image_1_modify_bitmap(Bitmap bm) {
		/*********************************
		 * memo
		 *********************************/
		int bm_w = bm.getWidth();
		int bm_h = bm.getHeight();
		
		Display disp=((WindowManager)getSystemService(
				Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		int w;
		int h;
		
		if (bm_w > bm_h) {
			
			h = disp.getHeight();
			
			w = (int) (h * ((float) bm_w / bm_h));
			
//			w = disp.getWidth();
//			
//			h = (int) (w * ((float) bm_h / bm_w));
			
		} else {//if (bm_w > bm_h)
			
			w = disp.getWidth();
			
			h = (int) (w * ((float) bm_h / bm_w));
			
//			h = disp.getHeight();
//			
//			w = (int) (h * ((float) bm_w / bm_h));
			
		}//if (bm_w > bm_h)
		
		
//		int w=disp.getWidth();
//		int h=disp.getHeight();

		return Bitmap.createScaledBitmap(bm, w, h, false);
		
	}//private Bitmap set_image_1_modify_bitmap(Bitmap bm)

	private void set_listeners() {
		/*----------------------------
		 * Steps
		 * 1. "Back" button
		 * 		1.1. OnTouch
		 * 		1.2. OnClick
		 * 
		 * 2. "Prev" button
		 * 3. "Next" button
			----------------------------*/
		ImageButton ib_back = (ImageButton) findViewById(R.id.image_activity_ib_back);
		
		ib_back.setTag(Tags.ButtonTags.image_activity_back);
		
		ib_back.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_back.setOnClickListener(new ButtonOnClickListener(this));
		
		/*********************************
		 * 2. "Prev" button
		 *********************************/
		ImageButton ib_prev = (ImageButton) findViewById(R.id.image_activity_ib_prev);
		
		ib_prev.setImageResource(R.drawable.ifm8_back);
		
		ib_prev.setTag(Tags.ButtonTags.image_activity_prev);
		
		ib_prev.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_prev.setOnClickListener(new ButtonOnClickListener(this));
		
		/*********************************
		 * 3. "Next" button
		 *********************************/
		ImageButton ib_next = (ImageButton) findViewById(R.id.image_activity_ib_next);
		
		ib_next.setImageResource(R.drawable.ifm8_forward);
		
		ib_next.setTag(Tags.ButtonTags.image_activity_next);
		
		ib_next.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_next.setOnClickListener(new ButtonOnClickListener(this));
		
	}//private void set_listeners()

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.image_actv_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.image_actv_menu_add_memo://------------------------------------
			
			// Log
			Log.d("ImageActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file_id => " + file_id);
			
			
//			Methods.dlg_addMemo(this, file_id, Methods.convertPathIntoTableName(this));
			Methods_dlg.dlg_addMemo(this, file_id, Methods.convert_path_into_table_name(this));
			
			// Log
			Log.d("ImageActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Methods.convert_path_into_table_name(this): " + Methods.convert_path_into_table_name(this));
			
			
			break;
			
		case R.id.image_actv_menu_add_patterns://------------------------------------
			
//			Methods.dlg_register_patterns(this);
			
			Methods_dlg.dlg_patterns(this);
			
			break;
		}//switch (item.getItemId())
		
		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSelected(MenuItem item)

	@Override
	protected void onPause() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onDestroy();
		
		// REF=> http://ameblo.jp/yolluca/entry-10725668557.html
		if (bm != null) {
			bm.recycle();
			
			// Log
			Log.d("ImageActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "bm => recycled");
			
			
		}//if (bm != null)
		
	}

}//public class ImageActivity extends Activity
