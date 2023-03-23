package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplementationAnalyzerTest {

	@Test
	public void threeComplementaryBasesShouldReturnFalse() {

		String testPrimer = "ATGCTGGTACAGGCT";

		assertFalse(ComplementationAnalyzer.doesIntraComplement(testPrimer));
	}

	@Test
	public void twoComplementaryBasesShouldReturnFalse() {

		String testPrimer = "ATGCTGGTACATGCT";

		assertFalse(ComplementationAnalyzer.doesIntraComplement(testPrimer));
	}

	@Test
	public void fourComplementaryBasesShouldReturnTrue() {

		String testPrimer = "ATGCTGGTCCAGGCT";

		assertTrue(ComplementationAnalyzer.doesIntraComplement(testPrimer));
	}

	@Test
	public void fiveComplementaryBasesShouldReturnTrue() {

		String testPrimer = "ATGCTGGTCACCAGGCT";

		assertTrue(ComplementationAnalyzer.doesIntraComplement(testPrimer));
	}
}