package transactions.view;

import database.SQLiteJDBC;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transactions.MainApp;
import transactions.model.Transaction;
import transactions.model.WhitelistItem;

public class AddWhitelistController extends GeneralController {

	private Transaction transaction;

	@FXML
	private CheckBox chb_transactionType;
	@FXML
	private CheckBox chb_usageText;
	@FXML
	private CheckBox chb_creditorID;
	@FXML
	private CheckBox chb_mandateReference;
	@FXML
	private CheckBox chb_customerReference;
	@FXML
	private CheckBox chb_recipient_payer;
	@FXML
	private CheckBox chb_iban;
	@FXML
	private CheckBox chb_bic;
	@FXML
	private CheckBox chb_valueFrom;
	@FXML
	private CheckBox chb_valueTo;
	@FXML
	private CheckBox chb_currency;

	@FXML
	private TextField txt_transactionType;
	@FXML
	private TextField txt_usageText;
	@FXML
	private TextField txt_creditorID;
	@FXML
	private TextField txt_mandateReference;
	@FXML
	private TextField txt_customerReference;
	@FXML
	private TextField txt_recipient_payer;
	@FXML
	private TextField txt_iban;
	@FXML
	private TextField txt_bic;
	@FXML
	private TextField txt_valueFrom;
	@FXML
	private TextField txt_valueTo;
	@FXML
	private TextField txt_currency;

	@FXML
	private Button btn_cancel;
	@FXML
	private Button btn_add;


	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public AddWhitelistController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}

	public void setTransaction(Transaction t) {
		transaction = t;

		// Fill textAreas by selected transaction
		txt_transactionType.setText(transaction.transactionTypeProperty().get().name());
		txt_usageText.setText(transaction.usageTextProperty().get());
		txt_creditorID.setText(transaction.creditorIDProperty().get());
		txt_mandateReference.setText(transaction.mandateReferenceProperty().get());
		txt_customerReference.setText(transaction.customerReferenceProperty().get());
		txt_recipient_payer.setText(transaction.recipient_payerProperty().get());
		txt_iban.setText(transaction.ibanProperty().get());
		txt_bic.setText(transaction.bicProperty().get());
		txt_currency.setText(transaction.currencyProperty().get().name());

		if (!txt_transactionType.getText().equals(""))
			chb_transactionType.setSelected(true);
		if (!txt_usageText.getText().equals(""))
			chb_usageText.setSelected(true);
		if (!txt_creditorID.getText().equals(""))
			chb_creditorID.setSelected(true);
		if (!txt_mandateReference.getText().equals(""))
			chb_mandateReference.setSelected(true);
		if (!txt_customerReference.getText().equals(""))
			chb_customerReference.setSelected(true);
		if (!txt_recipient_payer.getText().equals(""))
			chb_recipient_payer.setSelected(true);
		if (!txt_iban.getText().equals(""))
			chb_iban.setSelected(true);
		if (!txt_bic.getText().equals(""))
			chb_bic.setSelected(true);
		if (!txt_currency.getText().equals(""))
			chb_currency.setSelected(true);
	}

	@FXML
	private void handleCheckboxSelection() {
		if (!chb_transactionType.isSelected()) {
			txt_transactionType.setText("");
			txt_transactionType.setDisable(true);
		} else {
			txt_transactionType.setDisable(false);
		}
		if (!chb_usageText.isSelected()) {
			txt_usageText.setText("");
			txt_usageText.setDisable(true);
		} else {
			txt_usageText.setDisable(false);
		}
		if (!chb_creditorID.isSelected()) {
			txt_creditorID.setText("");
			txt_creditorID.setDisable(true);
		} else {
			txt_creditorID.setDisable(false);
		}
		if (!chb_mandateReference.isSelected()) {
			txt_mandateReference.setText("");
			txt_mandateReference.setDisable(true);
		} else {
			txt_mandateReference.setDisable(false);
		}
		if (!chb_customerReference.isSelected()) {
			txt_customerReference.setText("");
			txt_customerReference.setDisable(true);
		} else {
			txt_customerReference.setDisable(false);
		}
		if (!chb_recipient_payer.isSelected()) {
			txt_recipient_payer.setText("");
			txt_recipient_payer.setDisable(true);
		} else {
			txt_recipient_payer.setDisable(false);
		}
		if (!chb_iban.isSelected()) {
			txt_iban.setText("");
			txt_iban.setDisable(true);
		} else {
			txt_iban.setDisable(false);
		}
		if (!chb_bic.isSelected()) {
			txt_bic.setText("");
			txt_bic.setDisable(true);
		} else {
			txt_bic.setDisable(false);
		}
		if (!chb_valueFrom.isSelected()) {
			txt_valueFrom.setText("");
			txt_valueFrom.setDisable(true);
		} else {
			txt_valueFrom.setDisable(false);
		}
		if (!chb_valueTo.isSelected()) {
			txt_valueTo.setText("");
			txt_valueTo.setDisable(true);
		} else {
			txt_valueTo.setDisable(false);
		}
		if (!chb_currency.isSelected()) {
			txt_currency.setText("");
			txt_currency.setDisable(true);
		} else {
			txt_currency.setDisable(false);
		}

	}

	@FXML
	private void handleAddButton() {
		WhitelistItem i = new WhitelistItem();

		if (chb_transactionType.isSelected())
			i.setTransactionType(Transaction.TransactionType.valueOf(txt_transactionType.getText()));
		if (chb_usageText.isSelected())
			i.setUsageText(txt_usageText.getText());
		if (chb_creditorID.isSelected())
			i.setCreditorID(txt_creditorID.getText());
		if (chb_mandateReference.isSelected())
			i.setMandateReference(txt_mandateReference.getText());
		if (chb_customerReference.isSelected())
			i.setCustomerReference(txt_customerReference.getText());
		if (chb_recipient_payer.isSelected())
			i.setRecipient_payer(txt_recipient_payer.getText());
		if (chb_iban.isSelected())
			i.setIban(txt_iban.getText());
		if (chb_bic.isSelected())
			i.setBic(txt_bic.getText());
		if (chb_valueFrom.isSelected()) {
			i.setValueFrom(Double.parseDouble(txt_valueFrom.getText()));
		} else {
			i.setValueFrom(Double.MIN_VALUE);
		}
		if (chb_valueTo.isSelected()) {
			i.setValueTo(Double.parseDouble(txt_valueTo.getText()));
		} else {
			i.setValueTo(Double.MAX_VALUE);
		}
		if (chb_currency.isSelected())
			i.setCurrency(Transaction.Currency.valueOf(txt_currency.getText()));

		// Add whitelist item to database
		SQLiteJDBC.insertWhitelistItem(i);
//		mainApp.applyWhitelist();
	}
}
