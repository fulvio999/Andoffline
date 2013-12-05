package andoffline.parser.vcf;


/**
 * Each instance of this class represents a pair type - value 
 * contained in a .vcf file (eg FN:john doe)
 *
 */
public class VcardEntry {
	
	//The vcard parameter type (eg F, FN, TEL ,NAME, FULL NAME...)
	private String type;
	
	//The value of the vcard parameter type (eg: jonh doe)
	private String value;	


	/**
	 * Constructor
	 */
	public VcardEntry() {
		
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}

}
