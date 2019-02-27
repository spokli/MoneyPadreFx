package transactions.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import transactions.MainApp;
import transactions.model.TransactionFile;

public class FileUploadDialogController extends GeneralController {

	@FXML
	private TextField txt_path;
	@FXML
	private Button btn_upload;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	@FXML
	private void handlePathClick() {
		// open File chooser dialog
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV files (*.csv)", "*.csv"));
		List<File> fileList = fileChooser.showOpenMultipleDialog(this.dialogStage);
		if (fileList != null && !fileList.isEmpty()) {
			Iterator<File> iter = fileList.iterator();
			String pathText = iter.next().getAbsolutePath();
			while (iter.hasNext()) {
				pathText += ";" + iter.next().getAbsolutePath();
			}
			txt_path.setText(pathText);
		}
	}

	@FXML
	private void handleUploadButton() {
		// Upload File
		String path = txt_path.getText();
		if (path == null || path.equals("")) {
			return;
		}
		String[] paths = path.split(";");
		try {
			for (String singlePath : paths) {
				TransactionFile f = new TransactionFile(singlePath, model);
				
				f.upload();
				
				model.loadTransactions();
				
				if (!f.uploadErrorMessages.isEmpty()) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(mainApp.getPrimaryStage());
					alert.setTitle("Upload");
					alert.setHeaderText("Errors at upload");
					Iterator<String> iter = f.uploadErrorMessages.iterator();
					String alertText = "";
					while (iter.hasNext()) {
						alertText += iter.next() + "\n";
					}
					alert.setContentText(alertText);
					alert.showAndWait();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialogStage.close();
	}
}
