public class Bullet extends Actor
{
	private boolean enemyBullet;

	public boolean getenemyBullet(){ return enemyBullet; }

    /**
     * Overrites calcMove in Actor, prevents bullets colliding with the screen boundaries.
    **/
    public void calcMove()
    {
        x += getDX();
        y += getDY();
    }

    /**
	 * Character constructor, calls parent constructor and creates bullet type for this character.
     * @param x X position.
     * @param y Y position.
     * @param dx Change in x position every re-draw.
     * @param dy Change in y position every re-draw.
     * @param r Rotation speed of current actor.
     * @param imagePath Path from root of program execution to the required image file.
    **/
    public Bullet(int x, int y, float dx, float dy, int r, String imagePath, boolean enemyBullet)
    {
        super(x, y, dx, dy, r, imagePath, false);
		this.enemyBullet = enemyBullet;
    }
}
