package andoffline.utility.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Custom file filter used in the JFileChooser. Used to pick-up only .VCF or .TXT file
 * containing Images encoded in Base64
 *
 */
public class TextAndVcfFileFilter extends FileFilter{
	
	public TextAndVcfFileFilter() {
		
	}

	@Override
	public boolean accept(File f) {
		return f.getName().toLowerCase().endsWith(".txt") || f.getName().toLowerCase().endsWith(".vcf");
		
	}

	@Override
	public String getDescription() {
		return "TXT or VCF files only";
	}

}
