package andoffline.parser.vcf;


/**
 * Enumeration with all the parameters availables for all the properties (picked up from rfc6350)
 * A property can have attributes associated with it. These are the "property parameters", someone are optional.
 * At a "property parameters" is assigned a value
 * (A property in vcf files is the first token at line beginning)
 * 
 * A property can have only some "property parameters"
 *
 */
public enum VcardPropertiesParamEnum {
	
	LANGUAGE,
	VALUE,
	PREF,
	ALTID,
	PID,
	TYPE,
	MEDIATYPE,
	CALSCALE,
	SORT_AS,
	GEO,
	TZ;

}
