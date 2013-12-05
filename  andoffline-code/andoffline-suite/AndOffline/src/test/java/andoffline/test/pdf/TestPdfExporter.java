
package andoffline.test.pdf;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

/**
 * Export the SMS to a PDF file
 *
 */
public class TestPdfExporter {
	
	//The pdf filename to write
	private String filePdfName;
	
	//The folder where write the pdf file
	private String destinationFolder;
	
	 /** Path to the resulting PDF file. */
    public static final String RESULT = "/home/fulvio/sms.pdf";

	/**
	 * Constructor
	 */
	public TestPdfExporter(String fileNamne, String destFolder) {
		this.filePdfName = fileNamne;
		this.destinationFolder = destFolder;
		
	}
	
	
    
    /**
     * Creates a PDF file: sms.pdf
     * @param    args    no arguments needed
     */
    public static void main(String[] args) throws DocumentException, IOException {
    	
//        // step 1
//        Document document = new Document();
        
        // step 1: creation of the document with a certain size and certain margins
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        
        // step 2: create a  writer (we have many type of writer, eg HtmlWriter, PdfWriter)
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
       
        /* step 3: BEFORE open the document we add some meta information to the document (that properties can be viewed with adobe reader or right click-properties)
         * they don't appear in the document view
        */
        document.addAuthor("Author Test"); 
        document.addSubject("This is the result of a Test."); 
        
        // step 4
        document.open();
        
        //The com.itextpdf.text.Image is used to add images to IText PDF documents
        Image image1 = Image.getInstance("src/main/resources/sms.png");
        document.add(image1);
        
        // step 5
        /*
         access at the content under the new pdf document just created (ie the writer object that i can control/move)
         PdfContentByte is the object that contains the text to write and the content of a page (it offer the methods to add content to a page)
        */
        PdfContentByte canvas = writer.getDirectContentUnder();
         
        //Sets the compression level to be used for streams written by the writer.
        writer.setCompressionLevel(0);
        canvas.saveState();                               
        canvas.beginText();                               
        //move the writer to tha X,Y position
        canvas.moveText(360, 788);                        
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        
        Rectangle rectangle = new Rectangle(400, 300);
        rectangle.setBorder(2);
        document.add(rectangle);
        
        /* 
           Writes something to the direct content using a convenience method
           A Phrase is a series of Chunks (A Chunk is the smallest significant part of text that can be added to a document)
           
           Conclusion: A chunk is a String with a certain Font ---> A Phrase is a series of Chunk
           Both Chunck and Font has a Font field (but if a Chunk haven't a Font uses the one of the Phrase that own it)
        */
        
        //------- Two modes to set a Phrase: --------
        
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
        
        document.add(Chunk.NEWLINE);
        
        //i chunk posso aggiungerli solo tramite l'oggetto Document ?
        Chunk chunk = new Chunk("I'm a chunk");
        chunk.setBackground(BaseColor.GRAY, 1f, 0.5f, 1f, 1.5f);
        document.add(chunk);
        
        /*
         * A Paragraph is a series of Chunks and/or Phrases, has the same qualities of a Phrase, but also some additional layout-parameters
         * A paragraph is a sub-section in the document. After each Paragraph a CRLF is added
         */
        Paragraph paragraph = new Paragraph("A:\u00a0");
        Chunk chunk1 = new Chunk("I'm a chunk1");
        paragraph.add(chunk1);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        
        
        //----- Add a table to the document ------
        
        //A cell in a PdfPTable
        PdfPCell cell;
        
        PdfPTable table = new PdfPTable(2); //in argument vis the number of column
        table.setWidths(new int[]{ 1, 2 }); //the width of the first and second cell. The number of element in the array must be equal at the number of column
 
        table.addCell("Name:");
        cell = new PdfPCell();
        //We can attach event at the cell
        //cell.setCellEvent(new TextFields(1));
        table.addCell(cell);
 
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
 
        document.add(table);
        
        //add an horizontal line
        LineSeparator ls = new LineSeparator(); 
        ls.setLineWidth(0);
        document.add(new Chunk(ls));
        
        Anchor pdfRef = new Anchor("http://www.java2s.com");
        document.add(pdfRef);
        
        // step 5
        document.close();
    }
	
	public boolean exportToPdf(){
		return false;
		
	}

	public String getFilePdfName() {
		return filePdfName;
	}

	public void setFilePdfName(String filePdfName) {
		this.filePdfName = filePdfName;
	}

	public String getDestinationFolder() {
		return destinationFolder;
	}

	public void setDestinationFolder(String destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

}
