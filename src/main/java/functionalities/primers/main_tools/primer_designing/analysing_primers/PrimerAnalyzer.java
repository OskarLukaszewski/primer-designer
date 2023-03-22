package functionalities.primers.main_tools.primer_designing.analysing_primers;

import java.util.ArrayList;

import functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements.*;
import functionalities.primers.main_tools.primer_designing.getting_raw_primers.SequenceCrawler;

public class PrimerAnalyzer {
    
    SequenceCrawler sequenceCrawler;
    TmAnalyzer tmAnalyzer;
    ContentOfGcAnalyzer contentOfGcAnalyzer;
    RepeatAnalyzer repeatAnalyzer;
    PositionAnalyzer positionAnalyzer;
    int numberOfSequences;
    int[] primerLength;
    int[][] flankPositions;
    boolean[] filters, toDesign;

    public PrimerAnalyzer (
            String[] sequence,
            int[] primerLength,
            double[] meltingTemp,
            int maxRepeatLength,
            double[] contentOfGC,
            double evenDistributionOfGC,
            int[][] flankPositions,
            boolean[] filters,
            boolean[] toDesign) {

        this.sequenceCrawler = new SequenceCrawler();
        this.numberOfSequences = this.sequenceCrawler.isolateSequences(sequence);
        this.tmAnalyzer = new TmAnalyzer(meltingTemp);
        this.contentOfGcAnalyzer = new ContentOfGcAnalyzer(contentOfGC, evenDistributionOfGC);
        this.repeatAnalyzer = new RepeatAnalyzer(maxRepeatLength);
        this.primerLength = primerLength;
        this.flankPositions = flankPositions;
        this.positionAnalyzer = new PositionAnalyzer(this.flankPositions[0][0], this.flankPositions[1][0]);
        this.filters = filters;
        this.toDesign = toDesign;
    }

    public String[][][] analyzePrimers(int sequenceNumber) throws Exception {

        String[][][] analyzedPrimers = new String[2][][];
        ArrayList<String[]> analyzedForPrimersList = new ArrayList<>();
        ArrayList<String[]> analyzedRevPrimersList = new ArrayList<>();
        String[] tempPrimersAndProperties = new String[3];

        String[][][] rawPrimers = getPossiblePrimers(sequenceNumber);
        this.positionAnalyzer.setFlanks(this.sequenceCrawler.getForFlank(), this.sequenceCrawler.getRevFlank());

        if (this.toDesign[0]) {
            for (String[] primers: rawPrimers[0]) {
                for (String primer: primers) {
                    if (doesMatchRequirements(primer)) {
                        tempPrimersAndProperties[0] = primer;
                        tempPrimersAndProperties[1] = String.format("%.1f", this.tmAnalyzer.getMeltingTemp(primer));
                        tempPrimersAndProperties[2] =
                                String.valueOf(this.positionAnalyzer.getPositionOfForPrimer(primer));
                        analyzedForPrimersList.add(tempPrimersAndProperties.clone());
                    }
                }
            }
        }
        String[][] analyzedForPrimers = new String[analyzedForPrimersList.size()][];
        analyzedForPrimersList.toArray(analyzedForPrimers);
        analyzedForPrimersList.clear();
        analyzedPrimers[0] = analyzedForPrimers;

        if (this.toDesign[1]) {
            for (String[] primers: rawPrimers[1]) {
                for (String primer: primers) {
                    if (doesMatchRequirements(primer)) {
                        tempPrimersAndProperties[0] = primer;
                        tempPrimersAndProperties[1] = String.format("%.1f", this.tmAnalyzer.getMeltingTemp(primer));
                        tempPrimersAndProperties[2] =
                                String.valueOf(this.positionAnalyzer.getPositionOfRevPrimer(primer));
                        analyzedRevPrimersList.add(tempPrimersAndProperties.clone());
                    }
                }
            }
        }
        String[][] analyzedRevPrimers = new String[analyzedRevPrimersList.size()][];
        analyzedRevPrimersList.toArray(analyzedRevPrimers);
        analyzedRevPrimersList.clear();
        analyzedPrimers[1] = analyzedRevPrimers;

        return analyzedPrimers;
    }

    public int getNumberOfSequences() {
        return this.numberOfSequences;
    }

    private String[][][] getPossiblePrimers(int numberOfSequence) throws Exception {
        return this.sequenceCrawler.getPossiblePrimers(this.primerLength, this.flankPositions,
                this.toDesign, numberOfSequence);
    }

    private boolean doesMatchRequirements(String primer) {

        if (this.filters[0]) {
            boolean endsInGC = EndAnalyzer.doesEndInGC(primer);
            if (!endsInGC) return false;
        }

        boolean matchesTm = this.tmAnalyzer.doesMatchTm(primer);
        if (!matchesTm) return false;

        if (this.filters[3]) {
            boolean contentOfGCinRange = this.contentOfGcAnalyzer.isGCcontentInRange(primer);
            if (!contentOfGCinRange) return false;
        }

        if (this.filters[4]) {
            boolean GCevenlyDistributed = this.contentOfGcAnalyzer.isGCspreadInRange(primer);
            if (!GCevenlyDistributed) return false;
        }

        if (this.filters[2]) {
            boolean containsRepeats = this.repeatAnalyzer.doesContainRepeats(primer);
            if (containsRepeats) return false;
        }

        if (this.filters[1]) {
            boolean intraComplements = ComplementationAnalyzer.doesIntraComplement(primer);
            if (intraComplements) return false;
        }

        return true;
    }
}