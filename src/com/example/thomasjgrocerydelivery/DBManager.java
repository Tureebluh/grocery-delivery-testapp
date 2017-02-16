package com.example.thomasjgrocerydelivery;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

/**************************************************************************************
 * Author: Jarek Thomas
 * Date: 05/04/2015
 * Description: This class manages everything with the SQLite Database.
 **************************************************************************************/

public class DBManager extends SQLiteOpenHelper {

	/**********************************************************************************
	*	Declare variables
	***********************************************************************************/
	private static final int DATABASE_VERSION = 8;
	private static final String DATABASE_NAME = "groceries.db";
	private static final String TABLE_ORDERS = "orders";
	private static final String TABLE_PRODUCTS = "products";
	private static final String TABLE_CART = "cart";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_PRODUCTID = "productid";
	private static final String COLUMN_CATEGORY = "category";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_DATE = "dateplaced";
	private static final String COLUMN_DELIVERY = "deliverydate";
	private static final String COLUMN_TOTAL = "total";
	private static final String COLUMN_PRICE = "price";
	
	/**********************************************************************************
	*	Constructor for DBManager
	***********************************************************************************/
	public DBManager(Context context, String name, CursorFactory factory, int version) {
		
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	/**********************************************************************************
	*	Called initially to create database tables.
	***********************************************************************************/
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String query = "CREATE TABLE " + TABLE_ORDERS + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_PRODUCTID + " TEXT, " +
				COLUMN_DATE + " TEXT, " +
				COLUMN_DELIVERY + " TEXT, " +
				COLUMN_TOTAL + " TEXT " +
				");";
		db.execSQL(query);
		
		query = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
				COLUMN_PRODUCTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_CATEGORY + " TEXT, " +
				COLUMN_NAME + " TEXT, " +
				COLUMN_PRICE + " TEXT " +
				");";
		db.execSQL(query);
		
		query = "CREATE TABLE " + TABLE_CART + "(" +
				COLUMN_PRODUCTID + " TEXT " +
				");";
		db.execSQL(query);
	}
	
	/**********************************************************************************
	*	Drops all tables and calls onCreate to rebuild. Used for updating database to
	*	a new version.
	***********************************************************************************/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
		onCreate(db);
	}
	
	/**********************************************************************************
	*	Adds products to the Products table
	***********************************************************************************/
	public void addProduct(String name, String category, String price){
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_CATEGORY, category);		
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_PRICE, price);
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/**********************************************************************************
	*	Adds orders to the Orders table
	***********************************************************************************/
	public void addOrder(String productid, String date, String deliveryDate, String total){
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTID, productid);
		values.put(COLUMN_DATE, date);
		values.put(COLUMN_DELIVERY, deliveryDate);
		values.put(COLUMN_TOTAL, total);
		db.insert(TABLE_ORDERS, null, values);
		db.close();
	}
	
	/**********************************************************************************
	*	Adds products to the Cart table
	***********************************************************************************/
	public void addToCart(int productid){
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTID, productid);
		db.insert(TABLE_CART, null, values);
		db.close();
	}
	
	/**********************************************************************************
	*	Deletes products from cart individually.
	***********************************************************************************/
	public void deleteFromCart(int productid){
		
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_CART + " WHERE " + COLUMN_PRODUCTID + "=\"" + productid + "\";" );
		db.close();
		
	}
	
	/**********************************************************************************
	*	Deletes all records from Cart table.
	***********************************************************************************/
	public void clearCart(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_CART + ";");
		db.close();
	}
	
	/**********************************************************************************
	*	Deletes all records from Orders table.
	***********************************************************************************/
	public void clearOrders(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_ORDERS + ";");
		db.close();
	}
	
	/**********************************************************************************
	*	Deletes all records from Products table.
	***********************************************************************************/
	public void clearProducts(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PRODUCTS + ";");
		db.close();
	}
	
	/**********************************************************************************
	*	Gets individual product info for ItemReview class.
	***********************************************************************************/
	public String getProductInfo(ItemReview object, int productId){
		
		String dbString = "";
		
		SQLiteDatabase db = getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTID + "=\"" + productId + "\";" ; 
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		
		while ( !c.isAfterLast() ) {
			
			dbString += "Product Name: ";
			dbString += c.getString(c.getColumnIndex(COLUMN_NAME));
			dbString += "\n";
			dbString += "Price: ";
			dbString += "$";
			dbString += c.getString(c.getColumnIndex(COLUMN_PRICE));
			dbString += "\n";
			
			object.currentItemPrice(c.getString(c.getColumnIndex(COLUMN_PRICE)));
			
			c.moveToNext();
		}
		
		db.close();
		return dbString;
		
	}
	
	/**********************************************************************************
	*	Gets individual product names and price for a particular productid. Used by the
	*	OrderConfirmation class.
	***********************************************************************************/
	public String getProductNames(String productId){
		
		String dbString = "";
		
		SQLiteDatabase db = getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTID + "=\"" + productId + "\";" ; 
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		
		while ( !c.isAfterLast() ) {
			
			dbString += c.getString(c.getColumnIndex(COLUMN_NAME));
			dbString += "\n";
			dbString += "Price: ";
			dbString += "$";
			dbString += c.getString(c.getColumnIndex(COLUMN_PRICE));
			dbString += "\n\n";
			
			c.moveToNext();
		}
		
		db.close();
		return dbString;
		
	}
	
	/**********************************************************************************
	*	Searches for products in a particular category and updates ArrayList in
	*	BrowseItem class.
	***********************************************************************************/
	public void getProducts(BrowseItem object, int category){
		
		SQLiteDatabase db = getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_CATEGORY + "=\"" + category + "\";" ;
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		
		while ( !c.isAfterLast() ) {
			
			object.updateList( c.getString(c.getColumnIndex(COLUMN_NAME)) );
			
			object.updateProductIds( c.getString(c.getColumnIndex(COLUMN_PRODUCTID)) );
			
			c.moveToNext();
		}
		
		db.close();
	}
	
	/**********************************************************************************
	*	Updates the product name to ArrayList in Checkout class for each productId in
	*	the cart. Query uses INNER JOIN to complete this task.
	***********************************************************************************/
	public void getIdsFromCart(Checkout object){
		
		SQLiteDatabase db = getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_PRODUCTS + " AS P JOIN " + TABLE_CART + " AS C ON P." + COLUMN_PRODUCTID + "=" + "C." + COLUMN_PRODUCTID + ";" ;
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		
		while ( !c.isAfterLast() ) {
			
			object.updateProductIds( c.getString(c.getColumnIndex(COLUMN_PRODUCTID)) );
			
			c.moveToNext();
		}
		
		db.close();
	}
	
	/**********************************************************************************
	*	Updates ArrayList for product names and corresponding product ids for ViewCart
	*	class.
	***********************************************************************************/
	public void getCart(ViewCart object){
			
		SQLiteDatabase db = getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_PRODUCTS + " AS P JOIN " + TABLE_CART + " AS C ON P." + COLUMN_PRODUCTID + "=" + "C." + COLUMN_PRODUCTID + ";" ;
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		
		while ( !c.isAfterLast() ) {
			
			object.updateList( c.getString(c.getColumnIndex(COLUMN_NAME)) );
			
			object.updateProductIds( c.getString(c.getColumnIndex(COLUMN_PRODUCTID)) );
			
			c.moveToNext();
		}
		
		db.close();
	}
	
	/**********************************************************************************
	*	Returns values for product names and price for all products in the cart as a
	*	String.
	***********************************************************************************/
	public String cartToString(){
		
		String dbString = "";
		SQLiteDatabase db = getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_PRODUCTS + " AS P JOIN " + TABLE_CART + " AS C ON P." + COLUMN_PRODUCTID + "=" + "C." + COLUMN_PRODUCTID + ";" ;
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		
		dbString += "Items in cart: \n\n";
		
		while( !c.isAfterLast() ){
			
			dbString += c.getString(c.getColumnIndex(COLUMN_NAME) );
			dbString += "\t\t";
			dbString += "$";
			dbString += c.getString(c.getColumnIndex(COLUMN_PRICE));
			dbString += "\n";
			
			c.moveToNext();
		}
		
		dbString += "\n\n";
		dbString += "Order Total: ";
		
		db.close();
		return dbString;
	}
	
	/**********************************************************************************
	*	Returns the order details for a particular order based off productids as a string
	***********************************************************************************/
	public String orderDetails(String productIds){
		
		String dbString = "";
		SQLiteDatabase db = getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_PRODUCTID + "=\"" + productIds + "\";" ;
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		
		while( !c.isAfterLast() ){
			
			dbString += "Order Number: ";
			dbString += c.getString(c.getColumnIndex(COLUMN_ID) );
			dbString += "\n";
			dbString += "Order placed on: ";
			dbString += c.getString(c.getColumnIndex(COLUMN_DATE) );
			dbString += "\n";
			dbString += "Delivery Date: ";
			dbString += c.getString(c.getColumnIndex(COLUMN_DELIVERY));
			dbString += "\n";
			dbString += "Order Total: ";
			dbString += c.getString(c.getColumnIndex(COLUMN_TOTAL));
			dbString += "\n\n";
			dbString += "Items in Order:\n\n";
			
			c.moveToNext();
		}
		
		db.close();
		return dbString;
		
	}
	
	/**********************************************************************************
	*	Returns all productId's and order ids for all orders as string values.
	***********************************************************************************/
	public void getOrders(ReviewOrders object){
		
		SQLiteDatabase db = getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_ORDERS + ";" ;
		
		Cursor c = db.rawQuery(query, null);
		c.moveToFirst();
		
		while ( !c.isAfterLast() ) {
			
			object.updateOrderIds( c.getString(c.getColumnIndex(COLUMN_ID)) );
			object.updateProductIds(c.getString(c.getColumnIndex(COLUMN_PRODUCTID)) );
			
			c.moveToNext();
		}
		
		db.close();
		
	}
	
}
