/**
 * 
 */
package andoffline.utility.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * File Filter for JFileChooser that allow the selection of folder only.
 * Used when the user must choose the folder where store Sqlite database file. 
 *
 */
public class FolderFileFilter extends FileFilter{

	/**
	 * Constructor
	 */
	public FolderFileFilter() {
		
	}

	
	@Override
	public boolean accept(File f) {
		return f.isDirectory();		
	}

	@Override
	public String getDescription() {
		return "Folder only";
	}
	

}
