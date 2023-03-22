package functionalities.sequence_editor.main_tools.sequence_conversion;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;

public class HtmlToRtfConverter {

    private String htmlText;
    private ArrayList<String> colorTableList;

    public String convert(String htmlText) {
        this.htmlText = htmlText;
        String baseHeader = "{\\rtf1\\ansi\\deff0{\\fonttbl{\\f0\\fmodern Courier New;}}";
        this.colorTableList = new ArrayList<>();
        this.colorTableList.add("{\\colortbl;\\red0\\green0\\blue0;");

        String[] body = this.htmlText.substring(this.htmlText.indexOf("<p"), this.htmlText.indexOf("</body>"))
                .replaceAll("\n", "")
                .replaceAll("<br /><p>", "</p><p>")
                .replaceAll("<br /></p>", "</p>")
                .replaceAll("<p.*?>", "")
                .replaceAll("<br />", Matcher.quoteReplacement("\\par "))
                .split("</p>");

        StringBuilder rtfBuilder = new StringBuilder();
        rtfBuilder.append("\\fs").append(getFontSize()).append("\\cf1").append("\n");
        for (String s: body) {
            rtfBuilder.append(convertLineSpans(ignoreUnrecognizedTags(s.strip()))).append("\\par\\par \n");
        }
        String rtfSequence = rtfBuilder.toString();
        if (rtfSequence.indexOf("\\par\\par \n\\par\\par \n") == rtfSequence.length()-20) {
            rtfSequence = rtfSequence.substring(0, rtfSequence.length()-20);
        } else if (rtfSequence.indexOf("\\par\\par \n") == rtfSequence.length()-10) {
            rtfSequence = rtfSequence.substring(0, rtfSequence.length()-10);
        }
        rtfSequence += "}";

        return baseHeader + getColorTable() + StringEscapeUtils.unescapeHtml4(rtfSequence);
    }

    private String getFontSize() {
        String styleInfo = this.htmlText.substring(this.htmlText.indexOf("<style>")+7,
                this.htmlText.indexOf("</style>"));
        Pattern pattern = Pattern.compile("font-size: (\\d+)pt");
        Matcher matcher = pattern.matcher(styleInfo);
        String fontSize;
        if (matcher.find()) {
            fontSize = String.valueOf(Integer.parseInt(matcher.group(1))*2);
        } else {
            fontSize = "18";
        }
        return fontSize;
    }

    private String convertLineSpans(String line) {

        StringBuilder rtfBuilder = new StringBuilder();
        Stack<String> closingArgumentsOfOpenedSpans = new Stack<>();

        Pattern spanPattern = Pattern.compile("</?span.*?>");
        Pattern spanColorPattern = Pattern.compile("rgb\\((\\d+), (\\d+), (\\d+)\\)");
        Matcher spanMatcher = spanPattern.matcher(line);
        Matcher colorMatcher;

        int currentStartIndex = 0;
        while (spanMatcher.find()) {
            String span = spanMatcher.group(0);
            if (span.equals("</span>")) {
                int spanStartIndex = line.indexOf(span);
                int spanEndIndex = spanStartIndex + span.length();
                rtfBuilder.append(line, currentStartIndex, spanStartIndex);
                line = line.substring(0, spanStartIndex) + line.substring(spanEndIndex);
                currentStartIndex = spanStartIndex;
                String closingArgumentsOfLastSpan = closingArgumentsOfOpenedSpans.pop();
                rtfBuilder.append(closingArgumentsOfLastSpan);
            }
            else {
                int spanStartIndex = line.indexOf(span);
                int spanEndIndex = spanStartIndex + span.length();
                rtfBuilder.append(line, currentStartIndex, spanStartIndex);
                line = line.substring(0, spanStartIndex) + line.substring(spanEndIndex);
                currentStartIndex = spanStartIndex;
                colorMatcher = spanColorPattern.matcher(span);
                StringBuilder closingArgumentsBuilder = new StringBuilder();
                if (colorMatcher.find()) {
                    String rtfColor = "\\red"+colorMatcher.group(1)+"\\green"
                            +colorMatcher.group(2)+"\\blue"+colorMatcher.group(3)+";";
                    if (this.colorTableList.contains(rtfColor)) {
                        rtfBuilder.append("\\cf").append(this.colorTableList.indexOf(rtfColor)+1);
                    } else {
                        this.colorTableList.add(rtfColor);
                        rtfBuilder.append("\\cf").append(this.colorTableList.size());
                    }
                    closingArgumentsBuilder.append("\\cf1");
                }
                if (span.contains("font-weight: bold")) {
                    rtfBuilder.append("\\b");
                    closingArgumentsBuilder.append("\\b0");
                }
                if (span.contains("font-style: italic")) {
                    rtfBuilder.append("\\i");
                    closingArgumentsBuilder.append("\\i0");
                }
                if (span.contains("text-decoration: underline")) {
                    rtfBuilder.append("\\ul");
                    closingArgumentsBuilder.append("\\ul0");
                }
                if (closingArgumentsBuilder.length() > 0) {
                    rtfBuilder.append(" ");
                    closingArgumentsBuilder.append(" ");
                }
                closingArgumentsOfOpenedSpans.push(closingArgumentsBuilder.toString());
            }
        }
        rtfBuilder.append(line.substring(currentStartIndex));

        return rtfBuilder.toString();
    }

    private String getColorTable() {
        StringBuilder output = new StringBuilder();
        for (String s : this.colorTableList) {
            output.append(s);
        }
        output.append("}");
        return output.toString();
    }

    private String ignoreUnrecognizedTags(String htmlText) {
        Pattern tagsPattern = Pattern.compile("<.*?>");
        Pattern recognizedTagsPattern = Pattern.compile("^</?span.*?>$");
        Matcher tagsMatcher = tagsPattern.matcher(htmlText);
        Matcher recognizedTagsMacher;
        while (tagsMatcher.find()) {
            String tag = tagsMatcher.group(0);
            recognizedTagsMacher = recognizedTagsPattern.matcher(tag);
            if (!recognizedTagsMacher.find()) {
                htmlText = htmlText.replace(tag, "");
            }
        }
        return htmlText;
    }
}