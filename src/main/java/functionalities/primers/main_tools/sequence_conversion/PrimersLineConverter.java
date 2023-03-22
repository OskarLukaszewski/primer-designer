package functionalities.primers.main_tools.sequence_conversion;

import functionalities.utils.LineConverter;

public class PrimersLineConverter extends LineConverter {

  public static String complementAndReverse (String line) {
    return reverseLine(complementLine(line));
  }
}