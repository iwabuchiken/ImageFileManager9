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
		
		// Get table name list
//		List<String> t_names = Methods.get_table_list(actv, "ifm");
		List<String> t_names = Methods.get_table_list(actv, "ifm%");
//		List<String> t_names = Methods.get_table_list(actv, "%_job");
		
		//
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		
		// For each table, add the table name to each record in the table
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
				
				// Move to first
				c.moveToFirst();
				
				/// B32: 1.2
				// Processing to all records
				for (int i = 0; i < c.getCount(); i++) {

					// If the column 'table_name' is empty, 
					//	then, insert the table name
					if (c.getString(9) == null) {
						
						String q = "UPDATE " + t_name + " SET "
								+ "table_name='" + t_name + "'"
								+ " WHERE " + android.provider.BaseColumns._ID
								+ "='" + c.getString(0) + "'";
						
						// Log
						Log.d("Task_add_table_name.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]", "q=" + q);
						
						try {
							
							wdb.execSQL(q);
							
							// Log
							Log.d("Task_add_table_name.java" + "["
							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
							+ "]", "q => Done: " + q);
							
						} catch (SQLException e) {
							// Log
							Log.d("Task_add_table_name.java" + "["
							+ Thread.currentThread().getStackTrace()[2].getLineNumber()
							+ "]", "Exception => " + e.toString() + " / " + "q: " + q);
							
						}

						
					}//if (c.getString(9) == null)
					
					// Move to the next record in the table
					c.moveToNext();
					
				}//for (int i = 0; i < c.getCount(); i++)

				
//////////////////////////////////////////////////////
//				// Get column list
//				String[] col_list = 
//						Methods.get_column_list(actv, MainActv.dbName, t_name);
//				
//				for (int i = 0; i < col_list.length; i++) {
//					
//					// Log
//					Log.d("Methods.java" + "["
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
		
		return "DONE";
		
	}//protected String doInBackground(String... params)

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		// debug
		Toast.makeText(actv, "Task starts => Add table name", Toast.LENGTH_SHORT).show();
	}
	
	
}//public class Task_add_table_name extends AsyncTask<String, Integer, String>
