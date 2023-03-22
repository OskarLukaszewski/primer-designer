package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

public class PositionAnalyzer {

    private String forFlank, revFlank;
    private final int forStart, revStart;

    public PositionAnalyzer(int forStart, int revStart) {
        this.forStart = forStart;
        this.revStart = revStart;
    }

    public void setFlanks(String forFlank, String revFlank) {
        this.forFlank = forFlank;
        this.revFlank = revFlank;
    }

    public int getPositionOfForPrimer(String primer) {
        int positionOfPrimerInFlank = this.forFlank.indexOf(primer);
        return positionOfPrimerInFlank + this.forStart;
    }

    public int getPositionOfRevPrimer(String primer) {
        int positionOfPrimerInFlank = this.revFlank.length() - this.revFlank.indexOf(primer) - 1;
        return positionOfPrimerInFlank + this.revStart;
    }
}
