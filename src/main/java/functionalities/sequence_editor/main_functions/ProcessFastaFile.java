package functionalities.sequence_editor.main_functions;

import functionalities.sequence_editor.main_tools.file_interaction.SequenceLineReader;
import functionalities.sequence_editor.main_tools.sequence_conversion.FastaConverter;

public class ProcessFastaFile {
  
  public static String[][] run(String fileName) throws Exception {

    String[] sequence = SequenceLineReader.getSequence(fileName);

    FastaConverter fastaConverter = new FastaConverter();

    return fastaConverter.convert(sequence);
  }
}