package functionalities.sequence_editor.main_tools.sequence_conversion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequenceToHtmlConverter {

    public String convert(String[] sequence, String fontSize) {
        StringBuilder output = new StringBuilder();
        Pattern pattern = Pattern.compile("^[atgc]+ (base pairs\n)|(\\d+ to \\d+\n\n)|(\\d+ to \\d+)$");
        Pattern pattern2 = Pattern.compile("^\\s*$");
        output.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("<title>Sequence</title>\n")
                .append("<style>\n")
                .append("body {\n")
                .append("font-family: 'Courier New';\n")
                .append("font-size: ").append(fontSize).append("pt;\n")
                .append("}\n")
                .append("</style>\n")
                .append("</head>\n")
                .append("<body>\n");
        int startIndex = 0;
        Matcher matcher = pattern2.matcher(sequence[startIndex]);
        while (matcher.find()) {
            startIndex++;
            matcher = pattern2.matcher(sequence[startIndex]);
        }
        matcher = pattern.matcher(sequence[startIndex]);
        boolean isHeader = !matcher.find();
        boolean isFirst = true;
        if (isHeader) {
            output.append("<p id=\"header\">");
        }
        for (int i=startIndex; i<sequence.length; i+=2) {
            while (isHeader) {
                if (isFirst) {
                    output.append(sequence[i].replaceAll("\n", ""));
                    isFirst = false;
                } else {
                    output.append("<br />").append(sequence[i].replaceAll("\n", ""));
                }
                i++;
                matcher = pattern.matcher(sequence[i]);
                isHeader = !matcher.find();
                if (!isHeader) {
                    output.append("</p>\n");
                }
            }
            output.append("<p id=\"sequence\">").append(sequence[i].replaceAll("\n", ""))
                    .append("<br />")
                    .append(sequence[i+1].replaceAll("\n", ""))
                    .append("</p>\n");
        }
        output.append("</body>\n").append("</html>");

        return output.toString();
    }
}
