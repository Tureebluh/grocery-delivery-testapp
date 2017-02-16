package com.example.thomasjgrocerydelivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Description: Class used for displaying information about a particular product.
 * 				Customer can add products to the shopping cart from this class.
 **************************************************************************************/

public class ItemReview extends Activity {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private GlobalState gs;
	private DBManager dbManager = new DBManager(this, null, null, 1);
	private float itemPrice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_review);
		
		/******************************************************************************
		*	Initialize components used by class
		*******************************************************************************/
		Button btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
		TextView txtProductInfo = (TextView) findViewById(R.id.txtProductInfo);
		
		/******************************************************************************
		*	Initialize GlobalState object and get productinfo from dbManager
		*******************************************************************************/
		this.gs = (GlobalState) getApplication();
		txtProductInfo.setText(dbManager.getProductInfo(this, gs.getItem() ) );
		
		/******************************************************************************
		*	Adds product to cart 
		*******************************************************************************/
		btnAddToCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dbManager.addToCart( gs.getItem() );
				gs.addToTotal( itemPrice );
				startActivity(new Intent(ItemReview.this, MainActivity.class));
				
			}
		});
	}
	/**********************************************************************************
	*	Returns the price of the current item being reviewed.
	***********************************************************************************/
	public void currentItemPrice(String price){
		
		this.itemPrice = Float.parseFloat(price);
		
	}
}
