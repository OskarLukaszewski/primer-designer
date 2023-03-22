package functionalities.primers.main_functions;

import functionalities.primers.main_tools.file_interaction.PrimersLineReader;
import functionalities.primers.main_tools.primer_designing.analysing_primers.PrimerAnalyzer;

public class DesignPrimers {

    public static String[][][][] run(
            String filePath,
            int[] primerLength,
            double[] meltingTemp,
            int maxRepeatLength,
            double[] GCcontent,
            double evenGCdistribution,
            int[][] flankPositions,
            boolean[] filters,
            boolean[] toDesign) throws Exception {

        String[] sequence;
        try {
            sequence = PrimersLineReader.getSequence(filePath);
        } catch (Exception e) {
            throw new Exception("Provided file cannot be read.");
        }

        PrimerAnalyzer primerAnalyzer = new PrimerAnalyzer(
                        sequence,
                        primerLength,
                        meltingTemp,
                        maxRepeatLength,
                        GCcontent,
                        evenGCdistribution,
                        flankPositions,
                        filters,
                        toDesign
        );

        int numberOfSequences = primerAnalyzer.getNumberOfSequences();
        String[][][][] analyzedPrimers = new String[numberOfSequences][][][];
        for (int i=0; i<numberOfSequences; i++) {
            analyzedPrimers[i] = primerAnalyzer.analyzePrimers(i);
        }

        return analyzedPrimers;
    }
}