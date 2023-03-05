import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
	/**
	 * Spawns a mine which explodes when colliding with a bullet
	 * @param volume Volume of the explosion sound
    **/
public class Mine extends Bullet
{
	private int volume;
	private String destoryOnCollide[] = {"class Misc", "class Bullet"};
	/**
	 * Overrites collisionAction in Actor, allows enemy specific collision behaviour.
	 * @param A Current actor instance which has collided with something.
	 **/
	public void collisionAction(Actor A)
	{
		if(Arrays.asList(destoryOnCollide).contains(A.getClass().toString())){
			toRemove = true;
			playSound("Data/Audio/explosion1.wav", 7*volume);
		}
	}


	public ArrayList<Actor> explode(int level)
	{
		ArrayList<Actor> mines = new ArrayList<Actor>();
		mines.add(new Bullet(x, y, 0f, -7f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, 3f, -6f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, 6f, -3f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, 7f, 0f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, 6f, 3f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, 3f, 6f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, 0f, 7f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, -3f, 6f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, -6f, 3f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, -7f, 0f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, -6f, -3f, 0, "Data/Graphics/Bullets/B2.png",false));
		mines.add(new Bullet(x, y, -3f, -6f, 0, "Data/Graphics/Bullets/B2.png",false));
		return mines;
	}


	public Mine(int x, int volume)
	{
		super(x, -10, 0f, 3f, 2, "Data/Graphics/Mines/mine.png",true);
		this.volume = volume;
	}
}
