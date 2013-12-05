package andoffline.parser.vcf;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 *
 */
public class VCFparser {

	//The path of the chosen vcf file with the contacts to parse
	private String vcfFilePath; 
	
	/**
	 * Constructor
	 * @param pathSourceVCFfile The path of the chosen vcf file with the contacts to parse
	 */
	public VCFparser(String pathSourceVCFfile) {
		this.vcfFilePath = pathSourceVCFfile;
	}

	/**
	 * 
	 * 
	 * 
	 */	
	public List<VcardRecord> parseContact() throws FileNotFoundException, IOException{	
		
		File file = new File(this.vcfFilePath);		
		FileReader fr = new FileReader(file);		
		BufferedReader br = new BufferedReader(fr);
		
		//The list of record to return filled with a VCF file (ie a block from BEGIN:VCARD to END:VCARD)
		//a VcardRecord is composed of a list of VcardEntry (ie N:MIKE)
		List<VcardRecord> vcardRecordList = new ArrayList<VcardRecord>();
		
		//The current line from the vcf file
		String currentLine = null;
		
		VcardRecord vcardRecord = null;
		
		while((currentLine = br.readLine()) != null)
		{
			//There are two type of input lines:
			//1)TYPE 1: Lines where property value is followed by ':'  N:pavoniano;paolo messa;;;
			//2)TYPE 2: Lines where property value is followed by ';'  ADR;HOME:;;via delle prove;milano;mi;20154;
			
			//Split on ':' char			
			String[] subLine = currentLine.split(":", 2);
			
			/*  Depend on the input line, propertyType can be like:
				N
				ADR;HOME			 	
			 */
			String propertyType = subLine[0].trim();	
			
			/*  Depend on the input line, value can be like:
				university;john doe;;;
				;;test street;rome;rm;20154;			 
			 */
			String value = subLine[1].trim();
	
			//Note: propertyType can contain the group name			
			String parameterName = null;
			
			//Check the type of propertyType: TYPE 1 or TYPE 2 ?
			if (propertyType.indexOf(';') != -1) { //true if line is TYPE 2
				 
				 //split to get the parameter Name and his value
				 parameterName = propertyType.substring(currentLine.indexOf(';')+1).trim().toUpperCase();
				
				 propertyType = propertyType.substring(0, propertyType.indexOf(';')).trim();
				 
			} 
			//else is TYPE 1
			
			//NOTE: propertyType can be an extended type (Non-standard, private properties, the ones that haven't standard name)
			if(propertyType.startsWith("X-")) {
				//TODO
			}
			
			if(propertyType.equalsIgnoreCase("BEGIN")){
				
					//System.out.println("***** Start record *****");					
					vcardRecord = new VcardRecord(); //the contents between begin and end
			}
			
			if(propertyType.equalsIgnoreCase(VcardPropertiesEnum.END.name())){
				
					//System.out.println("***** End record ******");
					//add all the entry found at the current Vcard record
					vcardRecordList.add(vcardRecord);					
			}				   
				
			if(!propertyType.equalsIgnoreCase("VERSION") && !propertyType.equalsIgnoreCase("PHOTO") && !propertyType.equalsIgnoreCase(VcardPropertiesEnum.END.name()) && !propertyType.equalsIgnoreCase(VcardPropertiesEnum.BEGIN.name())){ //skip some tags
						
						VcardEntry vcardEntry = new VcardEntry();
						
						String type = propertyType;
						
						if(parameterName != null)
							type += "("+parameterName+")"; 
						
						vcardEntry.setType(type);
						vcardEntry.setValue(value.replace(';', ' '));
						
						vcardRecord.getVcardEntry().add(vcardEntry);
						
					} if (propertyType.equalsIgnoreCase("PHOTO")){	//Skip the image-encoded base64 data block
						
						while(!currentLine.matches("$"))
							currentLine = br.readLine();
					}
					
				}	
	  return vcardRecordList;
		
	}	

	public String getVcfFilePath() {
		return vcfFilePath;
	}

	public void setVcfFilePath(String vcfFilePath) {
		this.vcfFilePath = vcfFilePath;
	}
			
}
		
	
	
		
 
