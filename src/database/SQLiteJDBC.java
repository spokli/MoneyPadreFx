package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import transactions.model.Category;
import transactions.model.Transaction;
import transactions.model.Transaction.Currency;
import transactions.model.Transaction.TransactionStatus;
import transactions.model.Transaction.TransactionType;
import transactions.model.WhitelistItem;

public class SQLiteJDBC {

	// public static void main(String[] args){
	// createTransactionTable();
	// createWhitelistTable();
	// }

	private static final boolean DEBUG_OUTPUT = true;

	// @formatter:off
	private static final String TABLE_TRANSACTIONS = "TRANSACTIONS", 
								TABLE_WHITELIST = "WHITELIST",
								TABLE_CATEGORY = "CATEGORY",
								COL_TRANSACTION_ACCOUNT = "TRANSACTION_ACCOUNT", 
								COL_TRANSACTION_DATE = "TRANSACTION_DATE",
								COL_VALUE_DATE = "VALUE_DATE", 
								COL_TRANSACTION_TYPE = "TRANSACTION_TYPE", 
								COL_USAGE_TEXT = "USAGE_TEXT",
								COL_CREDITOR_ID = "CREDITOR_ID", 
								COL_MANDATE_REFERENCE = "MANDATE_REFERENCE",
								COL_CUSTOMER_REFERENCE = "CUSTOMER_REFERENCE", 
								COL_COLLECTOR_REFERENCE = "COLLECTOR_REFERENCE",
								COL_DEBIT_SOURCE_VALUE = "DEBIT_SOURCE_VALUE", 
								COL_CHARGEBACK_FEE = "CHARGEBACK_FEE",
								COL_RECIPIENT_PAYER = "RECIPIENT_PAYER", 
								COL_IBAN = "IBAN", 
								COL_BIC = "BIC", 
								COL_VALUE = "VALUE",
								COL_CURRENCY = "CURRENCY", 
								COL_INFO = "INFO", 
								COL_STATUS = "STATUS",
								COL_ORDER = "SEQUENCE",
								COL_VALUE_MIN = "VALUE_MIN",
								COL_VALUE_MAX = "VALUE_MAX",
								COL_CATEGORY_ID = "CATEGORY_ID",
								COL_CATEGORY_NAME = "NAME",
								COL_CATEGORY_DEFAULT = "IS_DEFAULT",
								COL_NOTE = "NOTE",
								COL_TRANSACTION_ID = "ID",
								COL_PARENT_ID = "PARENT_ID";
	// @formatter:on

	private static Connection connection;

	private static Connection getConnection() {

		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			} else {
				Connection c = null;
				try {
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:test.db");
				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
					System.exit(0);
				}
				connection = c;
				return connection;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	private static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static String insertTransaction(Transaction t) {
		Connection c = getConnection();
		String errorMessage = "";
		// Statement stmt;
		try {
			c.setAutoCommit(false);
			// stmt = c.createStatement();

			// @formatter:off
			String sql = "INSERT INTO " + TABLE_TRANSACTIONS + "(" 
					+ COL_TRANSACTION_ACCOUNT + ","
					+ COL_TRANSACTION_DATE + "," 
					+ COL_VALUE_DATE + "," 
					+ COL_TRANSACTION_TYPE + "," 
					+ COL_USAGE_TEXT + "," 
					+ COL_CREDITOR_ID + "," 
					+ COL_MANDATE_REFERENCE + "," 
					+ COL_CUSTOMER_REFERENCE + ","
					+ COL_COLLECTOR_REFERENCE + "," 
					+ COL_DEBIT_SOURCE_VALUE + "," 
					+ COL_CHARGEBACK_FEE + ","
					+ COL_RECIPIENT_PAYER + "," 
					+ COL_IBAN + "," 
					+ COL_BIC + "," 
					+ COL_VALUE + "," 
					+ COL_CURRENCY + ","
					+ COL_INFO + "," 
					+ COL_STATUS + ","
					+ COL_CATEGORY_ID
					+ ") VALUES ("
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" + ");";
			// @formatter:on

			PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, t.transactionAccountProperty().get());
			stmt.setString(2, t.transactionDateProperty().get().toString());
			stmt.setString(3, t.valueDateProperty().get().toString());
			stmt.setString(4, t.transactionTypeProperty().get().name());
			stmt.setString(5, t.usageTextProperty().get());
			stmt.setString(6, t.creditorIDProperty().get());
			stmt.setString(7, t.mandateReferenceProperty().get());
			stmt.setString(8, t.customerReferenceProperty().get());
			stmt.setString(9, t.collectorReferenceProperty().get());
			stmt.setDouble(10, t.debitSourceValueProperty().get());
			stmt.setDouble(11, t.chargebackFeeProperty().get());
			stmt.setString(12, t.recipient_payerProperty().get());
			stmt.setString(13, t.ibanProperty().get());
			stmt.setString(14, t.bicProperty().get());
			stmt.setDouble(15, t.valueProperty().get());
			stmt.setString(16, t.currencyProperty().get().name());
			stmt.setString(17, t.infoProperty().get());
			stmt.setString(18, t.statusProperty().get().name());
			
			int categoryId;
			if (t.categoryProperty() == null) {
				categoryId = -1;
			} else {
				categoryId = t.categoryProperty().get().categoryIdProperty().get();
			}
			stmt.setInt(19, categoryId);

			int affectedRows = stmt.executeUpdate();

			// Get inserted ID
	        if (affectedRows == 0) {
	            errorMessage = "Creating Transaction failed, no rows affected.";
	        }
	        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                t.setId(generatedKeys.getLong(1));
	            }
	            else {
	                errorMessage = "Creating Transaction failed, no ID obtained.";
	            }
	        }
			
			stmt.close();
			c.commit();
			c.setAutoCommit(true);
			closeConnection();
			
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
				errorMessage = "Eintrag existiert bereits: " + t.transactionDateProperty().get().toString() + " , "
						+ t.transactionTypeProperty().get().name() + " , " + t.usageTextProperty().get();
			} else {
				errorMessage = e.getClass().getName() + ": " + e.getMessage();
			}
		}
		return errorMessage;
	}

	public static String insertCategory(Category cat) {
		Connection c = getConnection();
		String errorMessage = "";
		try {
			c.setAutoCommit(false);
			// @formatter:off
			String sql = "INSERT INTO " + TABLE_CATEGORY + "(" 
//					+ COL_CATEGORY_ID + "," // Autoincremented by DB
					+ COL_CATEGORY_NAME + ","
					+ COL_CATEGORY_DEFAULT
					+ ") VALUES ("
					+ "?,?" + ");";
			// @formatter:on

			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, cat.categoryNameProperty().get());

			int defaultValue = 0;
			if (cat.categoryDefaultProperty().get()) {
				defaultValue = 1;
			}
			stmt.setInt(2, defaultValue);

			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.setAutoCommit(true);
			closeConnection();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
				errorMessage = "Kategorie existiert bereits: " + cat.categoryNameProperty().get();
				
			} else {
				errorMessage = e.getClass().getName() + ": " + e.getMessage();
			}
		}
		return errorMessage;
	}

	public static String updateCategory(Category cat) {
		Connection c = getConnection();
		String errorMessage = "";
		try {
			c.setAutoCommit(false);
			// @formatter:off
			String sql = "UPDATE " + TABLE_CATEGORY + " SET " 
//					+ COL_CATEGORY_ID + "," // Autoincremented by DB
					+ COL_CATEGORY_NAME + " = ?," 
					+ COL_CATEGORY_DEFAULT + " = ?"
					+ " WHERE "
					+ COL_CATEGORY_ID + " = ?"
					+ ";";
			// @formatter:on

			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, cat.categoryNameProperty().get());

			int categoryDefault = 0;
			if (cat.categoryDefaultProperty().get()) {
				categoryDefault = 1;
			}
			stmt.setInt(2, categoryDefault);
			stmt.setInt(3, cat.categoryIdProperty().get());

			int defaultValue = 0;
			if (cat.categoryDefaultProperty().get()) {
				defaultValue = 1;
			}
			stmt.setInt(2, defaultValue);

			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.setAutoCommit(true);
			closeConnection();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
				errorMessage = "Kategorie existiert bereits: " + cat.categoryNameProperty().get();
				;
			} else {
				errorMessage = e.getClass().getName() + ": " + e.getMessage();
			}
		}
		return errorMessage;
	}

	public static String deleteCategory(Category cat) {
		Connection c = getConnection();
		String errorMessage = "";
		// Statement stmt;
		try {
			c.setAutoCommit(false);
			// stmt = c.createStatement();

			// @formatter:off
			String sql = "DELETE FROM " + TABLE_CATEGORY + " WHERE " 
								+ COL_CATEGORY_ID 	+ " = '" + cat.categoryIdProperty().get() + "'"
//					+ " AND " 	+ COL_CATEGORY_NAME + " = '" + cat.getCategoryName() + "'"
					+ ";";
			// @formatter:on

			PreparedStatement stmt = c.prepareStatement(sql);

			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.setAutoCommit(true);
			closeConnection();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
				errorMessage = "Kategorie existiert bereits: " + cat.categoryNameProperty().get();
				;
			} else {
				errorMessage = e.getClass().getName() + ": " + e.getMessage();
			}
		}
		return errorMessage;
	}

	public static String insertWhitelistItem(WhitelistItem i) {
		Connection c = getConnection();
		String errorMessage = "";
		String sql = "";
		// Statement stmt;
		try {
			c.setAutoCommit(false);
			// stmt = c.createStatement();

			// @formatter:off
			sql = "INSERT INTO " + TABLE_WHITELIST + "(" 
					+ COL_ORDER + ","
					+ COL_TRANSACTION_TYPE + "," 
					+ COL_USAGE_TEXT + "," 
					+ COL_CREDITOR_ID + "," 
					+ COL_MANDATE_REFERENCE + "," 
					+ COL_CUSTOMER_REFERENCE + ","
					+ COL_RECIPIENT_PAYER + "," 
					+ COL_IBAN + "," 
					+ COL_BIC + "," 
					+ COL_VALUE_MIN + "," 
					+ COL_VALUE_MAX + "," 
					+ COL_CURRENCY
					+ ") VALUES ("
					+ "?,?,?,?,?,?,?,?,?,?,?,?" + ");";
			// @formatter:on

			PreparedStatement stmt = c.prepareStatement(sql);

			if (i.getTransactionType() != null) {
				stmt.setString(2, i.getTransactionType().name());
			} else {
				stmt.setNull(2, java.sql.Types.CHAR);
			}
			stmt.setString(3, i.getUsageText());
			stmt.setString(4, i.getCreditorID());
			stmt.setString(5, i.getMandateReference());
			stmt.setString(6, i.getCustomerReference());
			stmt.setString(7, i.getRecipient_payer());
			stmt.setString(8, i.getIban());
			stmt.setString(9, i.getBic());
			stmt.setDouble(10, i.getValueFrom());
			stmt.setDouble(11, i.getValueTo());
			if (i.getCurrency() != null) {
				stmt.setString(12, i.getCurrency().name());
			} else {
				stmt.setNull(12, java.sql.Types.CHAR);
			}

			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.setAutoCommit(true);
			closeConnection();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			if (DEBUG_OUTPUT) {
				System.err.println(sql);
				System.err.println(i.toString());
			}
			// if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
			// errorMessage = "Eintrag existiert bereits: " +
			// t.getTransactionDateProperty().toString() + " , "
			// + t.getTransactionTypeProperty().name() + " , " +
			// t.getUsageTextProperty();
			// } else {
			errorMessage = e.getClass().getName() + ": " + e.getMessage();
			// }
		}
		return errorMessage;
	}

	public static boolean selectInWhitelist(Transaction t) {
		boolean found;
		String sql = "";
		Connection c = getConnection();
		Statement stmt;
		try {
			stmt = c.createStatement();

			// @formatter:off
			sql = "SELECT * FROM " + TABLE_WHITELIST 
					+ " WHERE "
					+ "( " + COL_TRANSACTION_TYPE + " = NULL OR " + COL_TRANSACTION_TYPE + " = '" + t.transactionTypeProperty().get().name() + "') "
					+ "AND (" + COL_USAGE_TEXT + " = NULL OR " + COL_USAGE_TEXT + " = '" + t.usageTextProperty().get() + "') "
					+ "AND (" + COL_CREDITOR_ID + " = NULL OR " + COL_CREDITOR_ID + " = '" + t.creditorIDProperty().get() + "') "
					+ "AND (" + COL_MANDATE_REFERENCE + " = NULL OR " + COL_MANDATE_REFERENCE + " = '" + t.mandateReferenceProperty().get() + "') "
					+ "AND (" + COL_CUSTOMER_REFERENCE + " = NULL OR " + COL_CUSTOMER_REFERENCE + " = '" + t.customerReferenceProperty().get() + "') "
					+ "AND (" + COL_RECIPIENT_PAYER + " = NULL OR " + COL_RECIPIENT_PAYER + " = '" + t.recipient_payerProperty().get() + "') "
					+ "AND (" + COL_IBAN + " = NULL OR " + COL_IBAN + " = '" + t.ibanProperty().get() + "') "
					+ "AND (" + COL_BIC + " = NULL OR " + COL_BIC + " = '" + t.bicProperty().get() + "') "
					+ "AND (" + COL_VALUE_MIN + " <= '" + t.valueProperty().get() + "') "
					+ "AND (" + COL_VALUE_MAX + " > '" + t.valueProperty().get() + "') "
					+ "AND (" + COL_CURRENCY + " = NULL OR " + COL_CURRENCY + " = '" + t.currencyProperty().get().name() + "') "					
					+ ";";
			// @formatter:on
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.isClosed() || rs.wasNull()) {
				found = false;
			} else {
				found = true;
			}

			stmt.close();

		} catch (

		SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			if (DEBUG_OUTPUT) {
				System.err.println(sql);
				System.err.println(t);
			}
			return false;
		}

		closeConnection();
		return found;

	}

	public static HashMap<Integer, Category> getCategories() {
		HashMap<Integer, Category> result = new HashMap<Integer, Category>();

		Connection c = getConnection();
		Statement stmt;
		try {
			stmt = c.createStatement();

			//@formatter:off
			String sql = "SELECT " 
							+ COL_CATEGORY_ID + ", " 
							+ COL_CATEGORY_NAME + ", "
							+ COL_CATEGORY_DEFAULT
						+ " FROM " + TABLE_CATEGORY + ";";
			//@formatter:on
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Integer category_id = rs.getInt(COL_CATEGORY_ID);
				String category_name = rs.getString(COL_CATEGORY_NAME);
				boolean category_default = Integer.parseInt(rs.getString(COL_CATEGORY_DEFAULT)) == 1;

				Category cat = new Category(category_id, category_name, category_default);
				result.put(category_id, cat);
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		closeConnection();
		return result;
	}

	public static HashMap<Integer, Double> getCategoryStatistics(LocalDate from, LocalDate to) {
		HashMap<Integer, Double> result = new HashMap<Integer, Double>();

		Connection c = getConnection();
		try {

			//@formatter:off
			String sql = "SELECT "
							+ COL_CATEGORY_ID + ", "
							+ "SUM(" + COL_VALUE + ") as " + COL_VALUE
						+ " FROM " + TABLE_TRANSACTIONS
						+ " WHERE " + COL_VALUE_DATE + " BETWEEN date(?) AND date(?)"
						+ " GROUP BY " + COL_CATEGORY_ID + ";";
			//@formatter:on

			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, from.toString());
			stmt.setString(2, to.toString());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Integer category_id = rs.getInt(COL_CATEGORY_ID);
				Double value = rs.getDouble(COL_VALUE);

				result.put(category_id, value);
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		closeConnection();
		return result;
	}

	public static ArrayList<Transaction> selectTransactions(boolean onlyNew) {
		ArrayList<Transaction> result = new ArrayList<Transaction>();

		Connection c = getConnection();
		Statement stmt;
		try {
			stmt = c.createStatement();

			String sql = "SELECT * FROM " + TABLE_TRANSACTIONS;
			if (onlyNew) {
				sql += " WHERE " + COL_STATUS + " = 'NEW';";
			} else {
				sql += ";";
			}
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Long id = rs.getLong(COL_TRANSACTION_ID);
				String transactionAccount = rs.getString(COL_TRANSACTION_ACCOUNT);
				LocalDate transactionDate = getLocalDate(rs.getString(COL_TRANSACTION_DATE));
				LocalDate valueDate = getLocalDate(rs.getString(COL_VALUE_DATE));
				TransactionType transactionType = Enum.valueOf(TransactionType.class,
						rs.getString(COL_TRANSACTION_TYPE));
				String usageText = rs.getString(COL_USAGE_TEXT);
				String creditorID = rs.getString(COL_CREDITOR_ID);
				String mandateReference = rs.getString(COL_MANDATE_REFERENCE);
				String customerReference = rs.getString(COL_MANDATE_REFERENCE);
				String collectorReference = rs.getString(COL_COLLECTOR_REFERENCE);
				double debitSourceValue = rs.getDouble(COL_DEBIT_SOURCE_VALUE);
				double chargebackFee = rs.getDouble(COL_CHARGEBACK_FEE);
				String recipient_payer = rs.getString(COL_RECIPIENT_PAYER);
				String iban = rs.getString(COL_IBAN);
				String bic = rs.getString(COL_BIC);
				double value = rs.getDouble(COL_VALUE);
				Currency currency = Enum.valueOf(Currency.class, rs.getString(COL_CURRENCY));
				String info = rs.getString(COL_INFO);
				TransactionStatus status = null;
				if (rs.getString(COL_STATUS) != null) {
					status = Enum.valueOf(TransactionStatus.class, rs.getString(COL_STATUS));
				}
				int categoryId = rs.getInt(COL_CATEGORY_ID);
				String note = rs.getString(COL_NOTE);
				Long parentId = rs.getLong(COL_PARENT_ID);

				Transaction t = new Transaction(id, transactionAccount, transactionDate, valueDate, transactionType,
						usageText, creditorID, mandateReference, customerReference, collectorReference,
						debitSourceValue, chargebackFee, recipient_payer, iban, bic, value, currency, info, status,
						categoryId, note, parentId);
				result.add(t);
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		closeConnection();
		return result;
	}

	public static void updateTransaction(Transaction t) {
		Connection c = getConnection();
		Statement stmt;
		try {
			stmt = c.createStatement();
			// @formatter:off
			String sql = "UPDATE " + TABLE_TRANSACTIONS 
					+ " SET " 
						+ COL_STATUS + "='" + t.statusProperty().get().name() + "',"
						+ COL_CATEGORY_ID + "='" + t.categoryProperty().get().categoryIdProperty().get() + "',"
						+ COL_NOTE + "='" + t.noteProperty().get() + "'"
					+ "WHERE " 
						+ COL_TRANSACTION_DATE + "='" + t.transactionDateProperty().get().toString() + "'" + " AND "
						+ COL_TRANSACTION_TYPE + "='" + t.transactionTypeProperty().get().name() + "'" + " AND "
						+ COL_USAGE_TEXT + "='" + t.usageTextProperty().get() + "'" + ";";
			// @formatter:on
			stmt.executeUpdate(sql);

			stmt.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		closeConnection();
	}

	@SuppressWarnings("unused")
	private static void createTransactionTable() {
		// Zur Sicherheit
		if (true) {
			return;
		}

		Connection c = getConnection();
		Statement stmt;
		try {
			stmt = c.createStatement();
			// @formatter:off
			String sql = "" + "DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS + ";" 
							+ "CREATE TABLE " + TABLE_TRANSACTIONS
							+ "(" 
							+ COL_TRANSACTION_ACCOUNT 		+ " TEXT NOT NULL," 
							+ COL_TRANSACTION_DATE			+ "	TEXT NOT NULL," 
							+ COL_VALUE_DATE 				+ " TEXT,"
							+ COL_TRANSACTION_TYPE 			+ " TEXT	NOT NULL," 
							+ COL_USAGE_TEXT				+ "	CHAR(200)," 
							+ COL_CREDITOR_ID				+ "	TEXT," 
							+ COL_MANDATE_REFERENCE			+ "	TEXT," 
							+ COL_CUSTOMER_REFERENCE		+ "	TEXT," 
							+ COL_COLLECTOR_REFERENCE		+ "	TEXT," 
							+ COL_DEBIT_SOURCE_VALUE 		+ "	REAL,"
							+ COL_CHARGEBACK_FEE 			+ " REAL," 
							+ COL_RECIPIENT_PAYER			+ "	TEXT," 
							+ COL_IBAN						+ "	CHAR(22) NOT NULL," 
							+ COL_BIC						+ "	CHAR(11) NOT NULL," 
							+ COL_VALUE						+ "	REAL NOT NULL," 
							+ COL_CURRENCY					+ "	CHAR(3)	NOT NULL," 
							+ COL_INFO						+ "	TEXT," 
							+ COL_STATUS					+ "	TEXT," 
							+ COL_CATEGORY_ID				+ " INTEGER,"
							+ COL_NOTE						+ " TEXT,"
							+ "PRIMARY KEY (" 
								+ COL_TRANSACTION_DATE + ","
								+ COL_TRANSACTION_TYPE + "," 
								+ COL_USAGE_TEXT + ")" 
							+ ")";
			// @formatter:on
			stmt.executeUpdate(sql);
			stmt.close();
			closeConnection();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private static void createWhitelistTable() {
		// Zur Sicherheit
		if (true) {
			return;
		}

		Connection c = getConnection();
		Statement stmt;
		try {
			stmt = c.createStatement();
			// @formatter:off
			String sql = "" + "DROP TABLE IF EXISTS " + TABLE_WHITELIST + ";" 
					+ "CREATE TABLE " + TABLE_WHITELIST + "("
					+ COL_ORDER + "					INTEGER				NOT NULL," 
					+ COL_TRANSACTION_TYPE + "		TEXT				NOT NULL," 
					+ COL_USAGE_TEXT+ "				CHAR(200)					," 
					+ COL_CREDITOR_ID	+ "			TEXT						," 
					+ COL_MANDATE_REFERENCE	+ "		TEXT						," 
					+ COL_CUSTOMER_REFERENCE+ "		TEXT						," 
					+ COL_RECIPIENT_PAYER	+ "		TEXT						," 
					+ COL_IBAN	+ "					CHAR(22)			NOT NULL," 
					+ COL_BIC	+ "					CHAR(11)			NOT NULL,"
					+ COL_VALUE_MIN + "				INTEGER						,"
					+ COL_VALUE_MAX + "				INTEGER						"
					+ COL_CURRENCY + "				CHAR(3)				NOT NULL,"
					 + "PRIMARY KEY ("+COL_ORDER+")"
					+ ")";
			// @formatter:on
			stmt.executeUpdate(sql);
			stmt.close();
			closeConnection();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private static void createCategoryTable() {
		// Zur Sicherheit
		if (true) {
			return;
		}

		Connection c = getConnection();
		Statement stmt;
		try {
			stmt = c.createStatement();
			// @formatter:off
			String sql = "" + "DROP TABLE IF EXISTS " + TABLE_CATEGORY + ";" 
							+ "CREATE TABLE " + TABLE_CATEGORY + "("
								+ COL_CATEGORY_ID 		+ " INTEGER NOT NULL AUTOINCREMENT, " 
								+ COL_CATEGORY_NAME 	+ "	TEXT NOT NULL, " 
								+ COL_CATEGORY_DEFAULT	+ " INTEGER NOT NULL, "
								+ "PRIMARY KEY ("+COL_CATEGORY_ID+")"
								+ ")";
			// @formatter:on
			stmt.executeUpdate(sql);
			stmt.close();
			closeConnection();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	private static LocalDate getLocalDate(String dateString) {
		int year = Integer.parseInt(dateString.substring(0, 4));
		int month = Integer.parseInt(dateString.substring(5, 7));
		int day = Integer.parseInt(dateString.substring(8, 10));

		return LocalDate.of(year, month, day);
	}
}
