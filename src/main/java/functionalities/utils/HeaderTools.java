package functionalities.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HeaderTools {

    public static String[][][] removeAndStoreHeaders(String[] rawSequence) {

        Pattern pattern = Pattern.compile("[^ATCG]|^\\s*$");
        Matcher matcher;
        boolean matchFound;
        boolean isPreviousHeader = false;
        boolean isPreviousSequence = false;
        ArrayList<String> header = new ArrayList<>();
        ArrayList<String[]> headers = new ArrayList<>();
        ArrayList<String> sequence = new ArrayList<>();
        ArrayList<String[]> sequences = new ArrayList<>();

        int numberOfLines = rawSequence.length;
        for (int i=0; i<numberOfLines; i++) {

            String line = rawSequence[i];
            matcher = pattern.matcher(line);
            matchFound = matcher.find();

            if (matchFound && !isPreviousSequence) {

                isPreviousHeader = true;
                header.add(line);

            } else if (matchFound) {

                isPreviousHeader = true;
                isPreviousSequence = false;
                String[] sequenceFinal = new String[sequence.size()];
                sequence.toArray(sequenceFinal);
                sequences.add(sequenceFinal);
                sequence.clear();
                header.add(line);

            } else if (!isPreviousHeader) {

                isPreviousSequence = true;
                sequence.add(line);

            } else {

                isPreviousHeader = false;
                isPreviousSequence = true;
                String[] headerFinal = new String[header.size()];
                header.toArray(headerFinal);
                headers.add(headerFinal);
                header.clear();
                sequence.add(line);
            }

            if (i == numberOfLines - 1) {
                String[] sequenceFinal = new String[sequence.size()];
                sequence.toArray(sequenceFinal);
                sequences.add(sequenceFinal);
                sequence.clear();
            }
        }

        String[][] headersFinal = new String[headers.size()][];
        headers.toArray(headersFinal);
        String[][] sequencesFinal = new String[sequences.size()][];
        sequences.toArray(sequencesFinal);

        return new String[][][]{headersFinal, sequencesFinal};
    }

    protected static String[][] removeHeaders(String[] rawSequence) {

        Pattern pattern = Pattern.compile("[^ATCG]|^\\s*$");
        Matcher matcher;
        boolean matchFound;
        boolean isPreviousHeader = false;
        boolean isPreviousSequence = false;
        ArrayList<String> sequence = new ArrayList<>();
        ArrayList<String[]> sequences = new ArrayList<>();

        int numberOfLines = rawSequence.length;
        for (int i=0; i<numberOfLines; i++) {

            String line = rawSequence[i];
            matcher = pattern.matcher(line);
            matchFound = matcher.find();

            if (matchFound && !isPreviousSequence) {

                isPreviousHeader = true;

            } else if (matchFound) {

                isPreviousHeader = true;
                isPreviousSequence = false;
                String[] sequenceFinal = new String[sequence.size()];
                sequence.toArray(sequenceFinal);
                sequences.add(sequenceFinal);
                sequence.clear();

            } else if (!isPreviousHeader) {

                isPreviousSequence = true;
                sequence.add(line);

            } else {

                isPreviousHeader = false;
                isPreviousSequence = true;
                sequence.add(line);
            }

            if (i == numberOfLines - 1) {
                String[] sequenceFinal = new String[sequence.size()];
                sequence.toArray(sequenceFinal);
                sequences.add(sequenceFinal);
                sequence.clear();
            }
        }

        String[][] sequencesFinal = new String[sequences.size()][];
        sequences.toArray(sequencesFinal);

        return sequencesFinal;
    }

    public static String[] addHeader(String[] header, String[] sequence) {

        if (header.length < 1) return sequence;

        String[] output = new String[header.length + sequence.length];

        for (int i=0; i<header.length; i++) {
            output[i] = header[i] + "\n";
        }
        for (int i=0; i<sequence.length; i++) {
            output[header.length+i] = sequence[i];
        }

        return output;
    }
}
