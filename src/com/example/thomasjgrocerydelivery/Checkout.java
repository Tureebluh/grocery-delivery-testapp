package com.example.thomasjgrocerydelivery;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Description: This class is used for placing an order. Customer can continue shopping
 * 				and return to the main screen, or they can select a delivery date/time
 * 				and place their order.
 **************************************************************************************/

public class Checkout extends Activity {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private DBManager dbManager = new DBManager(this, null, null, 1);
	private GlobalState gs;
	private String result, total, productIdString, currentTime;
	private TimePickerDialog timePicker;
	private Calendar c;
	private DateFormat fmtDate;
	private ArrayList<String> productIds = new ArrayList<String>();
	private CharSequence text;
	private int duration;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		
		/******************************************************************************
		*	Initialize GlobalState object and objects used by layout.
		*******************************************************************************/
		this.gs = (GlobalState) getApplication();
		TextView productsInCart = (TextView) findViewById(R.id.txtProductsInCart);
		Button btnConfirm = (Button)findViewById(R.id.btnConfirm);
		Button btnContinue = (Button)findViewById(R.id.btnContinue);
		Button btnDeliveryTime = (Button)findViewById(R.id.btnDeliveryTime);
		productIdString = "";
		
		this.text = "Please select a delivery time.";
		this.duration = Toast.LENGTH_LONG;
		context = this;
		
		/******************************************************************************
		*	Create objects for formating currency and dates.
		*******************************************************************************/
		DecimalFormat currency = new DecimalFormat("$###,###,###.00");
		c = Calendar.getInstance();
		fmtDate = DateFormat.getDateInstance();
		
		/******************************************************************************
		*	Get all items added to cart
		*******************************************************************************/
		dbManager.getIdsFromCart(this);
		
		/******************************************************************************
		*	Calculate the total and get all product names from cart.
		*******************************************************************************/
		total = currency.format(gs.getTotal());
		result = dbManager.cartToString();
		result += total;
		productsInCart.setText(result);
		
		/******************************************************************************
		*	Button listener for placing an order. Dialog box ask user if they really want
		*	to place the order.
		*******************************************************************************/
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder( v.getContext() );
				builder.setMessage("Are you sure you want to place this order?").setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener).show();
				
			}
		});
		
		/******************************************************************************
		*	Button listener to continue shopping. Sends user back to main screen.
		*******************************************************************************/
		btnContinue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(Checkout.this, MainActivity.class));
				
			}
		});
		
		/******************************************************************************
		*	Button listener for setting delivery date and time.
		*******************************************************************************/
		btnDeliveryTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new DatePickerDialog(Checkout.this, d, c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
				timePicker = new TimePickerDialog(Checkout.this, t, c.get(Calendar.HOUR_OF_DAY),
						c.get(Calendar.MINUTE), false);
				
			}
		});

	}//end onCreate
	
	/**********************************************************************************
	*	Event listener for setting time for delivery.
	***********************************************************************************/
	private TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			c.set(Calendar.HOUR, hourOfDay);
			c.set(Calendar.MINUTE, minute);
			int hour = c.get(c.HOUR);
			if(hour == 0) { 
				hour = 12; 
			}
			
			String temp = fmtDate.format(c.getTime()) + " at " + hour + ":" + c.get(c.MINUTE);
			
			gs.setDelivery(temp);
		}
	};
	
	/**********************************************************************************
	*	Event listener for setting date for delivery.
	***********************************************************************************/
	private DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, monthOfYear);
			c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			timePicker.show();
		}
	};
	
	/**********************************************************************************
	*	Event listener for dialog box. Listener ensures user has selected a delivery
	*	date/time. If they have not, a Toast message is displayed informing them about
	*	what they need to do.
	***********************************************************************************/
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			switch (which){
			
			case DialogInterface.BUTTON_POSITIVE:
				
				//Checks to see if user has picked a date/time for delivery
				if(gs.getDelivery() != null) {
					
					//Loops through ArrayList and adds each productid to a string
					for(int i = 0; i < productIds.size(); i++) {
						productIdString += productIds.get(i) + " ";
					}
					
					c = Calendar.getInstance();
					
					currentTime = "" + c.getTime();
					
					gs.setCurrentOrder(productIdString);
					
					//Add order to database and clear cart.
					dbManager.addOrder(productIdString, currentTime , gs.getDelivery(), total );
					dbManager.clearCart();
					
					startActivity(new Intent(Checkout.this, OrderConfirmation.class));
					
				} else {
					
					Toast.makeText(context, text, duration).show();
					
				}
				
				break;
			
			case DialogInterface.BUTTON_NEGATIVE:
				//Do nothing
				break;
			}
		}	
	};
	
	/**********************************************************************************
	*	Adds productIds to ArrayList for adding order to database.
	***********************************************************************************/
	public void updateProductIds(String productId){
		
		productIds.add(productId);
		
	}
}
