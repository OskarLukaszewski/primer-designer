package functionalities.sequence_editor.main_functions;

import functionalities.sequence_editor.main_tools.sequence_conversion.HtmlToRtfConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveSequence {

    private static final Exception exception = new Exception("Couldn't save sequence to file.");

    public static int run(String filePath, String htmlText) throws Exception {
        String extension = filePath.substring(filePath.lastIndexOf('.')+1);
        if (extension.equals("rtf")) {
            saveAsRTF(filePath, htmlText);
        } else {
            saveAsHTML(filePath, htmlText);
        }
        return 0;
    }

    private static void saveAsHTML(String filePath, String htmlText) throws Exception {
        FileOutputStream outputStream = null;
        try {
            File file = new File(filePath);
            outputStream = new FileOutputStream(file);
            outputStream.write(htmlText.getBytes());
        } catch (IOException e) {
            throw exception;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw exception;
                }
            }
        }
    }

    private static void saveAsRTF(String filePath, String htmlText) throws Exception {
        FileOutputStream outputStream = null;
        try {
            File file = new File(filePath);
            outputStream = new FileOutputStream(file);
            HtmlToRtfConverter converter = new HtmlToRtfConverter();
            outputStream.write(converter.convert(htmlText).getBytes());
        } catch (IOException e) {
            throw exception;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw exception;
                }
            }
        }
    }
}
