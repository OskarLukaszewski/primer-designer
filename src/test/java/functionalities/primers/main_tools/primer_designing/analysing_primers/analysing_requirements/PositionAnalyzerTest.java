package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionAnalyzerTest {

	private final String testForFlank = "ATGCTATGCTGC";
	private final String testRevFlank = "CAAGAGACGTAA";

	@Test
	public void testPositionOfForPrimer() {

		PositionAnalyzer positionAnalyzer = new PositionAnalyzer(10, 5);
		positionAnalyzer.setFlanks(testForFlank, testRevFlank);
		String testForPrimer = "GCTATGC";
		int expectedPosition = testForFlank.indexOf(testForPrimer) + 10;

		assertEquals(expectedPosition, positionAnalyzer.getPositionOfForPrimer(testForPrimer));
	}

	@Test
	public void testPositionOfRevPrimer() {

		PositionAnalyzer positionAnalyzer = new PositionAnalyzer(10, 5);
		positionAnalyzer.setFlanks(testForFlank, testRevFlank);
		String testRevPrimer = "GAGACG";
		int expectedPosition = testRevFlank.length() - testRevFlank.indexOf(testRevPrimer) - 1 + 5;

		assertEquals(expectedPosition, positionAnalyzer.getPositionOfRevPrimer(testRevPrimer));
	}
}