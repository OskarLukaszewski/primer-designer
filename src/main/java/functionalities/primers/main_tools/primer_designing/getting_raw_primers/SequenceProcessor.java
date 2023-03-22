package functionalities.primers.main_tools.primer_designing.getting_raw_primers;

import functionalities.primers.main_tools.sequence_conversion.PrimersHeaderTools;
import functionalities.primers.main_tools.sequence_conversion.PrimersLineConverter;

public class SequenceProcessor {
    
    String[][] sequences;
    String[] sequence;

    int forStartPosition;
    int forEndPosition;
    int revStartPosition;
    int revEndPosition;

    String forFlank, revFlank;

    public String[] isolateFlanks(int numberOfSequence, int[][] flankPositions, boolean[] toDesign) throws Exception{

        Exception exception = new Exception("Search fields out of bounds.");

        this.sequence = this.sequences[numberOfSequence];

        this.forStartPosition = flankPositions[0][0];
        this.forEndPosition = flankPositions[0][1];
        this.revStartPosition = flankPositions[1][0];
        this.revEndPosition = flankPositions[1][1];

        try {
            if (toDesign[0]) {
                this.forFlank = getFlank(this.sequences[numberOfSequence], this.forStartPosition, this.forEndPosition);
            }
            if (toDesign[1]) {
                this.revFlank =
                        PrimersLineConverter.complementAndReverse(
                                getFlank(this.sequences[numberOfSequence], this.revStartPosition, this.revEndPosition));
            }
        } catch (IndexOutOfBoundsException e) {
            throw exception;
        }

        return new String[]{this.forFlank, this.revFlank};
    }

    public int isolateSequences(String[] sequence) {

        this.sequences = PrimersHeaderTools.removeHeaders(sequence);

        return this.sequences.length;
    }

    private String getFlank(String[] sequence, int startPosition, int endPosition) {

        int lengthOfFirstLine = sequence[0].length();
        int[] startPoint = {startPosition / lengthOfFirstLine, startPosition % lengthOfFirstLine};
        int[] endPoint = {endPosition / lengthOfFirstLine, endPosition % lengthOfFirstLine};

        StringBuilder flank = new StringBuilder();
        if (startPoint[1] == 0) {
            startPoint[1] = lengthOfFirstLine;
            startPoint[0]--;
        }
        if (endPoint[0]>startPoint[0]) {
            flank.append(sequence[startPoint[0]].substring(startPoint[1]-1));
        }
        for (int i=startPoint[0]+1; i<endPoint[0]; i++) {
            flank.append(sequence[i]);
        }
        if (startPoint[0] != endPoint[0]) {
            flank.append(sequence[endPoint[0]], 0, endPoint[1]);
        } else {
            flank.append(sequence[startPoint[0]], startPoint[1]-1, endPoint[1]);
        }

        return flank.toString();
    }
}