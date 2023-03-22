package functionalities.sequence_editor.main_functions;

import functionalities.sequence_editor.main_tools.sequence_conversion.LineLengthHtmlConverter;

public class ChangeLineLength {

    public static String run(String htmlText, int newLineLength) throws Exception {

        LineLengthHtmlConverter converter = new LineLengthHtmlConverter();
        return converter.changeLineLength(htmlText, newLineLength);
    }
}
