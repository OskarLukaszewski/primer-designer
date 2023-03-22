package functionalities.primers.main_tools.primer_designing.getting_raw_primers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SequenceCrawlerTest {

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
	public void designForPrimers() throws Exception {
		SequenceCrawler sequenceCrawler = new SequenceCrawler();
		sequenceCrawler.isolateSequences(testSequences);
		int[] primerLength = {5, 5};
		int[][] flankPositions = {{1, 7}, {1, 7}};
		boolean[] toDesign = {true, false};
		String[][][] primers =
				sequenceCrawler.getPossiblePrimers(primerLength, flankPositions, toDesign, 0);

		assertNotNull(primers[0]);
		assertNull(primers[1]);
	}

	@Test
	public void designRevPrimers() throws Exception {
		SequenceCrawler sequenceCrawler = new SequenceCrawler();
		sequenceCrawler.isolateSequences(testSequences);
		int[] primerLength = {5, 5};
		int[][] flankPositions = {{1, 7}, {1, 7}};
		boolean[] toDesign = {false, true};
		String[][][] primers =
				sequenceCrawler.getPossiblePrimers(primerLength, flankPositions, toDesign, 0);

		assertNull(primers[0]);
		assertNotNull(primers[1]);
	}

	@Test
	public void designBothPrimers() throws Exception {
		SequenceCrawler sequenceCrawler = new SequenceCrawler();
		sequenceCrawler.isolateSequences(testSequences);
		int[] primerLength = {5, 5};
		int[][] flankPositions = {{1, 7}, {1, 7}};
		boolean[] toDesign = {true, true};
		String[][][] primers =
				sequenceCrawler.getPossiblePrimers(primerLength, flankPositions, toDesign, 0);

		assertNotNull(primers[0]);
		assertNotNull(primers[1]);
	}

	@Test
	public void designNoPrimers() throws Exception {
		SequenceCrawler sequenceCrawler = new SequenceCrawler();
		sequenceCrawler.isolateSequences(testSequences);
		int[] primerLength = {5, 5};
		int[][] flankPositions = {{1, 7}, {1, 7}};
		boolean[] toDesign = {false, false};
		String[][][] primers =
				sequenceCrawler.getPossiblePrimers(primerLength, flankPositions, toDesign, 0);

		assertNull(primers[0]);
		assertNull(primers[1]);
	}

	@Test
	public void getForFlank() throws Exception {
		SequenceCrawler sequenceCrawler = new SequenceCrawler();
		sequenceCrawler.isolateSequences(testSequences);
		int[] primerLength = {5, 5};
		int[][] flankPositions = {{1, 7}, {1, 7}};
		boolean[] toDesign = {true, false};
		sequenceCrawler.getPossiblePrimers(primerLength, flankPositions, toDesign, 0);
		String flank = sequenceCrawler.getForFlank();
		String expectedFlank = "ACGTCTC";

		assertEquals(expectedFlank, flank);
	}

	@Test
	public void getRevFlank() throws Exception {
		SequenceCrawler sequenceCrawler = new SequenceCrawler();
		sequenceCrawler.isolateSequences(testSequences);
		int[] primerLength = {5, 5};
		int[][] flankPositions = {{1, 7}, {1, 7}};
		boolean[] toDesign = {false, true};
		sequenceCrawler.getPossiblePrimers(primerLength, flankPositions, toDesign, 0);
		String flank = sequenceCrawler.getRevFlank();
		String expectedFlank = "GAGACGT";

		assertEquals(expectedFlank, flank);
	}

	@Test
	public void getOneLengthPrimers() throws Exception {
		SequenceCrawler sequenceCrawler = new SequenceCrawler();
		sequenceCrawler.isolateSequences(testSequences);
		int[] primerLength = {5, 5};
		int[][] flankPositions = {{1, 7}, {1, 7}};
		boolean[] toDesign = {true, true};
		String[][][] primers =
				sequenceCrawler.getPossiblePrimers(primerLength, flankPositions, toDesign, 0);
		String[][][] expectedPrimers = {{{"ACGTC", "CGTCT", "GTCTC"}}, {{"GAGAC", "AGACG", "GACGT"}}};

		for (int i=0; i<expectedPrimers[0].length; i++) {
			for (int j=0; j<expectedPrimers[0][i].length; j++) {
				assertEquals(expectedPrimers[0][i][j], primers[0][i][j]);
			}
		}
		for (int i=0; i<expectedPrimers[1].length; i++) {
			for (int j=0; j<expectedPrimers[1][i].length; j++) {
				assertEquals(expectedPrimers[1][i][j], primers[1][i][j]);
			}
		}
	}

	@Test
	public void getTwoLengthsPrimers() throws Exception {
		SequenceCrawler sequenceCrawler = new SequenceCrawler();
		sequenceCrawler.isolateSequences(testSequences);
		int[] primerLength = {5, 6};
		int[][] flankPositions = {{1, 7}, {1, 7}};
		boolean[] toDesign = {true, true};
		String[][][] primers =
				sequenceCrawler.getPossiblePrimers(primerLength, flankPositions, toDesign, 0);
		String[][][] expectedPrimers = {
				{{"ACGTC", "CGTCT", "GTCTC"}, {"ACGTCT", "CGTCTC"}},
				{{"GAGAC", "AGACG", "GACGT"}, {"GAGACG", "AGACGT"}}
		};

		for (int i=0; i<expectedPrimers[0].length; i++) {
			for (int j=0; j<expectedPrimers[0][i].length; j++) {
				assertEquals(expectedPrimers[0][i][j], primers[0][i][j]);
			}
		}
		for (int i=0; i<expectedPrimers[1].length; i++) {
			for (int j=0; j<expectedPrimers[1][i].length; j++) {
				assertEquals(expectedPrimers[1][i][j], primers[1][i][j]);
			}
		}
	}
}