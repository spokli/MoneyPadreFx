package misc;

public class Validator {

	public static boolean validatePositiveDouble(final String value) {
		if (value == null || value.length() == 0) {
			return true;
		}

		if (countMatches(value, ".") > 1) {
			return false;
		}
		if (value.endsWith(".")) {
			return true;
		}

		try {
			Double d = Double.parseDouble(value);
			if (d < 0) {
				return false;
			} else {
				return true;
			}
	
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	// ##############################

	private static int countMatches(final String s, final String pattern) {
		String[] parts = s.split(pattern);
		return parts.length;
	}

}
