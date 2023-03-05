import java.util.*;
import java.lang.*;

public class Level{
	private int stages;	//if you have 3 levels this tells you which level you are at
	private int Rounds = 10, isSpawn = 1;//define how many rounds in the level
	private boolean bossDefeated = false; // after 10 rounds boss will appear and will continue spawning enemies until boss is defeated
	private String[] EnemyImage = {"Data/Graphics/Level1/No Border/CL1 (No Border).png","Data/Graphics/Level1/No Border/CL2 (No Border).png","Data/Graphics/Level1/No Border/CL3 (No Border).png",
									"Data/Graphics/Level2/No Border/DL1 (No Border).png","Data/Graphics/Level2/No Border/DL2 (No Border).png","Data/Graphics/Level2/No Border/DL3 (No Border).png",
									"Data/Graphics/Level3/No Border/EL1 (No Border).png","Data/Graphics/Level3/No Border/EL2 (No Border).png","Data/Graphics/Level3/No Border/EL3 (No Border).png"};
	public int x, mode;
	private int volume;
	private Random rand = new Random();
	/**
	 * @param stages specific stages
	 * @param volume volume int
	 */
	public Level(int stages, int volume){ //stages = 1-3
		this.stages = stages; this.volume = volume;
	}
	/**
	 * Spawn new Enemy type
	 * @param level int 1-3
	 */
	public Enemy getEnemy(int level){
		x = rand.nextInt(500)+10;
		mode = rand.nextInt(3) + ((level-1)*3);
		Enemy newBorn = new Enemy(x,0,0,mode,EnemyImage[mode], volume);
		x = newBorn.getOppositeSide(x);
		if(x>=600||x<=0){x= rand.nextInt(500)+10;}
		Rounds--;
		//System.out.println(Rounds);
		return newBorn;
	}
	/**
	 * get previous Enemy
	 * @param level int 1-3
	 */ 
	public Enemy getOldEnemy(int level){
		mode = rand.nextInt(3) + ((level-1)*3);
		Enemy newBorn = new Enemy(x,0,0,mode,EnemyImage[mode], volume);
		x = newBorn.getOppositeSide(x);
		if(x>=600||x<=0){x= rand.nextInt(500)+10;}
		return newBorn;
	}
	/**
	 * determind SpawnBoss in Level
	 * @return boolean
	 */
	public boolean SpawnBoss(){
		if(Rounds==0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * check if the boss spawn
	 * @return boolean
	 */
	public boolean isBossSpawn(){
		if(isSpawn==1){return false;}else{return true;}
	}
	/**
	 * reset isSpawn
	 */
	public void isSpawnM(){
		isSpawn=0;
	}
}
