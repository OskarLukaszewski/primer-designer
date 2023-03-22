package functionalities.primers.services.tasks;

import functionalities.primers.main_functions.DesignPrimers;
import functionalities.primers.main_tools.InputAnalyzer;
import javafx.concurrent.Task;

public class DesignPrimersTask extends Task<String[][][][]> {

    private final String filePath, GCdistributionString, maxRepeatLengthString;
    private final String[] primerLengthString, meltingTempString, GCcontentString;
    private final String[][] flankPositionsString;
    private final boolean[] filters, toDesign;

    public DesignPrimersTask(
            String filePath,
            String[] primerLengthString,
            String[] meltingTempString,
            String maxRepeatLengthString,
            String[] GCcontentString,
            String GCdistributionString,
            String[][] flankPositionsString,
            boolean[] filters,
            boolean[] toDesign) {

        this.filePath = filePath;
        this.primerLengthString = primerLengthString;
        this.meltingTempString = meltingTempString;
        this.maxRepeatLengthString = maxRepeatLengthString;
        this.GCcontentString = GCcontentString;
        this.GCdistributionString = GCdistributionString;
        this.flankPositionsString = flankPositionsString;
        this.filters = filters;
        this.toDesign = toDesign;
    }

    @Override
    protected String[][][][] call() throws Exception {

        int[] primerLength = InputAnalyzer.checkPrimerLength(this.primerLengthString);
        double[] meltingTemp = InputAnalyzer.checkTm(this.meltingTempString);
        double[] contentOfGC;
        if (this.filters[3]) {
            contentOfGC = InputAnalyzer.checkContentOfGC(this.GCcontentString);
        } else {
            contentOfGC = null;
        }
        double distributionOfGC;
        if (this.filters[4]) {
            distributionOfGC = InputAnalyzer.checkDistributionOfGC(this.GCdistributionString);
        } else {
            distributionOfGC = 0;
        }
        int[][] flankPositions = InputAnalyzer.checkFlankLengthAndPosition(this.flankPositionsString, this.toDesign);
        int maxRepeatLength = InputAnalyzer.checkMaxRepeatLength(this.maxRepeatLengthString);
        InputAnalyzer.checkIfPrimersFitInSearchFields(flankPositions, primerLength[1], this.toDesign);

        return DesignPrimers.run(
                this.filePath,
                primerLength,
                meltingTemp,
                maxRepeatLength,
                contentOfGC,
                distributionOfGC,
                flankPositions,
                this.filters,
                this.toDesign
        );
    }
}
