package ifm9.utils;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class MethodsFTP {

	/*********************************
	 * -1	=> Exception
	 * >0	=> Reply code
	 *********************************/
	public static int ftp_connect_disconnect(Activity actv) {
		/*********************************
		 * memo
		 *********************************/
		FTPClient fp = new FTPClient();
		
		int reply_code;
		
		// Connect
		try {
			
			fp.connect("ftp.benfranklin.chips.jp");
			
			reply_code = fp.getReplyCode();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "fp.getReplyCode()=" + fp.getReplyCode());
			
//			// debug
//			Toast.makeText(actv, "Reply code => " + fp.getReplyCode(), Toast.LENGTH_SHORT).show();
			
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
		
		try {
			
			fp.disconnect();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "fp => Disconnected");

//			// debug
//			Toast.makeText(actv, "Disconnected", Toast.LENGTH_SHORT).show();

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
