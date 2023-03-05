public class Background extends Actor{
	private float speed = 2f;
	int width  = 0;
  int height = 0;

	public Background(int x, int y, int r, String textureFile, int height, int width){
		super(x,y,0f,0f,r,textureFile, false);
		this.height = height;
		this.width = width;
	}

	public void move(Background second)
	{
		// System.out.println(this.y + "   " + second.y);
		// System.out.println(this.x);
		this.y   += 3;
		second.y += 3;
		if (this.y - (height / 2) > 0 + 800) //if out of screen then reset on top
		{
			this.y = second.y - height; //reset image y position
		}
		if(second.y - (height / 2) > 0 + 800) //if image 2 out of screen reset to top
		{
			second.y = this.y - height; // reset image 2 y position
		}
	}
	// Moves the map in the oposite direction of the player movement
	public void moveWithPlayer(Background second, String key)
	{
		if (this.x > 250 && key.equals("D")) //if out of screen then reset on top
		{
			this.x   -= 1;
			second.x -= 1;
		}
		if (this.x < 350 && key.equals("A")) //if out of screen then reset on top
		{
			this.x   += 1;
			second.x += 1;
		}
		if (key.equals("W")) //if out of screen then reset on top
		{
			this.y   += 1;
			second.y += 1;
		}
	}
}
