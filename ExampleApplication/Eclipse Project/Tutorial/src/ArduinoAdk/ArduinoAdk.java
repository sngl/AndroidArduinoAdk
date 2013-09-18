package ArduinoAdk;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;

public class ArduinoAdk {
	
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;
	private ParcelFileDescriptor mFileDescriptor;
	private UsbAccessory mUsbAccessory;
	private UsbManager mUsbManager;
	private static final String ACTION_USB_PERMISSION = "com.arduino.adk";
	private PendingIntent pi;
	private String status;
	private int nStatus;
	
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
				String action = intent.getAction(); 
				
				if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
						UsbAccessory accessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
		    			if (mUsbAccessory.equals(accessory)) {
				        		try {
									mFileOutputStream.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				        		try {
									mFileInputStream.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				        		mUsbAccessory = null;
				        		try {
									mFileDescriptor.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				        		status = "Not Connected";
				        		nStatus = 0;
		    			}
			 	}
				if (ACTION_USB_PERMISSION.equals(action)) {
					
					connect();
					
				}
		}
	};
	
	
	public ArduinoAdk (Activity main_app) {
		
		pi = PendingIntent.getBroadcast(main_app, 0, new Intent(ACTION_USB_PERMISSION), 0);
		status = "Non connected";
		nStatus = 0;
		mUsbManager =  (UsbManager) main_app.getSystemService(Activity.USB_SERVICE);		
		IntentFilter filter = new IntentFilter(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		filter.addAction(ACTION_USB_PERMISSION);
        main_app.registerReceiver(mUsbReceiver, filter);
		
		

		
	}
	public void connect(){

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		mUsbAccessory = (accessories == null ? null : accessories[0]);

		if (mUsbAccessory != null) {

			if (mUsbManager.hasPermission(mUsbAccessory)) {

				mFileDescriptor = mUsbManager.openAccessory(mUsbAccessory);
				if (mFileDescriptor != null){

					FileDescriptor fd = mFileDescriptor.getFileDescriptor();
					mFileInputStream = new FileInputStream(fd);
					mFileOutputStream = new FileOutputStream(fd);
					status = "Connected";
					nStatus = 1;
				} else {
					
					status = "Error: Error opening the Accessory";
					nStatus = 0;
					return;

				}
			}  else {
				
				mUsbManager.requestPermission(mUsbAccessory, pi);
				if (mUsbManager.hasPermission(mUsbAccessory)) {

					mFileDescriptor = mUsbManager.openAccessory(mUsbAccessory);
					if (mFileDescriptor != null){

						FileDescriptor fd = mFileDescriptor.getFileDescriptor();
						mFileInputStream = new FileInputStream(fd);
						mFileOutputStream = new FileOutputStream(fd);
						status = "Connected";
						nStatus = 1;
					 } else {
					
						status = "Error: Error opening the Accessory";
						nStatus = 0;
						return;

					}

				} else {
					
					status = "Error: No permissions";
					nStatus = 0;
					return;
				}
			}	
		} else {
			status = "Not Connected";
			nStatus = 0;
			
		}
		
		
	}
	
	public void write(int data){
		
		if (nStatus == 1) {
			try {
				mFileOutputStream.write(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			
			}
		} else {
			// TODO Add alert "Not Connected"		
		}
	}
	
	public int read(){
		
		if (nStatus == 1) {
			try {
				return mFileInputStream.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			
			}
		} else {
			// TODO Add alert "Not Connected"		
		}
		return -1;
	}
	
	public int available(){
		
		if (nStatus == 1) {
			try {
				return mFileInputStream.available();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			
			}
		} else {
			// TODO Add alert "Not Connected"		
		}
		return -1;
	}
	
	
	public String getStatus(){
		
		return status;
		
	}
	
	public boolean isConnected(){
		
		if (nStatus == 1){
			return true;
		} else if (nStatus == 0 ){
			return false;
		}		
		return false;
	}
	
	
	

}
