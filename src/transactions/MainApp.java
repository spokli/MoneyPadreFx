package transactions;

import java.io.IOException;
import java.util.ArrayList;

import database.SQLiteJDBC;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import transactions.model.DataModel;
import transactions.model.Transaction;
import transactions.view.RootLayoutController;
import transactions.view.TransactionDetailsController;
import transactions.view.TransactionOverviewController;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
//	private ObservableList<Transaction> transactionData = FXCollections.observableArrayList();

//	private RootLayoutController rootLayoutController;
//	private TransactionOverviewController transactionOverviewController;

	public MainApp() {
	}

//	public ObservableList<Transaction> getTransactionData() {
//		return this.transactionData;
//	}

//	public void refreshTransactionList() {
//		// Tell TransactionOverviewController to refresh list
//		transactionOverviewController.refreshTransactionList();
//	}

//	public void applyWhitelist(){
//		for(Transaction t : transactionData){
//			t.checkForWhitelist();
//		}
//		refreshTransactionList();
//	}
	
	@Override
	public void start(Stage primaryStage) {
		
		BorderPane root = new BorderPane();
        FXMLLoader transactionOverviewloader = new FXMLLoader(getClass().getResource("view/TransactionOverview.fxml"));
        try {
			root.setCenter(transactionOverviewloader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        TransactionOverviewController transactionOverviewController = transactionOverviewloader.getController();

        FXMLLoader transactionDetailsLoader = new FXMLLoader(getClass().getResource("view/TransactionDetailsView.fxml"));
        try {
			root.setRight(transactionDetailsLoader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        TransactionDetailsController transactionDetailsController = transactionDetailsLoader.getController();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("view/RootLayout.fxml"));
        try {
			root.setTop(menuLoader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        RootLayoutController rootLayoutController = menuLoader.getController();
        rootLayoutController.setMainApp(this);

        DataModel model = new DataModel();
        rootLayoutController.initModel(model);
        transactionOverviewController.initModel(model);
        transactionDetailsController.initModel(model);
//        menuController.initModel(model);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        
//        root.getCenter().autosize();
//        root.getRight().autosize();
//        root.autosize();
        
        primaryStage.show();
	}

	/**
	 * Initializes the root layout.
	 */
//	public void initRootLayout() {
//		try {
//			// Load root layout from fxml file.
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
//			rootLayout = (BorderPane) loader.load();
//
//			rootLayoutController = loader.getController();
//			rootLayoutController.setMainApp(this);
//
//			// Show the scene containing the root layout.
//			Scene scene = new Scene(rootLayout);
//			primaryStage.setScene(scene);
//
//			primaryStage.show();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Shows the transaction overview inside the root layout.
	 */
//	public void showTransactionOverview() {
//		try {
//			// Load transaction overview.
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(MainApp.class.getResource("view/TransactionOverview.fxml"));
//			AnchorPane transactionOverview = (AnchorPane) loader.load();
//
//			// Set transaction overview into the center of root layout.
//			rootLayout.setCenter(transactionOverview);
//
//			// Give the controller access to the main app.
//			transactionOverviewController = loader.getController();
//			transactionOverviewController.setMainApp(this);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

//	public TransactionOverviewController getTransactionOverviewController() {
//		return transactionOverviewController;
//	}
//
//	public RootLayoutController getRootLayoutController() {
//		return rootLayoutController;
//	}
}
