package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepeatAnalyzerTest {

	@Test
	public void bothSingleAndDoubleBaseRepeatsAboveMaxShouldReturnTrue() {

		RepeatAnalyzer repeatAnalyzer = new RepeatAnalyzer(3);
		String testString = "ATGAAAACTGCATATATATGCTGCA";

		assertTrue(repeatAnalyzer.doesContainRepeats(testString));
	}

	@Test
	public void NoSingleOrDoubleBaseRepeatsAboveMaxShouldReturnFalse() {

		RepeatAnalyzer repeatAnalyzer = new RepeatAnalyzer(3);
		String testString = "ATGCTGCATGCTGCA";

		assertFalse(repeatAnalyzer.doesContainRepeats(testString));
	}

	@Test
	public void singleBaseRepeatsEqualToMaxShouldReturnFalse() {

		RepeatAnalyzer repeatAnalyzer = new RepeatAnalyzer(4);
		String testPrimer = "ATCGTAAAATGCT";

		assertFalse(repeatAnalyzer.doesContainRepeats(testPrimer));
	}

	@Test
	public void singleBaseRepeatsUnderMaxShouldReturnFalse() {

		RepeatAnalyzer repeatAnalyzer = new RepeatAnalyzer(4);
		String testPrimer = "ATCGTAAATGCT";

		assertFalse(repeatAnalyzer.doesContainRepeats(testPrimer));
	}

	@Test
	public void singleBaseRepeatsAboveMaxShouldReturnTrue() {

		RepeatAnalyzer repeatAnalyzer = new RepeatAnalyzer(4);
		String testPrimer = "ATCGTAAAAATGCT";

		assertTrue(repeatAnalyzer.doesContainRepeats(testPrimer));
	}

	@Test
	public void doubleBaseRepeatsEqualToMaxShouldReturnFalse() {

		RepeatAnalyzer repeatAnalyzer = new RepeatAnalyzer(4);
		String testPrimer = "ATGCTGATATATATCGTGC";

		assertFalse(repeatAnalyzer.doesContainRepeats(testPrimer));
	}

	@Test
	public void doubleBaseRepeatsUnderMaxShouldReturnFalse() {

		RepeatAnalyzer repeatAnalyzer = new RepeatAnalyzer(4);
		String testPrimer = "ATGCTGATATATCGTGC";

		assertFalse(repeatAnalyzer.doesContainRepeats(testPrimer));
	}

	@Test
	public void doubleBaseRepeatsAboveMaxShouldReturnTrue() {

		RepeatAnalyzer repeatAnalyzer = new RepeatAnalyzer(4);
		String testPrimer = "ATGCTGATATATATATCGTGC";

		assertTrue(repeatAnalyzer.doesContainRepeats(testPrimer));
	}
}