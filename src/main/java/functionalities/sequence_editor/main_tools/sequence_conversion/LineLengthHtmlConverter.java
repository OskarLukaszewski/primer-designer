package functionalities.sequence_editor.main_tools.sequence_conversion;

import functionalities.utils.HtmlConverter;

import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineLengthHtmlConverter extends HtmlConverter {

    private final Exception exception;

    public LineLengthHtmlConverter() {
        this.exception = new Exception("Incorrect sequence format.");
    }

    public String changeLineLength(String htmlText, int lineLength) throws Exception {

        StringBuilder htmlBuilder = new StringBuilder();

        String header = htmlText.substring(0, htmlText.indexOf("<body"));
        htmlBuilder.append(header).append("<body>\n");

        String body = htmlText.substring(htmlText.indexOf("<p"), htmlText.indexOf("</body")).strip();
        if (!isFormatCorrect(body)) {
            throw this.exception;
        }
        String bodyHeader;
        String bodySequence;
        try {
            if (body.contains("<p id=\"header\">")) {
                bodyHeader = body.substring(body.indexOf("<p id=\"header\">"), body.indexOf("<p id=\"sequence\">"));
                bodySequence = body.substring(body.indexOf("<p id=\"sequence\">"));
            } else {
                bodyHeader = "";
                bodySequence = body;
            }
        } catch (IndexOutOfBoundsException e) {
            return htmlText;
        }
        String newBodySequence = buildNewBodyFromStream(getBodyAsStream(bodySequence), lineLength);
        htmlBuilder.append(bodyHeader).append(newBodySequence).append("</body>\n</html>");

        return htmlBuilder.toString();
    }

    private LinkedList<String[]>[] getBodyAsStream(String body) throws Exception {

        LinkedList<String[]>[] dataStream = new LinkedList[] {new LinkedList<>(), new LinkedList<>()};
        int firstLineLength = 0;
        int secondLineLength = 0;

        String[] paragraphs = body.replaceAll("\n", "").split("</p>");

        Pattern linePattern = Pattern.compile("((?:(?:</?span.*?>)*[atcg]*(?:</?span.*?>)*)*)(.*)");
        Pattern dataPattern = Pattern.compile("(<span.*?>)|([atcg]+)|(</span>)");
        Matcher lineMatcher;
        Matcher dataMatcher;

        for (String paragraph: paragraphs) {
            String[] lines = paragraph.split("<br />");
            lineMatcher = linePattern.matcher(lines[0].strip().replaceAll("<p id=\"sequence\">", ""));
            if (lineMatcher.find()) {
                String[] processedLine = clearLine("", lineMatcher.group(1), lineMatcher.group(2));
                dataMatcher = dataPattern.matcher(processedLine[0]);
                while (dataMatcher.find()) {
                    if (dataMatcher.group(1) != null) {
                        dataStream[0].add(new String[] {"open_span", dataMatcher.group(1)});
                    } else if (dataMatcher.group(2) != null) {
                        dataStream[0].add(new String[] {"sequence", dataMatcher.group(2)});
                        firstLineLength += dataMatcher.group(2).length();
                    } else if (dataMatcher.group(3) != null) {
                        dataStream[0].add(new String[] {"close_span", dataMatcher.group(3)});
                    }
                }
                if (processedLine[2].length() > 0) {
                    dataStream[1].add(new String[] {"open_span", processedLine[2]});
                }
            }
            lineMatcher = linePattern.matcher(lines[1].strip());
            if (lineMatcher.find()) {
                String[] processedLine = clearLine("", lineMatcher.group(1), lineMatcher.group(2));
                dataMatcher = dataPattern.matcher(processedLine[0]);
                while (dataMatcher.find()) {
                    if (dataMatcher.group(1) != null && !dataMatcher.group(1).equals("")) {
                        dataStream[1].add(new String[] {"open_span", dataMatcher.group(1)});
                    } else if (dataMatcher.group(2) != null && !dataMatcher.group(2).equals("")) {
                        dataStream[1].add(new String[] {"sequence", dataMatcher.group(2)});
                        secondLineLength += dataMatcher.group(2).length();
                    } else if (dataMatcher.group(3) != null && !dataMatcher.group(3).equals("")) {
                        dataStream[1].add(new String[] {"close_span", dataMatcher.group(3)});
                    }
                }
            }
        }

        if (firstLineLength != secondLineLength) {
            throw this.exception;
        }

        return dataStream;
    }

    private String buildNewBodyFromStream(LinkedList<String[]>[] dataStream, int lineLength) throws Exception {

        StringBuilder bodyBuilder = new StringBuilder();

        int linesBuilt = 0;
        while (dataStream[0].size() > 0 || dataStream[1].size() > 0) {
            int currentFirstLineLength = 0;
            StringBuilder firstLineBuilder = new StringBuilder();
            Stack<String> firstLineOpenedSpans = new Stack<>();
            StringBuilder secondLineBuilder = new StringBuilder();
            Stack<String> secondLineOpenedSpans = new Stack<>();
            while (currentFirstLineLength < lineLength) {
                String[] element;
                if (dataStream[0].size() > 0) {
                    element = dataStream[0].removeFirst();
                } else {
                    break;
                }
                switch (element[0]) {
                    case "open_span":
                        firstLineBuilder.append(element[1]);
                        firstLineOpenedSpans.push(element[1]);
                        break;
                    case "close_span":
                        firstLineBuilder.append(element[1]);
                        if (firstLineOpenedSpans.size() > 0) {
                            firstLineOpenedSpans.pop();
                        }
                        break;
                    case "sequence":
                        if (currentFirstLineLength + element[1].length() < lineLength) {
                            firstLineBuilder.append(element[1]);
                            currentFirstLineLength += element[1].length();
                        } else if (currentFirstLineLength + element[1].length() > lineLength) {
                            String partOfElementToAdd = element[1].substring(0, lineLength - currentFirstLineLength);
                            String restOfElement = element[1].substring(lineLength - currentFirstLineLength);
                            firstLineBuilder.append(partOfElementToAdd);
                            dataStream[0].addFirst(new String[]{"sequence", restOfElement});
                            while (firstLineOpenedSpans.size() > 0) {
                                dataStream[0].addFirst(new String[]{"open_span", firstLineOpenedSpans.pop()});
                                firstLineBuilder.append("</span>");
                            }
                            currentFirstLineLength += partOfElementToAdd.length();
                        } else {
                            firstLineBuilder.append(element[1]);
                            while (firstLineOpenedSpans.size() > 0) {
                                dataStream[0].addFirst(new String[]{"open_span", firstLineOpenedSpans.pop()});
                                firstLineBuilder.append("</span>");
                            }
                            currentFirstLineLength += element[1].length();
                        }
                        break;
                }
            }

            int currentSecondLineLength = 0;
            while (currentSecondLineLength < lineLength) {
                String[] element;
                if (dataStream[1].size() > 0) {
                    element = dataStream[1].removeFirst();
                } else {
                    break;
                }
                switch (element[0]) {
                    case "open_span":
                        secondLineBuilder.append(element[1]);
                        secondLineOpenedSpans.add(element[1]);
                        break;
                    case "close_span":
                        secondLineBuilder.append(element[1]);
                        if (secondLineOpenedSpans.size() > 0) {
                            secondLineOpenedSpans.pop();
                        }
                        break;
                    case "sequence":
                        if (currentSecondLineLength + element[1].length() < lineLength) {
                            secondLineBuilder.append(element[1]);
                            currentSecondLineLength += element[1].length();
                        } else if (currentSecondLineLength + element[1].length() > lineLength) {
                            String partOfElementToAdd = element[1].substring(0, lineLength - currentSecondLineLength);
                            String restOfElement = element[1].substring(lineLength - currentSecondLineLength);
                            secondLineBuilder.append(partOfElementToAdd);
                            dataStream[1].addFirst(new String[]{"sequence", restOfElement});
                            while (secondLineOpenedSpans.size() > 0) {
                                dataStream[1].addFirst(new String[]{"open_span", secondLineOpenedSpans.pop()});
                                secondLineBuilder.append("</span>");
                            }
                            currentSecondLineLength += partOfElementToAdd.length();
                        } else {
                            secondLineBuilder.append(element[1]);
                            while (secondLineOpenedSpans.size() > 0) {
                                dataStream[1].addFirst(new String[]{"open_span", secondLineOpenedSpans.pop()});
                                secondLineBuilder.append("</span>");
                            }
                            currentSecondLineLength += element[1].length();
                        }
                        break;
                }
            }

            if (currentFirstLineLength != currentSecondLineLength) {
                throw this.exception;
            }
            
            if (currentFirstLineLength > 0) {
                bodyBuilder.append("<p id=\"sequence\">")
                        .append(firstLineBuilder)
                        .append(" base pairs<br />\n")
                        .append(secondLineBuilder)
                        .append(" ").append(1+lineLength*linesBuilt)
                        .append(" to ").append(lineLength*linesBuilt+currentFirstLineLength)
                        .append("</p>\n");
                linesBuilt++;
            }
        }

        return bodyBuilder.toString();
    }
}
