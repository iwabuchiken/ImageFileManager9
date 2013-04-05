package ifm9.utils;

import ifm9.items.TI;
import ifm9.main.MainActv;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class Methods_IFM9 {

	public static boolean
	delete_TI(Activity actv, TI ti) {
		// TODO Auto-generated method stub
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
				
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		 * 2. Query
			----------------------------*/
		String tableName = ti.getTable_name();
		
		if (tableName == null) {
		
			tableName = "IFM9";
			
		}//if (tableName == null)
		
//		String sql = "DELETE FROM " + ti.getTable_name() +
		String sql = "DELETE FROM " + tableName +
							" WHERE " + CONS.cols[0] + " = " + ti.getFileId();
		
		try {
			wdb.execSQL(sql);

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TI item deleted from db: " + ti.getFile_name());
		
			/*----------------------------
			 * 3. Dismiss dialogues
				----------------------------*/
			wdb.close();
			
			return true;

		} catch (SQLException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TI item deletion => Failed:  " + ti.getFile_name());
			
			wdb.close();
			
			return false;
			
		}
		
	}//delete_TI(Activity actv, Dialog dlg1, Dialog dlg2, TI ti)

}//public class Methods_IFM9
