package andoffline.utility.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Custom file filter used in the JFileChooser. Used to pick-up only image file (eg jpg, png...)
 *
 */
public class ImageFileFilter extends FileFilter{

	/**
	 * Constructor
	 */
	public ImageFileFilter() {
		
	}
	
	@Override
	public boolean accept(File f) {
		return f.isFile();
		
	}

	@Override
	public String getDescription() {
		return "Images files only";
	}

}
