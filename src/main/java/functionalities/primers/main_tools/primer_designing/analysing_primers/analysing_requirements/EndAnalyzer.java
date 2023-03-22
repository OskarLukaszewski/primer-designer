package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

public class EndAnalyzer {
    
    public static boolean doesEndInGC (String primer) {
        return primer.charAt(primer.length() - 1) == 'G' || primer.charAt(primer.length() - 1) == 'C';
    }
}
