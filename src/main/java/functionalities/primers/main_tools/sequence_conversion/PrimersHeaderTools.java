package functionalities.primers.main_tools.sequence_conversion;

import functionalities.utils.HeaderTools;

public class PrimersHeaderTools extends HeaderTools {

	public static String[][] removeHeaders(String[] rawSequence) {
		return HeaderTools.removeHeaders(rawSequence);
	}
}