package com.example.thomasjgrocerydelivery;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Course: CIT 299
 * Description: This class is used to view the items currently in the shopping cart.
 **************************************************************************************/

public class ViewCart extends ListActivity {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private GlobalState gs;
	private DBManager dbManager = new DBManager(this, null, null, 1);
	private ArrayAdapter<String> adapter;
	private ArrayList<String> productNames = new ArrayList<String>();
	private ArrayList<String> productIds = new ArrayList<String>();
	private CharSequence text;
	private int duration;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/******************************************************************************
		*	Initialize GlobalState object and objects used by Toast message.
		*******************************************************************************/
		this.gs = (GlobalState) getApplication();
		
		this.text = "Your cart is empty.";
		this.duration = Toast.LENGTH_LONG;
		
		/******************************************************************************
		*	Set adapter to default listactivity with productNames ArrayList
		*******************************************************************************/
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productNames );
		setListAdapter(adapter);
		
		/******************************************************************************
		*	Create connection to database and add Checkout has last option in List
		*******************************************************************************/
		dbManager.getCart( this );
		updateList("Checkout");
	}
	
	/**********************************************************************************
	*	Event listener for ListActivity. Ensures Checkout is last option in ListActivity
	***********************************************************************************/
	protected void onListItemClick(ListView l, View v, int position, long id){
		
		int checkout = productNames.size() - 1;
		
		if(position != checkout){
			switch(position){
				
				default:
					
					//setItem using GlobalState object
					this.gs.setItem( Integer.parseInt(productIds.get(position)) );
					
					//Create AlertDialog for Removing an Item from the cart
					AlertDialog.Builder builder = new AlertDialog.Builder( v.getContext() );
					builder.setMessage("Remove item from cart?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();
					
					break;
			}
		} else {
			
			if(checkout == 0){
				
				//Display toast message telling customer they have nothing in their cart
				Toast.makeText(v.getContext(), text, duration).show();
				
			} else {
				
				//Otherwise, send user to Checkout class
				startActivity(new Intent(ViewCart.this, Checkout.class));
				
			}
			
		}
	}
	
	/**********************************************************************************
	*	Dialog listener. If user says Yes, delete selected item and reload class. Otherwise
	*	do nothing.
	***********************************************************************************/
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			switch (which){
			
			case DialogInterface.BUTTON_POSITIVE:
				
				dbManager.deleteFromCart( gs.getItem() );
				startActivity(new Intent(ViewCart.this, ViewCart.class));
				
				break;
			
			case DialogInterface.BUTTON_NEGATIVE:
				//Do nothing
				break;
			}
		}	
	};
	
	/**********************************************************************************
	*	Add product names to ArrayList and notify adapter of dataset change
	***********************************************************************************/
	public void updateList(String productName){
		
		productNames.add(productName);
		adapter.notifyDataSetChanged();
		
	}
	
	/**********************************************************************************
	*	Add product ID's to productId's ArrayList to determine which item needs deleted.
	***********************************************************************************/
	public void updateProductIds(String productID){
		
		productIds.add(productID);
	}
}
