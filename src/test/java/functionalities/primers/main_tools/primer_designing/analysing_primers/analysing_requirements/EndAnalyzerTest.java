package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndAnalyzerTest {

	@Test
	public void endingInCShouldReturnTrue() {

		String testPrimer = "AAATGTCGTAGCTAC";

		assertTrue(EndAnalyzer.doesEndInGC(testPrimer));
	}

	@Test
	public void endingInGShouldReturnTrue() {

		String testPrimer = "AAATGTCGTAGCTACG";

		assertTrue(EndAnalyzer.doesEndInGC(testPrimer));
	}

	@Test
	public void endingInAShouldReturnFalse() {

		String testPrimer = "AAATGTCGTAGCTACA";

		assertFalse(EndAnalyzer.doesEndInGC(testPrimer));
	}

	@Test
	public void endingInTShouldReturnFalse() {

		String testPrimer = "AAATGTCGTAGCTACAT";

		assertFalse(EndAnalyzer.doesEndInGC(testPrimer));
	}
}