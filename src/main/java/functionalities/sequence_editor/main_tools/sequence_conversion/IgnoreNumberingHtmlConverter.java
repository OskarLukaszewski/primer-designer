package functionalities.sequence_editor.main_tools.sequence_conversion;

import functionalities.utils.HtmlConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IgnoreNumberingHtmlConverter extends HtmlConverter {

    public String ignoreChangesOutsideOfSequence(String htmlText) {

        StringBuilder htmlBuilder = new StringBuilder();

        String header = htmlText.substring(0, htmlText.indexOf("<body"));
        htmlBuilder.append(header).append("<body>\n");

        String body = htmlText.substring(htmlText.indexOf("<p"), htmlText.indexOf("</body>")).strip();
        String[] paragraphs = body.replaceAll("\n", "").split("</p>");

        Pattern fullLinePattern = Pattern.compile("((?:(?:</?span.*?>)*[atcg]*(?:</?span.*?>)*)*)(.*)");
        Pattern noSpanPattern = Pattern.compile("^[atcg]+ .*");
        Matcher matcher;
        Matcher noSpanMatcher;

        for (String paragraph : paragraphs) {
            if (!paragraph.contains("<p id=\"sequence\">")) {
                htmlBuilder.append(paragraph).append("</p>\n");
                continue;
            }
            String tailingSpansFromLastLine = "";
            if (paragraph.lastIndexOf("<br />") == paragraph.length()-6) {
                paragraph += "<pseudo_character>";
            }
            String[] lines = paragraph.split("<br />");
            if (lines.length > 0) {
                htmlBuilder.append("<p id=\"sequence\">");
                for (String line : lines) {
                    line = line.replaceAll("<p id=\"sequence\">", "");
                    noSpanMatcher = noSpanPattern.matcher(line.replaceAll("</?span.*?>", ""));
                    if (!noSpanMatcher.find()) {
                        String[] processedLine = clearLine(tailingSpansFromLastLine, line, "");
                        htmlBuilder.append(processedLine[0]).append("<br />");
                        tailingSpansFromLastLine = processedLine[2];
                        continue;
                    }
                    matcher = fullLinePattern.matcher(line);
                    if (matcher.find() && !matcher.group(1).equals("") && !matcher.group(2).equals("")) {
                        String[] processedLine = clearLine(tailingSpansFromLastLine, matcher.group(1), matcher.group(2));
                        htmlBuilder.append(processedLine[0]).append(processedLine[1]).append("<br />");
                        tailingSpansFromLastLine = processedLine[2];
                    } else {
                        String[] processedLine = clearLine(tailingSpansFromLastLine, line, "");
                        htmlBuilder.append(processedLine[0]).append("<br />");
                        tailingSpansFromLastLine = processedLine[2];
                    }
                }
                int indexOfLastLineBreak = htmlBuilder.lastIndexOf("<br />");
                htmlBuilder.replace(indexOfLastLineBreak, indexOfLastLineBreak+6, "</p>\n");
            }
        }

        htmlBuilder.append("</body>\n").append("</html>");

        return htmlBuilder.toString().replace("<pseudo_character>", "");
    }
}
