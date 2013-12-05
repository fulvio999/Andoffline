
package andoffline.exporter.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import andoffline.integration.database.dto.JobBean;

import com.itextpdf.text.BaseColor;
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

/**
 * Export the JOB(s) currently showed in a JTable to a PDF file
 *
 */
public class JobToPdfExporter {
	
	private static final Logger log = Logger.getLogger(JobToPdfExporter.class);

	/**
	 * Constructor
	 */
	public JobToPdfExporter() {
		
	}	
	
	/**
     * Creates a PDF containing the JOB showed in the list in argument
     * @param filePdfName The pdf file name
     * @param destinationFolder The destination folder where place the generated pdf file
     * @param jobBeanList The list of {@link JobBean} to export
     *
     */
	public boolean exportJob(String filePdfName, String destinationFolder, ArrayList<JobBean> jobBeanList) throws DocumentException, IOException{
		
		log.info("Exporting "+jobBeanList.size()+" jobs to PDF file: "+filePdfName+" in the folder: "+destinationFolder);
		
		//--- Step 1: Prepare the contents to be inserted in the PDF table header
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		String currentDate = SmsToPdfExporter.DATEFORMATTER.format(calendar.getTime());		
		String pathAndFile = null;
        
		// check if the user has provided or not the .pdf file extension at 'filePdfName' param
        if(filePdfName.endsWith("pdf"))        	
        	pathAndFile = destinationFolder+File.separator+filePdfName;
        else{
        	pathAndFile = destinationFolder+File.separator+filePdfName+".pdf";
        	filePdfName = filePdfName +".pdf";
        }
		
        //--- Step 2: Prepare the PDF document to be filled		
        Document document = new Document(PageSize.A4_LANDSCAPE.rotate(), 10, 10, 10, 10);      
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathAndFile));
       
        //The fonts type to be used in the document
        Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        
        /*--- Step 3: BEFORE open the document we add some meta information to the document 
         * (that properties can be viewed ONLY with Adobee reader or right click-properties) 
         */
        document.addAuthor("AndOffline");    
        document.open();
        
        //--- Step 4  
        //The paragraph where add the table header (ie a Paragraph is a section in the document that contains a group of elements)
        Paragraph headerParagraph = new Paragraph();       
        
        // Add an header table to the document        
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
        
        //---- Add a SMS command syntax summary -----
        Paragraph commandSyntaxParagraph = new Paragraph();  
        
        commandSyntaxParagraph.add(new Chunk("SMS command syntax summary: \n"));
        commandSyntaxParagraph.add(new Chunk("To execute a job:  execute job <id>   (Example: execute job 1) \n"));        
        commandSyntaxParagraph.add(new Chunk("To start server: execute server start \n"));        
        commandSyntaxParagraph.add(new Chunk("To stop server: execute server stop \n"));
              
        
        document.add(commandSyntaxParagraph);
        document.add(Chunk.NEWLINE); 
        
		//---- Core functionality: Extract the content from the job List	
        Paragraph jobsTableParagraph = new Paragraph();  
        float[] columnWidths = {10,50,50,50,50,50,50};
        
        PdfPTable jobsTable = new PdfPTable(7);
        jobsTable.setWidthPercentage(100);
        jobsTable.setWidths(columnWidths);
        
        /* prepare the table column name */
        PdfPCell idCell = new PdfPCell(new Paragraph("Id"));        
        PdfPCell nameCell = new PdfPCell(new Paragraph("Name"));
        PdfPCell interpreterCell = new PdfPCell(new Paragraph("Interpreter"));
        PdfPCell interpreterArgumentCell = new PdfPCell(new Paragraph("Interpreter Arguments"));
        PdfPCell scriptCell = new PdfPCell(new Paragraph("Script"));
        PdfPCell scriptArgumentCell = new PdfPCell(new Paragraph("Script Arguments"));
        PdfPCell descriptionCell = new PdfPCell(new Paragraph("Description"));
        
        /* fields 'lastExecutionDate' and 'executorMisisdn' are not exported because not necessary  */
        
        idCell.setBackgroundColor(BaseColor.YELLOW);
        nameCell.setBackgroundColor(BaseColor.YELLOW);
        interpreterCell.setBackgroundColor(BaseColor.YELLOW);
        interpreterArgumentCell.setBackgroundColor(BaseColor.YELLOW);
        scriptCell.setBackgroundColor(BaseColor.YELLOW);
        scriptArgumentCell.setBackgroundColor(BaseColor.YELLOW);
        descriptionCell.setBackgroundColor(BaseColor.YELLOW);          
        
        jobsTable.addCell(idCell);
        jobsTable.addCell(nameCell);
        jobsTable.addCell(interpreterCell);
        jobsTable.addCell(interpreterArgumentCell);
        jobsTable.addCell(scriptCell);
        jobsTable.addCell(scriptArgumentCell);
        jobsTable.addCell(descriptionCell);             
        
        for(int i=0; i<jobBeanList.size(); i++)
		{	
        	PdfPCell idCellValue = new PdfPCell(new Paragraph(jobBeanList.get(i).getId()));
        	PdfPCell nameCellValue = new PdfPCell(new Paragraph(jobBeanList.get(i).getName()));
        	PdfPCell interpreterCellValue = new PdfPCell(new Paragraph(jobBeanList.get(i).getInterpreter()));
        	PdfPCell interpreterArgumentCellValue = new PdfPCell(new Paragraph(jobBeanList.get(i).getInterpreterArgument()));
        	PdfPCell scriptCellValue = new PdfPCell(new Paragraph(jobBeanList.get(i).getScript()));
        	PdfPCell scriptArgumentCellValue = new PdfPCell(new Paragraph(jobBeanList.get(i).getScriptArgument()));
        	PdfPCell descriptionCellValue = new PdfPCell(new Paragraph(jobBeanList.get(i).getDescription()));
        	
        	jobsTable.addCell(idCellValue);
        	jobsTable.addCell(nameCellValue);
        	jobsTable.addCell(interpreterCellValue);
        	jobsTable.addCell(interpreterArgumentCellValue);
        	jobsTable.addCell(scriptCellValue);
        	jobsTable.addCell(scriptArgumentCellValue);
        	jobsTable.addCell(descriptionCellValue);        	
		}
        
        jobsTableParagraph.add(jobsTable);
        document.add(jobsTableParagraph);
        
        //--- Step 5
        document.close();
        
        return true;
	}
}
