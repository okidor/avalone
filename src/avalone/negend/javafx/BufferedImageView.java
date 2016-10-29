package avalone.negend.javafx;

import java.awt.image.BufferedImage;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class BufferedImageView extends ImageView
{
	public BufferedImageView(BufferedImage img)
	{
		super(convertToJavaFXImg(img));
	}
	
	public static WritableImage convertToJavaFXImg(BufferedImage img)
	{
		WritableImage wr = null;
        if (img != null) 
        {
            wr = new WritableImage(img.getWidth(), img.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < img.getWidth(); x++) 
            {
                for (int y = 0; y < img.getHeight(); y++) 
                {
                    pw.setArgb(x, y, img.getRGB(x, y));
                }
            }
        }
        return wr;
	}
}
