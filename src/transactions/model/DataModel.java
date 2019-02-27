package transactions.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.Iterator;

import database.SQLiteJDBC;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

//MODELL DARF KEINE REFERENZEN AUF VIEW ODER CONTROLLER ENTHALTEN

public class DataModel {
	public DataModel() {
		loadCategories();
		loadTransactions();
	}

	private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
	private final ObservableList<Category> categoryList = FXCollections.observableArrayList();
	private final ObjectProperty<Transaction> currentTransaction = new SimpleObjectProperty<>(null);
	private final ObjectProperty<Category> defaultCategory = new SimpleObjectProperty<>(null);
	private final ObservableList<Integer> categoryIdList = FXCollections.observableArrayList();

	public ObjectProperty<Transaction> currentTransactionProperty() {
		return currentTransaction;
	}

	public final Transaction getCurrentTransaction() {
		return currentTransaction.get();
	}

	public final void setCurrentTransaction(Transaction transaction) {
		currentTransaction.set(transaction);
	}

	public final ObservableList<Transaction> getTransactionList() {
		return transactionList;
	}
	
	public final void updateTransaction(Transaction t){
		SQLiteJDBC.updateTransaction(t);
	}
	
	public final void loadTransactions() {
		ArrayList<Transaction> transactions = SQLiteJDBC.selectTransactions(true);
		
		// Add change listeners to transactions
//		Iterator<Transaction> iter = transactions.iterator();
//		while(iter.hasNext()){
//			Transaction t = iter.next();
//			t.registerForChange
//		}
		
		transactionList.clear();
		transactionList.addAll(transactions);

		if(this.categoryList == null){
			loadCategories();
		}
		
		// Map categoryIds in transactions to category-objects, if the are
		// already loaded from the db
		if (this.categoryList != null && !(this.categoryList.isEmpty())) {
			this.mapCategoryForAllTransactions();
		}
	}
	
	public final void createTransaction(Transaction newTransaction){
		
		String errorMessage = SQLiteJDBC.insertTransaction(newTransaction);
		
		// Now, the new transaction should contain a correct autoincrement id
		
	}
	
	//##########################################
	//########### Categories ###################
	//##########################################
	
	public final void updateCategory(Category c){
		SQLiteJDBC.updateCategory(c);
		loadCategories();
	}

	public final ObservableList<Category> getCategoryList() {
		return categoryList;
	}
	
	public final void updateCategoryIdList(){
		Iterator<Category> iter = categoryList.iterator();
		while(iter.hasNext()){
			int categoryId = iter.next().categoryIdProperty().get();
			if(!categoryIdList.contains(categoryId)){
				categoryIdList.add(categoryId);
			}
		}
	}
	
//	public final ObservableList<Integer> getCategoryIdList(){
//		return this.categoryIdList;
//	}

	public final Category getDefaultCategory() {
		return defaultCategory.get();
	}

	public final void setDefaultCategory(Category cat) {
		if(cat == null){
			return;
		}
		
		// Set property at old default category
		Category oldDefault = this.defaultCategory.get();
		if (oldDefault != null) {
			oldDefault.categoryDefaultProperty().set(false);
			SQLiteJDBC.updateCategory(oldDefault);
		}

		// set property at new default category
		cat.categoryDefaultProperty().set(true);
		SQLiteJDBC.updateCategory(cat);

		// set new default property
		this.defaultCategory.set(cat);

	}

	public final void loadCategories() {
		HashMap<Integer, Category> categories = SQLiteJDBC.getCategories();
		categoryList.clear();
		categoryList.addAll(categories.values());

		// Find default category
		for (Category c : categories.values()) {
			if (c.categoryDefaultProperty().get()) {
				this.defaultCategory.set(c);
				break;
			}
		}
		
//		updateCategoryIdList();

		// Map categoryIds in transactions to category-objects
//		this.mapCategoryForAllTransactions();
	}

	public final void addCategory(Category cat) {
		SQLiteJDBC.insertCategory(cat);
		loadCategories();
	}

	public final void deleteCategory(Category cat) {
		SQLiteJDBC.deleteCategory(cat);
		loadCategories();
	}

	public final void mapCategoryForAllTransactions() {
		// Gehe über alle Transaktionen
		Iterator<Transaction> iter = this.transactionList.iterator();
		while (iter.hasNext()) {
			Transaction t = iter.next();
			// until now, the transaction's category has empty name but correct id
			int catId = t.categoryProperty().get().categoryIdProperty().get();	
			Category cat = getCategoryById(catId); 	// This is the correct category with name
			if (cat == null) {
				// Id not found, use default
				cat = this.defaultCategory.get();
			}
			t.categoryProperty().set(cat);
		}
	}

	public final Category getCategoryById(int id) {
		Iterator<Category> iter = this.categoryList.iterator();
		while (iter.hasNext()) {
			Category cat = iter.next();
			if (cat.categoryIdProperty().get() == id) {
				return cat;
			}
		}
		// If id not found
		return this.getDefaultCategory();
	}
	
	public final Category getCategoryByName(String name) {
		Iterator<Category> iter = this.categoryList.iterator();
		while (iter.hasNext()) {
			Category cat = iter.next();
			if (cat.categoryNameProperty().get().equals(name)) {
				return cat;
			}
		}
		// If id not found
//		return this.getDefaultCategory();
		return null;
	}
	
	/*
	 * ========== STATISTICS =============
	 */
	public final HashMap<Category, Double> getCategoryBalances(LocalDate from, LocalDate to){
		
		HashMap<Category, Double> result = new HashMap<Category, Double>();
		
		HashMap<Integer, Double> categoryStatistics = SQLiteJDBC.getCategoryStatistics(from, to);
		Iterator<Entry<Integer, Double>> iter = categoryStatistics.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer, Double> entry = iter.next();
			Category cat = getCategoryById(entry.getKey());
			result.put(cat, entry.getValue());
		}
		
		return result;
		// Am besten irgendwie eine Art observable hashmap, welche direkt von der View angesteuert wird
	}
}
