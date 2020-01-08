

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class XWing extends Sprite{

	protected BufferedImage image;
	protected double spinAngle;
	protected double size;
	protected Point[] lasers = new Point[4];
	AffineTransform at = new AffineTransform();
	protected int health = 4;

	public XWing(BufferedImage icon, int x, int y, double scale, int lives){
		image = icon;
		this.setPos(x, y);
		this.angle = 90;
		spinAngle = 0;
		size = scale;
		health = lives;
		
		loadShape();

		lasers[0] = new Point((int) (this.xPos+100*size),(int) (this.yPos-image.getHeight()*size+60*size));
		lasers[1] = new Point((int) (this.xPos-100*size),(int) (this.yPos-image.getHeight()*size+60*size));
		lasers[2] = new Point((int) (this.xPos+90*size),(int) (this.yPos+image.getHeight()*size-120*size));
		lasers[3] = new Point((int) (this.xPos-90*size),(int) (this.yPos+image.getHeight()*size-120*size));
	}
	public void setSpin(int ang){
		spinAngle = ang;
	}
	public void incrementSpin(int ang){
		spinAngle+=ang;
	}
	public Point[] getLasers(){
		return lasers;
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
		at.rotate(spinAngle/180.0*Math.PI,this.xPos,this.yPos);
		at.translate(this.xPos-(int)(image.getWidth()/2*size), this.yPos-(int)(image.getHeight()/2*size));
		at.scale(size, size);

		g2.drawImage(image, at,null);
		
		loadShape();

		lasers[0].move((int) (this.xPos+100*size),(int) (this.yPos-image.getHeight()*size+60*size));
		lasers[1].move((int) (this.xPos-100*size),(int) (this.yPos-image.getHeight()*size+60*size));
		lasers[2].move((int) (this.xPos+90*size),(int) (this.yPos+image.getHeight()*size-120*size));
		lasers[3].move((int) (this.xPos-90*size),(int) (this.yPos+image.getHeight()*size-120*size));

		AffineTransform pt = new AffineTransform();
		pt.rotate(spinAngle/180.0*Math.PI,this.xPos,this.yPos);

		for(int i=0;i<4;i++){
			pt.transform(lasers[i],lasers[i]);
		}

	}
	private void loadShape(){
		Shape hitBox = at.createTransformedShape(image.getData().getBounds());
		this.setShape(hitBox);
	}


}
