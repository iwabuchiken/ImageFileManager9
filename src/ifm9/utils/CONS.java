package ifm9.utils;

import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

public class CONS {

	public static enum MoveMode {
		// TIListAdapter.java
		ON, OFF,
		
	}//public static enum MoveMode

	public static enum PrefenceLabels {
		
		CURRENT_PATH,
		
		thumb_actv,
		
		chosen_list_item,
		
	}//public static enum PrefenceLabels

	/*********************************
	 * DB
	 *********************************/
	 // DB name
	static String dbName = null;

	// Database
	SQLiteDatabase db = null;

	//
	static String[] cols_with_index = 
				{android.provider.BaseColumns._ID, 
					"file_id", 		"file_path", "file_name", "date_added",
					"date_modified", "memos", "tags"};
	
	static String[] col_types_with_index =
				{	"INTEGER", "TEXT", 	"TEXT",		"INTEGER",
					"INTEGER",		"TEXT",	"TEXT"};

	// Main data
	public static String[] cols = 
		{"file_id", "file_path", "file_name", 	"date_added",
		"date_modified",	"memos", "tags", 	"last_viewed_at",
		"table_name"};
//	"date_modified", "memos", "tags"};

	public static String[] col_types =
		{"INTEGER", "TEXT", 	"TEXT",			"INTEGER",
		"INTEGER",			"TEXT",	"TEXT",		"INTEGER",
		"String"};

	static String[] cols_for_insert_data = 
		{"file_id", 		"file_path", "file_name", "date_added", "date_modified"};

	// Proj
	static String[] proj = {
		MediaStore.Images.Media._ID, 
		MediaStore.Images.Media.DATA,
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.DATE_ADDED,
		MediaStore.Images.Media.DATE_MODIFIED,
		};

	static String[] proj_for_get_data = {
		MediaStore.Images.Media._ID, 
		MediaStore.Images.Media.DATA,
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.DATE_ADDED,
		MediaStore.Images.Media.DATE_MODIFIED,
		"memos",
		"tags"
		};

	static String[] cols_refresh_log = {
		"last_refreshed", "num_of_items_added"
	};
	
	static String[] col_types_refresh_log = {
		"INTEGER", 			"INTEGER"
	};

	static String[] cols_memo_patterns = {"word", "table_name"};
	static String[] col_types_memo_patterns = {"TEXT", "TEXT"};
	
	static String table_name_memo_patterns = "memo_patterns";

	/****************************************
	 * Vars
	 ****************************************/
	public static final int vibLength_click = 35;

	static int tempRecordNum = 20;


//	public static enum ListTags {
//		// MainActivity.java
//		actv_main_lv,
//		
//		// Main
//		main_list_adapter,
//		
//	}//public static enum ListTags

	
}
