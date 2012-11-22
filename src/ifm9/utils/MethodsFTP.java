package ifm9.utils;

import ifm9.main.MainActv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class MethodsFTP {

	/*********************************
	 * -1	=> Exception
	 * -2	=> Log in failed
	 * >0	=> Reply code
	 * 
	 * REF=> http://www.searchman.info/tips/2640.html
	 * 
	 * #sqlite db file: "database disk image is malformed"
	 * REF=> http://stackoverflow.com/questions/9058169/sqlite-database-disk-image-is-malformed-on-windows-but-fine-on-android
	 *********************************/
	public static int ftp_connect_disconnect(Activity actv) {
		/*********************************
		 * memo
		 *********************************/
		// FTP client
		FTPClient fp = new FTPClient();
		
		int reply_code;
		
		String server_name = "ftp.benfranklin.chips.jp";
		
		String uname = "chips.jp-benfranklin";

		String passwd = "9x9jh4";
		
		String fpath = StringUtils.join(
				new String[]{
						MainActv.dirPath_db,
						MainActv.fileName_db
				}, File.separator);
		
		String fpath_audio = StringUtils.join(
				new String[]{
						MainActv.dirName_ExternalStorage,
						"Audios",
						"Fiddle_music",
						"Gaelic Folk Song.mp3"
				}, File.separator);

//		String fpath_remote = "./" + MainActv.fileName_db;
		
		String fpath_remote = "./" + "Gaelic Folk Song.mp3";
		
		/*********************************
		 * Connect
		 *********************************/
		try {
			
			fp.connect(server_name);
			
			reply_code = fp.getReplyCode();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "fp.getReplyCode()=" + fp.getReplyCode());
			
		} catch (SocketException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Error: " + e.toString());
			
			return -1;
			
		} catch (IOException e) {

			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Error: " + e.toString());
			
			return -1;
		}
		
		/*********************************
		 * Log in
		 *********************************/
		
		boolean res;
		try {
			
			res = fp.login(uname, passwd);
			
			if(res == false) {
				
				reply_code = fp.getReplyCode();
				
				// Log
				Log.e("MethodsFTP.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Log in failed => " + reply_code);
				
				fp.disconnect();
				
				return -2;
				
			} else {
				
				// Log
				Log.d("MethodsFTP.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Log in => Succeeded");
				
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*********************************
		 * FTP files
		 *********************************/
		// ファイル送信
		FileInputStream is;
		
		try {
			
			is = new FileInputStream(fpath);
//			is = new FileInputStream(fpath_audio);
			
//			fp.storeFile("./" + MainActv.fileName_db, is);// サーバー側
			fp.storeFile(fpath_remote, is);// サーバー側
			
//			fp.makeDirectory("./ABC");
			
			
			// Log
			Log.d("MethodsFTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File => Stored");
			
			is.close();

		} catch (FileNotFoundException e) {

			// Log
			Log.e("MethodsFTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			
			// Log
			Log.e("MethodsFTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());

		}
						
		/*********************************
		 * Disconnect
		 *********************************/
		try {
			
			fp.disconnect();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "fp => Disconnected");

			return reply_code;
			
		} catch (IOException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Error: " + e.toString());
			
			return -1;
			
		}
		
	}//public static void ftp_connect_disconnect()

	
}//public class MethodsFTP
