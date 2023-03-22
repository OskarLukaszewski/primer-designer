package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

public class ContentOfGcAnalyzer {

    double[] contentOfGC1 = new double[2];
    double distributionOfGC;

    public ContentOfGcAnalyzer(double[] contentOfGC, double evenDistributionOfGC) {
        if (contentOfGC != null) {
            this.contentOfGC1[0] = contentOfGC[0];
            this.contentOfGC1[1] = contentOfGC[1];
            this.distributionOfGC = evenDistributionOfGC;
        }
    }

    public boolean isGCcontentInRange(String primer) {

        double contentOfGC = getGCcontent(primer);

        if (contentOfGC<this.contentOfGC1[0]) return false;
        else return !(contentOfGC>this.contentOfGC1[1]);
    }

    public boolean isGCspreadInRange(String primer) {

        double currentContentOfGC = getGCcontent(primer);
        double[] contentOfGC2 = new double[2];
        contentOfGC2[0] = currentContentOfGC - currentContentOfGC*(1-this.distributionOfGC);
        contentOfGC2[1] = currentContentOfGC + (1-currentContentOfGC)*(1-this.distributionOfGC);

        int fragmentLength = primer.length();

        if(fragmentLength>3) {

            String leftSubstring = primer.substring(0, primer.length()/2);
            if (!isGCspreadInRange(leftSubstring)) return false;

            String rightSubstring = primer.substring(primer.length()/2);
            if (!isGCspreadInRange(rightSubstring)) return false;

            if (!isGCcontentInRange(rightSubstring, contentOfGC2)) return false;
            else return isGCcontentInRange(leftSubstring, contentOfGC2);
        } else return true;
    }

    private static double getGCcontent(String primer) {

        double numberOfGC = 0;
        double allBases = primer.length();

        for (int i=0; i<allBases; i++) {
            if (primer.charAt(i) == 'G' || primer.charAt(i) == 'C') {
                numberOfGC++;
            }
        }

        return numberOfGC / allBases;
    }

    private boolean isGCcontentInRange(String primer, double[] GCcontentRange) {

        if (primer.length()<4) return true;

        double contentOfGC = getGCcontent(primer);

        if (contentOfGC<GCcontentRange[0]) return false;
        else return !(contentOfGC > GCcontentRange[1]);
    }
}