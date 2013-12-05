package andoffline.web.decorator;

import org.apache.commons.beanutils.BasicDynaBean;
import org.displaytag.decorator.TableDecorator;

/**
 * Displaytag Table Decorator that render a radio button in a column of the JOB table
 *
 */
public class JobTableDecorator extends TableDecorator{

	/**
	 * Constructor
	 */
	public JobTableDecorator() {
		
	}
	
	/**
	 * Create a special cell in the table containing a checkbox that when clicked load the element associated with the selected JOB
	 * Note the pattern name, must be getXXXX where XXX id the value of the attribute "property" in the <display:column>
	 * 
	 */
	public String getDecoratorJobId()
    {	
		// The object represented by the current line to be decorated
		BasicDynaBean mb = (BasicDynaBean)getCurrentRowObject(); 
		
		return "<input type=\"radio\" name=\"jobId\" value=\""+ mb.get("id")+"\" id=\"jobId\" onclick=\"hideDetailsBlock('editjobDiv')\" />";
    }
	

}

