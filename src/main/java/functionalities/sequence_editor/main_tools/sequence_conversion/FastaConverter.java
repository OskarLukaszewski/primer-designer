package functionalities.sequence_editor.main_tools.sequence_conversion;

import java.util.ArrayList;

public class FastaConverter {

  public String[][] convert(String[] rawSequence) {

    String[][][] headersAndSequences = SequenceHeaderTools.removeAndStoreHeaders(rawSequence);
    String[][] headers = headersAndSequences[0];
    String[][] sequences = headersAndSequences[1];
    ArrayList<String[]> output = new ArrayList<>();


    for (int i=0; i<sequences.length; i++) {

      String[] sequence = sequences[i];
      int lengthOfFirstLine = sequence[0].length();
      String[] convertedSequence = new String[sequence.length * 2];

      for (int j=0; j<sequence.length; j++) {
        String line = sequence[j];
        convertedSequence[2*j] = line.toLowerCase() + " base pairs\n";
        convertedSequence[2*j+1] = SequenceLineConverter.complementAndAddRange(line.toLowerCase(), j, lengthOfFirstLine);
      }

      String[] strippedSequence = SequenceLineConverter.stripTailingLineBreaks(convertedSequence);
      if (i<headers.length) {
        String[] sequenceWithHeader = SequenceHeaderTools.addHeader(headers[i], strippedSequence);
        output.add(sequenceWithHeader);
      } else {
        output.add(strippedSequence);
      }
    }

    String[][] outputFinal = new String[output.size()][];

    return output.toArray(outputFinal);
  }
}