package edu.vt.ece4564.hessionb.snakevb;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Controller implements SensorEventListener {
	
	private static final float TILT_THRESHOLD = (float)3.0;
	private static final float SHAKE_THRESHOLD = (float)15.0;
	private static final float LIGHT_THRESHOLD = (float)10.0;

	private SensorManager sensors_;
	private Sensor gravSensor_,accelSensor_,lightSensor_;
	
	private Context parent_;
	private Game game_;
	private Player player_;
	private boolean light_;
	
	private long delay_;
	private long previousTime_,currentTime_,elapsedTime_;
	
	public Controller(Context parent, Game game, long delay) {
		parent_ = parent;
		game_ = game;
		player_ = game_.getPlayer();
		light_ = true;
		
		// Set up timing variables
		delay_ = delay*1000000; // Nano seconds
		previousTime_ = currentTime_ = System.nanoTime();
		elapsedTime_ = 0;
		
		// Create sensor listeners
		sensors_ = (SensorManager)parent_.getSystemService(Context.SENSOR_SERVICE);
		gravSensor_ = sensors_.getDefaultSensor(Sensor.TYPE_GRAVITY);
		accelSensor_ = sensors_.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		lightSensor_ = sensors_.getDefaultSensor(Sensor.TYPE_LIGHT);
		sensors_.registerListener(this, gravSensor_, SensorManager.SENSOR_DELAY_GAME);
		sensors_.registerListener(this, accelSensor_, SensorManager.SENSOR_DELAY_GAME);
		sensors_.registerListener(this, lightSensor_, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		if(sensor.getType() == Sensor.TYPE_GRAVITY) {
			// Get 8 values to average
			if(getDelay() >= delay_) {
				previousTime_ = currentTime_;

				// Round sensor values
				float x = event.values[0];
				float y = event.values[1];
				
				// Get current direction
				int dir;
				synchronized(player_) {
					dir = player_.getDirection();
				}
				
				// Calculate direction
				if(dir == Player.RIGHT || dir == Player.LEFT) {
					if(y > TILT_THRESHOLD) dir = Player.DOWN;
					else if(y < -TILT_THRESHOLD) dir = Player.UP;
				}
				else {
					if(x > TILT_THRESHOLD) dir = Player.LEFT;
					else if(x < -TILT_THRESHOLD) dir = Player.RIGHT;
				}
				
				// Update direction
				synchronized(player_) {
					player_.setNextDirection(dir);
				}
			}
		}
		else if(sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			float x = Math.abs(event.values[0]);
			float y = Math.abs(event.values[1]);
			float z = Math.abs(event.values[2]);
			
			// Finds the max of the three
			float max = (x > y) ? (x > z) ? x : z : (y > z) ? y : z;
			
			// Start the game by shaking
			if(max > SHAKE_THRESHOLD) {
				game_.redraw();
				game_.execute();
				sensors_.unregisterListener(this, accelSensor_);
			}
		}
		else if(sensor.getType() == Sensor.TYPE_LIGHT) {
			float intensity = Math.abs(event.values[0]);
			
			// I do not want to redraw unnecessarily, so i have a boolean I check
			// before redrawing
			if(!light_ && intensity > LIGHT_THRESHOLD) {
				synchronized(player_) {
					player_.setColor(Color.WHITE);
				}
				light_ = true;
				game_.redraw();
			}
			else if(light_ && intensity < LIGHT_THRESHOLD) {
				synchronized(player_) {
					player_.setColor(Color.BLACK);
				}
				light_ = false;
				game_.redraw();
			}
			
		}
	}
	
	public long getDelay() {
		currentTime_ = System.nanoTime();
		elapsedTime_ = currentTime_ - previousTime_;
		return elapsedTime_;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {}
}
