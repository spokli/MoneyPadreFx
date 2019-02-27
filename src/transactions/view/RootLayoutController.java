package transactions.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transactions.MainApp;
import transactions.model.DataModel;

public class RootLayoutController extends GeneralController {

	@FXML
	private MenuItem mi_upload;
	@FXML
	private MenuItem mi_showWhitelist;
	@FXML
	private MenuItem mi_checkAll;
	@FXML
	private MenuItem mi_categories;
	@FXML
	private MenuItem mi_statisticsDummy;

	@FXML
	private void initialize() {

	}

	// public void setRootStage(Stage stage) {
	// this.stage = stage;
	// }

	@FXML
	private void handleUpload() {
		showFileUploadDialog();
	}

	@FXML
	private void handleShowWhitelist() {
		// TODO
	}

	@FXML
	private void handleCheckAll() {

	}

	@FXML
	private void handleCategories() {
		showCategoryDialog();
	}
	
	@FXML
	private void showStatisticsDialog(){
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/StatisticsDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			StatisticsDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(mainApp);
			controller.initModel(model);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();


		} catch (IOException e) {
			e.printStackTrace();
			// return false;
		}

	}

	public void showFileUploadDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/FileUploadDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Upload File");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			FileUploadDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(mainApp);
			controller.initModel(model);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			// Refresh TransactionOverview
			// Load the fxml file and create a new stage for the popup dialog.
			// mainApp.refreshTransactionList();

		} catch (IOException e) {
			e.printStackTrace();
			// return false;
		}
	}

	public void showCategoryDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/CategoryDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Categories");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			CategoryDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(mainApp);
			controller.initModel(model);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			// Refresh TransactionOverview
			// Load the fxml file and create a new stage for the popup dialog.
			// mainApp.refreshTransactionList();

		} catch (IOException e) {
			e.printStackTrace();
			// return false;
		}
	}
}
