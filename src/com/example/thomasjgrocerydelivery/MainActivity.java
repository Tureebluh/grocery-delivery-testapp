package com.example.thomasjgrocerydelivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Course: CIT 299
 * Description: This class is the main screen. Users can view past orders, browse items,
 * 				or view their shopping cart from this screen.
 **************************************************************************************/

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/******************************************************************************
		*	Initialize Buttons used for changing activities
		*******************************************************************************/
		Button btnBrowse = (Button) findViewById(R.id.btnBrowse);
		Button btnOrders = (Button) findViewById(R.id.btnOrders);
		Button btnCart = (Button) findViewById(R.id.btnCart);
		
		/******************************************************************************
		*	Sends user to Browse items when clicked.
		*******************************************************************************/
		btnBrowse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(MainActivity.this, BrowseCategory.class));
				
			}
		});
		
		/******************************************************************************
		*	Sends user to review past orders when clicked.
		*******************************************************************************/
		btnOrders.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(MainActivity.this, ReviewOrders.class));
				
			}
		});
		
		/******************************************************************************
		*	Sends user to their shopping cart when clicked.
		*******************************************************************************/
		btnCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(MainActivity.this, ViewCart.class));
				
			}
		});
	}
}
