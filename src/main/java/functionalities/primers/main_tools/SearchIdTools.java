package functionalities.primers.main_tools;

public class SearchIdTools {

    public static String getSearchId(
            String filePath,
            String[] primerLengthString,
            String[] meltingTempString,
            String maxRepeatLengthString,
            String[] GCcontentString,
            String GCdistributionString,
            String[][] flankPositionsString,
            boolean[] filters,
            boolean[] toDesign) {

        String SearchId = filePath + "?|@|?";
        if (toDesign[0]) {
            SearchId += true + "?|@|?" + flankPositionsString[0][0] + "?|@|?" + flankPositionsString[0][1] + "?|@|?";
        } else {
            SearchId += false + "?|@|?";
        }
        if (toDesign[1]) {
            SearchId += true + "?|@|?" + flankPositionsString[1][0] + "?|@|?" + flankPositionsString[1][1] + "?|@|?";
        } else {
            SearchId += false + "?|@|?";
        }
        SearchId += meltingTempString[0] + "?|@|?" + meltingTempString[1] + "?|@|?" +
                primerLengthString[0] + "?|@|?" + primerLengthString[1] + "?|@|?" +
                filters[0] + "?|@|?" + filters[1] + "?|@|?";
        if (filters[2]) {
            SearchId += true + "?|@|?" + maxRepeatLengthString + "?|@|?";
        } else {
            SearchId += false+ "?|@|?";
        }
        if (filters[3]) {
            SearchId += true + "?|@|?"+ GCcontentString[0] + "?|@|?" + GCcontentString[1] + "?|@|?";
        } else {
            SearchId += false + "?|@|?";
        }
        if (filters[4]) {
            SearchId += true + "?|@|?" + GCdistributionString;
        } else {
            SearchId += false;
        }
        return SearchId;
    }

    public static String decipherId(String searchId) throws Exception {

        Exception exception = new Exception("No information found.");

        if (searchId == null) {
            throw exception;
        }

        StringBuilder decipheredId = new StringBuilder();

        String[] matches = searchId.split("\\?\\|@\\|\\?");

        int nextIndex;
        decipheredId.append("File path:   ").append(matches[0]).append("\n");
        if (matches[1].equals("true")) {
            decipheredId.append("Forward search field:   ").append(matches[2]).append(" - ")
                    .append(matches[3]).append("\n");
            nextIndex = 4;
        } else {
            nextIndex = 2;
        }
        if (matches[nextIndex].equals("true")) {
            decipheredId.append("Reverse search field:   ").append(matches[nextIndex+1]).append(" - ")
                    .append(matches[nextIndex+2]).append("\n");
            nextIndex += 3;
        } else {
            nextIndex++;
        }
        decipheredId.append("Melting temperature:   ").append(matches[nextIndex]).append(" - ")
                .append(matches[nextIndex+1]).append("\n");
        nextIndex += 2;
        if (matches[nextIndex].equals(matches[nextIndex+1])) {
            decipheredId.append("Primer length:   ").append(matches[nextIndex]).append("\n");
        } else {
            decipheredId.append("Primer length:   ").append(matches[nextIndex]).append(" - ")
                    .append(matches[nextIndex+1]).append("\n");
        }
        nextIndex += 2;
        if (matches[nextIndex].equals("true")) {
            decipheredId.append("GC at the 3' end\n");
        }
        if (matches[nextIndex+1].equals("true")) {
            decipheredId.append("No self-complementation\n");
        }
        if (matches[nextIndex+2].equals("true")) {
            decipheredId.append("Max repeat length:   ").append(matches[nextIndex+3]).append("\n");
            nextIndex += 4;
        } else {
            nextIndex += 3;
        }
        if (matches[nextIndex].equals("true")) {
            decipheredId.append("GC content:   ").append(matches[nextIndex+1]).append("%").append(" - ")
                    .append(matches[nextIndex+2]).append("%").append("\n");
            nextIndex += 3;
        } else {
            nextIndex++;
        }
        if (matches[nextIndex].equals("true")) {
            decipheredId.append("GC distribution:   ").append(matches[nextIndex+1]).append("%").append("\n");
        }

        return decipheredId.toString();
    }
}
