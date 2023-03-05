import java.util.Arrays;
import java.util.List;
/**
 * Gives the player higher fire rate, extra health  and increases speed by 2.
 *
 */
public class Powerup extends Actor
{
	private String destoryOnCollide[] = {"class Misc", "class Player"};
	private String type;

	/**
     * Overrites calcMove in Actor, prevents bullets colliding with the screen boundaries.
    **/
	public void calcMove()
	{
		x += getDX();
		y += getDY();
	}

	public String getType(){ return type; }

	/**
	 * Character constructor, calls parent constructor and creates bullet type for this character.
     * @param x X position.
     * @param y Y position.
     * @param r Rotation speed of current actor.
     * @param textureFile Path from root of program execution to the required image file.
	 * @param type Type used to determine what effect the powerup will have.
    **/
	public Powerup(int x, int y, int r, String textureFile, String type)
	{
		super(x, y, 0, 0, r, textureFile, false);
		this.type = type;
		setDY(2f);
	}
}
