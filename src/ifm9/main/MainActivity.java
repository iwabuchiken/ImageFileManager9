package ifm9.main;

import ifm9.listeners.CustomOnItemLongClickListener;
import ifm9.utils.Methods;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	public static Vibrator vib;

	/*----------------------------
	 * Preference labels
		----------------------------*/
	public static String prefs_current_path = "current_path";
	
	/*----------------------------
	 * Paths and names
		----------------------------*/
	public static String dirName_ExternalStorage = "/mnt/sdcard-ext";

	public static String  dirName_base = "IFM8";

	public static String dirPath_base = dirName_ExternalStorage + File.separator + dirName_base;

	public static String dirPath_current = null;
	
	// Used => create_list_file()
	public static String listFileName = "list.txt";

	// Used => 
	public static List<String> file_names = null;

	public static ArrayAdapter<String> adapter = null;

	private static SharedPreferences prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*----------------------------
		 * 1. super
		 * 2. Set content
		 * 2-2. Set title
		 * 3. Vibrate
		 * 
		 *  4. Set list
			----------------------------*/
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*----------------------------
		 * 2-2. Set title
			----------------------------*/
		this.setTitle(this.getClass().getName());
        
        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        
        /*----------------------------
		 * 4. Set list
			----------------------------*/
        set_initial_dir_list();
        
    }//public void onCreate(Bundle savedInstanceState)

	private boolean set_initial_dir_list() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		set_initial_dir_list_part1();
//		set_initial_dir_list_part2();
		
		return false;
	}//private boolean set_initial_dir_list()
	

	private boolean set_initial_dir_list_part1() {
		/*----------------------------
		 * Steps
		 * 1. Create root dir
		 * 1-2. Create "list.txt"
		 * 2. Set variables => currentDirPath, baseDirPath
		 * 3. Get file list
		 * 3-1. Sort file list
		 * 4. Set list to adapter
		 * 5. Set adapter to list view
		 * 
		 * 6. Set listener to list
		 * 
		 * 9. Return
			----------------------------*/
		/*----------------------------
		 * 1. Create root dir
			----------------------------*/
		File file = create_root_dir();
		
		if (file == null) {
			Log.d("MainActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file == null");
			
			return false;
		}//if (file == null)
		
		/*----------------------------
		 * 1-2. Create "list.txt"
			----------------------------*/
		boolean res = create_list_file(file);
		
		if (res == false) {
			Log.d("MainActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			return false;
		}//if (res == false)

		/*----------------------------
		 * 2. Set variables => currentDirPath, baseDirPath
			----------------------------*/
		init_prefs_current_path();

		/*----------------------------
		 * 3. Get file list
			----------------------------*/
		if (file_names == null) {
			
			init_file_list(file);
			
		}//if (this.file_names == null)
			
		/*----------------------------
		 * 4. Set list to adapter
			----------------------------*/
		res = set_list_to_adapter();
		
		if (res == false)
			return false;
		
		/*----------------------------
		 * 6. Set listener to list
			----------------------------*/
		set_listener_to_list();
		
		/*----------------------------
		 * 9. Return
			----------------------------*/
		return false;
	}//private boolean set_initial_dir_list()

	private void set_listener_to_list() {
		
		ListView lv = this.getListView();
		
//		lv.setTag(Methods.ItemTags.dir_list);
		lv.setTag(Methods.ListTags.actv_main_lv);
		
		lv.setOnItemLongClickListener(new CustomOnItemLongClickListener(this));
		
	}//private void set_listener_to_list()

	private boolean set_list_to_adapter() {
		
		adapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1,
				file_names
				);

		this.setListAdapter(adapter);
		
		// Log
		Log.d("IFM9.java" + "["
		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		+ "]", "adapter => set");
		
		if (adapter == null) {
			// Log
			Log.d("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "adapter => null");
			
			return false;
			
		} else {//if (adapter == null)
			// Log
			Log.d("Methods.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "adapter => Not null");
			
			return true;
			
		}//if (adapter == null)
		


	}//private boolean set_list_to_adapter()

	private void init_file_list(File file) {
		/*----------------------------
		 * 1. Get file array
		 * 2. Sort the array
		 * 3. Return
			----------------------------*/
		
		File[] files = file.listFiles();
		
		/*----------------------------
		 * 2. Sort the array
			----------------------------*/
		Methods.sortFileList(files);
		
//		List<String> file_names = new ArrayList<String>();
		file_names = new ArrayList<String>();

		for (File item : files) {
			file_names.add(item.getName());
		}
				
	}//private void init_file_list(File file)

	private void init_prefs_current_path() {
		/*----------------------------
		 * If the preference already set, then no operation
		 * 
		 * 1. Get preference
		 * 0. Prefs set already?
		 * 2. Get editor
		 * 3. Set value
			----------------------------*/
		
		
		/*----------------------------
		 * 1. Get preference
			----------------------------*/
		prefs = 
				this.getSharedPreferences(prefs_current_path, MODE_PRIVATE);

		/*----------------------------
		 * 0. Prefs set already?
			----------------------------*/
		String temp = prefs.getString(prefs_current_path, null);
		
		if (temp != null) {
			
			// Log
			Log.d("MainActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Prefs alread set: " + temp);
			
			return;
			
		}//if (temp == null)
		
		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putString(prefs_current_path, dirPath_base);
		
		editor.commit();
		
		// Log
		Log.d("MainActivity.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs init => " + prefs_current_path + "/" + dirPath_base);
		
	}//private void init_prefs_current_path()

	private boolean create_list_file(File file) {
		File list_file = new File(file, listFileName);
		
		if (list_file.exists()) {
			// Log
			Log.d("IFM9.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "\"list.txt\" => Exists");
			
			return true;
			
		} else {//if (list_file.exists())
			try {
				BufferedWriter br = new BufferedWriter(new FileWriter(list_file));
				br.close();
				
				return true;
				
			} catch (IOException e) {
				// Log
				Log.d("IFM9.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "BufferedWriter: Exception => " + e.toString());
				
				return false;
			}
		}//if (list_file.exists())
		
	}//private boolean create_list_file()

	private File create_root_dir() {
		String baseDirPath = StringUtils.join(
				new String[]{
						this.dirName_ExternalStorage, this.dirName_base},
				File.separator);

		File file = new File(baseDirPath);
		
		if (!file.exists()) {
			try {
				file.mkdir();
				
				// Log
				Log.d("IFM9.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "Dir created => " + file.getAbsolutePath());
				
				return file;
				
			} catch (Exception e) {
				// Log
				Log.d("IFM9.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "Exception => " + e.toString());
				
				return null;
			}
			
		} else {//if (file.exists())
			// Log
			Log.d("IFM9.java"
			+ "["
			+ Thread.currentThread().getStackTrace()[2]
				.getLineNumber() + "]", "Dir exists => " + file.getAbsolutePath());
			
			return file;
		}//if (file.exists())

	}//private void create_root_dir()

	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		/*----------------------------
		 * Steps
		 * 0. Vibrate
		 * 1. Get item name
		 * 2. Get file object
		 * 3. Is a directory?
		 * 		=> If yes, update the current path
			----------------------------*/
		//
		vib.vibrate(Methods.vibLength_click);
		
		String itemName = (String) lv.getItemAtPosition(position);
		
		/*----------------------------
		 * 2. Get file object
			----------------------------*/
		File target = get_file_object(itemName);
		
		/*----------------------------
		 * 3. Is a directory?
			----------------------------*/
		if (!target.exists()) {
			// debug
			Toast.makeText(this, "This item doesn't exist in the directory: " + itemName, 
					2000)
					.show();
			
			// Log
			Log.d("IFM9.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"This item doesn't exist in the directory: " + itemName);

			return;
		}//if (!target.exists())
		
		//
		if (target.isDirectory()) {
			
			Methods.enterDir(this, target);
			
//			// debug
//			Toast.makeText(this, "Enter directory: " + itemName, 
//					2000)
//					.show();
			
		} else if (target.isFile()) {//if (target.isDirectory())
			
//			Methods.startThumbnailActivity(this, target);
			
//			Methods.toastAndLog(this, "This is a file: " + itemName, 2000);
			
			// debug
			Toast.makeText(this, "This is a file: " + itemName, 
					2000)
					.show();
			
		}//if (target.isDirectory())
		
		
		super.onListItemClick(lv, v, position, id);
	}//protected void onListItemClick(ListView l, View v, int position, long id)

	private File get_file_object(String itemName) {
		/*----------------------------
		 * 1. 
			----------------------------*/
		
		dirPath_current = prefs.getString(prefs_current_path, null);
		
		if (dirPath_current == null) {
			
			init_prefs_current_path();
			
			dirPath_current = prefs.getString(prefs_current_path, null);
			
		}//if (dirPath_current == null)
		
		File target = new File(dirPath_current, itemName);
		
		// Log
		Log.d("IFM9.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "dirPath_current: " + dirPath_current);
		
		return target;
	}//private File get_file_object(String itemName)

}//public class MainActivity extends Activity