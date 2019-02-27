package transactions.model.converter;

import javafx.util.StringConverter;
import transactions.model.Transaction.Currency;

public class CurrencyConverter extends StringConverter<Currency> {

	@Override
	public Currency fromString(String string) {
		try {
			return Currency.valueOf(string);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public String toString(Currency object) {
		return object.name();
	}
}
