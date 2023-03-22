package functionalities.utils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public abstract class LineConverter {

    protected static String complementLine(String line) {

        Map<Character, Character> myMap = Stream.of(
                new AbstractMap.SimpleEntry<>('a', 't'),
                new AbstractMap.SimpleEntry<>('t', 'a'),
                new AbstractMap.SimpleEntry<>('c', 'g'),
                new AbstractMap.SimpleEntry<>('g', 'c'),
                new AbstractMap.SimpleEntry<>('A', 'T'),
                new AbstractMap.SimpleEntry<>('T', 'A'),
                new AbstractMap.SimpleEntry<>('C', 'G'),
                new AbstractMap.SimpleEntry<>('G', 'C')
        ).collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        char[] charsInComplementaryLine = new char[line.length()];

        for (int i=0; i<line.length(); i++) {
            charsInComplementaryLine[i] = myMap.get(line.charAt(i));
        }

        return String.valueOf(charsInComplementaryLine);
    }

    protected static String addRange(String line, int numberOfLine, int lengthOfFirstLine) {

        int startPosition = lengthOfFirstLine * numberOfLine + 1;
        int endPosition = startPosition + line.length() - 1;
        String range = startPosition + " to " + endPosition;

        return line + " " + range + "\n\n";

    }

    protected static String reverseLine(String line) {
        char[] temp = new char[line.length()];
        for (int i=0; i<line.length(); i++) {
            temp[i] = line.charAt(line.length()-1-i);
        }
        return String.copyValueOf(temp);
    }

}
