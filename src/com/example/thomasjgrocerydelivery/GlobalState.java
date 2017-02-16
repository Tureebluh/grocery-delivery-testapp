package com.example.thomasjgrocerydelivery;

import android.app.Application;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Course: CIT 299
 * Description: GlobalState class used for passing data around between classes.
 **************************************************************************************/

public class GlobalState extends Application {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private int category, item, total = 0;
	private String delivery, currentOrder;
	
	/**********************************************************************************
	*	Accessor methods
	***********************************************************************************/
	public int getCategory(){
		return category;
	}
	
	public int getItem(){
		return item;
	}
	
	public float getTotal(){
		return total;
	}
	
	public String getDelivery(){
		return delivery;
	}
	
	public String getCurrentOrder(){
		return currentOrder;
	}
	
	/**********************************************************************************
	*	Mutator methods
	***********************************************************************************/
	public void setCategory(int category){
		this.category = category;
	}
	
	public void setItem(int item){
		this.item = item;
	}
	
	public void addToTotal(float price){
		total += price;
	}
	
	public void setDelivery(String delivery){
		this.delivery = delivery;
	}
	
	public void setCurrentOrder(String productIds){
		this.currentOrder = productIds;
	}
}
