package functionalities.primers.main_tools;

public class InputAnalyzer {

    public static double[] checkTm(String[] Tm) throws Exception {

        Exception exception = new Exception("Incorrect primer property: melting temperature.");

        double[] meltingTemp = new double[2];

        try {
            meltingTemp[0] = Double.parseDouble(Tm[0]);
            meltingTemp[1] = Double.parseDouble(Tm[1]);
        } catch (Exception e) {
            throw exception;
        }
        if (meltingTemp[0]>meltingTemp[1]) throw exception;
        else if (meltingTemp[0]<0 || meltingTemp[1]<0) throw exception;
        else return meltingTemp;
    }

    public static int[] checkPrimerLength(String[] primerLengthString) throws Exception {

        Exception exception = new Exception("Incorrect primer property: length.");

        int[] primerLength = new int[2];

        try {
            primerLength[0] = Integer.parseInt(primerLengthString[0]);
            primerLength[1] = Integer.parseInt(primerLengthString[1]);
        } catch (Exception e) {
            throw exception;
        }
        if (primerLength[0]>primerLength[1]) throw exception;
        else if (primerLength[0]<0) throw exception;
        else return primerLength;
    }

    public static int[][] checkFlankLengthAndPosition(String[][] flanksString, boolean[] toDesign) throws Exception {

        Exception exception = new Exception("Incorrect search fields.");

        int[][] flanks = new int[2][2];

        try {
            if (toDesign[0]) {
                flanks[0][0] = Integer.parseInt(flanksString[0][0]);
                flanks[0][1] = Integer.parseInt(flanksString[0][1]);
                if (flanks[0][0]>=flanks[0][1]) throw exception;
                else if (flanks[0][0]<0) throw exception;
            }
            if (toDesign[1]) {
                flanks[1][0] = Integer.parseInt(flanksString[1][0]);
                flanks[1][1] = Integer.parseInt(flanksString[1][1]);
                if (flanks[1][0]>=flanks[1][1]) throw exception;
                else if (flanks[1][0]<0) throw exception;
            }
        } catch (Exception e) {
            throw exception;
        }

        return flanks;
    }

    public static void checkIfPrimersFitInSearchFields(int[][] flanks, int maxPrimerLength, boolean[] toDesign) throws Exception{

        Exception exception = new Exception("Primers out of bounds of defined search fields.");

        if (toDesign[0]) {
            if (flanks[0][1]-flanks[0][0]+1<maxPrimerLength) throw exception;
        }
        if (toDesign[1]) {
            if (flanks[1][1]-flanks[1][0]+1<maxPrimerLength) throw exception;
        }
    }

    public static double[] checkContentOfGC(String[] contentOfGCString) throws Exception {

        Exception exception = new Exception("Incorrect primer property: GC content.");

        double[] contentOfGC = new double[2];

        try {
            contentOfGC[0] = Double.parseDouble(contentOfGCString[0])/100;
            contentOfGC[1] = Double.parseDouble(contentOfGCString[1])/100;
        } catch (Exception e) {
            throw exception;
        }
        if (contentOfGC[0]>contentOfGC[1]) throw exception;
        else if (contentOfGC[0]<0 || contentOfGC[1]<0) throw exception;
        else if (contentOfGC[0]>1 || contentOfGC[1]>1) throw exception;
        else return contentOfGC;
    }

    public static double checkDistributionOfGC(String distributionOfGCString) throws Exception {

        Exception exception = new Exception("Incorrect primer property: GC distribution.");

        double distributionOfGC;

        try {
            distributionOfGC = Double.parseDouble(distributionOfGCString)/100;
        } catch (Exception e) {
            throw exception;
        }
        if (distributionOfGC<0 || distributionOfGC>1) throw exception;
        else return distributionOfGC;
    }

    public static int checkMaxRepeatLength(String maxRepeatLengthString) throws Exception {

        Exception exception = new Exception("Incorrect primer property: maximum length of repeats.");

        int maxRepeatLength;

        try {
            maxRepeatLength = Integer.parseInt(maxRepeatLengthString);
        } catch (Exception e) {
            throw exception;
        }

        return maxRepeatLength;
    }
}
