package andoffline.test.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
 
/**
 * http://www.mkyong.com/java/how-to-write-an-image-to-file-imageio/
 * 
 */
public class ImageApiResize {
	
	public static final String RESULT = "/home/fulvio/Immagini/norris.jpg";
 
	private static final int IMG_WIDTH = 100;
	private static final int IMG_HEIGHT = 100;
 
	public static void main(String [] args){
 
	try{
		
		System.out.println("--Start--");
 
		BufferedImage originalImage = ImageIO.read(new File("/home/fulvio/Immagini/norris.jpg")); //c:\\image\\mkyong.jpg
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
 
		BufferedImage resizeImageJpg = resizeImage(originalImage, type);
		ImageIO.write(resizeImageJpg, "jpg", new File("/home/fulvio/Immagini/norris_jpg.jpg"));  
 
		BufferedImage resizeImagePng = resizeImage(originalImage, type);
		ImageIO.write(resizeImagePng, "png", new File("/home/fulvio/Immagini/norris_png.jpg")); 
 
		BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type);
		ImageIO.write(resizeImageHintJpg, "jpg", new File("/home/fulvio/Immagini/norris_jpg.jpg")); 
 
		BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
		ImageIO.write(resizeImageHintPng, "png", new File("/home/fulvio/Immagini/norris_png.jpg")); 
		
		System.out.println("--End--");
 
	}catch(IOException e){
		System.out.println(e.getMessage());
	}
 
    }
 
    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
    	
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
	 
		return resizedImage;
    }
 
    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){
 
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);
	 
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	 
		return resizedImage;
    }	
}