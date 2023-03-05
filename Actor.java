import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;

import org.jsfml.graphics.Font;

public class Actor
{
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private boolean collisionAllowed;
    public boolean destroyedByPlayer = false, toRemove = false;
	private String destoryOnCollide[] = {"class Player",};

    public Sprite actor;
    private Texture actorTexture = new Texture();
    public FloatRect bounds;
    public int x,y,r;
    private float dx,dy;
	public int health = 1;
    public double timeOfDeath;
    public Font spaceAge = new Font();
    public String imagePath = "";
    public int currentExp = 1;

    /**
     * Determines whether or not the current x and y speeds should change,
     * depending on current x and y positions.
    **/
    public void calcMove()
    {
        x += dx;
        y += dy;

        if(x <= 0 || x >= SCREEN_WIDTH){
            dx *= -1;
            x += dx;
        }

        if(y <= 0 || y >= SCREEN_HEIGHT){
            dy *= -1;
            y += dy;
        }
    }

    /**
	 * Method responsible for default collision action, overwritten in some child classes.
     * @param A Actor object which has been collided with.
	**/
    public void collisionAction(Actor A){}

    public ArrayList<Actor> explode(int level){return new ArrayList<Actor>(); }

    /**
	 * Determines whether or not an Actor instance should be destroyed.
     * @return Whether the Actor instance should be destroyed.
	**/
    public boolean remove()
    {
        if(toRemove == true){ return true; }
        else if(y>getScreenHeight()+bounds.height/2){return true;}
        else if(y<-30){return true; }
        else if(x > getScreenWidth() + bounds.width/2){return true;}
        else if(x < -30){return true; }
        else {return false;}
    }

    /**
	 * Compares current Actor instance against parameter Actor A to determine if they are colliding.
     * @param A Actor to be checked for collision.
	**/
    public void within(Actor A)
    {
        if( (x + (bounds.width/2) > A.x - (A.bounds.width/2)) && (x - (bounds.width/2) < A.x + (A.bounds.width)/2) ){
            if( (y + (bounds.height/2) > A.y - (A.bounds.height)/2) && (y - (bounds.height/2) < A.y + (A.bounds.height)/2) ){
                if(A.doesCollide()){
                    y -= dy;
                    x -= dx;
                }
                collisionAction(A);
            }
        }
    }

    /**
	 * Facilitates comparing all Actors be looping over Actors and called within method.
     * @param actors ArrayList containing all present actor instances on the screen.
     * @param currentActor Actor instance to be compared against all others.
	**/
    public void checkCollision(ArrayList<Actor> actors, Actor currentActor)
    {
    	calcMove();

    	for(Actor A: actors){
    		if(currentActor != A) { within(A); }
    	}
    }

	public void playSound(String pathToSound, int volume)
	{
		try{
			SoundBuffer soundBuff = new SoundBuffer();
			Sound sound;
			Path path = Paths.get(pathToSound);
			soundBuff.loadFromFile(path);
			sound = new Sound(soundBuff);
			sound.setVolume(volume);
			sound.play();
		}catch(IOException e){ System.out.println("Error retreiving sound.\n" + e); }
	}

    /**
	 * Loads new texture onto the sprite attribute of an actor.
     * @param imagePath Path from root of program execution to the required image file.
	**/
    public void updateSprite(String imagePath)
    {
        try{
            actorTexture.loadFromFile(Paths.get(imagePath));
            this.imagePath = imagePath;
        }catch(IOException e){
            e.printStackTrace();
        }

        actor = new Sprite(actorTexture);
        actor.setOrigin(Vector2f.div(new Vector2f(actorTexture.getSize()), 2));
        actor.setPosition(x, y);
    }

    /**
	 * Redrawing actor instance to the window, thus showing any changes made to position etc.
     * @param window RenderWindow that the actor instance will be drawn to.
	**/
    public void render(RenderWindow window)
    {
        SCREEN_WIDTH = window.getSize().x;
        SCREEN_HEIGHT = window.getSize().y;

        actor.setPosition(x, y);
        actor.setScale(1f, 1f);
        if(r != 0){ actor.rotate(r); }
        bounds = new FloatRect(x, y, actorTexture.getSize().x, actorTexture.getSize().y);

        window.draw(actor);
        drawAttachments(window);
    }

    /**
	 * Actor contructor, used to create a new actor, and it's associated attributes.
     * @param x X position.
     * @param y Y position.
     * @param dx Change in the x position every re-drawing of the actor.
     * @param dy Change in the y position every re-drawing of the actor.
     * @param r Rotation speed of current actor.
     * @param imagePath Path from root of program execution to the required image file.
     * @param collisionAllowed True or False value for whether this actor collides physically.
    **/
    public Actor(int x, int y, float dx, float dy, int r, String imagePath, boolean collisionAllowed)
    {
        this.x = x; this.y = y; this.dx = dx; this.dy = dy; this.r = r; this.collisionAllowed = collisionAllowed; this.imagePath = imagePath;

        try{ actorTexture.loadFromFile(Paths.get(imagePath));
        }catch(IOException e){ e.printStackTrace(); }

        try{ spaceAge.loadFromFile(Paths.get("Data/Graphics/Font/SpaceAge.ttf"));
		}catch (IOException ex) { ex.printStackTrace(); }

        actor = new Sprite(actorTexture);
        actor.setOrigin(Vector2f.div(new Vector2f(actorTexture.getSize()), 2));
        actor.setPosition(x, y);
        bounds = actor.getGlobalBounds();
    }

	public void setDX(float dx){ this.dx = dx; }

	public void setDY(float dy){ this.dy = dy; }

    public float getDX(){ return dx; }

    public float getDY(){ return dy; }

    public int getScreenWidth(){ return SCREEN_WIDTH; }

	public int getScreenHeight(){ return SCREEN_HEIGHT; }

    public void setScreenWidth(int SCREEN_WIDTH){ this.SCREEN_WIDTH = SCREEN_WIDTH; }

    public void setScreenHeight(int SCREEN_HEIGHT){ this.SCREEN_HEIGHT = SCREEN_HEIGHT; }

	public boolean getenemyBullet(){ return false; }

    public Texture getActorTexture(){ return actorTexture; }

    public boolean doesCollide(){ return collisionAllowed; }

	public String getType(){ return null; }

	public ArrayList<Bullet> shootBullet(){ return null; }

    public void setDoubleShot(boolean doubleShot){ }

    public void drawAttachments(RenderWindow window){};

    public void setTimeOfDeath(){ timeOfDeath = java.lang.System.currentTimeMillis(); }

    public void setSpeed(int speed){ }
}
