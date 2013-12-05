
package andoffline.exporter.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

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
 * Export the SMS currently showed in a JTree to a PDF file
 *
 */
public class SmsToPdfExporter {
	
	public static SimpleDateFormat DATEFORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/* 
	 * The list of sms field to export when the user don't want export the sms technical fields but only this "base" fileds 
	 * Note: this filter is applied only for the sms tree, the log call tree doesn't have technical fields 
	 * */	
	public static List<String> smsBaseNodeNamesToExport = new ArrayList<String>(Arrays.asList("Phone Number (Destination/Receiver)","Date of the sms (dd/mm/yyyy)","Type","SMS text","Service Center (only for received sms)","SMS read"));
	
	/**
	 * Constructor
	 * 
	 */
	public SmsToPdfExporter() {
	
	}	
   
    /**
     * Creates a PDF file using the SMS showed in the JTree in argument
     * @param args no arguments needed
     */
	public boolean exportSms(String filePdfName, String destinationFolder, JTree tree, boolean exportOnlyBaseSmsFieldsFlag) throws DocumentException, IOException{
		
		//--- Step 1: Prepare the contents to be inserted in the PDF table header -----------
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		String currentDate = SmsToPdfExporter.DATEFORMATTER.format(calendar.getTime());		
		String pathAndFile = null;
        
		//Check if the user has provided or not the .pdf file extension
        if(filePdfName.endsWith("pdf"))        	
        	pathAndFile = destinationFolder+File.separator+filePdfName;
        else{
        	pathAndFile = destinationFolder+File.separator+filePdfName+".pdf";
        	filePdfName = filePdfName +".pdf";
        }
		
        //--- Step 2: Prepare the PDF document to be filled -----------		
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);      
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathAndFile));
       
        //The fonts type to be used in the document
        Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        
        /*--- Step 3: BEFORE open the document we add some meta information to the document 
         * (that properties can be viewed ONLY with adobe reader or right click-properties)         *  
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
        
		//-------- Core functionality: Extract the content from the JTree
		TreeNode treeRoot = (TreeNode)tree.getModel().getRoot();  		
		DefaultMutableTreeNode currentNode = null;		
		
		Enumeration child = treeRoot.children();	    
		
		while (child.hasMoreElements()) {
			 
			 currentNode = (DefaultMutableTreeNode) child.nextElement();
			
			 //get only the nodes with at least one child
			 if (currentNode.getChildCount() > 0) {
			 
				Enumeration childEnum = currentNode.breadthFirstEnumeration(); 
		       
		        while(childEnum.hasMoreElements())
		        {		           
		        	currentNode = (DefaultMutableTreeNode)childEnum.nextElement();
	            		
	               //true if the current node represents an sms object     
	               if(currentNode.toString().contains("["))
	               {   			
	            	  DefaultMutableTreeNode smsElement = null;	          	   
	            	  
	            	  //add an header
	            	  Phrase smsHeaderPhrase = new Phrase("Message  ", headerFont);           	  
	            	  document.add(smsHeaderPhrase);	            	 	
	            	  
	            	  //add an horizontal line
	                  LineSeparator ls = new LineSeparator(); 
	                  ls.setLineWidth(0);
	                  document.add(new Chunk(ls));  	  
	            	  document.add(Chunk.NEWLINE);	            	 
	            	  
	            	  //System.out.println("------ SMS ------");	            			
	            	  Enumeration smsNodeChildsEnum = currentNode.breadthFirstEnumeration();
	            	  
	            	  while(smsNodeChildsEnum.hasMoreElements())
	    			  {
	            		  smsElement = (DefaultMutableTreeNode)smsNodeChildsEnum.nextElement();
	            		  
	            		  /* Decide if export or not the current element: depends on the choice of the user in the pdf export option pop-up */ 
	            		  if(exportOnlyBaseSmsFieldsFlag)
	            		  {	  
	            			  //System.out.println("Exporting only the base sms fields");
	            			  
	            			  //search only in the left part of the node element
	            		      if(smsBaseNodeNamesToExport.contains(smsElement.toString().split("=")[0].trim())){ 
	            		    	  
			            		  //System.out.println("VAL: "+smsElement);
			            		  document.add(new Chunk(smsElement.toString()));
			            		  document.add(Chunk.NEWLINE);
	            		      }
	            		  
	            		  }else { //export all sms fields
	            			  
	            			  //System.out.println("Exporting All the sms fields");	            			  
	            			  //System.out.println("VAL: "+smsElement);
		            		  document.add(new Chunk(smsElement.toString()));
		            		  document.add(Chunk.NEWLINE);
	            		  }          		  
	            		  
	            	  }	     		 
	            		  document.add(Chunk.NEWLINE);
	            	}
		       }
		  }
		
	 }
		
        
       
        
        // step 5
        /*
         access at the content under the new pdf document just created (ie the writer object that i can control/move)
         PdfContentByte is the object that contains the text to write and the content of a page (it offer the methods to add content to a page)
       
        PdfContentByte canvas = writer.getDirectContentUnder();
         
        //Sets the compression level to be used for streams written by the writer.
        writer.setCompressionLevel(0);
        canvas.saveState();                               
        canvas.beginText();                               
        //move the writer to tha X,Y position
        canvas.moveText(360, 788);                        
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        
        Rectangle r = new Rectangle(400, 300);
        r.setBorder(2);
        document.add(r);
         */
        
        
        /* 
           Writes something to the direct content using a convenience method
           A Phrase is a series of Chunks (A Chunk is the smallest significant part of text that can be added to a document)
           
           Conclusion: A chunk is a String with a certain Font ---> A Phrase is a series of Chunk
           Both Chunck and Font has a Font field (but if a Chunk haven't a Font uses the one of the Phrase that own it)
        */
        
        //Two modes to set a Phrase:
        
		/*
		
        //mode 1) set the phrase directly without a separate chunk object
        Phrase hello = new Phrase("Hello World3");
        document.add(hello);
        
        //mode 2) create before a chunk, adjust it and after assign it to a Phrase(s) 
        Chunk chunk2 = new Chunk("Setting the Font", FontFactory.getFont("dar-black"));
        chunk2.setUnderline(0.5f, -1.5f);
        
        Phrase p1 = new Phrase(chunk2);
        document.add(p1);         
        
        canvas.showText("Hello sms");                   
        canvas.endText();                                 
        canvas.restoreState();           
        
        //i chunk posso aggiungerli solo tramite l'oggetto Document ?
        Chunk chunk = new Chunk("I'm a chunk");
        chunk.setBackground(BaseColor.GRAY, 1f, 0.5f, 1f, 1.5f);
        document.add(chunk);
        
        */        
        
        /*
         * A Paragraph is a series of Chunks and/or Phrases, has the same qualities of a Phrase, but also some additional layout-parameters
        
        Paragraph paragraph = new Paragraph("A:\u00a0");
        Chunk chunk1 = new Chunk("I'm a chunk1");
        paragraph.add(chunk1);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
         */
        
        //-- Add a table to the document
 
		/*
        table.addCell("Loginname:");
        cell = new PdfPCell();
        //cell.setCellEvent(new TextFields(2));
        table.addCell(cell);
 
        table.addCell("Password:");
        cell = new PdfPCell();        
        table.addCell(cell);
 
        table.addCell("Reason:");
        cell = new PdfPCell();        
        cell.setFixedHeight(60);
        table.addCell(cell);        
        */
        
         // step 5
         document.close();
        
        return true;
    }    
	
	public boolean exportToPdf(){
		return false;		
	}

}
