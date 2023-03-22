package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

import static java.lang.Math.abs;

public class TmAnalyzer {

    double meltingTemp;
    double precision;

    public TmAnalyzer(double[] meltingTemp) {
        this.meltingTemp = (meltingTemp[1]+meltingTemp[0])/2;
        this.precision = (meltingTemp[1]-meltingTemp[0])/2;
    }

    public boolean doesMatchTm (String primer) {
        return abs(getMeltingTemp(primer) - meltingTemp) <= precision;
    }

    public double getMeltingTemp (String primer) {

        double temp = 0;

        if (primer.length() < 14) {
            for (int i=0; i<primer.length(); i++) {
                if (primer.charAt(i) == 'A' || primer.charAt(i) == 'T') {
                    temp += 2;
                } else temp +=4;
            }
        } else {

            int numberOfGC = 0;
            int numberOfAll = 0;

            for (int i=0; i<primer.length(); i++) {
                if (primer.charAt(i) == 'G' || primer.charAt(i) == 'C') {
                    numberOfGC++;
                }
                numberOfAll++;
            }

            temp = 64.9+41*(numberOfGC-16.4)/(numberOfAll);
        }

        return temp;
    }
}