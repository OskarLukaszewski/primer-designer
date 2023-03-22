package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

import functionalities.primers.main_tools.sequence_conversion.PrimersLineConverter;

public class ComplementationAnalyzer {
    
    public static boolean doesIntraComplement(String primer) {

        boolean intraComplements = false;

        String[][] allFragments = getFragments(primer);
        String[] fragments = allFragments[0];
        String[] complementaryFragments = allFragments[1];

        for (int i=0; i<fragments.length; i++) {
            if (intraComplements) break;
            for (int j=4+i; j<fragments.length; j++) {
                if (fragments[i].compareTo(complementaryFragments[j]) == 0) {
                    intraComplements = true;
                    break;
                }
            }
        }

        return intraComplements;
    }

    private static String[][] getFragments(String primer) {

        String[] fragments = new String[primer.length()-3];
        String[] complementaryFragments = new String[primer.length()-3];
        char[] charsForFragment = new char[4];

        for (int i=0; i<primer.length()-3; i++) {
            for (int j=0; j<4; j++) {
                charsForFragment[j] = primer.charAt(j+i);
            }
            fragments[i] = String.valueOf(charsForFragment);
            complementaryFragments[i] = PrimersLineConverter.complementAndReverse(String.valueOf(charsForFragment));
        }

        return new String[][]{fragments, complementaryFragments};
    }
}
