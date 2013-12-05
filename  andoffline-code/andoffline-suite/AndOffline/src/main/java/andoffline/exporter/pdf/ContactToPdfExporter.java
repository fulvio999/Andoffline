
package andoffline.exporter.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import andoffline.gui.common.utility.VcfTranslatorUtils;
import andoffline.parser.vcf.VcardEntry;
import andoffline.parser.vcf.VcardRecord;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

/**
 * Export the User Contact, extracted from a parsed VCF file to a PDF file
 *
 */
public class ContactToPdfExporter {

	/**
	 * Constructor
	 */
	public ContactToPdfExporter() {
		
	}
	
	
	/**
	 * Creates a PDF file using the List of VcardEntry extracted from a VCF file
	 * 
	 * @param filePdfName
	 * @param destinationFolder	 
	 * @param vcardRecordList The list of Vcard records extracted from a vcf file and to be exported to a pdf file
	 * @return true if the export operation is execute successfully
	 * @throws DocumentException
	 * @throws IOException
	 */
	public boolean exportContact(String filePdfName, String destinationFolder, List<VcardRecord> vcardRecordList) throws DocumentException, IOException{
		
		//--- Step 1: Prepare the contents to be inserted in the PDF table header -----------
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		String currentDate = SmsToPdfExporter.DATEFORMATTER.format(calendar.getTime());		
		String pathAndFile = null;
        
		// Check if the user has provided or not the file extension
        if(filePdfName.endsWith("pdf"))        	
        	pathAndFile = destinationFolder+File.separator+filePdfName;
        else
        	pathAndFile = destinationFolder+File.separator+filePdfName+".pdf";
		
        //--- Step 2: Prepare the PDF document to be filled -----------		
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);      
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathAndFile));
       
        //The fonts type to be used in the document
        Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        
        /*--- Step 3: BEFORE open the document we add some meta information to the document 
         * (that properties can be viewed ONLY with adobe reader or right click-properties)
         */
        document.addAuthor("AndOffline");    
        document.open();
        
        //--- Step 4  
        //The paragraph where add the table header (ie a Paragraph is a section in the document that contains a group of elements)
        Paragraph headerParagraph = new Paragraph();       
        
        //--- Add an header table to the document        
        PdfPTable table = new PdfPTable(2); //the number of column       
        table.setWidthPercentage(100);

        //Create two cells to put in a PdfPTable above
        PdfPCell fileNamCell = new PdfPCell(new Paragraph("File: "+ filePdfName));
        PdfPCell dateCell = new PdfPCell(new Paragraph("Date: "+currentDate));
        
        fileNamCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
 
        table.addCell(fileNamCell);
        table.addCell(dateCell);
        
        headerParagraph.add(table);
        //note: for each created paragraph is added a CR in auto by itext
        document.add(headerParagraph);	
        
        //add an empty row
        document.add(Chunk.NEWLINE);
        
		//----- Core functionality: Extract the content from the  List<VcardRecord> in input -----		  
		for(int i=0; i<vcardRecordList.size(); i++)
		{			  
			  //System.out.println("---- Start Record ------");
			  Phrase smsHeaderPhrase = new Phrase("CONTACT ", headerFont);           	  
        	  document.add(smsHeaderPhrase);
			  
        	  document.add(Chunk.NEWLINE);
        	  
			  VcardRecord rec = vcardRecordList.get(i);
			  List<VcardEntry> entryList = rec.getVcardEntry();
			  
			  for(int j=0; j<entryList.size(); j++)
			  {				  
				  VcardEntry entry = entryList.get(j);				 	            				  		            				  
				  
				  /* Convert the label in a human readable label (eg N ---> Name) */ 		            				   
				  String labelToDisplay = VcfTranslatorUtils.conversionNameTable.get(entry.getType().trim());
				  
				  if(labelToDisplay == null)
					  labelToDisplay = entry.getType().trim();
				  
				  document.add(new Chunk(labelToDisplay+" : "+entry.getValue()));
				  document.add(Chunk.NEWLINE);
			  }
			  
			  //System.out.println("---- End Record ------");			  
              LineSeparator ls = new LineSeparator(); 
              ls.setLineWidth(0);
              document.add(new Chunk(ls));	            	  
        	 
        	  document.add(Chunk.NEWLINE);
		  }
		
		// step 5
        document.close();
       
        return true;		
	}

}
