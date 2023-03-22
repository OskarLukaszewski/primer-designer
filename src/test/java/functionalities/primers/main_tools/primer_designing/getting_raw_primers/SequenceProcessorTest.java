package functionalities.primers.main_tools.primer_designing.getting_raw_primers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SequenceProcessorTest {

	private final String[] testSequences = {
			">OYE2 YHR179W SGDID:S000001222, Chromosome VIII:462502..463704+/- 1kb",
			"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
			"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
			"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
			"AATTGATTATATATATATATCTTTGCAATTATGTCGTTTGTTGCAAGATGC",
			"",
			"",
			"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
			"TACTATACGGCAGCGGCTTGTTGCTGCCGTTTAATGAAACAGTTTTTTTCACGACAAG"
	};

	@Test
	public void isolateSequencesTest() {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		int numberOfSequences = sequenceProcessor.isolateSequences(testSequences);

		assertEquals(2, numberOfSequences);
	}

	@Test
	public void getOnlyForwardFlanks() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, false};
		int[][] flankPositions = {{1, 10}, {1, 10}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);

		assertNotNull(flanks[0]);
		assertNull(flanks[1]);
	}

	@Test
	public void getOnlyReverseFlanks() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {false, true};
		int[][] flankPositions = {{1, 10}, {1, 10}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);

		assertNull(flanks[0]);
		assertNotNull(flanks[1]);
	}

	@Test
	public void getBothFlanks() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{1, 10}, {1, 10}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);

		assertNotNull(flanks[0]);
		assertNotNull(flanks[1]);
	}

	@Test
	public void getNoFlanks() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {false, false};
		int[][] flankPositions = {{1, 10}, {1, 10}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);

		assertNull(flanks[0]);
		assertNull(flanks[1]);
	}

	@Test
	public void throwExceptionIfIncorrectFlankPositions() {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{0, 10}, {0, 10}};

		assertThrows(Exception.class,
				() -> sequenceProcessor.isolateFlanks(0, flankPositions, toDesign));
	}

	@Test
	public void startLineGetFlank() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{1, 10}, {1, 10}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);
		String[] expectedFlanks = {"ACGTCTCTTG", "CAAGAGACGT"};

		assertEquals(expectedFlanks[0], flanks[0]);
		assertEquals(expectedFlanks[1], flanks[1]);
	}

	@Test
	public void midLineGetFlank() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{5, 14}, {5, 14}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);
		String[] expectedFlanks = {"CTCTTGGGTT", "AACCCAAGAG"};

		assertEquals(expectedFlanks[0], flanks[0]);
		assertEquals(expectedFlanks[1], flanks[1]);
	}

	@Test
	public void startToEndLineGetFlank() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{1, 58}, {1, 58}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);
		String[] expectedFlanks = {
				"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
				"ACGACATGACAAAACTCGGTGAACATATGTCATACAAGTTTATCAACCCAAGAGACGT"
		};

		assertEquals(expectedFlanks[0], flanks[0]);
		assertEquals(expectedFlanks[1], flanks[1]);
	}

	@Test
	public void endOfLineToNextLineGetFlank() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{58, 67}, {58, 67}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);
		String[] expectedFlanks = {
				"TACGTCTCTT",
				"AAGAGACGTA"
		};

		assertEquals(expectedFlanks[0], flanks[0]);
		assertEquals(expectedFlanks[1], flanks[1]);
	}

	@Test
	public void midToEndLineGetFlank() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{10, 58}, {10, 58}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);
		String[] expectedFlanks = {
				"GGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
				"ACGACATGACAAAACTCGGTGAACATATGTCATACAAGTTTATCAACCC"
		};

		assertEquals(expectedFlanks[0], flanks[0]);
		assertEquals(expectedFlanks[1], flanks[1]);
	}

	@Test
	public void midToMidLinesGetFlank() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{10, 80}, {10, 80}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);
		String[] expectedFlanks = {
				"GGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGTACGTCTCTTGGGTTGATAAACT",
				"AGTTTATCAACCCAAGAGACGTACGACATGACAAAACTCGGTGAACATATGTCATACAAGTTTATCAACCC"
		};

		assertEquals(expectedFlanks[0], flanks[0]);
		assertEquals(expectedFlanks[1], flanks[1]);
	}

	@Test
	public void midToMidWithFullLinesGetFlank() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{10, 129}, {10, 129}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);
		String[] expectedFlanks = {
				"GGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT" +
						"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGTACGTCTCTTGGGT",
				"ACCCAAGAGACGTACGACATGACAAAACTCGGTGAACATATGTCATACA" +
						"AGTTTATCAACCCAAGAGACGTACGACATGACAAAACTCGGTGAACATATGTCATACAAGTTTATCAACCC"
		};

		assertEquals(expectedFlanks[0], flanks[0]);
		assertEquals(expectedFlanks[1], flanks[1]);
	}

	@Test
	public void midToEndWithFullLinesGetFlank() throws Exception {
		SequenceProcessor sequenceProcessor = new SequenceProcessor();
		sequenceProcessor.isolateSequences(testSequences);
		boolean[] toDesign = {true, true};
		int[][] flankPositions = {{10, 174}, {10, 174}};
		String[] flanks = sequenceProcessor.isolateFlanks(0, flankPositions, toDesign);
		String[] expectedFlanks = {
				"GGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGTACGTCTCTTGGG" +
						"TTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGTACGTCTCTTGGGTTGAT" +
						"AAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
				"ACGACATGACAAAACTCGGTGAACATATGTCATACAAGTTTATCAACCCAAGAGACGTACG" +
						"ACATGACAAAACTCGGTGAACATATGTCATACAAGTTTATCAACCCAAGAGACGTACGACATG" +
						"ACAAAACTCGGTGAACATATGTCATACAAGTTTATCAACCC"
		};

		assertEquals(expectedFlanks[0], flanks[0]);
		assertEquals(expectedFlanks[1], flanks[1]);
	}
}