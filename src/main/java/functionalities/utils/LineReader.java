package functionalities.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class LineReader {

    public static String[] getSequence(String fileName) throws Exception {

        Exception exception = new Exception("The provided file cannot be read.");

        ArrayList<String> lines = new ArrayList<>();

        try {
            File fileSequence = new File(fileName);
            Scanner myScanner = new Scanner(fileSequence);
            while (myScanner.hasNextLine()) {
                lines.add(myScanner.nextLine());
            }
            myScanner.close();
        } catch (FileNotFoundException e) {
            throw exception;
        }

        String[] answer = new String[lines.size()];
        lines.toArray(answer);

        return answer;
    }
}
