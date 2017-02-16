package com.example.thomasjgrocerydelivery;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Description: This class is used to display all of the orders the user has made in a 
 * 				listactivity and allow the user to view the details of each order.
 **************************************************************************************/

public class ReviewOrders extends ListActivity {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private GlobalState gs;
	private DBManager dbManager = new DBManager(this, null, null, 1);
	private ArrayList<String> orderIds = new ArrayList<String>();
	private ArrayList<String> productIds = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private CharSequence text;
	private int duration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/******************************************************************************
		*	Initialize GlobalState object and set default list activity to orderIds
		*******************************************************************************/
		this.gs = (GlobalState) getApplication();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderIds );
		setListAdapter(adapter);
		
		/******************************************************************************
		*	Create connection with database and display toast message instructing user
		*	what to do on the screen.
		*******************************************************************************/
		dbManager.getOrders(this);
		
		this.text = "Click the order number to view it's details.";
		this.duration = Toast.LENGTH_LONG;
		Toast.makeText(this, text, duration).show();
	}
	
	/**********************************************************************************
	*	Determine which item user selects and setCurrentOrder with GlobalState object.
	*	Send user to OrderReview
	***********************************************************************************/
	protected void onListItemClick(ListView l, View v, int position, long id){
		
		switch(position){
			
			default:
				
				this.gs.setCurrentOrder( productIds.get(position) );
				startActivity(new Intent(ReviewOrders.this, OrderReview.class));
				
				break;
		}
	}
	
	/**********************************************************************************
	*	Add orderId's to ArrayList to populate ListActivity
	***********************************************************************************/
	public void updateOrderIds(String orderId){
		
		orderIds.add("Order Number: " + orderId);
		
	}
	
	/**********************************************************************************
	*	Add productId's to ArrayList to setCurrentOrder.
	***********************************************************************************/
	public void updateProductIds(String productIds){
		
		this.productIds.add(productIds);
		
	}
}
