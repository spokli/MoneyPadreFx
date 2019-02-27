package transactions.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;

import transactions.model.Category;
import transactions.model.DataModel;
import transactions.model.Transaction;
import transactions.model.converter.CategoryNameConverter;
import transactions.model.converter.CurrencyConverter;
import transactions.model.converter.TransactionStatusConverter;
import transactions.model.converter.TransactionTypeConverter;

public class TransactionDetailsController extends GeneralController {

	@FXML
	private Label lbl_transactionAccount;
	@FXML
	private Label lbl_transactionDate;
	@FXML
	private Label lbl_valueDate;
	@FXML
	private Label lbl_transactionType;
	@FXML
	private Label lbl_usageText;
	@FXML
	private Label lbl_creditorID;
	@FXML
	private Label lbl_mandateReference;
	@FXML
	private Label lbl_customerReference;
	@FXML
	private Label lbl_recipient_payer;
	@FXML
	private Label lbl_iban;
	@FXML
	private Label lbl_bic;
	@FXML
	private Label lbl_value;
	@FXML
	private Label lbl_currency;
	@FXML
	private Label lbl_info;
	@FXML
	private Label lbl_status;
	@FXML
//	private ComboBox<Integer> box_category;
	private ComboBox<Category> box_category;
	@FXML
	private TextArea txt_note;

	@FXML
	private Button btn_checkEntry;
	@FXML
	private Button btn_addToWhitelist;

	@FXML
	private void initialize() {
		txt_note.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				handleNoteChanged();
			}
		});

		box_category.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
					handleCategoryChanged();
			}
		});
	}

	/**
	 * Initialisiert model und setzt Listener auf �nderung von aktuell
	 * selektierter Transaction im Model
	 * 
	 * @param model
	 */
	@Override
	public void initModel(DataModel model) {
		super.initModel(model);

		model.currentTransactionProperty().addListener((obs, oldTransaction, newTransaction) -> {
			if (oldTransaction != null) {

				// Entbinde den Wert der Labels von der alten Transaktion
				lbl_transactionAccount.textProperty().unbindBidirectional(oldTransaction.transactionAccountProperty());
				lbl_transactionDate.textProperty().unbindBidirectional(oldTransaction.transactionDateProperty());
				lbl_valueDate.textProperty().unbindBidirectional(oldTransaction.valueDateProperty());
				lbl_transactionType.textProperty().unbindBidirectional(oldTransaction.transactionTypeProperty());
				lbl_usageText.textProperty().unbindBidirectional(oldTransaction.usageTextProperty());
				lbl_creditorID.textProperty().unbindBidirectional(oldTransaction.creditorIDProperty());
				lbl_mandateReference.textProperty().unbindBidirectional(oldTransaction.mandateReferenceProperty());
				lbl_customerReference.textProperty().unbindBidirectional(oldTransaction.customerReferenceProperty());
				lbl_recipient_payer.textProperty().unbindBidirectional(oldTransaction.recipient_payerProperty());
				lbl_iban.textProperty().unbindBidirectional(oldTransaction.ibanProperty());
				lbl_bic.textProperty().unbindBidirectional(oldTransaction.bicProperty());
				lbl_value.textProperty().unbindBidirectional(oldTransaction.valueProperty());
				lbl_currency.textProperty().unbindBidirectional(oldTransaction.currencyProperty());
				lbl_info.textProperty().unbindBidirectional(oldTransaction.infoProperty());
				lbl_status.textProperty().unbindBidirectional(oldTransaction.statusProperty());
				box_category.valueProperty().unbindBidirectional(oldTransaction.categoryProperty());
//				box_category.valueProperty().unbindBidirectional(oldTransaction.categoryIdProperty());
				txt_note.textProperty().unbindBidirectional(oldTransaction.noteProperty());
			}
			if (newTransaction == null) {
				lbl_transactionAccount.setText("");
				lbl_transactionDate.setText("");
				lbl_valueDate.setText("");
				lbl_transactionType.setText("");
				lbl_usageText.setText("");
				lbl_creditorID.setText("");
				lbl_mandateReference.setText("");
				lbl_customerReference.setText("");
				lbl_recipient_payer.setText("");
				lbl_iban.setText("");
				lbl_bic.setText("");
				lbl_value.setText("");
				lbl_currency.setText("");
				lbl_info.setText("");
				lbl_status.setText("");
				box_category.setValue(model.getDefaultCategory());
//				box_category.setValue(model.getDefaultCategory().categoryIdProperty().get());
				txt_note.setText("");

			} else {
				lbl_transactionAccount.textProperty().bindBidirectional(newTransaction.transactionAccountProperty());
				lbl_transactionDate.textProperty().bindBidirectional(newTransaction.transactionDateProperty(),
						new LocalDateStringConverter());
				lbl_valueDate.textProperty().bindBidirectional(newTransaction.valueDateProperty(),
						new LocalDateStringConverter());
				lbl_transactionType.textProperty().bindBidirectional(newTransaction.transactionTypeProperty(),
						new TransactionTypeConverter());
				lbl_usageText.textProperty().bindBidirectional(newTransaction.usageTextProperty());
				lbl_creditorID.textProperty().bindBidirectional(newTransaction.creditorIDProperty());
				lbl_mandateReference.textProperty().bindBidirectional(newTransaction.mandateReferenceProperty());
				lbl_customerReference.textProperty().bindBidirectional(newTransaction.customerReferenceProperty());
				lbl_recipient_payer.textProperty().bindBidirectional(newTransaction.recipient_payerProperty());
				lbl_iban.textProperty().bindBidirectional(newTransaction.ibanProperty());
				lbl_bic.textProperty().bindBidirectional(newTransaction.bicProperty());
				lbl_value.textProperty().bindBidirectional(newTransaction.valueProperty(), new DecimalFormat());
				lbl_currency.textProperty().bindBidirectional(newTransaction.currencyProperty(),
						new CurrencyConverter());
				lbl_info.textProperty().bindBidirectional(newTransaction.infoProperty());
				lbl_status.textProperty().bindBidirectional(newTransaction.statusProperty(),
						new TransactionStatusConverter());
				box_category.valueProperty().bindBidirectional(newTransaction.categoryProperty());
//				box_category.valueProperty().bindBidirectional(newTransaction.categoryIdProperty());
				txt_note.textProperty().bindBidirectional(newTransaction.noteProperty());
			}
		});

//		box_category.setConverter(new CategoryNameConverter(model));
//		box_category.itemsProperty().set(model.getCategoryIdList());
		box_category.itemsProperty().set(model.getCategoryList());

		// model.getCategoryIdList().addListener(new
		// ListChangeListener<Integer>() {
		//
		// @Override
		// public void onChanged(Change c) {
		// box_category.itemsProperty().set(model.getCategoryIdList());
		// }
		// });
		// // Add categories to Dropdown menu
		// box_category.getItems().clear();
		// box_category.getItems().addAll(model.getCategoryList());
	}

	@FXML
	private void handleAddToWhitelist() {
		// int selectedIndex =
		// transactionTable.getSelectionModel().getSelectedIndex();
		// if (selectedIndex >= 0) {
		//
		// // Set transaction type checked
		// Transaction t = transactionTable.getItems().get(selectedIndex);
		//
		// showAddWhitelistDialog(t);
		//
		// } else {
		// // Nothing selected.
		// Alert alert = new Alert(AlertType.WARNING);
		// alert.initOwner(mainApp.getPrimaryStage());
		// alert.setTitle("No Selection");
		// alert.setHeaderText("No Transaction Selected");
		// alert.setContentText("Please select a transaction in the table.");
		//
		// alert.showAndWait();
		// }
	}

	private void showAddWhitelistDialog(Transaction t) {
		// try {
		// // Load the fxml file and create a new stage for the popup dialog.
		// FXMLLoader loader = new FXMLLoader();
		// loader.setLocation(MainApp.class.getResource("view/AddWhitelistDialog.fxml"));
		// AnchorPane page = (AnchorPane) loader.load();
		//
		// // Create the dialog Stage.
		// Stage dialogStage = new Stage();
		// dialogStage.setTitle("Add to Whitelist");
		// dialogStage.initModality(Modality.WINDOW_MODAL);
		// // dialogStage.initOwner();
		// Scene scene = new Scene(page);
		// dialogStage.setScene(scene);
		//
		// AddWhitelistController controller = loader.getController();
		// controller.setTransaction(t);
		// controller.setDialogStage(dialogStage);
		// controller.setMainApp(mainApp);
		//
		// // Show the dialog and wait until the user closes it
		// dialogStage.showAndWait();
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// // return false;
		// }

	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleSetChecked() {
		// int selectedIndex =
		// transactionTable.getSelectionModel().getSelectedIndex();
		// if (selectedIndex >= 0) {
		//
		// // Set transaction type checked
		// Transaction t = transactionTable.getItems().get(selectedIndex);
		// t.setStatusProperty(TransactionStatus.ACCEPTED_ONCE);
		//
		// transactionTable.getItems().remove(selectedIndex);
		//
		// } else {
		// // Nothing selected.
		// Alert alert = new Alert(AlertType.WARNING);
		// alert.initOwner(mainApp.getPrimaryStage());
		// alert.setTitle("No Selection");
		// alert.setHeaderText("No Transaction Selected");
		// alert.setContentText("Please select a transaction in the table.");
		//
		// alert.showAndWait();
		// }
	}

	// @FXML
	private void handleCategoryChanged() {
		Transaction t = model.currentTransactionProperty().get();
//		if (t.categoryIdProperty().getValue() != box_category.getValue()) {
//			t.categoryIdProperty().setValue(box_category.getValue());
			model.updateTransaction(t);
//		}
	}

	// @FXML
	private void handleNoteChanged() {
		Transaction t = model.currentTransactionProperty().get();
		t.noteProperty().setValue(txt_note.getText());
		model.updateTransaction(t);
	}
	
	//############################################################
//	private void createComboBox() {
//		comboBox = new ComboBox<>(typData);
//		comboBoxConverter(comboBox);
//		comboBox.valueProperty().set(getTyp());
//		comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
//	}
//	
//	private void comboBoxConverter(ComboBox<Category> comboBox) {
//		// Define rendering of the list of values in ComboBox drop down.
//		comboBox.setCellFactory((c) -> {
//			return new ListCell<Category>() {
//				@Override
//				protected void updateItem(Category item, boolean empty) {
//					super.updateItem(item, empty);
//
//					if (item == null || empty) {
//						setText(null);
//					} else {
//						setText(item.getCategory());
//					}
//				}
//			};
//		});
//	}
//
//	private Category getCategory() {
//		return getItem() == null ? new Category("") : getItem();
//	}

}
