package andoffline.test.swing;
/*  
* This example is from javareference.com  
* for more information visit,  
* http://www.javareference.com  
*/ 

import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 
import javax.swing.tree.*; 
import java.util.Enumeration;

/** 
 * This class creates a editable JTree and allows adding 
 * new nodes to it, which are immediately made available for 
 * editing after adding, deleting nodes, searching nodes by string,
 * and deleting nodes by string. 
 *  
 * @author Rahul Sapkal(rahul@javareference.com) 
 */ 
public class EditableTree extends JFrame implements ActionListener 
{  
    private JTree m_tree; 
    private DefaultTreeModel m_model; 
    private JButton m_addButton; 
    private JButton m_delButton;
    private JButton m_searchButton;
    private JButton m_searchAndDeleteButton;
    private JTextField m_searchText;
    
    private DefaultMutableTreeNode m_rootNode;

    /** 
     * Constructor 
     */      
    public EditableTree()  
    {  
        setTitle("Add/Delete/Search node from JTree Demo"); 
         
        // constructing tree
        TreeNode m_rootNode = buildDummyTree(); 
        m_model = new DefaultTreeModel(m_rootNode); 

        m_tree = new JTree(m_model); 
        m_tree.setEditable(true); 
        m_tree.setSelectionRow(0); 
         
        JScrollPane scrollPane = new JScrollPane(m_tree); 
        getContentPane().add(scrollPane, BorderLayout.CENTER); 
         
        JPanel panel = new JPanel(); 
        m_addButton = new JButton("Add Node"); 
        m_addButton.addActionListener(this); 
        panel.add(m_addButton); 
         
        m_delButton = new JButton("Delete Node"); 
        m_delButton.addActionListener(this); 
        panel.add(m_delButton); 
                        
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createEtchedBorder());
                                        
        m_searchText = new JTextField(10);
        searchPanel.add(m_searchText);
                                                
        m_searchButton = new JButton("Search Node"); 
        m_searchButton.addActionListener(this); 
        searchPanel.add(m_searchButton); 
        
        m_searchAndDeleteButton = new JButton("Search and Delete Node"); 
        m_searchAndDeleteButton.addActionListener(this); 
        searchPanel.add(m_searchAndDeleteButton); 
        
        panel.add(searchPanel);
        
        getContentPane().add(panel, BorderLayout.SOUTH);
         
        setSize(700, 400); 
        setVisible(true); 
    } 
     
    /** 
     * This method builds a dummy tree and returns the root node 
     * @return root node 
     */ 
    public TreeNode buildDummyTree()  
    { 
        m_rootNode = new DefaultMutableTreeNode("JavaReference"); 
        DefaultMutableTreeNode forums = new DefaultMutableTreeNode("Forum"); 
        forums.add(new DefaultMutableTreeNode("Thread 1")); 
        forums.add(new DefaultMutableTreeNode("Thread 2")); 
        forums.add(new DefaultMutableTreeNode("Thread 3")); 
        DefaultMutableTreeNode articles = new DefaultMutableTreeNode("Articles"); 
        articles.add(new DefaultMutableTreeNode("Article 1")); 
        articles.add(new DefaultMutableTreeNode("Article 2")); 
        DefaultMutableTreeNode examples = new DefaultMutableTreeNode("Examples"); 
        examples.add(new DefaultMutableTreeNode("Examples 1")); 
        examples.add(new DefaultMutableTreeNode("Examples 2")); 
        examples.add(new DefaultMutableTreeNode("Examples 3")); 
         
        m_rootNode.add(forums); 
        m_rootNode.add(articles); 
        m_rootNode.add(examples); 
         
        return m_rootNode; 
    } 

    /** 
     * Handles the button action 
     */ 
    public void actionPerformed(ActionEvent event)  
    { 
        if(event.getSource().equals(m_addButton)) 
        { 
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)m_tree.getLastSelectedPathComponent(); 

            if (selNode != null) 
            { 
                // add new node as a child of a selected node at the end
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New Node"); 
                m_model.insertNodeInto(newNode, selNode, selNode.getChildCount()); 
                   
                  //make the node visible by scroll to it
                TreeNode[] nodes = m_model.getPathToRoot(newNode); 
                TreePath path = new TreePath(nodes); 
                m_tree.scrollPathToVisible(path); 
                 
                //select the newly added node
                m_tree.setSelectionPath(path); 
                 
                //Make the newly added node editable
                m_tree.startEditingAtPath(path); 
            } 
        }             
        else if(event.getSource().equals(m_delButton))
        {
            //remove the selected node, except the parent node
            //get the selected node
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)m_tree.getLastSelectedPathComponent();
            
            removeNode(selNode);            
        }
        else if(event.getSource().equals(m_searchButton))
        {
            //search the node
            DefaultMutableTreeNode node = searchNode(m_searchText.getText());
            
            if(node != null)
            {
                //make the node visible by scroll to it
                TreeNode[] nodes = m_model.getPathToRoot(node);
                TreePath path = new TreePath(nodes); 
                m_tree.scrollPathToVisible(path); 
                m_tree.setSelectionPath(path);
            }
            else
            {
                //node with string not found show message
                JOptionPane.showMessageDialog(this, 
                "Node with string " + m_searchText.getText() + " not found", 
                "Node not found", JOptionPane.INFORMATION_MESSAGE);                         
            }
        }
        else if(event.getSource().equals(m_searchAndDeleteButton))
        {
            //search the node
            DefaultMutableTreeNode node = searchNode(m_searchText.getText());
            
            if(node != null)
            {
                //remove the node that was found
                removeNode(node);
            }
            else
            {
                //node with string not found show message
                  JOptionPane.showMessageDialog(this, 
                  "Node with string " + m_searchText.getText() + " not found", 
                  "Node not found", JOptionPane.INFORMATION_MESSAGE);                         
            }
        }
    } 
    
    /**
     * This method takes the node string and
     * traverses the tree till it finds the node
     * matching the string. If the match is found 
     * the node is returned else null is returned
     * 
     * @param nodeStr node string to search for
     * @return tree node 
     */
    public DefaultMutableTreeNode searchNode(String nodeStr)
    {
        DefaultMutableTreeNode node = null;
        
        //Get the enumeration
        Enumeration en = m_rootNode.breadthFirstEnumeration();
        
        //iterate through the enumeration
        while(en.hasMoreElements())
        {
            //get the node
            node = (DefaultMutableTreeNode)en.nextElement();
            
            //match the string with the user-object of the node
            if(nodeStr.equals(node.getUserObject().toString()))
            {
                //tree node with string found
                return node;                         
            }
        }
        
        //tree node with string node found return null
        return null;
    }
         
    /**
     * This method removes the passed tree node from the tree
     * and selects appropriate node
     * 
     * @param selNode node to be removed
     */
    public void removeNode(DefaultMutableTreeNode selNode) 
    {
        if (selNode != null) 
        { 
            //get the parent of the selected node
            MutableTreeNode parent = (MutableTreeNode)(selNode.getParent());
        
            // if the parent is not null
            if (parent != null) 
            {
                //get the sibling node to be selected after removing the
                //selected node
                MutableTreeNode toBeSelNode = getSibling(selNode);
                
                //if there are no siblings select the parent node after removing the node
                if(toBeSelNode == null)
                {
                    toBeSelNode = parent;
                }
            
                //make the node visible by scroll to it
                TreeNode[] nodes = m_model.getPathToRoot(toBeSelNode);
                TreePath path = new TreePath(nodes); 
                m_tree.scrollPathToVisible(path); 
                m_tree.setSelectionPath(path);
             
                //remove the node from the parent
                m_model.removeNodeFromParent(selNode);
            }    
        } 
    }

    /**
     * This method returns the previous sibling node 
     * if there is no previous sibling it returns the next sibling
     * if there are no siblings it returns null
     * 
     * @param selNode selected node
     * @return previous or next sibling, or parent if no sibling
     */
    private MutableTreeNode getSibling(DefaultMutableTreeNode selNode)
    {
        //get previous sibling
        MutableTreeNode sibling = (MutableTreeNode)selNode.getPreviousSibling();

        if(sibling == null)
          {
              //if previous sibling is null, get the next sibling
            sibling    = (MutableTreeNode)selNode.getNextSibling();
        }
        
        return sibling;
    }
         
    /** 
     * Main method to run as an Application 
     * @param argv 
     */ 
    public static void main(String[] arg)  
    {  
        EditableTree editableTree = new EditableTree(); 
    } 
}     
