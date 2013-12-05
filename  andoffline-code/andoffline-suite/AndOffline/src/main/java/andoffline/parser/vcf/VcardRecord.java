
package andoffline.parser.vcf;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a record in a VCF file (ie a block from BEGIN:VCARD to END:VCARD) 
 *
 */
public class VcardRecord {
	
	private List<VcardEntry> vcardEntry;

	/**
	 * Constructor
	 */
	public VcardRecord() {
		this.vcardEntry = new ArrayList<VcardEntry>();
		
	}

	public List<VcardEntry> getVcardEntry() {
		return vcardEntry;
	}

	public void setVcardEntry(List<VcardEntry> vcardEntry) {
		this.vcardEntry = vcardEntry;
	}

}
