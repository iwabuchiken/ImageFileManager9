package ifm9.tasks;

import ifm9.main.MainActv;
import ifm9.main.TNActv;
import ifm9.utils.DBUtils;
import ifm9.utils.Methods;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Task_add_table_name extends AsyncTask<String, Integer, String>{

	//
	Activity actv;
	
	
	public Task_add_table_name(Activity actv) {
		
		this.actv = actv;
		
	}//public SearchTask(Activity actv, String[] search_words)

	@Override
	protected void onPostExecute(String result) {
		// TODO 自動生成されたメソッド・スタブ
		super.onPostExecute(result);

		// debug
//		Toast.makeText(actv, result, 2000).show();
		// Log
		Log.d("SearchTask.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Task done => " + result);


	}//protected void onPostExecute(String result)

	@Override
	protected String doInBackground(String... params) {

		// 20121226_152057
		doInBackground_B31_v_1_13_fix_records();
		
		// 20121226_141329
//		doInBackground_B31_v_1_12_fix_records();
		
		// 20121226_130600
//		doInBackground_B28_v_1_3_fix_records();
		
		// 20121224_180458
//		doInBackground_B28_v_1_2_fix_records();
		
		// 20121224_175006
//		doInBackground_B28_v_1_1_fix_records();
		
//		doInBackground_B31_v_1_11_insert_table_name();
		
//		doInBackground_get_12_columns_tables_B31_v_1_9();
		
		// B31 v-1.3
//		doInBackground_insert_table_name();
		
		/// B31 v-1.4
		/// B31 v-1.5
		// Test: modifying tables with 9 columns
//		doInBackground_modify_table_with_9_columns();
		
		/// B31 v-1.5
//		Methods.restore_db(actv);
//		doInBackground_get_12_columns_tables();
		
		
		
//		doInBackground_get_12_columns_tables_B31_v_1_8();
		
		return "DONE";
		
	}//protected String doInBackground(String... params)

	private void doInBackground_B31_v_1_13_fix_records() {

		int column_num = 11;
		String table_name_key_word = "IFM9%";
		
		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getReadableDatabase();
		
		// Table names
//		List<String> t_names = Methods.get_table_list(actv);		
		List<String> t_names = Methods.get_table_list(actv, table_name_key_word);
		
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "t_names.size()=" + t_names.size());
		
		// Counters
		int count_others = 0;
		int count_is_equal = 0;
		
		for (String t_name : t_names) {

			// Log
			Log.d("Task_add_table_name.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "t_name=" + t_name);
			
			// Execute a query,
			String sql = "SELECT * FROM " + t_name;
			
			Cursor c = null;

			/*********************************
			 * Execute query
			 * If the table doesn't have a record, then
			 * 		next table
			 *********************************/
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);

				// If the table doesn't have a record, then
				if (c.getCount() < 1) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "c.getCount() < 1");

					// next table
					continue;
					
				} else {//if (c.getCount() == condition)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"Query done => c.getCount()=" + c.getCount());
					
				}//if (c.getCount() == condition)
				
				/*********************************
				 * Move the cursor to the first one,
				 * Do c.getString(11)
				 * If no value in it, then message so
				 * If the two values conflict, then also, message so
				 *********************************/
				c.moveToFirst();

				for (int i = 0; i < c.getCount(); i++) {

//					String val = c.getString(11);
					String val = c.getString(column_num);
					
					// If no value in it, then message so
					if (!val.equals(t_name)) {
						
						// Count
						count_others += 1;
						
						Methods.update_data_table_name(actv, wdb, t_name, c.getLong(0), t_name);
						
					} else {//if (val == null)
						
						count_is_equal += 1;
						
					}//if (val == null)

					// Next
					c.moveToNext();
					
				}//for (int i = 0; i < c.getCount(); i++)
				
			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
				continue;
			
			}//try

		}//for (String t_name : t_names)
		
		// Close db
		wdb.close();

		// Result
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"count_others=" + count_others + "/"
				+ "count_is_equal=" + count_is_equal
				);
		
	}//private void doInBackground_B31_v_1_13_fix_records()

	private void doInBackground_B31_v_1_12_fix_records() {

		int column_num = 11;
		String table_name_key_word = "IFM9";
//		String table_name_key_word = "IFM9__Android__AM";
		
		Methods.inspect_tables(actv, table_name_key_word, column_num);
		
//		// Setup db
//		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
//		SQLiteDatabase rdb = dbu.getReadableDatabase();
//		
//		// Table names
//		List<String> t_names = Methods.get_table_list(actv);		
//		
//		// Counters
//		int count_null = 0;
//		int count_blank = 0;
//		int count_not_equal = 0;
//		int count_is_equal = 0;
//		int count_unknown = 0;
//		
//		for (String t_name : t_names) {
//
//			// Log
//			Log.d("Task_add_table_name.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "t_name=" + t_name);
//			
//			// Execute a query,
//			String sql = "SELECT * FROM " + t_name;
//			
//			Cursor c = null;
//
//			/*********************************
//			 * Execute query
//			 * If the table doesn't have a record, then
//			 * 		next table
//			 *********************************/
//			try {
//				// Exec query
//				c = rdb.rawQuery(sql, null);
//
//				// If the table doesn't have a record, then
//				if (c.getCount() < 1) {
//					
//					// Log
//					Log.d("Task_add_table_name.java"
//							+ "["
//							+ Thread.currentThread().getStackTrace()[2]
//									.getLineNumber() + "]", "c.getCount() < 1");
//
//					// next table
//					continue;
//					
//				} else {//if (c.getCount() == condition)
//					
//					// Log
//					Log.d("Task_add_table_name.java"
//							+ "["
//							+ Thread.currentThread().getStackTrace()[2]
//									.getLineNumber() + "]",
//							"Query done => c.getCount()=" + c.getCount());
//					
//				}//if (c.getCount() == condition)
//				
//				/*********************************
//				 * Move the cursor to the first one,
//				 * Do c.getString(11)
//				 * If no value in it, then message so
//				 * If the two values conflict, then also, message so
//				 *********************************/
//				c.moveToFirst();
//
//				for (int i = 0; i < c.getCount(); i++) {
//
////					String val = c.getString(11);
//					String val = c.getString(column_num);
//					
//					// If no value in it, then message so
//					if (val == null || val.equals("null")) {
//						
//						// Count
//						count_null += 1;
//						
//						Methods.update_data_table_name(actv, rdb, t_name, c.getLong(0), t_name);
//						
//					} else if (val.equals("")) {//if (val == null)
//
//						count_blank += 1;
//
//					// If the two values conflict, then also, message so
//					} else if (!val.equals(t_name)) {//if (val == null)
//						
//						// Count
//						count_not_equal += 1;
//						
//					} else if (val.equals(t_name)) {//if (val == null)
//						
//						// Count
//						count_is_equal += 1;
//						
//					} else {//if (val == null)
//						
//						// Count
//						count_unknown += 1;
//						
//					}//if (val == null)
//
//				}//for (int i = 0; i < c.getCount(); i++)
//				
//			} catch (Exception e) {
//
//				// Log
//				Log.d("Task_add_table_name.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Exception => " + e.toString());
//			
//				continue;
//			
//			}//try
//
//		}//for (String t_name : t_names)
//		
//		// Close db
//		rdb.close();
//
//		// Result
//		// Log
//		Log.d("Task_add_table_name.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]",
//				"count_null=" + count_null + "/"
//				+ "count_blank=" + count_blank + "/"
//				+ "count_not_equal=" + count_not_equal + "/"
//				+ "count_is_equal=" + count_is_equal + "/"
//				+ "count_unknown=" + count_unknown);
		
	}//private void doInBackground_B31_v_1_12_fix_records()

	private void doInBackground_B28_v_1_3_fix_records() {

		/*********************************
		 * memo
		 *********************************/
		String t_name_trunk = "IFM9__ELECTRONICS__D5";
		
		List<String> t_names = Methods.get_table_list(actv, t_name_trunk + "%");
		
		List<String[]> data_list = new ArrayList<String[]>();
		List<String[]> data_list_new = new ArrayList<String[]>();

		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "t_names.size()=" + t_names.size());
		
		for (String t_name : t_names) {
			
			// Execute a query,
//			String sql = "SELECT * FROM " + t_name_trunk;
			String sql = "SELECT * FROM " + t_name;
			
			Cursor c = null;

			/*********************************
			 * Execute query
			 * If the table doesn't have a record, then
			 * 		next table
			 *********************************/
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);

				// If the table doesn't have a record, then
				if (c.getCount() < 1) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "c.getCount() < 1");

					// next table
					continue;
					
				} else {//if (c.getCount() == condition)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"Query done => c.getCount()=" + c.getCount());
					
				}//if (c.getCount() == condition)
				
				/*********************************
				 * Move the cursor to the first one,
				 * Do c.getString(11)
				 * If no value in it, then message so
				 * If the two values conflict, then also, message so
				 *********************************/
				c.moveToFirst();

				for (int i = 0; i < c.getCount(); i++) {

					// file_id
					String val = c.getString(3);
					
					// If no value in it, then message so
					if (val == null || val.equals("null") || val.equals("")) {
						
						String[] obj = null;
						
						obj = new String[]{
							c.getString(5),	// file_id
							c.getString(6),	// file_path
							c.getString(7),	// file_name tags
							c.getString(8),	// date_added last_viewed_at
							c.getString(9),	// date_modified table_name
							c.getString(10),// memos
							null,			// tags
							null,			// last_viewed_at
							null			// table_name
						};
						
						// Build a list of data arrays,
						data_list.add(obj);

						c.moveToNext();
						
					// If the two values conflict, then also, message so
					} else {//if (val == null)
						
					}//if (val == null)

				}//for (int i = 0; i < c.getCount(); i++)
				
				// Drop the original table,
				Methods.drop_table(actv, MainActv.dbName, t_name);
				
				// Create a new one with the same name,
				Methods.create_table(actv, MainActv.dbName, t_name);

					
				// Insert data
				for (String[] data : data_list) {
					
					Methods.insertDataIntoDB(actv, t_name, DBUtils.cols, data);
				}
				
				// Clear the data_list
				data_list.clear();

				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Process done for: " + t_name);
				
			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
			}//try
			
		}//for (String t_name : t_names)
			
		// Close db
		wdb.close();
		
	}//private void doInBackground_B28_v_1_3_fix_records()
	
	private void doInBackground_B28_v_1_2_fix_records() {

		/*********************************
		 * memo
		 *********************************/
		String t_name_trunk = "IFM9__ELECTRONICS__D5";
		
		List<String> t_names = Methods.get_table_list(actv, t_name_trunk + "%");
		
		List<String[]> data_list = new ArrayList<String[]>();
		List<String[]> data_list_new = new ArrayList<String[]>();

		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		// Counter
		int count_null = 0;
		int count_blank = 0;
		int count_yes = 0;

		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "t_names.size()=" + t_names.size());
		
//		for (String t_name : t_names) {
			
			// Execute a query,
			String sql = "SELECT * FROM " + t_name_trunk;
			
			Cursor c = null;

			/*********************************
			 * Execute query
			 * If the table doesn't have a record, then
			 * 		next table
			 *********************************/
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);

				// If the table doesn't have a record, then
				if (c.getCount() < 1) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "c.getCount() < 1");

					// next table
//					continue;
					
				} else {//if (c.getCount() == condition)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"Query done => c.getCount()=" + c.getCount());
					
				}//if (c.getCount() == condition)
				
				/*********************************
				 * Move the cursor to the first one,
				 * Do c.getString(11)
				 * If no value in it, then message so
				 * If the two values conflict, then also, message so
				 *********************************/
				c.moveToFirst();

				for (int i = 0; i < c.getCount(); i++) {

					// file_id
					String val = c.getString(3);
					
					// If no value in it, then message so
					if (val == null || val.equals("null") || val.equals("")) {
						
						// Count
						count_null += 1;
						
//						Methods.update_data_table_name(actv, wdb, t_name, c.getLong(0), t_name);
						
						String[] obj = null;
						
						obj = new String[]{
							c.getString(5),	// file_id
							c.getString(6),	// file_path
							c.getString(7),	// file_name tags
							c.getString(8),	// date_added last_viewed_at
							c.getString(9),	// date_modified table_name
							c.getString(10),// memos
							null,			// tags
							null,			// last_viewed_at
							null			// table_name
						};
						
						// Build a list of data arrays,
						data_list.add(obj);

						c.moveToNext();
						
					// If the two values conflict, then also, message so
					} else {//if (val == null)
						
						// Count
						count_yes += 1;
						
					}//if (val == null)

				}//for (int i = 0; i < c.getCount(); i++)
				
			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
//				continue;
			
			}//try
			
//		}//for (String t_name : t_names)
		
			
		// Drop the original table,
		Methods.drop_table(actv, MainActv.dbName, t_name_trunk);
		
		// Create a new one with the same name,
		Methods.create_table(actv, MainActv.dbName, t_name_trunk);

			
		// Insert data
		for (String[] data : data_list) {
			
			Methods.insertDataIntoDB(actv, t_name_trunk, DBUtils.cols, data);
		}
		
			
		// Close db
		wdb.close();
		
		// Show the result
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"count_null=" + count_null + "/"
//				+ "count_blank=" + count_blank
				+ "count_yes=" + count_yes + "/"
				+ "data_list.size()=" + data_list.size()
				
				);

		
	}//private void doInBackground_B28_v_1_2_fix_records()

	private void doInBackground_B28_v_1_1_fix_records() {
		/*********************************
		 * memo
		 *********************************/
		List<String> t_names = Methods.get_table_list(actv, "IFM9__ELECTRONICS__D5%");

		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		// Counter
		int count_null = 0;
		int count_blank = 0;
		int count_yes = 0;

		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "t_names.size()=" + t_names.size());
		
		for (String t_name : t_names) {
			
			// Execute a query,
			String sql = "SELECT * FROM " + t_name;
			
			Cursor c = null;

			/*********************************
			 * Execute query
			 * If the table doesn't have a record, then
			 * 		next table
			 *********************************/
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);

				// If the table doesn't have a record, then
				if (c.getCount() < 1) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "c.getCount() < 1");

					// next table
					continue;
					
				} else {//if (c.getCount() == condition)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"Query done => c.getCount()=" + c.getCount());
					
				}//if (c.getCount() == condition)
				
				/*********************************
				 * Move the cursor to the first one,
				 * Do c.getString(11)
				 * If no value in it, then message so
				 * If the two values conflict, then also, message so
				 *********************************/
				c.moveToFirst();

				for (int i = 0; i < c.getCount(); i++) {

					// file_id
					String val = c.getString(3);
					
					// If no value in it, then message so
					if (val == null || val.equals("null")) {
						
						// Count
						count_null += 1;
						
						Methods.update_data_table_name(actv, wdb, t_name, c.getLong(0), t_name);
						
					} else if (val.equals("")) {//if (val == null)
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]", "val.equals(\"\")");

						// Count
//						count_null += 1;
						count_blank += 1;

					// If the two values conflict, then also, message so
					} else {//if (val == null)
						
						// Count
						count_yes += 1;
						
					}//if (val == null)

				}//for (int i = 0; i < c.getCount(); i++)
				
			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
				continue;
			
			}//try
			
		}//for (String t_name : t_names)
		
		wdb.close();
		
		// Show the result
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"count_null=" + count_null
				+ "count_blank=" + count_blank
				+ "count_yes=" + count_yes
				);

	}//private void doInBackground_B28_v_1_1_fix_records()

	private void doInBackground_B31_v_1_11_insert_table_name() {
		// Get list
		List<String> t_names = Methods.get_table_list(actv, "ifm%");

		// Variables
		int count_null = 0;
		int count_blank = 0;
		int count_different = 0;
		int count_same = 0;
		
		// Execute a query,
		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		for (String t_name : t_names) {
			
			// Log
			Log.d("Task_add_table_name.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "t_name=" + t_name);
			
			// Execute a query,
			String sql = "SELECT * FROM " + t_name;
			
			Cursor c = null;

			/*********************************
			 * Execute query
			 * If the table doesn't have a record, then
			 * 		next table
			 *********************************/
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);

				// If the table doesn't have a record, then
				if (c.getCount() < 1) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "c.getCount() < 1");

					// next table
					continue;
					
				} else {//if (c.getCount() == condition)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"Query done => c.getCount()=" + c.getCount());
					
				}//if (c.getCount() == condition)
				
				/*********************************
				 * Move the cursor to the first one,
				 * Do c.getString(11)
				 * If no value in it, then message so
				 * If the two values conflict, then also, message so
				 *********************************/
				c.moveToFirst();

				for (int i = 0; i < c.getCount(); i++) {

					String val = c.getString(11);
					
					// If no value in it, then message so
					if (val == null || val.equals("null")) {
						
						// Count
						count_null += 1;
						
						Methods.update_data_table_name(actv, wdb, t_name, c.getLong(0), t_name);
						
					} else if (val.equals("")) {//if (val == null)
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]", "val.equals(\"\")");

						// Count
//						count_null += 1;
						count_blank += 1;

					// If the two values conflict, then also, message so
					} else if (!val.equals(t_name)) {//if (val == null)
						
						// Log
						Log.d("Task_add_table_name.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"!val.equals(t_name)");
						
						// Log
						Log.d("Task_add_table_name.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"val=" + val + "/"
								+ "t_name=" + t_name);
						
						// Count
						count_different += 1;
						
					} else {//if (val == null)
						
						// Count
						count_same += 1;
						
					}//if (val == null)

				}//for (int i = 0; i < c.getCount(); i++)
				
			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
				continue;
			
			}//try

		}//for (String t_name : t_names)
		
		// Close db
		wdb.close();
		
		// Show the result
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"count_null=" + count_null
				+ "count_blank=" + count_blank
				+ "count_different=" + count_different
				+ "count_same=" + count_same);

	}//private void doInBackground_B31_v_1_11_insert_table_name()

	private void doInBackground_get_12_columns_tables_B31_v_1_9() {
		/*********************************
		 * Get table list,
		 * Execute a query,
		 * Get a value in the column "table_name" (m1)
		 * Compare m1 with the table name the record is in
		 *********************************/
		// Get list
		List<String> t_names = Methods.get_table_list(actv, "ifm%");

		// Variables
		int count_null = 0;
		int count_blank = 0;
		int count_different = 0;
		int count_same = 0;
		
		// Execute a query,
		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		for (String t_name : t_names) {
			
			// Log
			Log.d("Task_add_table_name.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "t_name=" + t_name);
			
			// Execute a query,
			String sql = "SELECT * FROM " + t_name;
			
			Cursor c = null;

			/*********************************
			 * Execute query
			 * If the table doesn't have a record, then
			 * 		next table
			 *********************************/
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);

				// If the table doesn't have a record, then
				if (c.getCount() < 1) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "c.getCount() < 1");

					// next table
					continue;
					
				} else {//if (c.getCount() == condition)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"Query done => c.getCount()=" + c.getCount());
					
				}//if (c.getCount() == condition)
				
				/*********************************
				 * Move the cursor to the first one,
				 * Do c.getString(11)
				 * If no value in it, then message so
				 * If the two values conflict, then also, message so
				 *********************************/
				c.moveToFirst();

				for (int i = 0; i < c.getCount(); i++) {

					String val = c.getString(11);
					
					// If no value in it, then message so
//					if (val == null) {
					if (val == null || val.equals("null")) {
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]", "val == null");
						
						// Count
						count_null += 1;
						
					} else if (val.equals("")) {//if (val == null)
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]", "val.equals(\"\")");

						// Count
//						count_null += 1;
						count_blank += 1;

					// If the two values conflict, then also, message so
					} else if (!val.equals(t_name)) {//if (val == null)
						
						// Log
						Log.d("Task_add_table_name.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"!val.equals(t_name)");
						
						// Log
						Log.d("Task_add_table_name.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"val=" + val + "/"
								+ "t_name=" + t_name);
						
						// Count
						count_different += 1;
						
					} else {//if (val == null)
						
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]",
//								"val=" + val + "/"
//								+ "t_name=" + t_name);
						
						// Count
						count_same += 1;
						
					}//if (val == null)

				}//for (int i = 0; i < c.getCount(); i++)
				
			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
				continue;
			
			}//try

		}//for (String t_name : t_names)
		
		// Close db
		wdb.close();
		
		// Show the result
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"count_null=" + count_null
				+ "count_blank=" + count_blank
				+ "count_different=" + count_different
				+ "count_same=" + count_same);

		
	}//private void doInBackground_get_12_columns_tables_B31_v_1_9()

	private void doInBackground_get_12_columns_tables_B31_v_1_8() {

		// Get list
		List<String> t_names = Methods.get_table_list(actv, "ifm%");

		// Get tables that has 10 columns
		List<String> t_names_10 = this.get_table_list_10(t_names);

		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "t_names_10.size()=" + t_names_10.size());
		
		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		List<String[]> data_list = new ArrayList<String[]>();
		
		int count_table_num = 0;
		
		for (String t_name : t_names) {
			
			// Log
			Log.d("Task_add_table_name.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "t_name=" + t_name);
			
			// Select all records in that table
			String sql = "SELECT * FROM " + t_name;
			
			Cursor c = null;
		
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);

				/*********************************
				 * If no record, then,
				 * Drop the table,
				 * Create a new one, then,
				 * Continue the for loop
				 *********************************/
				// 
				if (c.getCount() < 1) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "c.getCount() < 1");

					// Drop the original table,
					Methods.drop_table(actv, MainActv.dbName, t_name);
					
					// Create a new one with the same name,
					Methods.create_table(actv, MainActv.dbName, t_name);

					continue;
					
				} else {//if (c.getCount() == condition)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"Query done => c.getCount()=" + c.getCount());
					
				}//if (c.getCount() == condition)
				
				count_table_num += 1;
				
				/*********************************
				 * Move the cursor to the first one,
				 * Get all data from each record,
				 * Build a list of data arrays,
				 * Drop the original table,
				 * Create a new one with the same name,
				 * Insert data from the list into the new table,
				 * Nullify the data list,
				 * Repeat the above procedures for the rest of the
				 * 		tables.
				 *********************************/
				// Move the cursor to the first one,
				c.moveToFirst();
				
				// Get all data from each record,
				String[] obj = null;
				
				for (int i = 0; i < c.getCount(); i++) {

					obj = new String[]{
//						c.getString(0),	// _id
						c.getString(1),	// file_id
						c.getString(2),	// file_path
						c.getString(3),	// file_name
						c.getString(4),	// date_added
						c.getString(5),	// date_modified
						c.getString(6),	// memos
						c.getString(7),	// tags
						c.getString(8),	// last_viewed_at
						c.getString(9)	// table_name
					};
					
					// Build a list of data arrays,
					data_list.add(obj);
					
					c.moveToNext();
					
				}//for (int i = 0; i < c.getCount(); i++)

				
				
				// Drop the original table,
				Methods.drop_table(actv, MainActv.dbName, t_name);
				
				// Create a new one with the same name,
				Methods.create_table(actv, MainActv.dbName, t_name);
				
				// Insert data from the list into the new table
				for (String[] data : data_list) {
					
					Methods.insertDataIntoDB(actv, t_name, DBUtils.cols, data);
					
				}
//				Methods.insertDataIntoDB(actv, t_name, DBUtils.cols, obj);
				
				// Nullify the data list,
				data_list.clear();
				
			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
				continue;
			
			}//try

		}//for (String t_name : t_names)
		
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "count_table_num=" + count_table_num);

		
		
		// Close db
		wdb.close();

		
	}//private void doInBackground_get_12_columns_tables_B31_v_1_8()

	private void doInBackground_get_12_columns_tables() {

		// Get list
		List<String> t_names = Methods.get_table_list(actv, "ifm%");

		// Get tables that has 10 columns
		List<String> t_names_10 = this.get_table_list_10(t_names);

		
		doInBackground_get_12_columns_tables_B31_v1_7(t_names);
		
		/// B31 v-1.5 ////////////////////////////////////
//		if (t_names_10 != null) {
//
//			// Log
//			Log.d("Task_add_table_name.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "t_names_10.size()=" + t_names_10.size());
//			
//			for (int i = 0; i < 10; i++) {
//
//				// Log
//				Log.d("Task_add_table_name.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]", "t_names_10.get(" + i + ")=" + t_names_10.get(i));
//
//			}
//
//		}//if (t_names_10 == condition)
		////////////////////////////////////////////////

		/// B31 v-1.6 ////////////////////////////////////
		//
//		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
//		SQLiteDatabase wdb = dbu.getWritableDatabase();

//		List<Object[]> data_list = new ArrayList<Object[]>();
//		
//		// Target table
//		String t_name = "IFM9__Android__AM";
//		
//		// Sql sentence
//		String sql = "SELECT * from " + t_name;
//		
//		Cursor c = null;
//	
//		try {
//			// Exec query
//			c = wdb.rawQuery(sql, null);
//			
//			// Count check
//			if (c.getCount() < 1) {
//				
//				// Log
//				Log.d("Task_add_table_name.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "c.getCount() < 1");
//				
//				wdb.close();
//				
//				return;
//				
//			}//if (c.getCount() == condition)
//			
//			// Move to first
//			c.moveToFirst();
//			
////			Object[] data = new Object[c.getColumnCount() - 1];
//			String[] data = new String[c.getColumnCount() - 1];
//			
////			for (int i = 0; i < c.getColumnCount(); i++) {
//			for (int i = 1; i < c.getColumnCount(); i++) {
//				
//				// Log
//				Log.d("Task_add_table_name.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"c.getString(" + i + ")" + c.getString(i));
//				
//				if (c.getString(i) != null) {
//
////					data[i] = c.getString(i);
//					data[i - 1] = c.getString(i);
//
//				} else {//if (c.getString(i) == condition)
//					
//					data[i] = null;
//					
//					// Log
//					Log.d("Task_add_table_name.java"
//							+ "["
//							+ Thread.currentThread().getStackTrace()[2]
//									.getLineNumber() + "]",
//							"c.getString(" + i + ") != null");
//					
//				}//if (c.getString(i) == condition)
//				
//			}//for (int i = 0; i < c.getColumnCount(); i++)
//			
//			for (int i = 0; i < data.length; i++) {
//				
//				// Log
//				Log.d("Task_add_table_name.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"data[" + i + "]" + data[i]);
//				
//			}
//			
//			// Insert data to a new table
//			String t_name_new = "IFM9__Android__AM__TEST";
//			
//			boolean res = Methods.insertDataIntoDB(
//							actv, t_name_new, DBUtils.cols, data);
//			
//			// Log
//			Log.d("Task_add_table_name.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "res=" + res);
//			
//			//
//			sql = "SELECT * from " + t_name_new;
//			
//			try {
//				c = wdb.rawQuery(sql, null);
//				
//				// Log
//				Log.d("Task_add_table_name.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"t_name_new=" + t_name_new);
//				
//				// Log
//				Log.d("Task_add_table_name.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"c.getColumnCount()=" + c.getColumnCount());
//				
//				if (c.getCount() > 0) {
//
//					// Move to first
//					c.moveToFirst();
//					
//					for (int i = 0; i < c.getColumnCount(); i++) {
//						
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]",
//								"c.getString(" + i + ")=" + c.getString(i));
//						
//					}//for (int i = 0; i < c.getColumnCount(); i++)
//					
//				} else {//if (c.getCount() > 0)
//					
//					// Log
//					Log.d("Task_add_table_name.java"
//							+ "["
//							+ Thread.currentThread().getStackTrace()[2]
//									.getLineNumber() + "]", "!c.getCount() > 0");
//					
//				}//if (c.getCount() > 0)
//				
//				
//				
//			} catch (Exception e) {
//				
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				
//			}//try
//
//			// Try => Table "...GET_UP
//			t_name_new = "IFM9__MEMO__GET_UP";
//			
//			sql = "SELECT * from " + t_name_new;
//			
//			try {
//				c = wdb.rawQuery(sql, null);
//				
//				// Log
//				Log.d("Task_add_table_name.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"t_name_new=" + t_name_new);
//				
//				// Log
//				Log.d("Task_add_table_name.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"c.getColumnCount()=" + c.getColumnCount());
//				
//				if (c.getCount() > 0) {
//
//					// Move to first
//					c.moveToFirst();
//					
//					for (int i = 0; i < c.getColumnCount(); i++) {
//						
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]",
//								"c.getString(" + i + ")=" + c.getString(i));
//						
//					}//for (int i = 0; i < c.getColumnCount(); i++)
//					
//				} else {//if (c.getCount() > 0)
//					
//					// Log
//					Log.d("Task_add_table_name.java"
//							+ "["
//							+ Thread.currentThread().getStackTrace()[2]
//									.getLineNumber() + "]", "!c.getCount() > 0");
//					
//				}//if (c.getCount() > 0)
//				
//				
//				
//			} catch (Exception e) {
//				
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				
//			}//try
//			
//		} catch (Exception e) {
//
//			// Log
//			Log.d("Task_add_table_name.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "Exception => " + e.toString());
//		
//		}//try
		
		//////////////////////////////////////////////////////////B31 v-1.6
		
		/// B31 v-1.5 ////////////////////////////////////
//		for (String t_name : t_names) {
//
//			// Log
//			Log.d("Task_add_table_name.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "t_name=" + t_name);
//			
//			// Select all records in that table
//			String sql = "SELECT * FROM " + t_name;
//			
//			Cursor c = null;
//		
//			try {
//				// Exec query
//				c = wdb.rawQuery(sql, null);
				
//				// If no record, then next
//				if (c.getCount() < 1) {
//					
//					// Log
//					Log.d("Task_add_table_name.java"
//							+ "["
//							+ Thread.currentThread().getStackTrace()[2]
//									.getLineNumber() + "]", "c.getCount() < 1");
//					
//					continue;
//					
//				}//if (c.getCount() == condition)
//
//				// Number of columns => 12?
//				if (c.getColumnCount() != 12) {
//					
//					continue;

//				} else {//if (c.getColumnCount() == condition)
//					
//					// Move to first
//					c.moveToFirst();
//
//					for (int i = 0; i < c.getColumnCount(); i++) {
//						
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]",
//								"c.getString(" + i + ")=" + c.getString(i));
//						
//					}
//					
//					
//				}//if (c.getColumnCount() == condition)
//		
//				
//			} catch (Exception e) {
//
//				// Log
//				Log.d("Task_add_table_name.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Exception => " + e.toString());
//			
//				continue;
//			
//			}//try
//		
//		}//for (String t_name : t_names)
		
		//////////////////////////////////////////
		
		//
//		wdb.close();

	}//private void doInBackground_get_12_columns_tables()

	private void doInBackground_get_12_columns_tables_B31_v1_7(
			List<String> t_names) {
		
		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		List<String[]> data_list = new ArrayList<String[]>();
		
		int count_table_num = 0;
		
		for (String t_name : t_names) {
			
			// Log
			Log.d("Task_add_table_name.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "t_name=" + t_name);
			
			// Select all records in that table
			String sql = "SELECT * FROM " + t_name;
			
			Cursor c = null;
		
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);

				// If no record, then next
				if (c.getCount() < 1) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "c.getCount() < 1");
					
					continue;
					
				}//if (c.getCount() == condition)

				// Number of columns => 12?
				if (c.getColumnCount() != 10) {
					
					continue;
					
				}//if (c.getColumnCount() != 10)

				//
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "t_name=" + t_name);
				
				count_table_num += 1;
				
				/*********************************
				 * Get all data from each record,
				 * Build a list of data arrays,
				 * Drop the original table,
				 * Create a new one with the same name,
				 * Insert data from the list into the new table
				 * Repeat the above procedures for the rest of the
				 * 		tables.
				 *********************************/

			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
				continue;
			
			}//try

		}//for (String t_name : t_names)
		
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "count_table_num=" + count_table_num);

		
		
		// Close db
		wdb.close();
		
	}//private void doInBackground_get_12_columns_tables_B31_v1_7

	private List<String> get_table_list_10(List<String> t_names) {

		// New list
		List<String> t_names_10 = new ArrayList<String>();

		// Setup db
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		// Build a new list
		for (String t_name : t_names) {
			
			String sql = "SELECT * from " + t_name;
			
			// Log
			Log.d("Task_add_table_name.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "t_name=" + t_name);
			
			Cursor c = null;
			
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);
				
				// If no record, then next
				if (c.getColumnCount() == 10) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "Column count=10");

					t_names_10.add(t_name);
					
					continue;
					
				}//if (c.getCount() == condition)
				
			} catch (Exception e) {

				// Log
				Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception => " + e.toString());
			
				continue;
			
			}//try
			
		}//for (String t_name : t_names)
		
		// Close db
		wdb.close();
		
		// Return
		return t_names_10;
		
	}//private List<String> get_table_list_10(List<String> t_names)

	private void doInBackground_modify_table_with_9_columns() {

		/////////////////////////////////////////////////////////
		/// B31 v-1.5

		// DB setup
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		// Table name
		String t_name = "IFM9__Violin";
//		String t_name = "IFM9";
		
		// Select all records in that table
		String sql = "SELECT * FROM " + t_name;
		
		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql=" + sql);
		
		Cursor c = null;
		
		try {
			/*********************************
			 * Generate a cursor
			 * Build an objects list
			 *********************************/
			// Exec query
			c = wdb.rawQuery(sql, null);

			// Contain any record?
			if (c.getCount() < 1) {
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Result => c.getCount() < 1");
				
				// Close db
				wdb.close();
				
				// Exit the method
				return;
				
			}//if (c.getCount() == condition)

			// Number of columns is 10?
			if (c.getColumnCount() != 10) {

				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"Result => c.getColumnCount() != 10");

				// Close db
				wdb.close();
				
				// Exit the method
				return;
				
			}//if (c.getCount() == condition)
			
//			// Move to first
//			c.moveToFirst();
			
			// Create a list of objects using db data
			List<Object[]> list = doInBackground_modify_table_with_9_columns_1_prepare_list(c);
			
			// Validate the list
			if (list == null) {
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "list == null");
				
				wdb.close();
				
				return;
				
			} else {//if (list == null)
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"list.size()" + list.size());
				
			}//if (list == null)
			
			/*********************************
			 * Drop the existing table
			 * Create a new one
			 *********************************/
//			// Delete the existing table
//			boolean res = Methods.drop_table(actv, MainActv.dbName, t_name);
//			
//			if (res != true) {
//				
//				// Log
//				Log.d("Task_add_table_name.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"Table was not dropped; exitting the method");
//				
//				wdb.close();
//				
//				return;
//				
//			}//if (res != true)
//			
//			// Then, create a new one with the same table name
//			res = Methods.create_table(actv, MainActv.dbName, t_name);
//			
			/*********************************
			 * Insert data from the list into db
			 *********************************/
			Object[] obj = null;
			
			for (int j = 0; j < 5; j++) {

				obj = list.get(j);
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "list.get(" + j + ")");
				// Log
				Log.d("Task_add_table_name.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "obj.length=" + obj.length);
				
				// Log
				Log.d("Task_add_table_name.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "obj.toString()" + obj.toString());
				
				for (int i = 0; i < obj.length; i++) {
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "obj[" + i + "]=" + obj[i]);
					
				}//for (int i = 0; i < obj.length; i++)

				
			}//for (int j = 0; j < 3; j++)
			
		} catch (Exception e) {

			// Log
			Log.e("Task_add_table_name.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
		}//try

		// Close db
		wdb.close();
		/////////////////////////////////////////////////////////
		
		
		/////////////////////////////////////////////////////////
		/// B31 v-1.4
		
//		// DB setup
//		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
//
//		// Table name
//		String t_name = "IFM9__Violin";
//		
//		// Select all records in that table
//		String sql = "SELECT * FROM " + t_name;
//		
//		Cursor c = null;
//		
//		try {
//			// Exec query
//			c = wdb.rawQuery(sql, null);
//			
//			// Move to first
//			c.moveToFirst();
//			
//			// Log
//			Log.d("Task_add_table_name.java"
//					+ "["
//					+ Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]",
//					"t_name=" + t_name + "(c.getColumnCount()=" + c.getColumnCount() + ")");
//			
//			// Log
//			Log.d("Task_add_table_name.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "c.getCount()=" + c.getCount());
//			
//		} catch (Exception e) {
//
//			// Log
//			Log.e("Task_add_table_name.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Exception => " + e.toString());
//			
//		}//try
//
//		// Close db
//		wdb.close();
		/////////////////////////////////////////////////////////
		
	}//private void doInBackground_modify_table_with_9_columns()

	private List<Object[]> doInBackground_modify_table_with_9_columns_1_prepare_list(
			Cursor c) {
		
		// List object
		List<Object[]> list = new ArrayList<Object[]>();
		
		// Move to first
		c.moveToFirst();
		
		for (int i = 0; i < c.getCount(); i++) {

			Object[] obj = new Object[]{
				c.getLong(0),	// _id
				c.getLong(1),	// file_id
				c.getString(2),	// file_path
				c.getString(3),	// file_name
				c.getString(4),	// date_added
				c.getString(5),	// date_modified
				c.getString(6),	// memos
				c.getString(7),	// tags
				c.getString(8),	// last_viewed_at
				c.getString(9)	// table_name
			};
			
			// Add object to the list
			list.add(obj);
			
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		return list;
	}//private List<Object> doInBackground_modify_table_with_9_columns_1_prepare_list

	private void doInBackground_insert_table_name() {
		// Get table name list
//		List<String> t_names = Methods.get_table_list(actv, "ifm");
		List<String> t_names = Methods.get_table_list(actv, "ifm%");
//		List<String> t_names = Methods.get_table_list(actv, "%_job");
		
		//
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/// B32: 1.3: DEBUG
/////////////////////////////////////////////////////////////////////
		int col_num_10 = 0;
		int col_num_12 = 0;
		int col_num_unknown = 0;
/////////////////////////////////////////////////////////////////////

		
		// For each table, add the table name to each record in the table
		for (String t_name : t_names) {

/////////////////////////////////////////////////////////////////////
			// Log
			Log.d("Task_add_table_name.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "t_name=" + t_name);
			
			// Select all records in that table
			String sql = "SELECT * FROM " + t_name;
			
			Cursor c = null;
			
			try {
				// Exec query
				c = wdb.rawQuery(sql, null);
				
				// Move to first
				c.moveToFirst();
				
				/// B32: 1.3: DEBUG
/////////////////////////////////////////////////////////////////////
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"t_name=" + t_name + "(c.getColumnCount()=" + c.getColumnCount() + ")");
				
				if (c.getColumnCount() == 10) {
					
					col_num_10 += 1;
					
				} else if (c.getColumnCount() == 12){//if (c.getColumnCount() == 10)
					
					col_num_12 += 1;
					
				} else {//if (c.getColumnCount() == 10)
			
					col_num_unknown += 1;
					
				}//if (c.getColumnCount() == 10)
				
				
/////////////////////////////////////////////////////////////////////

				/// B32: 1.2
/////////////////////////////////////////////////////////////////////
//				// Processing to all records
//				for (int i = 0; i < c.getCount(); i++) {
//
//					// If the column 'table_name' is empty, 
//					//	then, insert the table name
//					if (c.getString(9) == null ||
////							!c.getString(9).equals(t_name)) {
//							!(c.getString(9).equals(t_name))) {
//						
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]",
//								"c.getString(9)=" + c.getString(9)
//								+ "/" + "t_name=" + t_name);
//						
//						String q = "UPDATE " + t_name + " SET "
//								+ "table_name='" + t_name + "'"
//								+ " WHERE " + android.provider.BaseColumns._ID
//								+ "='" + c.getString(0) + "'";
//						
//						// Log
//						Log.d("Task_add_table_name.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]", "q=" + q);
//						
//						try {
//							
//							wdb.execSQL(q);
//							
//							// Log
//							Log.d("Task_add_table_name.java" + "["
//							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//							+ "]", "q => Done: " + q);
//							
//						} catch (SQLException e) {
//							// Log
//							Log.d("Task_add_table_name.java" + "["
//							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//							+ "]", "Exception => " + e.toString() + " / " + "q: " + q);
//							
//						}
//
//						
//					}//if (c.getString(9) == null)
//					
//					// Move to the next record in the table
//					c.moveToNext();
//					
//				}//for (int i = 0; i < c.getCount(); i++)
/////////////////////////////////////////////////////////////////////
				
//////////////////////////////////////////////////////
//				// Get column list
//				String[] col_list = 
//						Methods.get_column_list(actv, MainActv.dbName, t_name);
//				
//				for (int i = 0; i < col_list.length; i++) {
//					
//					// Log
//					Log.d("Task_add_table_name.java" + "["
//							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//							+ "]", "col_list[" + i + "]=" + col_list[i]);
//				
//				}
//////////////////////////////////////////////////////
				
				// B31: 1.1
//////////////////////////////////////////////////////
//				// Log
//				Log.d("Methods.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]",
//						"table=" + t_name
//						+ "/c.getCount()=" + c.getCount());
//				
//				// If the table has record(s)
//				if (c.getCount() > 0) {
//					// Move to first
//					c.moveToFirst();
//					
//					for (int i = 0; i < c.getCount(); i++) {
//
////						// Log
////						Log.d("Task_add_table_name.java"
////								+ "["
////								+ Thread.currentThread().getStackTrace()[2]
////										.getLineNumber() + "]",
////								"c.getLong(0)=" + c.getLong(0)
////								+ "c.getString(9)=" + c.getString(9));
//						
//						if (c.getString(9) == null) {
//							
//							String q = "UPDATE " + t_name + " SET "
//									+ "table_name='" + t_name + "'"
//									+ " WHERE " + android.provider.BaseColumns._ID
//									+ "='" + c.getString(0) + "'";
//							
//							// Log
//							Log.d("Task_add_table_name.java"
//									+ "["
//									+ Thread.currentThread().getStackTrace()[2]
//											.getLineNumber() + "]", "q=" + q);
//							
//							try {
//								
//								wdb.execSQL(q);
//								
//								// Log
//								Log.d("DBUtils.java" + "["
//								+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//								+ "]", "q => Done: " + q);
//								
//							} catch (SQLException e) {
//								// Log
//								Log.d("DBUtils.java" + "["
//								+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//								+ "]", "Exception => " + e.toString() + " / " + "q: " + q);
//								
//							}
//
//							
//						}//if (c.getString(9) == null)
//						
//						// 
//						
//						c.moveToNext();
//						
//					}//for (int i = 0; i < c.getCount(); i++)
//					
//				}//if (c.getCount() == condition)
//////////////////////////////////////////////////////
				
			} catch (Exception e) {
				// Log
				Log.d("Task_add_table_name.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Exception => " + e.toString());
				
				continue;
				
			}//try
			
		}//for (String t_name : t_names)

		//
		wdb.close();

		/// B32: 1.3: DEBUG
/////////////////////////////////////////////////////////////////////

		// Log
		Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]",
				"col_num_10=" + col_num_10 + "/"
				+ "col_num_12=" + col_num_12
				+ "col_num_unknown=" + col_num_unknown);
		
/////////////////////////////////////////////////////////////////////

		
	}//private void doInBackground_insert_table_name()


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		// debug
		Toast.makeText(actv, "Task starts => Add table name", Toast.LENGTH_SHORT).show();
	}
	
	
}//public class Task_add_table_name extends AsyncTask<String, Integer, String>
