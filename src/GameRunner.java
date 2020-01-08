import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GameRunner extends JFrame implements KeyListener{
	
	private static final int START = KeyEvent.VK_SPACE;
	private static final int TORPEDO = KeyEvent.VK_W;
	private static final int ROLL_LEFT = KeyEvent.VK_A;
	private static final int ROLL_RIGHT = KeyEvent.VK_D;
	private static final int SHOOT = KeyEvent.VK_S;
	/*
	 * Change these Strings to whatever the address of the files
	 * on your computer is 
	 */
	private static String xWingPicAddress = "/X-Wing.png";
	private static String tiePicAddress = "/TIE.png";
	private static String torpedoPicAddress = "/torpedo.png";
	private static String explodingXWingPicAddress = "/explodingXWing.png";
	

	//frame dimensions
	private static final int FRAME_WIDTH = 1400;
	private static final int FRAME_HEIGHT = 850;
	private static final Point CENTER = new Point(FRAME_WIDTH/2,FRAME_HEIGHT/4);

	//hold colors to use
	private static Color background = Color.BLACK;
	private static Color missileColor = Color.RED;
	private static Color tieMissileColor = Color.GREEN;

	//holds images from the computer
	private static SpriteImage image;
	private static BufferedImage xWingPic;
	private static BufferedImage tiePic;
	private static BufferedImage torpedoPic;
	private static BufferedImage explodingXWingPic;

	//ArrayLists of the Sprite objects in the game
	private static XWing player;
	private static ArrayList<TIE> empire = new ArrayList<TIE>();
	private static ArrayList<Sphere> stars = new ArrayList<Sphere>();
	private static ArrayList<Missile> missiles = new ArrayList<Missile>();
	private static ArrayList<Missile> tieMissiles = new ArrayList<Missile>();
	private static ArrayList<Torpedo> torpedoes = new ArrayList<Torpedo>();
	private static ArrayList<Sprite> array = new ArrayList<Sprite>();
	private static ArrayList<XWing> lifeIcons = new ArrayList<XWing>();

	//score counters
	private static TextBox finalScore = new TextBox("Your score is 0",FRAME_WIDTH/2,FRAME_HEIGHT/2,Color.YELLOW,Color.BLACK);
	private static TextBox scoreCount = new TextBox("Score: 0",90,30,Color.YELLOW);
	private static int score = 0;

	//used for laser overheat
	private static TextBox overheat = new TextBox("Overheat: 10", FRAME_WIDTH-150,30,Color.YELLOW);
	private static int overheatCounter = 0;
	private static boolean overheated = false;
	private static int reloadCounter = 0;
	private static final int ROUNDS = 10;
	private static final int OVERHEAT_DELAY = 200; //4 seconds
	private static final int RELOAD_DELAY = 60; //1.2 seconds

	//used to fire torpedoes
	private static TextBox torpedoBox = new TextBox("Torpedo Arming.",FRAME_WIDTH-175,75,Color.YELLOW);
	private static int torpedoCounter = 0;
	private static boolean torpedoReady = false;
	private static final int TORPEDO_DELAY = 500; //10 seconds
	private static boolean invincibleTorpedoes = true;

	//controls spin of the XWing
	private static int spinning = 0;
	private static int spinCounter = 0;
	private static final double DODGE_CHANCE = 1;
	//this is the chance of avoiding laser if spinning

	//number of lives things have
	private static final int PLAYER_HEALTH = 4;
	private static int tieHealth = 6;

	//chance of each TIE firing per frame
	private double fireChance = 0.007;
	private final double OFF_CHANCE = 0.20; 

	//sets grace period between possible player deaths
	private static final int GRACE_PERIOD = 50;
	private static int graceCounter = GRACE_PERIOD;


	public GameRunner(){
		try{
			xWingPic = ImageIO.read(GameRunner.class.getResource(xWingPicAddress));
			tiePic = ImageIO.read(GameRunner.class.getResource(tiePicAddress));
			torpedoPic = ImageIO.read(GameRunner.class.getResource(torpedoPicAddress));
			explodingXWingPic = ImageIO.read(GameRunner.class.getResource(explodingXWingPicAddress));
		}catch(IOException e){
			e.printStackTrace();
		}

		player = new XWing(xWingPic,200,200,0.25,PLAYER_HEALTH);

		image = new SpriteImage(array);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

	}

	public static void main(String[] args) {

		/* a weird voodoo magic-y way to allow mouse inputs that creates
		 * code that will run once and involves instantiating an object
		 * of this class within the main method of this class for some reason
		 */
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				GameRunner frame = new GameRunner();
				frame.setTitle("Don't Die!");
				frame.setResizable(false);
				frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
				frame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().setBackground(background);
				frame.getContentPane().add(image);
				frame.pack();
				frame.setVisible(true); 

				Timer mover = new Timer(20,frame.stuffMover);
				mover.start();

				Timer generateTIE = new Timer (2000,frame.makeTIE);
				generateTIE.start();
			}
		});
	}

	private ActionListener stuffMover = new ActionListener(){
		public void actionPerformed(ActionEvent evt) {

			//adds 5 new stars and moves all stars
			for(int i=0;i<5;i++){
				addStar();
			}
			moveStars();

			//moves the missiles and torpedoes
			moveMissiles();
			moveTorpedoes();
			moveTieMissiles();

			//deals with missiles hitting things
			checkTieHits();

			//moves the XWing to the cursor
			movePlayer();

			//deals with XWing spinning
			spinPlayer();

			//checks for overheat
			checkOverheat();

			//check if torpedo ready
			checkTorpedo();

			//the ties shoot at you
			tiesShoot();

			//check if the ties hit you or if  you ran into them
			checkPlayerHit();
			checkTieCollision();

			//increment grace period
			if(graceCounter<GRACE_PERIOD){
				graceCounter++;
			}

			//update icons for player lives
			updateLives();


			array.clear();
			array.addAll(stars);
			array.add(scoreCount);
			array.add(torpedoBox);
			array.addAll(lifeIcons);
			array.add(overheat);
			array.addAll(empire);
			array.addAll(missiles);
			array.addAll(torpedoes);
			array.addAll(tieMissiles);
			if(player.isAlive){
				array.add(player);
			}else{
				array.add(finalScore);
			}
			image.repaint();
		}
	};

	private ActionListener makeTIE = new ActionListener(){
		public void actionPerformed(ActionEvent evt) {
			if(player.isAlive){
				int xStart = randMinMax(200,FRAME_WIDTH-200);
				int yStart = randMinMax(CENTER.y+100,FRAME_HEIGHT-250);
				empire.add(new TIE(tiePic,xStart,yStart,0.25, tieHealth));
				if(empire.get(empire.size()-1).intersects(player.getShape()) && player.getY()>CENTER.y){
					empire.remove(empire.size()-1);
				}
			}
		}
	};

	private void addStar(){
		double direction = randMinMax(0,360);
		int xAdd = (int) ((50+20*Math.random())*Math.cos(direction/180*Math.PI));
		int yAdd = (int) ((50+20*Math.random())*Math.sin(direction/180*Math.PI));
		//int xAdd = (int) (50*Math.cos(direction/180*Math.PI));
		//int yAdd = (int) (50*Math.sin(direction/180*Math.PI));
		stars.add(new Sphere(CENTER.x+xAdd,CENTER.y+yAdd,1,Color.WHITE,(int)direction));
	}
	private void moveStars(){
		for(int i=0;i<stars.size();i++){
			double distance = CENTER.distance(stars.get(i).getX(),stars.get(i).getY());
			int dx = (int)(((distance+50)*Math.cos(stars.get(i).getAng()*Math.PI/180.0))/20);
			int dy = (int)(((distance+50)*Math.sin(stars.get(i).getAng()*Math.PI/180.0))/20);
			stars.get(i).moveNow(dx,dy);

			if(stars.get(i).xPos>FRAME_WIDTH ||stars.get(i).xPos<0 ||stars.get(i).yPos>FRAME_HEIGHT ||stars.get(i).yPos<0){
				stars.remove(i);
			}
		}
	}
	private void moveMissiles(){
		if(!missiles.isEmpty()){
			for(int i=0;i<missiles.size();i++){
				missiles.get(i).moveNow();
				int x = missiles.get(i).getX();
				int y = missiles.get(i).getY();
				if(x>FRAME_WIDTH || x<0 || y>FRAME_HEIGHT || y<0){
					missiles.remove(i);
				}else if(Math.abs(x-CENTER.x)<60 && Math.abs(y-CENTER.y)<60){
					missiles.remove(i);
				}
			}
		}
	}
	private void moveTieMissiles(){
		if(!tieMissiles.isEmpty()){
			for(int i=0;i<tieMissiles.size();i++){
				tieMissiles.get(i).moveNow();
				int x = tieMissiles.get(i).getX();
				int y = tieMissiles.get(i).getY();
				if(x>FRAME_WIDTH || x<0 || y>FRAME_HEIGHT || y<0){
					tieMissiles.remove(i);
				}
			}
		}
	}
	private void moveTorpedoes(){
		if(!torpedoes.isEmpty()){
			for(int i=0;i<torpedoes.size();i++){
				torpedoes.get(i).moveNow();
				int x = torpedoes.get(i).getX();
				int y = torpedoes.get(i).getY();
				if(x>FRAME_WIDTH || x<0 || y>FRAME_HEIGHT || y<0){
					torpedoes.remove(i);
				}
				if(Math.abs(x-CENTER.x)<60 && Math.abs(y-CENTER.y)<60){
					torpedoes.remove(i);
				}
			}
		}

	}
	private void checkTieHits(){
		if(!missiles.isEmpty() && !empire.isEmpty()){
			for(int i=0;i<missiles.size();i++){
				boolean remove = false;
				for(int j=0;j<empire.size();j++){
					if(!missiles.isEmpty()){
						if(missiles.get(i).intersects(empire.get(j).getShape())){
							empire.get(j).injure();
							if(!empire.get(j).isAlive){
								empire.remove(j);
								score++;
							}
							finalScore.setText("Your score is "+score);
							scoreCount.setText("Score: "+score);
							remove = true;
						}
					}
				}	
				if(remove){
					missiles.remove(i);
				}
			}
		}
		if(!torpedoes.isEmpty() && !empire.isEmpty()){
			for(int i=0;i<torpedoes.size();i++){
				boolean remove = false;
				for(int j=0;j<empire.size();j++){
					if(!torpedoes.isEmpty()){
						if(torpedoes.get(i).intersects(empire.get(j).getShape())){
							empire.get(j).die();
							empire.remove(j);
							score++;
							finalScore.setText("Your score is "+score);
							scoreCount.setText("Score: "+score);
							remove = true;
						}
					}
				}	
				if(remove && !invincibleTorpedoes){
					torpedoes.remove(i);
				}
			}
		}
	}
	private void movePlayer(){
		Point a = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(a, image);
		if(a.x<FRAME_WIDTH && a.x>0 && a.y<FRAME_HEIGHT && a.y>0){
			player.setPos(a.x, a.y);
		}else{
			player.setPos(FRAME_WIDTH/2, FRAME_HEIGHT*2/3);
			spinning = 0;
		}
	}
	private void spinPlayer(){
		if(spinning == 0){
			double dy = CENTER.y-player.getY();
			double dx = CENTER.x-player.getX();
			double angToCenter = Math.atan(dy/dx)*180/Math.PI;
			if(dx>=0){
				angToCenter+=180;
			}
			player.setSpin((int) angToCenter-90);
		}else if (spinning == 1){
			if(spinCounter<=18){
				player.incrementSpin(20);
			}else{
				spinning = 0;
				spinCounter = 0;
			}
			spinCounter++;
		}else if (spinning == -1){
			if(spinCounter<=18){
				player.incrementSpin(-20);
			}else{
				spinning = 0;
				spinCounter = 0;
			}
			spinCounter++;
		}
	}
	private void checkOverheat(){
		if(!overheated){
			if(overheatCounter<ROUNDS){
				overheat.setText("Overheat: "+(ROUNDS-overheatCounter));
				if(reloadCounter>RELOAD_DELAY){
					if(overheatCounter>0){
						overheatCounter--;
					}
					reloadCounter = 0;
				}
				reloadCounter++;
			}else{
				overheat.setText("Overheated!!!");
				overheated = true;
				overheatCounter = 0;
				reloadCounter = 0;
			}
		}else{
			if(overheatCounter<OVERHEAT_DELAY){
				overheatCounter++;
			}else{
				overheated = false;
				overheatCounter = 0;
			}
		}
	}
	private void checkTorpedo(){
		if(!torpedoReady){
			if(torpedoCounter<TORPEDO_DELAY){
				torpedoCounter++;
			}else{
				torpedoReady = true;
				torpedoCounter = 0;
				torpedoBox.setText("Torpedo Ready!");
			}
		}
	}
	private void checkTieCollision(){
		if(!empire.isEmpty()){
			for(int i=0;i<empire.size();i++){
				if(player.intersects(empire.get(i).getShape())){
					if(graceCounter>=GRACE_PERIOD){
						player.injure();
						graceCounter = 0;
						empire.remove(i);
					}
				}
			}
		}
	}
	private void tiesShoot(){
		if(!empire.isEmpty() && player.isAlive){
			for(int i=0;i<empire.size();i++){
				if(Math.random()<fireChance){
					if(Math.random()>OFF_CHANCE){
						Point[] lasers = empire.get(i).getLasers();
						for(int j=0;j<lasers.length;j++){
							double dy = player.getY()-lasers[j].y;
							double dx = player.getX()-lasers[j].x;
							double angToPlayer = Math.atan(dy/dx)*180/Math.PI;
							if(dx>=0){
								angToPlayer+=180;
							}

							tieMissiles.add(new Missile(lasers[j].x,lasers[j].y,tieMissileColor,(int) angToPlayer));
						}
					}else {
						Point[] lasers = empire.get(i).getLasers();
						for(int j=0;j<lasers.length;j++){
							if(player.getY()<empire.get(i).getY()){
								tieMissiles.add(new Missile(lasers[j].x,lasers[j].y,tieMissileColor,90));
							}else{
								tieMissiles.add(new Missile(lasers[j].x,lasers[j].y,tieMissileColor,270));
							}
						}
					}
				}
			}
		}
	}
	private void checkPlayerHit(){
		if(!tieMissiles.isEmpty()){
			for(int i=0;i<tieMissiles.size();i++){
				if(player.intersects(tieMissiles.get(i).getShape()) && graceCounter>=GRACE_PERIOD){
					if(spinning==0){
						player.injure();
						graceCounter = 0;
						tieMissiles.remove(i);
					}else{
						if(Math.random()>DODGE_CHANCE){
							player.injure();
							graceCounter = 0;
							tieMissiles.remove(i);
						}
					}
				}
			}
		}
	}
	private void updateLives(){
		lifeIcons.clear();
		for(int i=0;i<player.getHealth();i++){
			lifeIcons.add(new XWing(xWingPic,300+100*i,30,0.3,1));
		}
		if(graceCounter<GRACE_PERIOD){
			double percent = (double)graceCounter/(double)GRACE_PERIOD;
			if(percent>0.75 || (percent<0.5&& percent>0.25)){
				lifeIcons.add(new XWing(explodingXWingPic,300+100*player.getHealth(),30,0.3,1));
			}
		}
	}
	private int randMinMax(int min, int max){
		return (int)((max-min+1)*Math.random())+min;
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()== SHOOT && player.isAlive && !overheated){

			//make 4 new missiles
			Point[] origins = player.getLasers();
			for(int i=0;i<origins.length;i++){
				double dy = CENTER.y-origins[i].y;
				double dx = CENTER.x-origins[i].x;
				double angToCenter = Math.atan(dy/dx)*180/Math.PI;
				if(dx>0){
					angToCenter+=180;
				}
				missiles.add(new Missile(origins[i].x,origins[i].y,missileColor,(int) angToCenter));
			}
			//repaint image and increment counter
			image.repaint();
			overheatCounter++;
		}else if(e.getKeyCode()== ROLL_RIGHT && player.isAlive){
			spinning = 1;
		}else if(e.getKeyCode()== ROLL_LEFT && player.isAlive){
			spinning = -1;
		}else if(e.getKeyCode()== TORPEDO && player.isAlive && torpedoReady){
			double dy = CENTER.y-player.getY();
			double dx = CENTER.x-player.getX();
			double angToCenter = Math.atan(dy/dx)*180/Math.PI;
			if(dx>0){
				angToCenter+=180;
			}
			torpedoes.add(new Torpedo(torpedoPic,player.getX(),player.getY(),(int)angToCenter));
			torpedoReady = false;
			torpedoBox.setText("Torpedo Arming.");

		}else if(e.getKeyCode()==START && !player.isAlive){
			missiles.clear();
			empire.clear();
			stars.clear();
			tieMissiles.clear();
			score = 0;
			player.revive();
			player = new XWing(xWingPic,200,200,0.25,PLAYER_HEALTH);
			overheatCounter = 0;
			reloadCounter = 0;
			torpedoCounter = 0;
			overheated = false;
			torpedoReady = false;
		}

	}
	@Override
	public void keyTyped(KeyEvent arg0) {

	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -900050737882537599L;
}
