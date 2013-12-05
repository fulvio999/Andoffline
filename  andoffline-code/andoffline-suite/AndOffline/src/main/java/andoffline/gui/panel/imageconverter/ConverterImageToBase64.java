package andoffline.gui.panel.imageconverter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;


public class ConverterImageToBase64 {
	
	//The metadata String to append at the beginning of the produced Base64 file
	public static String METADATA = "PHOTO;ENCODING=BASE64;";
	
	/**
	 * Constructor
	 */
	public ConverterImageToBase64(){
		
	}

	/**
	 * Read the input image, convert it in a byte[] and then encode the bytes in a Base64
	 * If the user has chosen the "Append Metadata" option a metadata string is inserted on the top of the file, so that in
	 * opposite conversion (ie Base64 --> Image) is not necessary remember the original image format (jpg, png...)
	 * @throws IOException If Some error happens during the read/write process
	 * 	 
	 */
	public String convert(String fileImageToConvert, String outputTxtFile) throws IOException {		
		
		// 1) Read the source image to convert
        File file = new File(fileImageToConvert);
        FileInputStream fis = new FileInputStream(file);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);                
            }
        } catch (IOException ex) {
           System.err.println("Error: "+ex.toString());
        }
        
        //The arrary of bytes representing the input image
        byte[] bytes = bos.toByteArray();
 
        // 2) Encode the array of bytes in base64 format. With true parameter create a chunk of 76 char
        String base64String = new String(Base64.encodeBase64(bytes,true)); 
        
        //DEBUG: print the string representing the byte[] converted in a base64 format 
        //System.out.println("Bytes encoded in base 64:\n"+base64String);
        
        // 3) Write the result to output file        
	 	byte[] buffer = base64String.getBytes();        
        File fileTxtOutput = new File(outputTxtFile+".txt");	 	
	 	OutputStream output = new FileOutputStream(fileTxtOutput);
	 	
	 	output.write(buffer);
	 	output.close();

        return "SUCCESS";        
	}
	
	
	 /**
	  * Utility method to extract the extension of the given file
	  * @param fullPath
	  * @return The extension of the file or null if any
	  */
	 private String getFileExtension(String fullPath) 
	 {
		 try {
			 int dot = fullPath.lastIndexOf(System.getProperty("file.separator"));
			 String file = fullPath.substring(dot + 1); 
			 
			 return file.split("\\.")[1];
			 
		 }catch (Exception e) {
			return null; //the file has no extension
		}
	 }

}
