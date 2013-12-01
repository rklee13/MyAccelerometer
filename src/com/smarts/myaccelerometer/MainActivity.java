package com.smarts.myaccelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensor;
	private Sensor accel;
	TextView x_view, y_view, z_view;
	public static short flag = 0;
	int[] x_coor = new int[4];
	int[] y_coor = new int[4];
	int[] z_coor = new int[4];
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		x_view = (TextView)findViewById(R.id.x_coor);
		y_view = (TextView)findViewById(R.id.y_coor);
		z_view = (TextView)findViewById(R.id.z_coor);
		
		sensor = (SensorManager)getSystemService(SENSOR_SERVICE);
    	accel = sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	sensor.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST); 
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        
        return true;
    }*/
    
    /*public void get_Sensor_Data() {
    	sensor = (SensorManager)getSystemService(SENSOR_SERVICE);
    	accel = sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	sensor.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }*/

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		//flag += 1;
		
		
		//float x = event.values[0];
		//float y = event.values[1];
		//float z = event.values[2];
		
		x_coor[flag] = (int)(event.values[0] * 10);
		y_coor[flag] = (int)(event.values[1] * 10);
		z_coor[flag] = (int)(event.values[2] * 10);
		
		if(flag == 3)
		{
			
			float x = (x_coor[0] + x_coor[1] + x_coor[2] + x_coor[3]) / 4;
			float y = (y_coor[0] + y_coor[1] + y_coor[2] + y_coor[3]) / 4;
			float z = (z_coor[0] + z_coor[1] + z_coor[2] + z_coor[3]) / 4;
				
			x_view.setText("X: " + x);
			y_view.setText("Y: " + y);
			z_view.setText("Z: " + z);
			
			flag = 0;
			
			return;
		}
		
		flag += 1;
		
	}

    
}
