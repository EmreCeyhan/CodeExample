import java.util.ArrayList;
import java.util.Arrays;
import java.lang.*;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
/**
 * The main player class that extends the character class,
 * creating a spaceship sprite that acts as the contestant
 * @param health the hp of the player
 * @param hearts displays an icon for a heart that follows the player
 * @param healthText text displaying the numer of lives
 * @param lastHit remember the last time the player took damage
 * @param intialSpeed the normal speed
 * @param speed The speed with which the player is currently moving
 */
public class Player extends Character{
	private int health = 3;
	private Actor hearts;
	private Text healthText;
    private long present = java.lang.System.currentTimeMillis();
    private long lastHit = present;
    private boolean quHit = false;
	private float initialSpeed = 2f, speed;
	private String destoryOnCollide[] = {"class Mine", "class Bullet", "class Powerup", "class Enemy", "class Boss", "class Blackhole"};
	private String textureFile;
	public boolean increaseDamage = false, increaseFirerate = false;


	/**
	 * Player constructor, calls parent constructor.
     * @param x X position.
     * @param y Y position.
     * @param r Rotation speed of current actor.
     * @param textureFile Path from root of program execution to the required image file.
    **/
	public Player(int x, int y, int r, String textureFile, int volume){
		super(x,y,r,textureFile, "Data/Graphics/Bullets/B2.png", false, volume);
		this.speed = this.initialSpeed; this.textureFile = textureFile;
		hearts = new Actor(x-15,y+35,0f,0f,0,"Data/Graphics/Hud/h1.png",false); hearts.actor.setScale(1f, 1f);
		healthText = new Text("x "+Integer.toString(health), spaceAge, 15); healthText.setColor(Color.WHITE); healthText.setPosition(x, y);
	}

	public void drawAttachments(RenderWindow window)
    {
		hearts.actor.setPosition(x-15, y+35);
		window.draw(hearts.actor);

		healthText.setPosition(x, y+25);
		healthText.setString("x "+Integer.toString(health));
		window.draw(healthText, RenderStates.DEFAULT);
    }

	/**
	 * Overrites collisionAction in Actor, allows enemy specific collision behaviour.
     * @param A Current actor instance which has collided with something.
	**/
	public void collisionAction(Actor A)
	{
		if(Arrays.asList(destoryOnCollide).contains(A.getClass().toString())){
			if(A.getenemyBullet() == false){
				if(A.getClass().toString().equals("class Powerup")){
					A.toRemove = true;
					playSound("Data/Audio/powerup1.wav", 2*volume);
					upgrade(A.getType());
				}
                if(A.getClass().toString().equals("class Enemy")){
                    loseHP();
                    if(health == 0){ toRemove = true; }
    				else{ playSound("Data/Audio/explosion1.wav", 7*volume); }
				}
                if(A.getClass().toString().equals("class Boss")){
                    loseHP();
                    if(health == 0){ toRemove = true; }
                    else{ playSound("Data/Audio/explosion1.wav", 7*volume); }
                }
				if(A.getClass().toString().equals("class Blackhole")){
					loseHP();
					if(health == 0){ toRemove = true; }
					else{ playSound("Data/Audio/explosion1.wav", 7*volume); }
				}
			}else{
				loseHP();
				A.toRemove = true;
				System.out.println("HEALTH: " + health);
				if(health == 0){ toRemove = true; }
				else{ playSound("Data/Audio/explosion1.wav", 7*volume); }
			}
		}
	}
	/**
	 * Decreases the hp of the playeer by 1
	 * also checks if the last hit happened in the last 3 milliseconds
    **/
    private void loseHP()
    {
        present = java.lang.System.currentTimeMillis();
        if (present > lastHit + 3000 )
		{
            health--;
            lastHit = present;
            quHit = true;
			String[] splitString = textureFile.split(".png");
            updateSprite( splitString[0] + "-hit.png");
        }
		if(health == 0){
			increaseDamage = false;
			increaseFirerate = false;
			speed = initialSpeed;
		}
    }
	/**
	 * Checks if the player is currently invincible
	 * The player has a certain delay when getting a powerup
    **/
    public void CheckInvincibility()
    {
        present = java.lang.System.currentTimeMillis();
        if (present > lastHit + 3000 && quHit == true)
        {
            updateSprite(textureFile);
            playSound("Data/Audio/powerup1.wav", 1*volume);
            quHit = false;
        }
    }
	/**
	 * Checks the type of the powerup the player has
	 * Modifies the player's specifications accordingly
    **/
	private void upgrade(String powerupType)
	{
		if(powerupType.equals("SPEED-UP")){ initialSpeed+=3; speed=initialSpeed; }
		else if(powerupType.equals("DAMAGE-UP")){ increaseDamage = true; }
		else if(powerupType.equals("FIRERATE-UP")){ increaseFirerate = true; }
		else if(powerupType.equals("HEALTH-UP")){ health++; }
	}


	/** * Changes change in dy to speed **/
	public void down(){
		setDY(speed);
	}

	/** * Changes change in dy to -speed **/
	public void up(){
		setDY(-speed);
	}

	/** * Changes change in dx to -speed **/
	public void left(){
		setDX(-speed);
	}

	/** * Changes change in dx to speed **/
	public void right(){
		setDX(speed);
	}

	/**
	 * Returns the current health of the player
    **/
	public int getHealth(){
		return health;
	}

	/**
	 * Increases or decreases the hp of the player
    **/
	public void modifyHealth(int value){
		health += value;
	}

	/**
	 * TIncreases the speed of the playyer by 3 when pressing the shift key
    **/
	public void increaseSpeed(){
		this.speed = this.initialSpeed * 3;
	}
	/**
	 * Sets the player's speed to normal after it has been increased
    **/
	public void decreaseSpeed(){
		this.speed = this.initialSpeed;
	}
}
