package andoffline.parser.call;

import java.util.Date;
import java.util.Hashtable;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



/*
 * Extends the default SaxParser Event Handler, During the reading of the file the SaxParser call the methods of this handler.
 * This Custom Parser during the parsing convert some technical parameters name into a more simple one. For that purpose use an HashTable
 * to convert the technical name into his easier one for the user
 * 
 * type = 2 means call made (sent)
 * type = 1 means call received
 * type = 3 means lost call
 * 
 */
public class CustomCallFilterParser extends DefaultHandler{

	//The current node which appends the others ones
	private DefaultMutableTreeNode base;
	private JTree xmlJTree;
	
	//The counter to add a sequence number at each call node (received and made or lost)
	private int callNodeCounter;	
	
	//The counter for the calls containing the search String 
	private int callFoundCounter;	
	
	private boolean appendNodeFlag;
	
	public static Hashtable<String, String> conversionNameTable = new Hashtable<String, String>();
	
	//The string inserted by the user to be searched in the xml nodes
    private String stringTosearch;
	
	/* 
	 * Constructor: initialize the counter and create the conversion label table
	 *  */
	public CustomCallFilterParser(DefaultMutableTreeNode b, JTree tree, String search) {
		
		this.base = b; //the first base of the tree, after the new created nodes will become the new tree base
		this.xmlJTree = tree;
		this.callNodeCounter = 1;
		
		this.stringTosearch = search.toLowerCase();
		
		callFoundCounter = 0;
		
		//-- Fill the conversion table with original values (as key) and translated values (as value)
		conversionNameTable.put("count", "Total Call found");
		conversionNameTable.put("number", "Tel. Number");
		conversionNameTable.put("duration", "Duration (Sec.)");
		conversionNameTable.put("date", "Date of the call (dd/mm/yyyy)");
		conversionNameTable.put("type", "Type");
		
	}
	
	/**
	 * Called when the parser find a start xml tag
	 */
	public void startElement(String uri, String localName, String tagName, Attributes attr) throws SAXException {

		//The current node of the xml file
		DefaultMutableTreeNode currentNode = null;
		
		// The current attribute of the currentNode node
		DefaultMutableTreeNode currentAtt = null;		
		
		//Append a counter suffix at all the <call> tags to enumerate them
		if(tagName.equalsIgnoreCase("call"))
		{
			currentNode = new DefaultMutableTreeNode(tagName+" [ "+callNodeCounter+" ]");			
			callNodeCounter++;
			
		} else {
			currentNode = new DefaultMutableTreeNode(tagName);
		}
		
		//default
		appendNodeFlag = false;
			
		if(tagName.equalsIgnoreCase("calls"))
		{
			base.add(currentNode);		
			//The new base (ie the parent) become the current node
			base = currentNode;					
		}
			
		/*
		 *  Before append the node at the tree decide if it must be showed: only the nodes with the searched String will be appended
		 *  convert all in LowerCase to have a case insensitive search
		 */				
		for (int j = 0; j < attr.getLength(); j++) {
				
			String attrValue = attr.getValue(j).toLowerCase();
				
		    if((attrValue.toLowerCase()).indexOf(stringTosearch) !=-1){	   
		        appendNodeFlag = true;
		        break;
			}
		}
		
		if(appendNodeFlag) //true if the node must be attached to the tree
		{
			//increments the counter for the sms containing the searched string
			callFoundCounter ++;
			
			base.add(currentNode);		
			//The new base (ie the parent) become the current node
			base = currentNode;
			
			//Inspect the attribute of the current node and append them to the tree node
			for (int i = 0; i < attr.getLength(); i++) 
			{				
				String labelToDisplay = conversionNameTable.get(attr.getLocalName(i).trim());		
				
				//To prevent missing parameters in the table converter
				if(labelToDisplay == null)
					labelToDisplay = "Unknown Param";
				
				//If the field is "date" convert it from epoch to human readable format
				if(attr.getLocalName(i).equalsIgnoreCase("date"))
				{								
				    //Before append the attributes of a tags, format (eg convert the sms date from epoch to human readable format)
					long epoch = Long.parseLong(attr.getValue(i));
				    String smsDateConverted = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(epoch));			   		    
				    
				    currentAtt = new DefaultMutableTreeNode(labelToDisplay+" = "+ smsDateConverted);
					base.add(currentAtt);
				
				 //Convert the "type" attributes ie: type=2 --> sent (made); type=1 --> received; type=3 --> lost
				}else if(attr.getLocalName(i).equalsIgnoreCase("type")){				
									
						if(attr.getValue(i).equalsIgnoreCase("1")){
							currentAtt = new DefaultMutableTreeNode(labelToDisplay +"= Received");
							base.add(currentAtt);							
							
						}else if(attr.getValue(i).equalsIgnoreCase("2")){
							currentAtt = new DefaultMutableTreeNode(labelToDisplay +"= Made");
							base.add(currentAtt);							
						
						}else if(attr.getValue(i).equalsIgnoreCase("3")){
							currentAtt = new DefaultMutableTreeNode(labelToDisplay +"= Lost");
							base.add(currentAtt);				
							
						}else { //unknown value
							currentAtt = new DefaultMutableTreeNode(labelToDisplay + " = "+ attr.getValue(i));
							base.add(currentAtt);
						}
					
				}else if(attr.getLocalName(i).equalsIgnoreCase("number")){
					currentAtt = new DefaultMutableTreeNode(labelToDisplay + " = "+ attr.getValue(i));
					base.add(currentAtt);
					
				}else if(attr.getLocalName(i).equalsIgnoreCase("duration")){
					currentAtt = new DefaultMutableTreeNode(labelToDisplay + " = "+ attr.getValue(i));
					base.add(currentAtt);
					
				}else {
					currentAtt = new DefaultMutableTreeNode(labelToDisplay + " = "+ attr.getValue(i));					
					base.add(currentAtt);
				}
				
			}	
				
		}
  }

	public void skippedEntity(String name) throws SAXException {
		
	}

	public void startDocument() throws SAXException {		
		 super.startDocument();
		 base = new DefaultMutableTreeNode("XML Viewer");		
		((DefaultTreeModel) xmlJTree.getModel()).setRoot(base);

	}

	public void characters(char[] ch, int start, int length) throws SAXException {

		String s = new String(ch, start, length).trim();
		
		if (!s.equals("")) {			
			DefaultMutableTreeNode current = new DefaultMutableTreeNode("Description : " + s);
			base.add(current);
		}
	}

	/**
	 * Called when the parser find a xml close tag
	 */
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		
		//only if the node type match with the filter will be attached the end element of the node
		if(appendNodeFlag) 
			base = (DefaultMutableTreeNode) base.getParent();
	}

	
	/**
	 * Called when end of the xml file is reached
	 */
	public void endDocument() throws SAXException {
		
		//Append at the tree two new nodes with the total amount of made and received call
		DefaultTreeModel model = (DefaultTreeModel)xmlJTree.getModel();
		
		MutableTreeNode root = (MutableTreeNode) model.getRoot();
		MutableTreeNode callFoundCounterNode = new DefaultMutableTreeNode("Total call found = "+callFoundCounter);
		
		//Check if the JTree has some children. If no nodes contains the searched String the JTree has no child
		if(root.getChildCount() != 0){	
			
			//the tree node with the amount (made/received/lost)
			MutableTreeNode tn =(MutableTreeNode)root.getChildAt(0);
			tn.insert(callFoundCounterNode, 0);
			
			//refresh the tree and expand only the second row with the sms count
			((DefaultTreeModel) xmlJTree.getModel()).reload();		
			xmlJTree.expandRow(1);
		}
		
	}


	public boolean isAppendNodeFlag() {
		return appendNodeFlag;
	}


	public void setAppendNodeFlag(boolean appendNodeFlag) {
		this.appendNodeFlag = appendNodeFlag;
	}


	public int getCallNodeCounter() {
		return callNodeCounter;
	}


	public void setCallNodeCounter(int callNodeCounter) {
		this.callNodeCounter = callNodeCounter;
	}

	public String getStringTosearch() {
		return stringTosearch;
	}

	public void setStringTosearch(String stringTosearch) {
		this.stringTosearch = stringTosearch;
	}

	public int getCallFoundCounter() {
		return callFoundCounter;
	}

	public void setCallFoundCounter(int callFoundCounter) {
		this.callFoundCounter = callFoundCounter;
	}



}
