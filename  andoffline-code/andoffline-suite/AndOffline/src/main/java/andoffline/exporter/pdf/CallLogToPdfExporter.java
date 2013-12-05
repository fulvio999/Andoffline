
package andoffline.exporter.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

/**
 * Export the Log Call to a PDF file
 *
 */
public class CallLogToPdfExporter {

	/**
	 * Constructor
	 */
	public CallLogToPdfExporter() {
		
	}
	
	/**
     * Creates a PDF file using the LOG CALL showed in the JTree in argument
     * @param args no arguments needed
     */
	public boolean exportLogCall(String filePdfName, String destinationFolder, JTree tree) throws DocumentException, IOException{
		
		//--- Step 1: Prepare the contents to be inserted in the PDF table header -----------
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		String currentDate = SmsToPdfExporter.DATEFORMATTER.format(calendar.getTime());		
		String pathAndFile = null;
        
		//Check if the user has provided or not the file extension
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
        
        // add an header table to the document        
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
        
		//----- Core functionality: Extract the content from the JTree
		TreeNode treeRoot = (TreeNode)tree.getModel().getRoot();  
		
		DefaultMutableTreeNode currentNode = null;		
		
		Enumeration child = treeRoot.children();	    
		
		while (child.hasMoreElements()) {
			 
			 currentNode = (DefaultMutableTreeNode) child.nextElement();
			
			 //get only the nodes with at least one child
			 if (currentNode.getChildCount() > 0) {
			 
				Enumeration childEnum = currentNode.breadthFirstEnumeration(); 
				 
		        //iterate through the enumeration
		        while(childEnum.hasMoreElements())
		        {		            
		        	currentNode = (DefaultMutableTreeNode)childEnum.nextElement();
	            		
	               //true if the current node represents an sms object     
	               if(currentNode.toString().contains("["))
	               {   			
	            	  DefaultMutableTreeNode callElement = null;	          	   
	            	  
	            	  //add an header
	            	  //Phrase callHeaderPhrase = new Phrase("CALL HEADER ", headerFont);           	  
	            	  //document.add(callHeaderPhrase);	            	 	
	            	  
	            	  //add an horizontal line
	                  LineSeparator ls = new LineSeparator(); 
	                  ls.setLineWidth(0);
	                  document.add(new Chunk(ls));	  	  
	            	  document.add(Chunk.NEWLINE);	            	 
	            	  
	            	  //System.out.println("------ CALL ------");	            			
	            	  Enumeration callNodeChildsEnum = currentNode.breadthFirstEnumeration();
	            	  
	            	  while(callNodeChildsEnum.hasMoreElements())
	    			  {
	            		  callElement = (DefaultMutableTreeNode)callNodeChildsEnum.nextElement();
	            			  
	            	     //NOTE: a call node doesn't have technical fields, so that is not necessary apply a filter
		            	 document.add(new Chunk(callElement.toString()));
		            	 document.add(Chunk.NEWLINE);
	            		  
	            	  }	            			
	            		  //System.out.println("----------------");
	            		  document.add(Chunk.NEWLINE);
	            	}
		      }
		   }
		
		}
		
		// step 5
        document.close();
       
       return true;
		
	}

}
