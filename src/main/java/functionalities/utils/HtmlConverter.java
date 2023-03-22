package functionalities.utils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HtmlConverter {

    public String normalizeHtmlFormat(String htmlText) {

        StringBuilder htmlBuilder = new StringBuilder();

        String header = htmlText.substring(0, htmlText.indexOf("<body"));
        htmlBuilder.append(header).append("<body>\n");

        String body = htmlText.substring(htmlText.indexOf("<p"), htmlText.indexOf("</body>"))
                .strip()
                .replaceAll("<br>", "<br />")
                .replaceAll("(<br />)+", "<br />");
        String[] paragraphs = body.replaceAll("\n", "").split("</p>");

        for (String paragraph: paragraphs) {
            htmlBuilder.append(paragraph).append("</p>\n");
        }

        htmlBuilder.append("</body>\n</html>");

        return htmlBuilder.toString();
    }

    protected boolean isFormatCorrect(String body) {

        String[] paragraphs = body.replaceAll("\n", "").strip().split("</p>");

        Pattern fullLinePattern = Pattern.compile("((?:(?:</?span.*?>)*[atcg]*(?:</?span.*?>)*)*)(.*)");
        Pattern noSpanPattern = Pattern.compile("^[atcg]+ .*");
        Matcher matcher;
        Matcher noSpanMatcher;

        for (String paragraph: paragraphs) {
            if (paragraph.contains("<p id=\"header\">")) {
                continue;
            } else if (!paragraph.contains("p id=\"sequence\">")) {
                return false;
            }
            String[] lines = paragraph.split("<br />");
            if (lines.length != 2) {
                return false;
            }
            for (String line : lines) {
                line = line.replace("<p id=\"sequence\">", "");
                noSpanMatcher = noSpanPattern.matcher(line.replaceAll("</?span.*?>", ""));
                if (!noSpanMatcher.find()) {
                    return false;
                }
                matcher = fullLinePattern.matcher(line);
                if (matcher.find()) {
                    if (matcher.group(1).equals("")) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    protected String[] clearLine(String tailingSpansFromLastLine, String currentSequencePart, String rest) {

        if (rest == null) {
            rest = "";
        }

        String sequencePart = tailingSpansFromLastLine + currentSequencePart;

        StringBuilder lineBuilder = new StringBuilder();
        StringBuilder tailingSpansBuilder = new StringBuilder();

        LinkedList<String> openSpanSequenceList = new LinkedList<>();
        LinkedList<String> openSpanRestList = new LinkedList<>();
        int spanRestCount = 0;

        Pattern spanPattern = Pattern.compile("</?span.*?>");
        Matcher matcher = spanPattern.matcher(sequencePart);

        while (matcher.find()) {
            if (matcher.group(0).equals("</span>")) {
                if (openSpanSequenceList.size() > 0) {
                    openSpanSequenceList.removeLast();
                }
            } else {
                openSpanSequenceList.add(matcher.group(0));
            }
        }

        lineBuilder.append(sequencePart);
        lineBuilder.append("</span>".repeat(openSpanSequenceList.size()));

        matcher = spanPattern.matcher(rest);
        while (matcher.find()) {
            if (matcher.group(0).equals("</span>")) {
                if (openSpanRestList.size() > 0) {
                    openSpanRestList.removeLast();
                }
                int restIndexOfClosingSpan = rest.indexOf("</span>");
                rest = rest.substring(0, restIndexOfClosingSpan) + rest.substring(restIndexOfClosingSpan+7);
                spanRestCount--;
            } else {
                openSpanRestList.add(matcher.group(0));
                int restIndexOfOpenSpan = rest.indexOf(matcher.group(0));
                rest = rest.substring(0, restIndexOfOpenSpan) +
                        rest.substring(restIndexOfOpenSpan+matcher.group(0).length());
                spanRestCount++;
            }
        }
        spanRestCount -= openSpanRestList.size();

        while (openSpanSequenceList.size() > Math.max(0, spanRestCount*(-1))) {
            tailingSpansBuilder.append(openSpanSequenceList.removeFirst());
        }
        while (openSpanRestList.size() > 0) {
            tailingSpansBuilder.append(openSpanRestList.removeFirst());
        }

        return new String[] {lineBuilder.toString(), rest, tailingSpansBuilder.toString()};
    }
}
