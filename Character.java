import java.util.ArrayList;

public class Character extends Actor{
	private String bulletPath;
	private boolean isEnemy, doubleShot = false;
	public int volume;

	/**
	 * Creates a bullet.
     * @return Instance of the class Bullet.
	**/
	public ArrayList<Bullet> shootBullet()
	{
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		float dy = -7f;
		if(isEnemy){ dy=5f; }
		playSound("Data/Audio/shot1.wav", 2*volume);

		if(doubleShot == false){
			bullets.add(new Bullet(x, y, 0f, dy, 0, bulletPath, isEnemy));
		}else{
			bullets.add(new Bullet(x-10, y, 0f, dy, 0, bulletPath, isEnemy));
			bullets.add(new Bullet(x+10, y, 0f, dy, 0, bulletPath, isEnemy));
		}
		return bullets;
	}


	public void setDoubleShot(boolean doubleShot){ this.doubleShot = doubleShot; }

	/**
	 * Character constructor, calls parent constructor and creates bullet type for this character.
     * @param x X position.
     * @param y Y position.
     * @param r Rotation speed of current actor.
     * @param textureFile Path from root of program execution to the required image file.
     * @param bulletPath Path from root of program execution to the required image file.
    **/
	public Character(int x, int y, int r, String textureFile, String bulletPath, boolean isEnemy, int volume){
		super(x, y, 0, 0, r, textureFile, false);
		this.bulletPath = bulletPath; this.isEnemy = isEnemy; this.volume = volume;
	}
}
