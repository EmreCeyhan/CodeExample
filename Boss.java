import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;

public class Boss extends Enemy
{
	private int speed = 1, health = 50, volume, level, tempX, stage = 1, countShots = 0;
    private String destoryOnCollide[] = {"class Misc", "class Bullet"}; //could include player so player can ram into the boss
	private boolean toggle = true;
	private String moving = "RIGHT";
	private Actor hearts;
	private Text healthText;
    private boolean isSpawned = false;
	/**
	 * Enemy constructor, calls parent constructor and specifies this enemy instances mode.
     * @param x X position.
     * @param y Y position.
     * @param r Rotation speed of current actor.
	 * @param mode Mode of this enemy, determines enemy behaviour.
     * @param textureFile Path from root of program execution to the required image file.
    **/
	public Boss(int x, int y, int r, String textureFile, int volume, int level){//y be somewhere below 0 && x be random number between little bit below zero to little bit over the screenWidth
		super(x,y,r,0,textureFile,volume);
		this.volume = volume; this.level = level;

		if(level == 1){ health = 50; }
		else if(level == 2){ health = 100; }
		else if(level == 3){ health = 200; }

		hearts = new Actor(x,y-50,0f,0f,0,"Data/Graphics/Hud/h1.png",false); hearts.actor.setScale(1f, 1f);
		healthText = new Text("x "+Integer.toString(health), spaceAge, 15); healthText.setColor(Color.WHITE); healthText.setPosition(x+10, y-55);
	}

	/**
	 * Heart display that attach to the boss
	 * @param window window present
	 */
	public void drawAttachments(RenderWindow window)
    {
		hearts.actor.setPosition(x, (y-bounds.height/2)-20);
		window.draw(hearts.actor);

		healthText.setPosition(x+10, (y-bounds.height/2)-25);
		healthText.setString("x "+Integer.toString(health));
		window.draw(healthText, RenderStates.DEFAULT);
    }

	/**
	 * Overrites calcMove method in Actor/Enemy class, allows specific enemy paths.
	**/
	public void calcMove()
	{
		if(level == 1 || level == 3){
			if(y<200){y+=speed;}
			if(x<50){moving = "RIGHT";}
			else if(x>550){moving = "LEFT";}

			if(moving.equals("RIGHT")){ x+=2; }
			else{ x-=2; }
		}else if(level == 2){
			if(y<100){ moving = "DOWN"; }
			else if(y > 500){ moving = "UP"; }

			if(moving.equals("DOWN")){ y+=speed; }
			else if(moving.equals("UP")){ y-=2; }
		}
		this.tempX = x;
	}

	/**
	 * Creates a bullet.
	 * @return Instance of the class Bullet.
	**/
	public ArrayList<Actor> explode(int level)
	{
		int dx=0;
		playSound("Data/Audio/shot1.wav", 2*volume);
		ArrayList<Actor> bullets = new ArrayList<Actor>();
		if(moving.equals("RIGHT")){dx+=3;}
		else{dx-=3;}
		if(level < 3){
			if(toggle == true){
				toggle = false;
				bullets.add(new Bullet(x+dx, y, 0f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, 2f, 4f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, -2f, 4f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, 3f, 3f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, -3f, 3f, 0, "Data/Graphics/Bullets/B3.png", true));
			}else{
				toggle = true;
				bullets.add(new Bullet(x+dx, y, 1f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, -1f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, -2.5f, 4f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, 2.5f, 4f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, 3.5f, 3f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x+dx, y, -3.5f, 3f, 0, "Data/Graphics/Bullets/B3.png", true));
			}
		}else{
			if(stage == 1){
				if(toggle == true){
					toggle = false;
					for(int i=0; i<4; i++){
						bullets.add(new Bullet(x+(i*80), y, 0f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
						bullets.add(new Bullet(x-(i*80), y, 0f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
					}
				}else{
					toggle = true;
					bullets.add(new Bullet(x, y, 0f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
					bullets.add(new Bullet(x, y, 1f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
					bullets.add(new Bullet(x, y, 2f, 4f, 0, "Data/Graphics/Bullets/B3.png", true));
					bullets.add(new Bullet(x, y, -2f, 4f, 0, "Data/Graphics/Bullets/B3.png", true));
					bullets.add(new Bullet(x, y, -1f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
					bullets.add(new Bullet(x, y, 0f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
				}
			}else if(stage == 2){
				if(toggle == true){
					toggle = false;
					for(int i=0; i<11; i++){
						bullets.add(new Bullet(x, y, 0f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
						bullets.add(new Bullet(x, y, 1f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
						bullets.add(new Bullet(x, y, -1f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
						bullets.add(new Bullet(x, y, 2f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
						bullets.add(new Bullet(x, y, -2f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
						bullets.add(new Bullet(x, y, 3f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
						bullets.add(new Bullet(x, y, -3f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
					}
				}else{
					toggle = true;
						bullets.add(new Enemy(x+40,y-40,0,4,"Data/Graphics/Level3/No Border/EL3 (No Border).png", volume));
						bullets.add(new Enemy(x,y,0,4,"Data/Graphics/Level3/No Border/EL3 (No Border).png", volume));
						bullets.add(new Enemy(x-40,y-40,0,4,"Data/Graphics/Level3/No Border/EL3 (No Border).png", volume));
				}
			}else{
				if(countShots % 10 == 0){
					bullets.add(new Blackhole(x,y));
				}
				bullets.add(new Bullet(x, y, 0f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
				bullets.add(new Bullet(x, y-40, 0f, 5f, 0, "Data/Graphics/Bullets/B3.png", true));
				countShots++;
			}
		}
		return bullets;
	}

	/**
	 * if the boss is spawned
	 * @return isSpawned boolean
	 */
    public boolean getBossSpawned()
    {
        return isSpawned;
    }

	/**
	 * set isSpawned
	 * @param isSpawned boolean
	 */
    public void  setBossSpawned(boolean isSpawned)
    {
        this.isSpawned = isSpawned;
    }

	/**
	 * isBossDefeated boolean to check if the boss has 0 health
	 * @return boolean
	 */
	public boolean isBossDefeated()
	{
        if (health > 0)
        {
            return false;
        }
        else if (health <= 0 )
        {
            return true;
        }
        else
        {
            System.out.println("Error boss health caused a return error.");
            return false;
        }
	}

	/**
	 * increment stage
	 */
	public void nextStage()
	{
		stage++;
		if(stage == 2){
			updateSprite("Data/Graphics/Boss/Boss 3-2.png");
		}else{
			updateSprite("Data/Graphics/Boss/Boss 3-3.png");
		}
	}

	/**
	 * overide collisionAction
	 * @param A check if there is collision between boss and A
	 * 
	 */
    public void collisionAction(Actor A)
    {
        if(Arrays.asList(destoryOnCollide).contains(A.getClass().toString())){
            if(A.getenemyBullet() == true){
                // System.out.println(A.getenemyBullet());
			}else{
                health -= 1;
				A.toRemove = true;
                if(health <= 0){
                    toRemove = true;
                    playSound("Data/Audio/explosion1.wav", 7*volume);
                }else if(health <= 150 && health > 100 && stage == 1){
					nextStage();
				}else if(health <= 100 && health > 50 && stage == 2){
					nextStage();
				}

            }
        }
    }

}
