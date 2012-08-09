package co.teksof.droidSync;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class droidSync extends Activity {
	private static final String TAG = droidSync.class.getName();
	private static final int UPLOAD_FILES_REQUEST = 0;
	private static final int DOWNLOAD_FILES_REQUEST = 1;
	private static final int UPLOAD_FOLDER_REQUEST = 2;
	private static final int DOWNLOAD_FOLDER_REQUEST = 3;
	private static final int DOWNLOAD_FILE_ALIAS_REQUEST = 4;
	private static final int BROWSE_REQUEST = 5;
	private static final int SEND_REQUEST = 6;
	
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
     // Download full folder
        Button downloadFolderButton = (Button) findViewById(R.id.button_download_folder_id);
        downloadFolderButton.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v) 
			{
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				// FTP URL (Starts with ftp://, sftp://, ftps:// or scp:// followed by hostname and port).
				Uri ftpUri = Uri.parse("sftp://13.12.11.6:22");
				intent.setDataAndType(ftpUri, "vnd.android.cursor.dir/lysesoft.andftp.uri");
				// FTP credentials (optional)
				intent.putExtra("ftp_username", "root");
				intent.putExtra("ftp_password", "P@ssw0rd");
				//intent.putExtra("ftp_keyfile", "/sdcard/dsakey.txt");
				//intent.putExtra("ftp_keypass", "optionalkeypassword");
				// FTP settings (optional)
				intent.putExtra("ftp_pasv", "true");
				intent.putExtra("ftp_resume", "false");
				intent.putExtra("ftps_mode", "false");
				//intent.putExtra("ftp_resume", "true");
				//intent.putExtra("ftp_encoding", "UTF-8");		
				// Download
				intent.putExtra("command_type", "download");
				// Activity title
				intent.putExtra("progress_title", "");
				// Remote folder to download (must not end with /).
				//intent.putExtra("remote_file1", "/home/arquivos2");
				intent.putExtra("remote_file1", "/var/www/WEBMarkPlan/guarucoop/sync");
				intent.putExtra("local_folder", "/sdcard/guarucoop");
				intent.putExtra("close_ui", "true");
				startActivityForResult(intent, DOWNLOAD_FOLDER_REQUEST);
			}
        }); 
    }


	protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
		
		Toast.makeText(droidSync.this, "Aqui", Toast.LENGTH_LONG).show();
		
		Log.i(TAG, "Result: "+resultCode+ " from request: "+requestCode);
		if (intent != null)
		{
			String transferredBytesStr = intent.getStringExtra("TRANSFERSIZE");
			String transferTimeStr = intent.getStringExtra("TRANSFERTIME");
			Log.i(TAG, "Transfer status: " + intent.getStringExtra("TRANSFERSTATUS"));
			Log.i(TAG, "Transfer amount: " + intent.getStringExtra("TRANSFERAMOUNT") + " file(s)");
			Log.i(TAG, "Transfer size: " + transferredBytesStr + " bytes");
			Log.i(TAG, "Transfer time: " + transferTimeStr + " milliseconds");

			if ((transferredBytesStr != null) && (transferTimeStr != null))
			{
				try
				{
					long transferredBytes = Long.parseLong(transferredBytesStr);
					long transferTime = Long.parseLong(transferTimeStr);
					double transferRate = 0.0;
					if (transferTime > 0) transferRate = ((transferredBytes) * 1000.0) / (transferTime * 1024.0);
					Log.i(TAG, "Transfer rate: " + transferRate + " KB/s");
				} 
				catch (NumberFormatException e)
				{
					finish();
				}
			}
		}
	}
}