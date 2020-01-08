

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Torpedo extends Sprite{

	protected BufferedImage image;
	protected double size = 0.25;
	AffineTransform at = new AffineTransform();

	protected Torpedo(BufferedImage img, int x,int y, int ang){
		xPos = x;
		yPos = y;
		angle = ang;
		xVel = -20.0*Math.cos(angle*Math.PI/180.0);
		yVel = -20.0*Math.sin(angle*Math.PI/180.0);	
		image = img;
		
		loadShape();
	}
	public void draw(Graphics gvar){
		Graphics2D g2 = (Graphics2D) gvar;
		
		at.setToIdentity();
		at.rotate((angle-90)/180.0*Math.PI,this.xPos,this.yPos);
		at.translate(this.xPos-(int)(image.getWidth()/2*size), this.yPos-(int)(image.getHeight()/2*size));
		at.scale(size, size);
		
		loadShape();

		g2.drawImage(image, at,null);
	}
	private void loadShape(){
		Shape hitBox = at.createTransformedShape(image.getData().getBounds());
		this.setShape(hitBox);
		
	}

}
