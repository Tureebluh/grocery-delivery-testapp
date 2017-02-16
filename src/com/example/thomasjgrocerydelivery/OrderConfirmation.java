package com.example.thomasjgrocerydelivery;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Course: CIT 299
 * Description: Class is used for order confirmation, showing the user the order has been
 * 				successfully placed. 
 **************************************************************************************/

public class OrderConfirmation extends Activity {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private String productIds, productDetails;
	private String[] splitProductIds;
	private GlobalState gs;
	private DBManager dbManager = new DBManager(this, null, null, 1);
	private MediaPlayer mpConfirmation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirmation);
		
		/******************************************************************************
		*	Initialize GlobalState Object and TextView object.
		*******************************************************************************/
		this.gs = (GlobalState) getApplication();
		TextView txtDetails = (TextView)findViewById(R.id.txtDetails);
		
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
		txtDetails.setText(productDetails);
		
		/******************************************************************************
		*	Play audio file letting customer audibly hear the order was successfully 
		*	placed.
		*******************************************************************************/
		mpConfirmation = new MediaPlayer();
		mpConfirmation = MediaPlayer.create(this, R.raw.confirmation);
		mpConfirmation.start();
		
	}
}
