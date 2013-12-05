package andoffline.gui.panel.imageconverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64InputStream;


/**
 * Read a text or vcf file containing only Base64 String and decode them to an image file (the output format in decided in auto by the Decoder)
 * so that is not necessary specify an output format (eg png, jpg...)      
 *
 */
public class ConverterBase64ToImage {
	
	/**
	 * Constructor
	 */
	public ConverterBase64ToImage(){
		
	}

	/**
	 * Read a text file containing a Base64 data and decode them to an image file (the output format in decided in auto by the Decoder)
	 * 
	 * @param fileBase64ToConvert The input file with only Base64 data
	 * @param outputFileImage The image type to produce. Necessary to know the file extension to use
	 * @return the image format written (ie the file extension, eg, jpg, png...)
	 * 
	 * @throws IOException 
	 */
	public String convert(String fileBase64ToConvert, String outputFileImage) throws IOException {
		
		// Read the source file with Base64 data (that file don't have any empty row)
		File file64Input = new File(fileBase64ToConvert);		 
		
	    FileInputStream file64InputStream = new FileInputStream(file64Input);	     
	    
	    int BUFFER_SIZE = 4096;
	 	byte[] buffer = new byte[BUFFER_SIZE];
	 	
	 	Base64InputStream input = new Base64InputStream(file64InputStream);
	 	
		File fileImageOutput = new File(outputFileImage);
		 	
		OutputStream output = new FileOutputStream(fileImageOutput);
		int n = input.read(buffer, 0, BUFFER_SIZE);
		
		while (n >= 0) {
		 	output.write(buffer, 0, n);
		 	n = input.read(buffer, 0, BUFFER_SIZE);
		}
		
		output.flush();	
		
		input.close();
		output.close();
		
		/* Obtain the image type of the produced one, so that is possible rename the generated file		
		   this is necessary because the input Base64 don't have informations about the source image converted
		*/		
	    String formatName = getImageFormatName(fileImageOutput);    
	  
	    //DEBUG
	    //printSupportedImageFormat();
	    
	    if(formatName.equalsIgnoreCase("JPEG") || formatName.equalsIgnoreCase("JPG"))
	    	formatName = "jpg";
	    
	    //add the right extension at the written file
	    fileImageOutput.renameTo(new File(outputFileImage+"."+formatName));
	 	
        return formatName;
	 	
	}
	
	 /**
	  * Utility method that print to stdout the supported image format supported by the running JRE
	  * (ie the supported extension: 
	  *  bmp,jpg,jpeg,wbmp,png,gif 
	  * )
	  * @param fullPath
	  * @return The extension of the file or null if any
	  */
	 private void printSupportedImageFormat() 
	 { 
		 String[] format = ImageIO.getReaderFileSuffixes();
		
	       for(int i=0; i<format.length;i++)
	    	   System.out.println("Format Supported: "+format[i]);
	 }
	
	 
	 /** 
	  * Utility method that return the image format (jpe, png,...) of the input File 
	  * return null if the input file has an unknown format
	  * 
	  * @param file The File object to detect the format
	  * @return A string that represents the image format of the file (eg JPEG) 
	  * */
 	 private String getImageFormatName(Object file) {
 		
 	    try {
 	        // Create an image input stream on the image
 	        ImageInputStream iis = ImageIO.createImageInputStream(file);

 	        // Find all image readers that recognize the image format
 	        Iterator iter = ImageIO.getImageReaders(iis);
 	        
 	        if (!iter.hasNext()) {
 	            // No readers found
 	            return null;
 	        }

 	        // Use the first reader
 	        ImageReader reader = (ImageReader)iter.next();
 	       
 	        iis.close();

 	        // Return the format name
 	        return reader.getFormatName();
 	        
 	    } catch (IOException e) {
 	    	return e.getMessage();
 	    }
 	    
 	}


}
