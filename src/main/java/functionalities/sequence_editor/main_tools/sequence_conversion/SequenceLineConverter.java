package functionalities.sequence_editor.main_tools.sequence_conversion;

import functionalities.utils.LineConverter;

public class SequenceLineConverter extends LineConverter {

  public static String complementAndAddRange(String line, int numberOfLine, int lengthOfFirstLine) {

    String complementaryLine = complementLine(line);

    return addRange(complementaryLine, numberOfLine, lengthOfFirstLine);
  }

  public static String[] stripTailingLineBreaks(String[] sequence) {

    int lastIndex = sequence.length - 1;
    String lastElement = sequence[lastIndex];
    String newLastElement = lastElement.substring(0, lastElement.length() - 2);
    sequence[lastIndex] = newLastElement;

    return sequence;
  }
}