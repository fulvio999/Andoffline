package andoffline.parser.sms;

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
 * Extends the default SaxParser Event Handler, During the reading of the xml file the SaxParser call the methods of this handler.
 * This Custom Parser during the parsing convert some technical parameters name into a more simple one. For that purpose use an HashTable
 * to convert the technical name into his easier one for the user
 * 
 * It read all the xml node and append it in the JTree ONLY the Received SMS
 * 
 */
public class CustomSmsFilterParser extends DefaultHandler{

	//The current node which appends the others ones
	private DefaultMutableTreeNode base;
	private JTree xmlJTree;
	
	//The counter to add a sequence number at each sms node (received and sent)
	private int smsNodeCounter;	
	
	//The counter for the sms containing the search String
	private int smsFoundCounter;
	
	private boolean appendNodeFlag;
	
	//The string inserted by the user to be searched in the xml nodes
    private String stringTosearch;
	
	public static Hashtable<String, String> conversionNameTable = new Hashtable<String, String>();	
	
	public CustomSmsFilterParser(DefaultMutableTreeNode b, JTree tree, String search) {
		
		this.base = b; //the first base of the tree, after the new created nodes will become the new tree base
		this.xmlJTree = tree;
		this.smsNodeCounter = 1;
		
		this.stringTosearch = search.toLowerCase();
		
		smsFoundCounter = 0;
		
		//-- Fill the label conversion table with original and translated values
		conversionNameTable.put("count", "Total SMS found");
		conversionNameTable.put("protocol", "Protocol");
		conversionNameTable.put("address", "Phone Number (Destination/Receiver)");
		conversionNameTable.put("date", "Date of the sms (dd/mm/yyyy)");
		conversionNameTable.put("type", "Type");
		conversionNameTable.put("subject", "Subjet of sms (null for sms)");
		conversionNameTable.put("body", "SMS text");
		conversionNameTable.put("toa", "TOA");
		conversionNameTable.put("sc_toa", "SC_TOA");
		conversionNameTable.put("service_center", "Service Center (only for received sms)");
		conversionNameTable.put("read", "SMS read");
		conversionNameTable.put("status", "SMS status");
		conversionNameTable.put("readable_date", "SMS date");
		conversionNameTable.put("contact_name", "Contact Name");
		conversionNameTable.put("locked", "Locked");  //?????
		
	}
	
	/**
	 * Called when the parser encounter a start tag
	 */
	public void startElement(String uri, String localName, String tagName, Attributes attr) throws SAXException {

		//The current node of the xml file
		DefaultMutableTreeNode currentNode = null;
		
		// The current attribute of the currentNode node
		DefaultMutableTreeNode currentAtt = null;		
		
		//Append a counter suffix at all the <sms> tags to enumerate them
		if(tagName.equalsIgnoreCase("sms"))
		{
			currentNode = new DefaultMutableTreeNode(tagName+" [ "+smsNodeCounter+" ]");			
			smsNodeCounter++;
			
		} else {
			currentNode = new DefaultMutableTreeNode(tagName);
		}
		
		//default
		appendNodeFlag = false;
		
		//the <smes> node must always attached at the tree, but in case of custom search filter must showed only the matching node, not the full total
		if(tagName.equalsIgnoreCase("smses")) 
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
			smsFoundCounter ++;
			
			base.add(currentNode);		
			//The new base (ie the parent) become the current node
			base = currentNode;
			
			//Inspect the attribute of the current node and append them to the tree node
			for (int i = 0; i < attr.getLength(); i++) 
			{					
				//Convert the attribute name into a human readable label				
				String labelToDisplay = conversionNameTable.get(attr.getLocalName(i).trim());		
				
				//To prevent missing parameters in the table converter
				if(labelToDisplay == null)
					labelToDisplay = "Unknown Param";
				
				//If the field name is "date", convert it from epoch to human readable format
				if(attr.getLocalName(i).equalsIgnoreCase("date"))
				{								
				    //Before append the attributes of a tags, format (eg convert the sms date from epoch to human readable format)
					long epoch = Long.parseLong(attr.getValue(i));
				    String smsDateConverted = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(epoch));			   		    
				    
				    currentAtt = new DefaultMutableTreeNode(labelToDisplay+" = "+ smsDateConverted);
					base.add(currentAtt);
				
				 //Convert the "type" attribute: type=2 --> sent; type=1 --> received	
				}else if(attr.getLocalName(i).equalsIgnoreCase("type")){				
									
						if(attr.getValue(i).equalsIgnoreCase("1")){
							currentAtt = new DefaultMutableTreeNode(labelToDisplay +"= Received");
							base.add(currentAtt);							
							
						}else { //unknown value
							currentAtt = new DefaultMutableTreeNode(labelToDisplay + " = "+ attr.getValue(i));
							base.add(currentAtt);
						}
					
				}
				else {
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
			DefaultMutableTreeNode current = new DefaultMutableTreeNode("Descrioption : " + s);
			base.add(current);
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		
		//only if the node type match with the filter will be attached the end element of the node
		if(appendNodeFlag) 
			base = (DefaultMutableTreeNode) base.getParent();
	}

	
	/* 
	 * Called when end of the xml file is reached 
	 * */
	public void endDocument() throws SAXException {
		
		//Append at the tree two new nodes with the total amount of sent and received sms
		DefaultTreeModel model = (DefaultTreeModel)xmlJTree.getModel();
		
		MutableTreeNode root = (MutableTreeNode) model.getRoot();
		
		MutableTreeNode smsFoundCounterNode = new DefaultMutableTreeNode("Total SMS found = "+smsFoundCounter);
		
		//Check if the JTree has some children. If no nodes contains the searched String the JTree has no child
		if(root.getChildCount() != 0){
			
			//the tree node with the amount (sent/received/sent)
			MutableTreeNode tn =(MutableTreeNode)root.getChildAt(0);
		
			tn.insert(smsFoundCounterNode, 0);
			
			//refresh the tree and expand only the second row with the sms count
			((DefaultTreeModel) xmlJTree.getModel()).reload();		
			xmlJTree.expandRow(1);
		}
		
	}

	public int getSmsNodeCounter() {
		return smsNodeCounter;
	}


	public void setSmsNodeCounter(int smsNodeCounter) {
		this.smsNodeCounter = smsNodeCounter;
	}


	public boolean isAppendNodeFlag() {
		return appendNodeFlag;
	}


	public void setAppendNodeFlag(boolean appendNodeFlag) {
		this.appendNodeFlag = appendNodeFlag;
	}


	public String getStringTosearch() {
		return stringTosearch;
	}

	public int getSmsFoundCounter() {
		return smsFoundCounter;
	}

	public void setSmsFoundCounter(int smsFoundCounter) {
		this.smsFoundCounter = smsFoundCounter;
	}

	public void setStringTosearch(String stringTosearch) {
		this.stringTosearch = stringTosearch;
	}
	

}
