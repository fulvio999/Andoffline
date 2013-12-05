package andoffline.parser.vcf;



/**
 * Enumeration with all the properties contained in a VCF file (picked up from rfc6350)
 * A property in vcf files is the first token at line beginning
 *
 */
public enum VcardPropertiesEnum {
	
	//General Properties
	BEGIN, 
	END, 
	SOURCE, 
	KIND, 
	XML,
	
	//Identification Properties
	FN, 
	N, 
	NICKNAME, 
	PHOTO, 
	BDAY, 
	ANNIVERSARY, 
	GENDER,
	
	//Delivery Addressing Properties dopo di esse ho il ; invece che i :
	ADR, 
	TEL, 
	EMAIL, 
	IMPP, 
	LANG, 
	TZ, 
	GEO,
	
	//Organizational Properties
	TITLE, 
	ROLE, 
	LOGO, 
	ORG, 
	MEMBER, 
	RELATED,
	
	//Explanatory Properties
	CATEGORIES, 
	NOTE,
	PRODID, 
	REV, 
	SOUND, 
	UID,
	CLIENTPIDMAP, 
	URL, 
	VERSION,
	
	//Security Properties
	KEY,
	
	//Calendar Properties
	FBURL,
	CALADRURI,
	CALURI;
	
	//Add here custom property ()they MUST begin with X-
	
	
	
	

}
