package com.example.thomasjgrocerydelivery;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Course: CIT 299
 * Description: This class is used for loading products into the database, as well as
 * 				displaying an animation of spinning fruits.
 **************************************************************************************/

public class Splash extends Activity {
	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private DBManager dbManager = new DBManager(this, null, null, 1);
	private Context context;
	private ImageView banana, orange, apple;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		context = this;
		
		/******************************************************************************
		*	Initialize ImageView for animations
		*******************************************************************************/
		banana = (ImageView) findViewById(R.id.imgBanana);
		orange = (ImageView) findViewById(R.id.imgOrange);
		apple = (ImageView) findViewById(R.id.imgApple);
		
		/******************************************************************************
		*	Schedules the TimerTask being triggered after a specified amount of time.
		*******************************************************************************/
		Timer splash = new Timer();
		splash.schedule(new ImageRotate(), 0);
		splash.schedule(new NextClass(), 6100);
		
		/******************************************************************************
		*	Determine if user is running app for first time.
		*******************************************************************************/
		if ( !firstRun() ){
			
			// Category:
			// Produce = 0
			// Meats = 1	
			// Dairy = 2
			
			dbManager.addProduct("Apple", "0", "1.00");
			dbManager.addProduct("Banana", "0", "2.00");
			dbManager.addProduct("Orange", "0", "1.00");
			dbManager.addProduct("Chicken", "1", "2.00");
			dbManager.addProduct("Pork", "1", "3.00");
			dbManager.addProduct("Beef", "1", "4.00");
			dbManager.addProduct("Milk", "2", "2.00");
			dbManager.addProduct("Yogurt", "2", "3.00");
			dbManager.addProduct("Sour Cream", "2", "3.00");
			
		}
	}
	/**********************************************************************************
	*	Method used for setting persistant data after user runs app so items aren't added
	*	to the database repeatedly.
	***********************************************************************************/
	private boolean firstRun(){
		
		final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ranBefore = sharedPref.getBoolean("RanBefore", false);
		
		if (!ranBefore){
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putBoolean("RanBefore", true);
			editor.commit();
		}
		
		return ranBefore;
	}

	/**********************************************************************************
	*	Ends thread and sends user to main screen.
	***********************************************************************************/
	private class NextClass extends TimerTask {

		@Override
		public void run() {
			
			finish();
			startActivity(new Intent(Splash.this, MainActivity.class));
			
		}	
	}
	
	/**********************************************************************************
	*	Starts animation for fruit
	***********************************************************************************/
	private class ImageRotate extends TimerTask {

		@Override
		public void run() {
			
			banana.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate));
			orange.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate));
			apple.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate));
		}
	}
}
