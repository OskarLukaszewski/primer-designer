package functionalities.primers.main_tools.primer_designing.getting_raw_primers;

public class SequenceCrawler {

    private final SequenceProcessor sequenceProcessor;
    private String forFlank, revFlank;

    public SequenceCrawler() {
        this.sequenceProcessor = new SequenceProcessor();
    }
    
    public String[][][] getPossiblePrimers(int[] primerLength, int[][] flankPositions, boolean[] toDesign,
                                           int numberOfSequence) throws Exception {

        String[] flanks = sequenceProcessor.isolateFlanks(numberOfSequence, flankPositions, toDesign);

        this.forFlank = flanks[0];
        this.revFlank = flanks[1];

        int minPrimerLength = primerLength[0];
        int maxPrimerLength = primerLength[1];

        String[][] forPrimers;
        if (toDesign[0]) {
            forPrimers = crawlFlank(this.forFlank, minPrimerLength, maxPrimerLength);
        } else {
            forPrimers = null;
        }

        String[][] revPrimers;
        if (toDesign[1]) {
            revPrimers = crawlFlank(this.revFlank, minPrimerLength, maxPrimerLength);
        } else {
            revPrimers = null;
        }

        return new String[][][]{forPrimers, revPrimers};
    }

    public int isolateSequences (String[] sequence) {
        return sequenceProcessor.isolateSequences(sequence);
    }

    public String getForFlank() {
        return this.forFlank;
    }
    public String getRevFlank() {
        return this.revFlank;
    }

    private String[][] crawlFlank (String flank, int minPrimerLength, int maxPrimerLength) {

        int flankLength = flank.length();

        String[][] primers = new String[maxPrimerLength-minPrimerLength+1][];
        for (int i=0; i<maxPrimerLength-minPrimerLength+1; i++) {
            primers[i] = new String[flankLength-minPrimerLength+1-i];
        }
        
        for (int i=0; i<flankLength; i++) {
            for (int j=minPrimerLength; j<maxPrimerLength+1; j++) {
                if (i+j > flankLength) break;
                String primer = flank.substring(i, i+j);
                primers[j - minPrimerLength][i] = primer;
            }
        }

        return primers;
    }
}