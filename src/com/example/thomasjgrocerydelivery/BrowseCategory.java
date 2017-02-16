package com.example.thomasjgrocerydelivery;

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

public class BrowseCategory extends ListActivity {
	
	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private GlobalState gs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/******************************************************************************
		*	Initialize GlobalState object and categories array.
		*******************************************************************************/
		this.gs = (GlobalState) getApplication();
		
		String[] category = {
				"Produce",
				"Meats",
				"Dairy"
		};
		
		/******************************************************************************
		*	Set default list activity
		*******************************************************************************/
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category ));
	}
	
	/**********************************************************************************
	*	Stores which category user selects in GlobalState object and sends user to
	*	BrowseItem class.
	***********************************************************************************/
	protected void onListItemClick(ListView l, View v, int position, long id){
		
		switch(position){
			
			default:
				this.gs.setCategory(position);
				startActivity(new Intent(BrowseCategory.this, BrowseItem.class));
				break;
		}
	}
}
