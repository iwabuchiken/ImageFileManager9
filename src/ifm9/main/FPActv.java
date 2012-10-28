package ifm9.main;

import ifm9.items.MyView;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class FPActv extends Activity implements OnTouchListener{

	public static Vibrator vib;

	long file_id;

	Canvas canvas;

	private Paint paint;

	private Path path;

	public static MyView v;

	public static Bitmap bm;
	
	public static Bitmap bm_scaled;

	public static LinearLayout LL;
	
	int w,h;
	
	float x1,y1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*********************************
		 * 1. super
		 * 
		 * 2. Initial setup
		 *********************************/
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.actv_fp);
//        
//        /*********************************
//		 * 3. Title
//		 *********************************/
//        this.setTitle(this.getClass().getName());
//        
//        /*********************************
//		 * 4. Vibrator
//		 *********************************/
//		vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
    
		/*********************************
		 * 2. Initial setup
		 *********************************/
		initial_setup();
		
    }//public void onCreate(Bundle savedInstanceState)

    private void initial_setup() {
		/*********************************
		 * 1. Get intent data
		 * 
		 * 2. Content view
		 * 
		 * 3. Title
		 * 4. Vibrator
		 * 
		 * 5. MyView
		 * 5-2. Get display size
		 * 
		 * 6. Bitmap
		 * 
		 * 7. Canvas, paint, path
		 * 
		 * 8. Setup => Paint
		 * 
		 * 9. Set image
		 * 
		 * 10. Set view to layout
		 * 
		 * 11. Set listener

		 *********************************/
		Intent i = getIntent();

		file_id = i.getLongExtra("file_id", -1);
		
		String file_path = i.getStringExtra("file_path");

		String file_name = i.getStringExtra("file_name");

		// Log
		Log.d("FPActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file_name=" + file_name);

		// Log
		Log.d("FPActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file_path=" + file_path);
		
		/*********************************
		 * 2. Content view
		 *********************************/
        setContentView(R.layout.image_activity_for_myview);
        
        /*********************************
		 * 3. Title
		 *********************************/
        this.setTitle(this.getClass().getName());
        
        /*********************************
		 * 4. Vibrator
		 *********************************/
		vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
		
		/*********************************
		 * 5. MyView
		 *********************************/
		v = new MyView(this);

		/*********************************
		 * 5-2. Get display size
		 *********************************/
		Display disp=((WindowManager)getSystemService(
				Context.WINDOW_SERVICE)).getDefaultDisplay();
		w=disp.getWidth();
		h=disp.getHeight();

		/*********************************
		 * 6. Bitmap
		 *********************************/
		bm = BitmapFactory.decodeFile(file_path);
		
		bm_scaled = Bitmap.createScaledBitmap(bm, w, h, false);
		
		/*********************************
		 * 7. Canvas, paint, path
		 *********************************/
//		canvas=new Canvas(bm);
		canvas=new Canvas(bm_scaled);
		
		paint=new Paint();
		
		path=new Path();

		/*********************************
		 * 8. Setup => Paint
		 *********************************/
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);

		/*********************************
		 * 9. Set image
		 *********************************/
//		v.setImageBitmap(bm);
		v.setImageBitmap(bm_scaled);
		
		/*********************************
		 * 10. Set view to layout
		 *********************************/
		LL = (LinearLayout) findViewById(R.id.image_activity_LL_image);
		
		LL.addView(v);

		/*********************************
		 * 11. Set listener
		 *********************************/
		v.setOnTouchListener(this);
		
	}//private void initial_setup()
    

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actv_fp, menu);
        return true;
    }

	
    public boolean onTouch(View v, MotionEvent event) {
		/*********************************
		 * memo
		 *********************************/
		float x=event.getX();
		float y=event.getY();
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			path.reset();
			path.moveTo(x, y);
			x1=x;
			y1=y;
			break;
		case MotionEvent.ACTION_MOVE:
			path.quadTo(x1, y1, x, y);
			x1=x;
			y1=y;
			canvas.drawPath(path,paint);
			path.reset();
			path.moveTo(x, y);
			break;
		case MotionEvent.ACTION_UP:
			if(x==x1&&y==y1)y1=y1+1;
			path.quadTo(x1, y1, x, y);
			canvas.drawPath(path,paint);
			path.reset();
			break;
		}

		v = new MyView(this);
		
    	// Log
		Log.d("FPActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "event.getX()=" + event.getX());

		Log.d("FPActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "event.getY()=" + event.getY());

    	
		return false;
	}//public boolean onTouch(View v, MotionEvent event)
}
