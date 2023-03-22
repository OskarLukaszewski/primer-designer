package functionalities.primers.main_tools.sequence_conversion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimersLineConverterTest {

	@Test
	public void complementAndReverseLine() {
		String testLine = "ACGTCTCTTGGGTTGATAAACTTGTATGA";
		String complementedAndReversedLine = PrimersLineConverter.complementAndReverse(testLine);
		String expectedLine = "TCATACAAGTTTATCAACCCAAGAGACGT";

		assertEquals(expectedLine, complementedAndReversedLine);
	}
}