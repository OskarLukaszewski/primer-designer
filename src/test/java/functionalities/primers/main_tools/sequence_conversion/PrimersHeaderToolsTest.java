package functionalities.primers.main_tools.sequence_conversion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimersHeaderToolsTest {

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
	public void removeHeaders() {
		String[][] sequences = PrimersHeaderTools.removeHeaders(testSequences);
		String[][] expectedSequences = {
				{
						"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
						"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
						"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
						"AATTGATTATATATATATATCTTTGCAATTATGTCGTTTGTTGCAAGATGC"
				},
				{
						"ACGTCTCTTGGGTTGATAAACTTGTATGACATATGTTCACCGAGTTTTGTCATGTCGT",
						"TACTATACGGCAGCGGCTTGTTGCTGCCGTTTAATGAAACAGTTTTTTTCACGACAAG"
				}
		};

		for (int i=0; i<sequences.length; i++) {
			for (int j=0; j<sequences[i].length; j++) {
				assertEquals(expectedSequences[i][j], sequences[i][j]);
			}
		}
	}
}