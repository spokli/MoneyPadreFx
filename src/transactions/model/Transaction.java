package transactions.model;

import database.SQLiteJDBC;

import java.time.LocalDate;

import com.sun.javafx.sg.prism.NGShape.Mode;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transaction {

	public enum TransactionType {
		FOLGELASTSCHRIFT, KARTENZAHLUNG, AUSZAHLUNG, SEPAELVLASTSCHRIFT, EIGENEKREDITKARTENABRECHN, ABSCHLUSS, EINMALLASTSCHRIFT, LOHNGEHALT, DAUERAUFTRAG, ONLINEUEBERWEISUNG, GUTSCHRIFT, ERSTLASTSCHRIFT
	}

	public enum Currency {
		EUR, DOL, YEN, PND
	}

	public enum TransactionStatus {
		NEW, ACCEPTED_ONCE, ACCEPTED_WHITELIST
	}

	private Long id;
	private StringProperty transactionAccountProperty;
	private ObjectProperty<LocalDate> transactionDateProperty;
	private ObjectProperty<LocalDate> valueDateProperty;
	private SimpleObjectProperty<TransactionType> transactionTypeProperty;
	private StringProperty usageTextProperty;
	private StringProperty creditorIDProperty;
	private StringProperty mandateReferenceProperty;
	private StringProperty customerReferenceProperty;
	private StringProperty collectorReferenceProperty;
	private DoubleProperty debitSourceValueProperty;
	private DoubleProperty chargebackFeeProperty;
	private StringProperty recipient_payerProperty;
	private StringProperty ibanProperty;
	private StringProperty bicProperty;
	private DoubleProperty valueProperty;
	private SimpleObjectProperty<Currency> currencyProperty;
	private StringProperty infoProperty;
	private SimpleObjectProperty<TransactionStatus> statusProperty;
	private SimpleObjectProperty<Category> categoryProperty;
	// private SimpleObjectProperty<Integer> categoryIdProperty;
	private StringProperty noteProperty;
	private LongProperty parentIdProperty;
	

	public Transaction(Long id, String transactionAccount, LocalDate transactionDate, LocalDate valueDate,
			TransactionType transactionType, String usageText, String creditorID, String mandateReference,
			String customerReference, String collectorReference, double debitSourceValue, double chargebackFee,
			String recipient_payer, String iban, String bic, double value, Currency currency, String info,
			TransactionStatus status, int categoryId, String note, Long parentId) {

		this.id = id;
		this.transactionAccountProperty = new SimpleStringProperty(transactionAccount);
		this.transactionDateProperty = new SimpleObjectProperty<LocalDate>(transactionDate);
		this.valueDateProperty = new SimpleObjectProperty<LocalDate>(valueDate);
		this.transactionTypeProperty = new SimpleObjectProperty<TransactionType>(transactionType);
		this.usageTextProperty = new SimpleStringProperty(usageText);
		this.creditorIDProperty = new SimpleStringProperty(creditorID);
		this.mandateReferenceProperty = new SimpleStringProperty(mandateReference);
		this.customerReferenceProperty = new SimpleStringProperty(customerReference);
		this.collectorReferenceProperty = new SimpleStringProperty(collectorReference);
		this.debitSourceValueProperty = new SimpleDoubleProperty(debitSourceValue);
		this.chargebackFeeProperty = new SimpleDoubleProperty(chargebackFee);
		this.recipient_payerProperty = new SimpleStringProperty(recipient_payer);
		this.ibanProperty = new SimpleStringProperty(iban);
		this.bicProperty = new SimpleStringProperty(bic);
		this.valueProperty = new SimpleDoubleProperty(value);
		this.currencyProperty = new SimpleObjectProperty<Currency>(currency);
		this.infoProperty = new SimpleStringProperty(info);
		// this.categoryIdProperty = new
		// SimpleObjectProperty<Integer>(categoryId);
		this.categoryProperty = new SimpleObjectProperty<Category>(new Category(categoryId, "", false)); // Create
																											// dummy
																											// category
		this.noteProperty = new SimpleStringProperty(note);
		this.parentIdProperty = new SimpleLongProperty(parentId);

		if (status == null) {
			this.statusProperty = new SimpleObjectProperty<TransactionStatus>(TransactionStatus.NEW);
		} else {
			this.statusProperty = new SimpleObjectProperty<TransactionStatus>(status);
		}

		// this.categoryProperty.addListener((obs, oldCategory, newCategory) ->
		// {
		// if (newCategory == null) {
		// this.categoryIdProperty.set(-1);
		// } else {
		// this.categoryIdProperty.set(newCategory.categoryIdProperty().get());
		// }
		// });

		// this.categoryIdProperty.addListener((obs, oldCategoryId,
		// newCategoryId) -> {
		// this.categoryProperty.set(model.getCategoryById(newCategoryId.intValue()));
		// });
	}

	public void checkForWhitelist() {
		// suche in Whitelist nach Transaction
		boolean foundInWhitelist = SQLiteJDBC.selectInWhitelist(this);

		// Wenn gefunden, setze Status ACCEPTED_WHITELIST
		if (foundInWhitelist) {
			this.statusProperty.set(TransactionStatus.ACCEPTED_WHITELIST);
		}
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StringProperty transactionAccountProperty() {
		return transactionAccountProperty;
	}

	public ObjectProperty<LocalDate> transactionDateProperty() {
		return transactionDateProperty;
	}

	public ObjectProperty<LocalDate> valueDateProperty() {
		return valueDateProperty;
	}

	public SimpleObjectProperty<TransactionType> transactionTypeProperty() {
		return transactionTypeProperty;
	}

	public StringProperty usageTextProperty() {
		return usageTextProperty;
	}

	public StringProperty creditorIDProperty() {
		return creditorIDProperty;
	}

	public StringProperty mandateReferenceProperty() {
		return mandateReferenceProperty;
	}

	public StringProperty customerReferenceProperty() {
		return customerReferenceProperty;
	}

	public StringProperty collectorReferenceProperty() {
		return collectorReferenceProperty;
	}

	public DoubleProperty debitSourceValueProperty() {
		return debitSourceValueProperty;
	}

	public DoubleProperty chargebackFeeProperty() {
		return chargebackFeeProperty;
	}

	public StringProperty recipient_payerProperty() {
		return recipient_payerProperty;
	}

	public StringProperty ibanProperty() {
		return ibanProperty;
	}

	public StringProperty bicProperty() {
		return bicProperty;
	}

	public DoubleProperty valueProperty() {
		return valueProperty;
	}

	public SimpleObjectProperty<Currency> currencyProperty() {
		return currencyProperty;
	}

	public StringProperty infoProperty() {
		return infoProperty;
	}

	public SimpleObjectProperty<TransactionStatus> statusProperty() {
		return statusProperty;
	}

	public SimpleObjectProperty<Category> categoryProperty() {
		return categoryProperty;
	}

	// public SimpleObjectProperty<Integer> categoryIdProperty(){
	// return categoryIdProperty;
	// }

	public StringProperty noteProperty() {
		return noteProperty;
	}

	public LongProperty parentIdProperty() {
		return parentIdProperty;
	}

	//@formatter:off
	public String toString(){
		return transactionAccountProperty.get()
				+ ", " + transactionDateProperty.get().toString()
				+ ", " + valueDateProperty.get().toString()
				+ ", " + transactionTypeProperty.get().name()
				+ ", " + usageTextProperty.get()
				+ ", " + creditorIDProperty.get()
				+ ", " + mandateReferenceProperty.get()
				+ ", " + customerReferenceProperty.get()
				+ ", " + collectorReferenceProperty.get()
				+ ", " + debitSourceValueProperty.get()
				+ ", " + chargebackFeeProperty.get()
				+ ", " + recipient_payerProperty.get()
				+ ", " + ibanProperty.get()
				+ ", " + bicProperty.get()
				+ ", " + valueProperty.get()
				+ ", " + currencyProperty.get()
				+ ", " + infoProperty.get()
				+ ", " + statusProperty.get().name()
//				+ ", " + categoryIdProperty.get();
				+ ", " + categoryProperty.get().categoryNameProperty().get();
	}
	//@formatter:on

	public Transaction copy() {
		Transaction n = new Transaction(null, this.transactionAccountProperty.get(), this.transactionDateProperty.get(),
				this.valueDateProperty.get(), this.transactionTypeProperty.get(), this.usageTextProperty.get(),
				this.creditorIDProperty.get(), this.mandateReferenceProperty.get(),
				this.customerReferenceProperty.get(), this.collectorReferenceProperty.get(),
				this.debitSourceValueProperty.get(), this.chargebackFeeProperty.get(),
				this.recipient_payerProperty.get(), this.ibanProperty.get(), this.bicProperty.get(),
				this.valueProperty.get(), this.currencyProperty.get(), this.infoProperty.get(),
				this.statusProperty.get(), this.categoryProperty.get().categoryIdProperty().get(),
				this.noteProperty.get(), this.parentIdProperty.get());
		
		// Category is not part of constructor, so set it manually
		n.categoryProperty.set(this.categoryProperty.get());
		
		return n;
	}

}
