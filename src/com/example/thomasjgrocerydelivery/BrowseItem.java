package com.example.thomasjgrocerydelivery;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Description: This class is used for loading products into the database, as well as
 * 				displaying an animation of spinning fruits.
 **************************************************************************************/

public class BrowseItem extends ListActivity {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private GlobalState gs;
	private DBManager dbManager = new DBManager(this, null, null, 1);
	private ArrayList<String> productNames = new ArrayList<String>();
	private ArrayList<String> productIds = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/******************************************************************************
		*	Initialize GlobalState object and set default list activity to productNames
		*******************************************************************************/
		this.gs = (GlobalState) getApplication();
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productNames );
		setListAdapter(adapter);
		
		/******************************************************************************
		*	Get products from database for the particular category that the user previously
		*	selected from GlobalState object.
		*******************************************************************************/
		dbManager.getProducts(this, this.gs.getCategory() );
	}
	
	/**********************************************************************************
	*	Determine which item user selects and setItem with GlobalState object.
	*	Send user to ItemReview
	***********************************************************************************/
	protected void onListItemClick(ListView l, View v, int position, long id){
		
		switch(position){
			
			default:
				
				this.gs.setItem( Integer.parseInt(productIds.get(position)) );
				startActivity(new Intent(BrowseItem.this, ItemReview.class));
				
				break;
		}
	}
	
	/**********************************************************************************
	*	This Object is passed to DBManager which uses this method to update the ArrayList.
	*	Adapter is notified every time ArrayList is updated.
	***********************************************************************************/
	public void updateList(String productName){
		
		productNames.add(productName);
		adapter.notifyDataSetChanged();
		
	}
	
	/**********************************************************************************
	*	The corresponding productID is added to a separate ArrayList to store which item
	*	the user selects in a GlobalState object.
	***********************************************************************************/
	public void updateProductIds(String productID){
		
		productIds.add(productID);
	}
}
