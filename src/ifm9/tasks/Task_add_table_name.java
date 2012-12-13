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

		// B31 v-1.3
//		doInBackground_insert_table_name();
		
		/// B31 v-1.4
		/// B31 v-1.5
		// Test: modifying tables with 9 columns
//		doInBackground_modify_table_with_9_columns();
		
		/// B31 v-1.5
//		Methods.restore_db(actv);
		doInBackground_get_12_columns_tables();
		
		return "DONE";
		
	}//protected String doInBackground(String... params)

	private void doInBackground_get_12_columns_tables() {

		// Get list
		List<String> t_names = Methods.get_table_list(actv, "ifm%");

		// Get tables that has 10 columns
		List<String> t_names_10 = this.get_table_list_10(t_names);

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
		
		//
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/// B31 v-1.6 ////////////////////////////////////
		List<Object[]> data_list = new ArrayList<Object[]>();
		
		// Target table
		String t_name = "IFM9__Android__AM";
		
		// Sql sentence
		String sql = "SELECT * from " + t_name;
		
		Cursor c = null;
	
		try {
			// Exec query
			c = wdb.rawQuery(sql, null);
			
			// Count check
			if (c.getCount() < 1) {
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "c.getCount() < 1");
				
				wdb.close();
				
				return;
				
			}//if (c.getCount() == condition)
			
			// Move to first
			c.moveToFirst();
			
//			Object[] data = new Object[c.getColumnCount() - 1];
			String[] data = new String[c.getColumnCount() - 1];
			
//			for (int i = 0; i < c.getColumnCount(); i++) {
			for (int i = 1; i < c.getColumnCount(); i++) {
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"c.getString(" + i + ")" + c.getString(i));
				
				if (c.getString(i) != null) {

//					data[i] = c.getString(i);
					data[i - 1] = c.getString(i);

				} else {//if (c.getString(i) == condition)
					
					data[i] = null;
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]",
							"c.getString(" + i + ") != null");
					
				}//if (c.getString(i) == condition)
				
			}//for (int i = 0; i < c.getColumnCount(); i++)
			
			for (int i = 0; i < data.length; i++) {
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"data[" + i + "]" + data[i]);
				
			}
			
			// Insert data to a new table
			String t_name_new = "IFM9__Android__AM__TEST";
			
			boolean res = Methods.insertDataIntoDB(
							actv, t_name_new, DBUtils.cols, data);
			
			// Log
			Log.d("Task_add_table_name.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
			
			//
			sql = "SELECT * from " + t_name_new;
			
			try {
				c = wdb.rawQuery(sql, null);
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"t_name_new=" + t_name_new);
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"c.getColumnCount()=" + c.getColumnCount());
				
				if (c.getCount() > 0) {

					// Move to first
					c.moveToFirst();
					
					for (int i = 0; i < c.getColumnCount(); i++) {
						
						// Log
						Log.d("Task_add_table_name.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"c.getString(" + i + ")=" + c.getString(i));
						
					}//for (int i = 0; i < c.getColumnCount(); i++)
					
				} else {//if (c.getCount() > 0)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "!c.getCount() > 0");
					
				}//if (c.getCount() > 0)
				
				
				
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}//try

			// Try => Table "...GET_UP
			t_name_new = "IFM9__MEMO__GET_UP";
			
			sql = "SELECT * from " + t_name_new;
			
			try {
				c = wdb.rawQuery(sql, null);
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"t_name_new=" + t_name_new);
				
				// Log
				Log.d("Task_add_table_name.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"c.getColumnCount()=" + c.getColumnCount());
				
				if (c.getCount() > 0) {

					// Move to first
					c.moveToFirst();
					
					for (int i = 0; i < c.getColumnCount(); i++) {
						
						// Log
						Log.d("Task_add_table_name.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"c.getString(" + i + ")=" + c.getString(i));
						
					}//for (int i = 0; i < c.getColumnCount(); i++)
					
				} else {//if (c.getCount() > 0)
					
					// Log
					Log.d("Task_add_table_name.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "!c.getCount() > 0");
					
				}//if (c.getCount() > 0)
				
				
				
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}//try
			
		} catch (Exception e) {

			// Log
			Log.d("Task_add_table_name.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception => " + e.toString());
		
		}//try
		
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
//				
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
//					
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
		wdb.close();

	}//private void doInBackground_get_12_columns_tables()

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
