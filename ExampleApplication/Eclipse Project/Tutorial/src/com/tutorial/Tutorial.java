package com.tutorial;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import ArduinoAdk.ArduinoAdk;
public class Tutorial extends Activity {
	
	private ArduinoAdk mArduinoAdk;
	private int Stato;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		Stato = 0;
		
		mArduinoAdk = new ArduinoAdk(this);		
		mArduinoAdk.connect();
		
		
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v){
					
					if (Stato == 0) {
						mArduinoAdk.write(255);
						Stato = 1;
					} else {
						mArduinoAdk.write(0);
						Stato = 0;
					}
					
				}
				
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial, menu);
		return true;
	}

}
