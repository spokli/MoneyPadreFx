package transactions.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import database.SQLiteJDBC;
import exceptions.UnknownCurrencyException;
import exceptions.UnknownTransactionTypeException;
import transactions.model.Transaction.Currency;
import transactions.model.Transaction.TransactionStatus;
import transactions.model.Transaction.TransactionType;

public class TransactionFile {

	private static final String SEPARATOR = ";";

	public ArrayList<Transaction> transactions;

	private final String filepath;
	private final DataModel model;

	// Transaction data
	private String transactionAccount;
	private LocalDate transactionDate;
	private LocalDate valueDate;
	private TransactionType transactionType;
	private String usageText;
	private String creditorID;
	private String mandateReference;
	private String customerReference;
	private String collectorReference;
	private double debitSourceValue;
	private double chargebackFee;
	private String recipient_payer;
	private String iban;
	private String bic;
	private double value;
	private Currency currency;
	private String info;

	public ArrayList<String> uploadErrorMessages = new ArrayList<String>();

	public TransactionFile(String path, DataModel model) {
		// Data model is needed for default category information
		this.filepath = path;
		this.model = model;
	}

	public void upload() throws UnknownTransactionTypeException, UnknownCurrencyException {

		String csvFile = filepath;
		BufferedReader br = null;
		String line = "";
		boolean firstLine = true;
		this.transactions = new ArrayList<Transaction>();

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				line = line.replaceAll("\"", "");

				// Werfe Indexzeile weg
				if (firstLine) {
					firstLine = false;
					continue;
				}

				String[] lineFields = line.split(SEPARATOR);

				// Create Transactions
				this.transactionAccount = lineFields[0];
				this.transactionDate = getDateFromString(lineFields[1]);
				this.valueDate = getDateFromString(lineFields[2]);
				this.transactionType = getTransactionType(lineFields[3]);
				this.usageText = lineFields[4];
				this.creditorID = lineFields[5];
				this.mandateReference = lineFields[6];
				this.customerReference = lineFields[7];
				this.collectorReference = lineFields[8];
				this.debitSourceValue = getDoubleValue(lineFields[9]);
				this.chargebackFee = getDoubleValue(lineFields[10]);
				this.recipient_payer = lineFields[11];
				this.iban = lineFields[12];
				this.bic = lineFields[13];
				this.value = getDoubleValue(lineFields[14]);
				this.currency = getCurrency(lineFields[15]);
				this.info = lineFields[16];
				
				int categoryId = model.getDefaultCategory().categoryIdProperty().get();

				// Create transaction instance
				Transaction t = new Transaction(null, this.transactionAccount, this.transactionDate, this.valueDate,
						this.transactionType, this.usageText, this.creditorID, this.mandateReference,
						this.customerReference, this.collectorReference, this.debitSourceValue, this.chargebackFee,
						this.recipient_payer, this.iban, this.bic, this.value, this.currency, this.info,
						TransactionStatus.NEW, categoryId, "", null);

				t.checkForWhitelist();

				// Insert transaction to database
				String errorMessage = SQLiteJDBC.insertTransaction(t);
				if (!errorMessage.equals("")) {
					this.uploadErrorMessages.add(errorMessage);
				}

				this.transactions.add(t);
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			this.uploadErrorMessages.add(e.getClass().getName() + ": " + e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			this.uploadErrorMessages.add(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
					this.uploadErrorMessages.add(e.getClass().getName() + ": " + e.getMessage());
				}
			}
		}
	}

	private static LocalDate getDateFromString(String s) {
		int year = Integer.parseInt(s.substring(6, 8)) + 2000;
		int month = Integer.parseInt(s.substring(3, 5));
		int day = Integer.parseInt(s.substring(0, 2));

		return LocalDate.of(year, month, day);
	}

	private static TransactionType getTransactionType(String s) throws UnknownTransactionTypeException {
		switch (s) {
		case "FOLGELASTSCHRIFT":
			return TransactionType.FOLGELASTSCHRIFT;
		case "KARTENZAHLUNG":
			return TransactionType.KARTENZAHLUNG;
		case "AUSZAHLUNG":
			return TransactionType.AUSZAHLUNG;
		case "ABSCHLUSS":
			return TransactionType.ABSCHLUSS;
		case "SEPA-ELV-LASTSCHRIFT":
			return TransactionType.SEPAELVLASTSCHRIFT;
		case "EIGENE KREDITKARTENABRECHN.":
			return TransactionType.EIGENEKREDITKARTENABRECHN;
		case "ONLINE-UEBERWEISUNG":
			return TransactionType.ONLINEUEBERWEISUNG;
		case "LOHN  GEHALT":
			return TransactionType.LOHNGEHALT;
		case "EINMAL LASTSCHRIFT":
			return TransactionType.EINMALLASTSCHRIFT;
		case "DAUERAUFTRAG":
			return TransactionType.DAUERAUFTRAG;
		case "GUTSCHRIFT":
			return TransactionType.GUTSCHRIFT;
		case "ERSTLASTSCHRIFT":
			return TransactionType.ERSTLASTSCHRIFT;
		default:
			throw new UnknownTransactionTypeException(s);
		}

	}

	private static Currency getCurrency(String s) throws UnknownCurrencyException {
		switch (s) {
		case "EUR":
			return Currency.EUR;
		default:
			throw new UnknownCurrencyException();
		}
	}

	private static double getDoubleValue(String s) {
		if (s.equals("")) {
			return 0;
		} else {
			try {
				s = s.replaceAll(",", ".");

				return Double.parseDouble(s);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

}
