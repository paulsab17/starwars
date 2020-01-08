

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TIE extends Sprite {
	
	protected BufferedImage image;
	protected double size;
	protected Point[] lasers = new Point[2];
	AffineTransform at = new AffineTransform();
	protected int health = 4;
	
	
	public TIE(BufferedImage icon, int x, int y, double scale, int lives){
		image = icon;
		this.setPos(x, y);
		this.angle = 90;
		size = scale;
		health = lives;
		
		at.setToIdentity();
		at.translate(this.xPos-(int)(image.getWidth()/2*size), this.yPos-(int)(image.getHeight()/2*size));
		at.scale(size, size);
		
		loadShape();
		
		lasers[0] = new Point((int) (this.xPos+50*size),(int) (this.yPos+60*size));
		lasers[1] = new Point((int) (this.xPos-50*size),(int) (this.yPos+60*size));
	}
	public Point[] getLasers(){
		return lasers;
	}
	public void setSize(double scale){
		size = scale;
	}
	public void injure(){
		health--;
		if(health<=0){
			this.die();
		}
	}
	public int getHealth(){
		return health;
	}
	public void draw(Graphics gvar){
		Graphics2D g2 = (Graphics2D) gvar;
		
		at.setToIdentity();
		at.translate(this.xPos-(int)(image.getWidth()/2*size), this.yPos-(int)(image.getHeight()/2*size));
		at.scale(size, size);
		
		g2.drawImage(image, at,null);
		
		loadShape();		
		
		Font textFont = new Font("DialogueInput",Font.BOLD,25);
		g2.setFont(textFont);
		g2.setColor(Color.RED);
		g2.drawString(""+health,this.xPos-5,(int) (this.yPos-80*size));
		
		lasers[0].move((int) (this.xPos+15*size),(int) (this.yPos+25*size));
		lasers[1].move((int) (this.xPos-15*size),(int) (this.yPos+25*size));
		
		
		
	}
	private void loadShape(){
		Shape hitBox = at.createTransformedShape(image.getData().getBounds());
		this.setShape(hitBox);
	}	
}
