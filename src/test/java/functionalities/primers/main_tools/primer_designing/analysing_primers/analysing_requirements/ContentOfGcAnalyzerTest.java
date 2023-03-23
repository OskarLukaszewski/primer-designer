package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentOfGcAnalyzerTest {

	@Test
	public void unevenGCSpreadShouldReturnFalse() {

		double[] contentOfGC = {0.4, 0.6};
		double degreeOfEvenDistribution = 0.8;
		ContentOfGcAnalyzer contentOfGcAnalyzer = new ContentOfGcAnalyzer(contentOfGC, degreeOfEvenDistribution);

		String testPrimer = "AAAGTTCGAAAACTGGGG";
		assertFalse(contentOfGcAnalyzer.isGCspreadInRange(testPrimer));
	}

	@Test
	public void evenGCSpreadShouldReturnTrue() {

		double[] contentOfGC = {0.4, 0.6};
		double degreeOfEvenDistribution = 0.8;
		ContentOfGcAnalyzer contentOfGcAnalyzer = new ContentOfGcAnalyzer(contentOfGC, degreeOfEvenDistribution);

		String testPrimer = "AAGACCTGGACAAG";
		assertTrue(contentOfGcAnalyzer.isGCspreadInRange(testPrimer));
	}

	@Test
	public void lowerContentOfGCShouldReturnFalse() {

		double[] contentOfGC = {0.4, 0.6};
		double degreeOfEvenDistribution = 0.8;
		ContentOfGcAnalyzer contentOfGcAnalyzer = new ContentOfGcAnalyzer(contentOfGC, degreeOfEvenDistribution);

		String testPrimer = "AATTAAACGCG";
		assertFalse(contentOfGcAnalyzer.isGCcontentInRange(testPrimer));
	}

	@Test
	public void higherContentOfGCShouldReturnFalse() {

		double[] contentOfGC = {0.4, 0.6};
		double degreeOfEvenDistribution = 0.8;
		ContentOfGcAnalyzer contentOfGcAnalyzer = new ContentOfGcAnalyzer(contentOfGC, degreeOfEvenDistribution);

		String testPrimer = "AATTCCCCGCG";
		assertFalse(contentOfGcAnalyzer.isGCcontentInRange(testPrimer));
	}

	@Test
	public void contentInRangeShouldReturnTrue() {

		double[] contentOfGC = {0.4, 0.6};
		double degreeOfEvenDistribution = 0.8;
		ContentOfGcAnalyzer contentOfGcAnalyzer = new ContentOfGcAnalyzer(contentOfGC, degreeOfEvenDistribution);

		String testPrimer = "AATTAACCGGCCCCC";
		assertTrue(contentOfGcAnalyzer.isGCcontentInRange(testPrimer));
	}

	@Test
	public void bottomBoundShouldReturnTrue() {

		double[] contentOfGC = {0.4, 0.6};
		double degreeOfEvenDistribution = 0.8;
		ContentOfGcAnalyzer contentOfGcAnalyzer = new ContentOfGcAnalyzer(contentOfGC, degreeOfEvenDistribution);

		String testPrimer = "AATTAAGGGG";
		assertTrue(contentOfGcAnalyzer.isGCcontentInRange(testPrimer));
	}

	@Test
	public void topBoundShouldReturnTrue() {

		double[] contentOfGC = {0.4, 0.6};
		double degreeOfEvenDistribution = 0.8;
		ContentOfGcAnalyzer contentOfGcAnalyzer = new ContentOfGcAnalyzer(contentOfGC, degreeOfEvenDistribution);

		String testPrimer = "AATTGGGGGG";
		assertTrue(contentOfGcAnalyzer.isGCcontentInRange(testPrimer));
	}
}