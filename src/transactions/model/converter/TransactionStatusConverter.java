package transactions.model.converter;

import javafx.util.StringConverter;
import transactions.model.Transaction.TransactionStatus;

public class TransactionStatusConverter extends StringConverter<TransactionStatus> {

	@Override
	public TransactionStatus fromString(String string) {
		try {
			return TransactionStatus.valueOf(string);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public String toString(TransactionStatus object) {
		return object.name();
	}

}
