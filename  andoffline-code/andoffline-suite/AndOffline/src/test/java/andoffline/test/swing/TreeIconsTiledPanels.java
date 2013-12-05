package andoffline.test.swing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.*;
 
public class TreeIconsTiledPanels
{
    BufferedImage[] images;
 
    public TreeIconsTiledPanels(BufferedImage[] images)
    {
        this.images = images;
    }
 
    private JPanel getContent()
    {
        JPanel panel = new JPanel(new GridLayout(1,0));
        panel.add(new JScrollPane(getLeftTree()));
        panel.add(new JScrollPane(getRightTree()));
        return panel;
    }
 
    private JTree getLeftTree()
    {
        JTree tree = getTree();
        DefaultTreeCellRenderer renderer =
                (DefaultTreeCellRenderer)tree.getCellRenderer();
        // make changes to renderer for this tree
        // works okay if the size of the new images does
        // not exceed the size of the default images
        renderer.setClosedIcon(new ImageIcon(images[0]));
        renderer.setOpenIcon(new ImageIcon(images[1]));
        renderer.setLeafIcon(new ImageIcon(images[2]));
        return tree;
    }
 
    private JTree getRightTree()
    {
        JTree tree = getTree();
        // set a renderer that allows more fine-grained
        // control of rendering for varying conditions
        tree.setCellRenderer(new IconRenderer(images));
        return tree;
    }
 
    private JTree getTree()
    {
        String[] birds = {
            // branches  |<-- child leaf nodes -->|
            "hawks",   "gray",    "red-tailed", "rough-legged",
            "falcons", "harrier", "kestrel",    "kite", 
            "owls",    "barred",  "saw-whet",   "snowy"
        };
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("birds");
        DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[birds.length];
        for(int j = 0; j < nodes.length; j++)
            nodes[j] = new DefaultMutableTreeNode(birds[j]);
        for(int j = 0; j < 9; j += 4)
        {
            root.insert(nodes[j], j % 3);
            for(int k = j + 1; k < j + 4; k++)
                nodes[j].insert(nodes[k], k - j - 1);
        }
        DefaultTreeModel model = new DefaultTreeModel(root);
        return new JTree(model);
    }
 
    public static void main(String[] args) throws IOException
    {
        String[] ids = { "pause", "play", "root" };
        BufferedImage[] bis = new BufferedImage[ids.length];
        for(int j = 0; j < bis.length; j++)
            bis[j] = ImageIO.read(new File("./src/main/resources/" + ids[j] + ".png"));
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(new TreeIconsTiledPanels(bis).getContent());
        f.setSize(500,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
 
class IconRenderer extends DefaultTreeCellRenderer
{
    ImageIcon closedIcon;
    ImageIcon openIcon;
    ImageIcon leafIcon;
 
    public IconRenderer(BufferedImage[] images)
    {
        closedIcon = new ImageIcon(images[0]);
        openIcon   = new ImageIcon(images[1]);
        leafIcon   = new ImageIcon(images[2]);
    }
 
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus)
    {
        // start with default behavior
        super.getTreeCellRendererComponent(tree, value, sel, expanded, 
                                           leaf, row, hasFocus);
 
        // customize based on local conditions/state
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        String userObject = (String)node.getUserObject();
        boolean isOwl = userObject.equalsIgnoreCase("owls");
        if(isOwl)
            setIcon(closedIcon);
        else if(leaf)
            setIcon(leafIcon);
        else if(expanded)
            setIcon(openIcon);
        return this;
    }
}
