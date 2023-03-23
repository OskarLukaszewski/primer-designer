package functionalities.primers.main_tools.primer_designing.analysing_primers.analysing_requirements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TmAnalyzerTest {

	@Test
	public void lowerMeltingTempShouldReturnFalseForLengthOverOrEqualTo14() {

		double[] meltingTemp = {53, 57};
		TmAnalyzer tmAnalyzer = new TmAnalyzer(meltingTemp);
		String testPrimer = "AAATCGTGATTTATGCTGACCCT";

		assertFalse(tmAnalyzer.doesMatchTm(testPrimer));
	}

	@Test
	public void higherMeltingTempShouldReturnFalseForLengthOverOrEqualTo14() {

		double[] meltingTemp = {53, 57};
		TmAnalyzer tmAnalyzer = new TmAnalyzer(meltingTemp);
		String testPrimer = "AAATCGTGATTTATGCTGACCCTGGG";

		assertFalse(tmAnalyzer.doesMatchTm(testPrimer));
	}

	@Test
	public void meltingTempEqualToTopBoundShouldReturnTrueForLengthOverOrEqualTo14() {

		double[] meltingTemp = {53, 58};
		TmAnalyzer tmAnalyzer = new TmAnalyzer(meltingTemp);
		String testPrimer = "AAATCGTGATTTATGCTGACCCTGGG";

		assertTrue(tmAnalyzer.doesMatchTm(testPrimer));
	}

	@Test
	public void lowerMeltingTempShouldReturnFalseForLengthUnder14() {

		double[] meltingTemp = {40, 50};
		TmAnalyzer tmAnalyzer = new TmAnalyzer(meltingTemp);
		String testPrimer = "AAAATGGGGGC";

		assertFalse(tmAnalyzer.doesMatchTm(testPrimer));
	}

	@Test
	public void higherMeltingTempShouldReturnFalseForLengthUnder14() {

		double[] meltingTemp = {30, 32};
		TmAnalyzer tmAnalyzer = new TmAnalyzer(meltingTemp);
		String testPrimer = "AAAATGGGGGC";

		assertFalse(tmAnalyzer.doesMatchTm(testPrimer));
	}

	@Test
	public void meltingTempEqualToTopBoundShouldReturnTrueForLengthUnder14() {

		double[] meltingTemp = {30, 34};
		TmAnalyzer tmAnalyzer = new TmAnalyzer(meltingTemp);
		String testPrimer = "AAAATGGGGGC";

		assertTrue(tmAnalyzer.doesMatchTm(testPrimer));
	}

	@Test
	public void meltingTempEqualToBottomBoundShouldReturnTrueForLengthUnder14() {

		double[] meltingTemp = {34, 50};
		TmAnalyzer tmAnalyzer = new TmAnalyzer(meltingTemp);
		String testPrimer = "AAAATGGGGGC";

		assertTrue(tmAnalyzer.doesMatchTm(testPrimer));
	}

	@Test
	public void meltingTempInRangeShouldReturnTrueForLengthUnder14() {

		double[] meltingTemp = {30, 40};
		TmAnalyzer tmAnalyzer = new TmAnalyzer(meltingTemp);
		String testPrimer = "AAAATGGGGGC";

		assertTrue(tmAnalyzer.doesMatchTm(testPrimer));
	}

}