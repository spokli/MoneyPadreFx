package transactions.model;

import java.util.Date;

import transactions.model.Transaction.Currency;
import transactions.model.Transaction.TransactionType;

public class WhitelistItem {
	private TransactionType transactionType;
	private String usageText;
	private String creditorID;
	private String mandateReference;
	private String customerReference;
	private String recipient_payer;
	private String iban;
	private String bic;
	private double valueFrom;
	private double valueTo;
	private Currency currency;

	public WhitelistItem() {

	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public String getUsageText() {
		return usageText;
	}

	public void setUsageText(String usageText) {
		this.usageText = usageText;
	}

	public String getCreditorID() {
		return creditorID;
	}

	public void setCreditorID(String creditorID) {
		this.creditorID = creditorID;
	}

	public String getMandateReference() {
		return mandateReference;
	}

	public void setMandateReference(String mandateReference) {
		this.mandateReference = mandateReference;
	}

	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public String getRecipient_payer() {
		return recipient_payer;
	}

	public void setRecipient_payer(String recipient_payer) {
		this.recipient_payer = recipient_payer;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public double getValueFrom() {
		return valueFrom;
	}

	public void setValueFrom(double valueFrom) {
		this.valueFrom = valueFrom;
	}

	public double getValueTo() {
		return valueTo;
	}

	public void setValueTo(double valueTo) {
		this.valueTo = valueTo;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public String toString(){
		return transactionType.name()
				+ ", " + usageText
				+ ", " + creditorID
				+ ", " + mandateReference
				+ ", " + customerReference
				+ ", " + recipient_payer
				+ ", " + iban
				+ ", " + bic
				+ ", " + valueFrom
				+ ", " + valueTo
				+ ", " + currency.name()
				;
	}

}
