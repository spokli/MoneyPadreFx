package transactions.view;

import javafx.stage.Stage;
import transactions.MainApp;
import transactions.model.DataModel;

public class GeneralController {
	protected Stage dialogStage;
	protected MainApp mainApp;
	protected DataModel model;

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void initModel(DataModel model) {
		// ensure model is only set once:
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}
		this.model = model;
	}
}
