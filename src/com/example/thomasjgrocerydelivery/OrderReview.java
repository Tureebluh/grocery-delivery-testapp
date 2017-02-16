package com.example.thomasjgrocerydelivery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Course: CIT 299
 * Description: This class is used to display the results of a particular order from the
 * 				View Orders button on the main page.
 **************************************************************************************/

public class OrderReview extends Activity {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private String productIds, productDetails;
	private String[] splitProductIds;
	private GlobalState gs;
	private DBManager dbManager = new DBManager(this, null, null, 1);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_review);
		
		/******************************************************************************
		*	Initialize GlobalState Object and TextView object.
		*******************************************************************************/
		this.gs = (GlobalState) getApplication();
		TextView txtOrderDetails = (TextView)findViewById(R.id.txtOrderDetails);
		
		/******************************************************************************
		*	Trim trailing space from string containing all the productid's for this order
		*	and get the details of the order with those productid's.
		*******************************************************************************/
		productIds = gs.getCurrentOrder().trim();
		productDetails = "";
		productDetails += dbManager.orderDetails( gs.getCurrentOrder() );
		
		/******************************************************************************
		*	Split string into individual productid's using space as a delimiter.
		*******************************************************************************/
		try {
		    splitProductIds = productIds.split("\\s+");
		} catch (Exception ex) {
		    
		}
		
		/******************************************************************************
		*	Get the name of each product in the order using the productid's
		*******************************************************************************/
		for(int i = 0; i < splitProductIds.length; i++){
			productDetails += dbManager.getProductNames( splitProductIds[i] );
		}
		
		/******************************************************************************
		*	Set txtDetails to products in order.
		*******************************************************************************/
		txtOrderDetails.setText(productDetails);
		
	}
}
