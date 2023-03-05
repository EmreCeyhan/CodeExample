import java.util.Arrays;
import java.util.List;

public class Enemy extends Character
{
	private String destoryOnCollide[] = {"class Misc", "class Bullet"};
	private int mode;
	private int speed = 2;
	private int up1 = 40, down1 = 100, change1 = 10;
	private int up2 =30, down2 = 50 , change2 = 20;
	private int up3 = 20, down3 = 30 , change3 = 30;
	private int Width = 600;
	private int steps = 0;
	private int dy = 1;

	/**
	 * Enemy constructor, calls parent constructor and specifies this enemy instances mode.
     * @param x X position.
     * @param y Y position.
     * @param r Rotation speed of current actor.
	 * @param mode Mode of this enemy, determines enemy behaviour.
     * @param textureFile Path from root of program execution to the required image file.
    **/
	public Enemy(int x, int y, int r, int mode, String textureFile, int volume){//y be somewhere below 0 && x be random number between little bit below zero to little bit over the screenWidth
		super(x,y,r,textureFile, "Data/Graphics/Bullets/B3.png", true, volume);
		this.mode = mode;
		if(x>Width/2){speed=speed*-1;}
	}

	/**
	 * Overrites calcMove method in Actor class, allows specific enemy paths.
	**/
	public void calcMove(){
		switch(mode){
			case 1:
				if(steps == down1&&dy>0){
					dy=dy*-1;
					steps=0;
					down1+=change1;
				}else if(steps==up1&&dy<0){
					dy=dy*-1;
					steps=0;
				}
				break;
			case 2:
				if(steps == down2&&dy>0){
					dy=dy*-1;
					steps=0;
					down2+=change2;
				}else if(steps==up2&&dy<0){
					dy=dy*-1;
					steps=0;
				}
				break;
			case 3:
				if(steps == down3&&dy>0){
					dy=dy*-1;
					steps=0;
					down3+=change3;
				}else if(steps==up3&&dy<0){
					dy=dy*-1;
					steps=0;
				}
				break;
			case 4:
				dy = 4;
				speed = 0;
		}
		steps++;
		y+=dy;
		if(x>=getScreenWidth()||x<0){speed=speed*-1;}
		x+=speed;
	}

	/**
	 * Overrites collisionAction in Actor, allows enemy specific collision behaviour.
     * @param A Current actor instance which has collided with something.
	**/
	public void collisionAction(Actor A)
	{
		if(Arrays.asList(destoryOnCollide).contains(A.getClass().toString())){
			if(A.getenemyBullet() == true){
				//System.out.println(A.getenemyBullet());
			}else{
				destroyedByPlayer = true;
				toRemove = true;
				playSound("Data/Audio/explosion1.wav", 7*volume);
				A.toRemove = true;
			}
		}
	}

	public int getOppositeSide(int x){
		int output = x - Width;
		if(output<0){
		output=output*-1;
		}if(output == 0){
			output =1;
		}
		return output;
	}

	public void setSpeed(int speed){ this.speed = speed; }


}
