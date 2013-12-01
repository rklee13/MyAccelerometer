package com.smarts.myaccelerometer;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MySocketHandler extends Activity implements SensorEventListener {

	private SensorManager sensor;
	private Sensor accel;
	TextView x_view, y_view, z_view;
	public static short flag = 0;
	int[] xVal = new int[4];
	int[] yVal = new int[4];
	int[] zVal = new int[4];
	
	String MySocketText = "";
	
	public static Socket socket_client = null;  //socket to send to server
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_socket_handler);
		
		x_view = (TextView)findViewById(R.id.x_coor);
		y_view = (TextView)findViewById(R.id.y_coor);
		z_view = (TextView)findViewById(R.id.z_coor);
		
		sensor = (SensorManager)getSystemService(SENSOR_SERVICE);
    	accel = sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	sensor.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_socket_handler, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		xVal[flag] = (int)(event.values[0] * 10);
		yVal[flag] = (int)(event.values[1] * 10);
		zVal[flag] = (int)(event.values[2] * 10);
		
		if(flag == 3)
		{
			
			float x = (xVal[0] + xVal[1] + xVal[2] + xVal[3]) / 4;
			float y = (yVal[0] + yVal[1] + yVal[2] + yVal[3]) / 4;
			float z = (zVal[0] + zVal[1] + zVal[2] + zVal[3]) / 4;
				
			x_view.setText("X: " + x);
			y_view.setText("Y: " + y);
			z_view.setText("Z: " + z);
			
			flag = 0;
			
			MySocketText = String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z);
			
			sendSockettoServer socketAsync = new sendSockettoServer();
			socketAsync.execute(MySocketText);
			
			return;
		}
		
		flag += 1;
	}
	
	private class sendSockettoServer extends AsyncTask<String, Void, String>
	{
	  
		@Override
		protected String doInBackground(String... mySocketString) 
		{
			String result = "";
			for(String mySocketText : mySocketString)
			{
				PrintWriter out = null;
				result = "Sent String: " + mySocketText;
				try
				{
					Intent GetIp = getIntent();
					String IpAdd = GetIp.getStringExtra("IpAddress");
	     
					InetAddress myAddress = InetAddress.getByName(IpAdd);
	     
					socket_client = new Socket(myAddress, 1000);
					out = new PrintWriter(socket_client.getOutputStream());
					out.println(mySocketText);
					out.flush();
					socket_client.close();
				}
	    
				catch(Exception ex)
				{
					result = ex.toString();
					ex.printStackTrace();
					return result;
				}
	    
			}
			return result;
	   
		}
		
		protected void onPostExecute(String result)
		{
			TextView textView = (TextView) findViewById(R.id.ResultOfSocketAction);
			textView.setText(result);
		}
		 
	}
	
	/*public void SendButtonHandler(View view)
	{
		EditText editText = (EditText) findViewById(R.id.MySocketData); //get data from textbox
		String MySocketText = editText.getText().toString();
	  
		sendSockettoServer socketAsync = new sendSockettoServer();
		socketAsync.execute(MySocketText);
	}*/

}
