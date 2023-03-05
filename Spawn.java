import java.util.*;
import java.lang.*;

public class Spawn{
	private long present = java.lang.System.currentTimeMillis();
	private long lastSpawn = present;
	private Random rand = new Random();
	private int EPR = 2, ECount=EPR, volume;	//EPR = Enemy Per Round
	private Level levels;
	//private Level[] levels;
	/**
	 * Timer 
	 */
	public Spawn(){

	}
	/**
	 * Spawner
	 * @param enter boolean
	 * @param volume int
	 */
	public Spawn(/*int amountOfLevels*/boolean enter, int volume){//boolean for trial identifying different constructor
		this.volume = volume;
		levels = new Level(1, volume);
	}
	/**
	 * @java.lang.System.currentTimeMillis return current time in long
	 */
	public long time(){
		return java.lang.System.currentTimeMillis();
	}
	/**
	 * @param SpawnTime long set last Spawn
	 */
	public void setLastSpawn(long SpawnTime){
		lastSpawn = SpawnTime;
	}
	/**
	 * @return lastSpawn get last Spawn
	 */
	public long getLastSpawn(){
		return lastSpawn;
	}
	/**
	 * @param range randomise range
	 * @return rand.nextInt(range)+1 return random number
	 */
	public int RandomCor(int range){
		return rand.nextInt(range)+1;
	}
	/**
	 * @param level int 
	 * @return Enemy return Enemy class
	 */
	public Enemy getEnemy(int level){
		if(0==ECount){
			ECount=EPR;
			return levels.getEnemy(level);
		}else{
			ECount--;
			return levels.getOldEnemy(level);
		}
	}
	/**
	 * @param level int 
	 * @return Boss return Boss for specific level
	 */
    public Boss getBoss(int level){
		String texture = "Data/Graphics/Boss/Boss 1.png";
		if(level == 1){texture = "Data/Graphics/Boss/Boss 1.png";}
		else if(level == 2){texture = "Data/Graphics/Boss/Boss 2.png";}
		else if(level == 3){texture = "Data/Graphics/Boss/Boss 3.png";}
        return new Boss(300,-30,0,texture,volume, level);
    }
	/**
	 * @return levels.isBossSpawn() boolean
	 */
    public boolean isSpawn(){
		return levels.isBossSpawn();
	}

}
