package transactions.model.converter;

import javafx.util.StringConverter;
import transactions.model.Transaction.TransactionType;

public class TransactionTypeConverter extends StringConverter<TransactionType> {

	@Override
	public TransactionType fromString(String string) {
		try {
			return TransactionType.valueOf(string);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public String toString(TransactionType object) {
		return object.name();
	}
}
