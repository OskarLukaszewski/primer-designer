package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

public class RepeatAnalyzer {

    private final int maxRepeatLength;

    public RepeatAnalyzer(int maxRepeatLength) {
        this.maxRepeatLength = maxRepeatLength;
    }

    public boolean doesContainRepeats(String primer) {
        return doesContainSingleRepeat(primer) || doesContainDoubleRepeat(primer);
    }

    private boolean doesContainSingleRepeat(String primer) {

        for (int i=0; i<primer.length()-this.maxRepeatLength; i++) {
            char currentChar = primer.charAt(i);
            for (int j=1; j<this.maxRepeatLength+1; j++) {
                if (primer.charAt(j+i) != currentChar) break;
                if (j == this.maxRepeatLength) return true;
            }
        }
        return false;
    }

    private boolean doesContainDoubleRepeat(String primer) {
        
        char[] currentChars = new char[2];

        for (int i=0; i<primer.length()-2*this.maxRepeatLength-1; i++) {
            currentChars[0] = primer.charAt(i);
            currentChars[1] = primer.charAt(i+1);
            for (int j=2; j<2*this.maxRepeatLength+1; j+=2) {
                if (primer.charAt(i+j)!=currentChars[0] || primer.charAt(i+j+1)!=currentChars[1]) break;
                if (j == 2*this.maxRepeatLength) return true;
            }
        }
        return false;
    }
}
