import org.jsfml.system.Vector2i;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.Color;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.Keyboard;
import org.jsfml.graphics.Text;
import java.lang.Thread;
import org.jsfml.system.Time;
import org.jsfml.system.Clock;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Font;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.StringBuilder;
import java.util.*;
import java.io.*;

public class Gameplay
{
	private int MAX_ENEMIES = 10, SPAWN_TIME = 800, SHOOT_CHANCE = 995, volume, level = 1, userScore = 0;
	private int height, width, leaderboardScore, seconds = 0;
    private Font spaceAge = new Font();
	private Background b1, b2;
	private String playerOnePath, playerTwoPath;
	private Bullet p1, p2, p3;

    /**
	 * Loads all visual components of the main gameplay state onto the renderWindow, game starts here.
     * @param window RenderWindow used first to make the menu, passed into this class to avoid having to close one window and open another.
	**/
    public Gameplay(RenderWindow window, String playerOnePath, String playerTwoPath, int volume, int musicVol, boolean hasBackground, boolean hasStory, String difficulty)
    {
		this.playerOnePath = playerOnePath; this.playerTwoPath = playerTwoPath;

		MusicPlayer music = new MusicPlayer("menuMusic.wav");
		MusicPlayer music2 = new MusicPlayer("sci-fi-music.wav");
		music.setVolume(musicVol);
		music2.setVolume(musicVol);
		music.play();

		if(hasStory == true){ Story stry = new Story(window, playerOnePath); }

		music.stop();
		music2.play();

		if(difficulty.equals("EASY")){
			MAX_ENEMIES = 5;
			SPAWN_TIME = 2000;
			SHOOT_CHANCE = 998;
		}else if(difficulty.equals("MEDIUM")){
			SHOOT_CHANCE = 995;
			SPAWN_TIME = 800;
			MAX_ENEMIES = 10;
		}else if(difficulty.equals("HARD")){
			MAX_ENEMIES = 20;
			SPAWN_TIME = 500;
			SHOOT_CHANCE = 992;
		}

        try{ spaceAge.loadFromFile(Paths.get("Data/Graphics/Font/SpaceAge.ttf"));
        }catch (IOException ex) { ex.printStackTrace(); }

        int numOfEnemies = 0; //number of enemies
        this.volume = volume;
    	ArrayList<Actor> actors = new ArrayList<Actor>(); //arraylist that will hold the objects in game
        boolean bossSpawned = false;//stop multiple bosses spawning
        int pointstowardsboss = 0;//points are kept to have mutliple bosess
        int pointsTillNxtBoss = 30;
		Spawn timer = new Spawn(true,volume), timer2 = new Spawn(), timer3 = new Spawn(), bossTimer = new Spawn();
		int bulletDelayP1 = 300, bulletDelayP2 = 300; //delay time between each bullet
		Random rand = new Random(); //Allows us to randomise items
		int speed = 0;

        Player player1 = new Player(300,399,0,playerOnePath,volume); //Creating player 1
        Player player2 = new Player(350,399,0,playerTwoPath,volume); //Creating player 2

		// The location of the background file
		String backgroundFile = "Data/Graphics/Background/b2.jpg";
		// Get the resolution of the image
		try
		{
			BufferedImage bimg = ImageIO.read(new File(backgroundFile));
			height = bimg.getHeight();
			width  = bimg.getWidth();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		/*Creating two backgrounds so that they replace eachother when out of bounds to look smooth*/
		b1 = new Background(300, 0 - height, 0, backgroundFile, height, width);
		b2 = new Background(300, 0, 0,backgroundFile, height, width);

		window.clear();
		b1.render(window);
		b2.render(window);

		if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
			actors.add(player2);
			player2.render(window);
			player1.x = 250;
		}
		player1.render(window);

		actors.add(new Powerup(300,-10, 0, "Data/Graphics/Powerup/Powerup_X.png","DAMAGE-UP"));
    	actors.add(player1); //add player one to arraylist

		Text timeDisplay = new Text();
		Text score = new Text();
		Font font = new Font();
		try{
			font.loadFromFile(Paths.get("Data/Graphics/Font/LucidaBrightDemiBold.ttf"));
		}catch(IOException ex){ex.printStackTrace();}
		timeDisplay.setFont(font);
		timeDisplay.setCharacterSize(20);
		timeDisplay.setColor(Color.WHITE);
		timeDisplay.setPosition(280, 770);
		timeDisplay.setString("00:00");

		score.setFont(font);
		score.setCharacterSize(20);
		score.setColor(Color.WHITE);
		score.setPosition(50, 770);
		score.setString("0");

		Clock clock = new Clock();
		clock.restart();
		int minutes = 0;

		int oldNetHeath = player1.getHealth() + player2.getHealth();
		Text levelText = new Text ("LEVEL " + Integer.toString(level), spaceAge, 60); levelText.setColor(Color.WHITE); levelText.setPosition(150, 100);

		window.draw(levelText, RenderStates.DEFAULT);
		window.display();
		try{ Thread.sleep(600); }catch(Exception ex){ ex.printStackTrace(); }
		/*If the window is open on screen*/
    	while (window.isOpen())
    	{
			window.clear();
			int newNetHealth = player1.getHealth() + player2.getHealth();
			if(oldNetHeath > newNetHealth){ userScore -= (oldNetHeath - newNetHealth)*250; }
			oldNetHeath = newNetHealth;

            if(hasBackground == true){
    			b1.render(window);
    			b2.render(window);
                b1.move(b2);
            }

			window.draw(timeDisplay);
			window.draw(score);
			seconds = (int)clock.getElapsedTime().asSeconds();
			if(seconds > 59)
			{
				seconds = 0;
				minutes++;
				clock.restart();
			}
			timeDisplay.setString(String.format("%02d:%02d", minutes, seconds));
			score.setString(String.format("%d", seconds + userScore));

            if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
                if(player2.getHealth() <= 0){
                    if(player1.getHealth() <= 0){
                        Text txtOver = new Text ("GAME OVER", spaceAge, 45); txtOver.setColor(Color.WHITE); txtOver.setPosition(140, 160);
                        txtOver.draw(window, RenderStates.DEFAULT);
                        window.display();
						leaderboardScore = seconds + userScore + 250;
						leaderboard(window);
                        break;
                    }
                }else{
					if(player2.increaseDamage){ player2.setDoubleShot(true); player2.increaseDamage = false;}
				 	if(player2.increaseFirerate){ bulletDelayP2 = bulletDelayP2-25; player2.increaseFirerate = false;}
   			 	}
            }else if(player1.getHealth() <= 0){
                 Text txtOver = new Text ("GAME OVER", spaceAge, 45); txtOver.setColor(Color.WHITE); txtOver.setPosition(140, 160);
                 txtOver.draw(window, RenderStates.DEFAULT);
                 window.display();
				 leaderboardScore = seconds + userScore + 250;
				 leaderboard(window);
                 break;
             }

			if(player1.getHealth() > 0){
				if(player1.increaseDamage){ player1.setDoubleShot(true); player1.increaseDamage = false; }
				if(player1.increaseFirerate){ bulletDelayP1 = bulletDelayP1-25; player1.increaseFirerate = false; }
			}

             player1.CheckInvincibility();
             player2.CheckInvincibility();

    		//spawn enemies
    		if(timer.time()-timer.getLastSpawn()>=SPAWN_TIME&&numOfEnemies<MAX_ENEMIES){
				if(bossSpawned == false && level < 4){
					actors.add(timer.getEnemy(level)); //spawn enemy with time intervals
                	numOfEnemies++; //increment number of enemy counter
    				timer.setLastSpawn(timer.time());
				}
    		}
            //spawn a boss
            if(bossSpawned == false && pointstowardsboss >= pointsTillNxtBoss)
            {
				Boss B = bossTimer.getBoss(level);
				if(level == 3){
					for(Actor A: actors){
						if(A.getClass().toString().equals("class Enemy")){
							while(A.y < 820){
								window.clear();
								b1.render(window);
								b2.render(window);
								for(Actor Ac: actors){
									if(Ac.getClass().toString().equals("class Player")){
										Ac.setDX(0f);
										Ac.setDY(0f);
									}
									Ac.calcMove();
									Ac.render(window);
								}
								A.y = A.y + 30;
								A.render(window);
								window.display();
							}
						}
					}
					if(finalBossCutscene(window, B, player1, player2)){
						break;
					}else{
						for(Actor Ac: actors){
							if(!Ac.getClass().toString().equals("class Player")){
								Ac.toRemove = true;
							}
						}
					}
				}
				actors.add(B);
				B.setTimeOfDeath();
				pointstowardsboss = 0;
                bossSpawned = true;
    		}

    		//list of removing enemy
    		ArrayList<Integer> removelist = new ArrayList<Integer>();
    		//move enemy and remove when reach to end of screen
    		if(actors.size()!=0){
    			int indexCount=0;
    			for(Actor A: actors){
                    A.checkCollision(actors, A); //colision detection
                    A.render(window); //render the actors onto the screen
    				if(A.remove()){
						if(A.getClass().toString().equals("class Enemy")){
							A.setDY(0f); A.setDX(0f); A.setSpeed(0);
							if(A.timeOfDeath == 0){
								A.setTimeOfDeath();
							}
							if(java.lang.System.currentTimeMillis() - A.timeOfDeath > 50){
								if(!A.imagePath.equals("Data/Graphics/Explosion/6.png")){
									A.currentExp++;
									A.updateSprite("Data/Graphics/Explosion/" + A.currentExp + ".png");
									A.setTimeOfDeath();
								}else{
									removelist.add(indexCount);
								}
							}
						}else{
							removelist.add(indexCount);
						}
    				}else{
    					A.render(window);
    				}
    				indexCount++;
    			}
    		}

    		if(removelist.size()!=0){
				boolean end = false;
    			for(int temp = removelist.size()-1;temp>=0;temp--){
					int x = actors.get(removelist.get(temp)).x;
					int y = actors.get(removelist.get(temp)).y;
                    if(actors.get(removelist.get(temp)).getClass().toString().equals("class Enemy")){
						userScore += 10; //increase user score when enemy
                        numOfEnemies--;
						pointstowardsboss++;

						if(actors.get(removelist.get(temp)).destroyedByPlayer == true){ userScore += 100; }
						else{ userScore -= 50; }

                        if(rand.nextInt(10)>8){
                            actors.add(new Bullet(x, y, 1f, 5f, 3, "Data/Graphics/Obstacle/tube.png",true));
                            actors.add(new Bullet(x, y, -1f, 5f, 3, "Data/Graphics/Obstacle/spacecraft wings.png",true));
                        }
						//SPAWN COMET - MAKE ME ON TIMER SOMEWHERE ELSE
                        if(rand.nextInt(10)>8){
                            int multi = 1;
                            if(rand.nextInt(1) == 1){ x = 630; multi = -1; }else{ x = -30; }
                            actors.add(new Bullet(x, rand.nextInt(600), multi*2f, 3f, 3, "Data/Graphics/Obstacle/comet.png", true));
                        }
						//SPAWN MINE - MAKE ME ON TIMER SOMEWHERE ELSE
						if(rand.nextInt(15)>13){
							actors.add(new Mine(rand.nextInt(600), volume));
						}
						//SPAWNING POWERUPS
						if(rand.nextInt(15)==14){
							int powerup = rand.nextInt(4);
							if(powerup == 0){ actors.add( new Powerup(300,-10, 0, "Data/Graphics/Powerup/Powerup_X.png","DAMAGE-UP")); }
							else if(powerup == 1){ actors.add( new Powerup(300,-10, 0, "Data/Graphics/Powerup/Powerup_S.png","SPEED-UP")); }
							else if(powerup == 2){ actors.add( new Powerup(300,-10, 0, "Data/Graphics/Powerup/Powerup_F.png","FIRERATE-UP")); }
							else if(powerup == 3){ actors.add( new Powerup(300,-10, 0, "Data/Graphics/Powerup/Powerup_Heart.png","HEALTH-UP")); }
						}
                    }
					if(actors.get(removelist.get(temp)).getClass().toString().equals("class Mine")){
                        actors.addAll(actors.get(removelist.get(temp)).explode(level));
					}
                    if(actors.get(removelist.get(temp)).getClass().toString().equals("class Boss")){
                        bossSpawned = false; //boss no longer spawned
						boolean goneAbove = false;
						level++;
						userScore += 1000;

						int xPos = actors.get(removelist.get(temp)).x;
						int yPos = actors.get(removelist.get(temp)).y;

                        pointsTillNxtBoss+=30; //increment points till next boss
						actors.remove(actors.get(removelist.get(temp)));
						if( level <= 3){
							if( level == 2){
								p1 = new Bullet(xPos,yPos,-3f,-2f,-3, "Data/Graphics/Boss/Boss 1-1.png",true);
								p2 = new Bullet(xPos,yPos,-3f,1f,-2, "Data/Graphics/Boss/Boss 1-2.png",true);
								p3 = new Bullet(xPos,yPos,3f,0f,1, "Data/Graphics/Boss/Boss 1-3.png",true);
							}else{
								p1 = new Bullet(xPos,yPos,-3f,-2f,-3, "Data/Graphics/Boss/Boss 2-1.png",true);
								p2 = new Bullet(xPos,yPos,-3f,1f,-2, "Data/Graphics/Boss/Boss 2-2.png",true);
								p3 = new Bullet(xPos,yPos,3f,0f,1, "Data/Graphics/Boss/Boss 2-3.png",true);
							}
							while(true){
								if(player1.y < -30){
									player1.y = 820;
									if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
										if(player2.y < -30){
											player2.y = 820;
											goneAbove = true;
										}
									}else{
										goneAbove = true;
									}
									if(level == 2){
										b1.updateSprite("Data/Graphics/Background/b1.jpg");
										b2.updateSprite("Data/Graphics/Background/b1.jpg");
									}else if(level == 3){
										b1.updateSprite("Data/Graphics/Background/b3.jpg");
										b2.updateSprite("Data/Graphics/Background/b3.jpg");
									}
								}

								window.clear();
								b1.render(window);
								b2.render(window);

								if(level == 2 && goneAbove == false || level == 3 && goneAbove == false){
									p1.calcMove();
									p2.calcMove();
									p3.calcMove();
									p1.render(window);
									p2.render(window);
									p3.render(window);
								}

								player1.y = player1.y - 15;
								if(player1.getHealth() > 0){
									player1.render(window);
								}

								if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
									player2.y = player2. y - 15;
									if(player2.getHealth() > 0){
										player2.render(window);
									}
								}

								window.display();

								if(player1.y < 550 && goneAbove == true){
									window.clear();
									b1.render(window);
									b2.render(window);
									levelText.setString("LEVEL " + Integer.toString(level));
									if(player1.getHealth() > 0){
										player1.render(window);
									}
									if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
										if(player2.getHealth() > 0){
											player2.render(window);
										}
									}
									window.draw(levelText, RenderStates.DEFAULT);
									window.display();
									try{ Thread.sleep(600); }catch(Exception ex){ ex.printStackTrace(); }
									break;
								}
							}
							for(Actor Ac: actors){
								if(!Ac.getClass().toString().equals("class Player")){
									Ac.toRemove = true;
								}
							}

						}

						if(player1.getHealth() <= 0){
							player1.updateSprite(playerOnePath);
							player1.toRemove = false;
							actors.add(player1);
						}

						if(player2.getHealth() <= 0){
							player2.updateSprite(playerTwoPath);
							player2.toRemove = false;
							actors.add(player2);
						}
						if(player1.getHealth() < 3){
							player1.modifyHealth(3-player1.getHealth());
						}
						if(player2.getHealth() < 3){
							player2.modifyHealth(3-player2.getHealth());
						}

						if(level == 4){
							Bullet p1 = new Bullet(xPos,yPos,3f,-2f,-1, "Data/Graphics/Boss/Boss 3-3-1.png",true);
							Bullet p2 = new Bullet(xPos,yPos,4f,0f,1, "Data/Graphics/Boss/Boss 3-3-2.png",true);
							Bullet p3 = new Bullet(xPos,yPos,1f,3f,1, "Data/Graphics/Boss/Boss 3-3-3.png",true);
							Bullet p4 = new Bullet(xPos,yPos,-2f,1f,-1, "Data/Graphics/Boss/Boss 3-3-4.png",true);
							Bullet p5 = new Bullet(xPos,yPos,-3f,-2f,-1, "Data/Graphics/Boss/Boss 3-3-5.png",true);
							double startTime = java.lang.System.currentTimeMillis();
							while(java.lang.System.currentTimeMillis() - startTime < 2000){
								window.clear();
								b1.render(window); b2.render(window);
								p1.calcMove(); p2.calcMove(); p3.calcMove(); p4.calcMove(); p5.calcMove();
								p1.render(window); p2.render(window); p3.render(window); p4.render(window); p5.render(window);
								if(player1.getHealth()>0){
									player1.render(window);
								}
								if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
									if(player2.getHealth() > 0){
										player2.render(window);
									}
								}
								window.display();
							}
							leaderboardScore = seconds + userScore + 250;
							endScreen(window);
							leaderboard(window);
							end = true;
							break;
						}
					}
                    actors.remove(actors.get(removelist.get(temp)));
    			}
				if(end){
					break;
				}
    		}

			int index = 0;
			ArrayList<Integer> shootingEnemies = new ArrayList<Integer>();
			for(Actor A: actors){
				if(A.getClass().toString().equals("class Enemy") && rand.nextInt(1000)>SHOOT_CHANCE){
					shootingEnemies.add(index);
				}
				if(A.getClass().toString().equals("class Boss") && (bossTimer.time()-bossTimer.getLastSpawn() >= 1000)){
					bossTimer.setLastSpawn(bossTimer.time());
					shootingEnemies.add(index);
				}
				index++;
			}

			if(shootingEnemies.size()!=0){
				for(int i = shootingEnemies.size()-1; i>=0; i--){
					if(actors.get(shootingEnemies.get(i)).getClass().toString().equals("class Boss")){
						if(java.lang.System.currentTimeMillis() - actors.get(shootingEnemies.get(i)).timeOfDeath > 5000){
							actors.addAll(actors.get(shootingEnemies.get(i)).explode(level));
						}
					}else{ actors.addAll(actors.get(shootingEnemies.get(i)).shootBullet()); }
				}
			}

    		player1.setDY(0f);
    		player1.setDX(0f);
            player2.setDY(0f);
    		player2.setDX(0f);
    		window.display();

    		for (Event event : window.pollEvents())
    		{
				if(event.type == event.type.CLOSED)
				{
					System.out.println("The user pressed the close button!");
					music.stop();
					music2.play();
					window.close();
				}
				if(event.type == event.type.KEY_RELEASED)
				{
					KeyEvent keyEvent = event.asKeyEvent();
					if(keyEvent.key == Keyboard.Key.LSHIFT)
						player1.decreaseSpeed();
				}
				if(event.type == event.type.KEY_RELEASED)
				{
					KeyEvent keyEvent = event.asKeyEvent();
					if(keyEvent.key == Keyboard.Key.RSHIFT)
						player2.decreaseSpeed();
				}
			}

			//Pause key control
			if(Keyboard.isKeyPressed(Keyboard.Key.P)){
				if(pauseMenu(window)){ break; }
			}
            //player1 1 key control
			if(Keyboard.isKeyPressed(Keyboard.Key.W))
			{
				player1.up();
				b1.moveWithPlayer(b2, "W");
			}
			if(Keyboard.isKeyPressed(Keyboard.Key.S))
			{
				player1.down();
			}
			if(Keyboard.isKeyPressed(Keyboard.Key.A))
				{
					player1.left();
					b1.moveWithPlayer(b2, "A");
				}
			if(Keyboard.isKeyPressed(Keyboard.Key.D))
				{
					player1.right();
					b1.moveWithPlayer(b2, "D");
				}
			if(Keyboard.isKeyPressed(Keyboard.Key.LSHIFT))
				player1.increaseSpeed();
			if(Keyboard.isKeyPressed(Keyboard.Key.SPACE) &&
				 timer2.time() - timer2.getLastSpawn() >= bulletDelayP1 &&
                 player1.getHealth()>0)
				 {
					 actors.addAll(player1.shootBullet());
					 timer2.setLastSpawn(timer2.time());
				 }

             //player1 2 key control
			if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
				 if(Keyboard.isKeyPressed(Keyboard.Key.UP))
					{
						player2.up();
						b1.moveWithPlayer(b2, "W");
					}
				 if(Keyboard.isKeyPressed(Keyboard.Key.DOWN))
					 player2.down();
				 if(Keyboard.isKeyPressed(Keyboard.Key.LEFT))
					{
						player2.left();
						b1.moveWithPlayer(b2, "A");
					}
				 if(Keyboard.isKeyPressed(Keyboard.Key.RIGHT))
					{
						player2.right();
						b1.moveWithPlayer(b2, "D");
					}
				 if(Keyboard.isKeyPressed(Keyboard.Key.RSHIFT))
					 player2.increaseSpeed();
				 if(Keyboard.isKeyPressed(Keyboard.Key.RETURN) &&
					  timer3.time() - timer3.getLastSpawn() >= bulletDelayP2 &&
					  player2.getHealth()>0)
					  {
						  actors.addAll(player2.shootBullet());
						  timer3.setLastSpawn(timer3.time());
					  }
            }
    	}
        try{ Thread.sleep(3000); }catch(Exception ex){ ex.printStackTrace(); }
    }


	private boolean finalBossCutscene(RenderWindow window, Boss boss, Player player1, Player player2)
	{
		player1.setDY(0f); player1.setDX(0f);
		String[] text = new String[]{"HALT!\nYOUR CAESAR DEMANDS IT!","STOP NOW?\nAFTER WE'VE COME ALL THIS WAY,\nWHY WOULD WE DO THAT?","BECAUSE ONE MORE SHOT AND I WON'T\nBE AS MERCIFUL AS MY PATHETIC\nUNDERLINGS!","SURRENDER NOW, AND RECOGNISE MY\nDIVINE RIGHT TO RULE THE GALAXY,\nOR SUFFER AN EXCRUITIATING DEATH.","WE CHOOSE...","DID YOU REALLY THINK I'D HAVE\nMERCY?\nTIME TO DIE YOU TRAITOROUS\nSCUM!","FOOL!"};
		int textI = 0, buttonOption = 1, y = boss.y, charX = 150, charY = 800;
		boolean bossInPlace = false;

		Bullet textBox = new Bullet(290,690,0f,0f,0, "Data/Graphics/Story/textbox2.png",true);
		Bullet charac = new Bullet(charX,charY,0f,0f,0,"Data/Graphics/IntroCharacter/emperor.png",true);
		Text storyText = new Text (text[textI], spaceAge, 18); storyText.setColor(Color.WHITE); storyText.setPosition(25,630);
		Text gameOver = new Text ("GAME OVER", spaceAge, 60); gameOver.setColor(Color.WHITE); gameOver.setPosition(100, 100);

		Actor btnForgive = new Actor(200,400,0f,0f,0,"Data/Graphics/Buttons/button_forgive.png", false);
		Actor btnFight = new Actor(400,400,0f,0f,0,"Data/Graphics/Buttons/button_fight_selected.png", false);

		while(true){
			window.clear();
			b1.render(window);
			b2.render(window);
			boss.render(window);

			if(player1.y < 395){
				player1.setDY(3f);
			}else if(player1.y > 399){
				player1.setDY(-3f);
			}else{
				player1.setDY(0f);
			}

			if(player2.y < 395){
				player2.setDY(3f);
			}else if(player2.y > 399){
				player2.setDY(-3f);
			}else{
				player2.setDY(0f);
			}

			if(playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
				if(player1.getHealth() > 0){
					if(player1.x < 295){
						player1.setDX(3f);
					}else if(player1.x > 305){
						player1.setDX(-3f);
					}else{
						player1.setDX(0f);
					}
				}
			}else{
				if(player1.getHealth() > 0){
					if(player2.getHealth() > 0){
						if(player1.x < 315){
							player1.setDX(3f);
						}else if(player1.x > 320){
							player1.setDX(-3f);
						}else{
							player1.setDX(0f);
						}

						if(player2.x < 275){
							player2.setDX(3f);
						}else if(player2.x > 280){
							player2.setDX(-3f);
						}else{
							player2.setDX(0f);
						}
					}else{
						if(player1.x < 295){
							player1.setDX(3f);
						}else if(player1.x > 305){
							player1.setDX(-3f);
						}else{
							player1.setDX(0f);
						}
					}
				}else{
					if(player2.x < 295){
						player2.setDX(3f);
					}else if(player2.x > 305){
						player2.setDX(-3f);
					}else{
						player2.setDX(0f);
					}
				}
			}

			if(player1.getHealth() > 0){
				player1.calcMove();
				player1.render(window);
			}

			if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
				if(player2.getHealth() > 0){
					player2.calcMove();
					player2.render(window);
				}
			}

			if(bossInPlace == true){
				if(textI == 0 || textI == 2 || textI == 3 || textI == 5){
					if(charac.y>=500){ charac.y = (charac.y-5); }
					charac.render(window);
				}

				if(textI == 4){
					btnFight.render(window);
					btnForgive.render(window);
				}

				textBox.render(window);
				window.draw(storyText, RenderStates.DEFAULT);

				for(Event event : window.pollEvents()){
					switch(event.type){
						case CLOSED:
							System.out.println("The user pressed the close button!");
							window.close();
							break;
						case KEY_PRESSED:
							if(textI < 4){ textI++; }
							else if(textI == 4){
								if(Keyboard.isKeyPressed(Keyboard.Key.A) || Keyboard.isKeyPressed(Keyboard.Key.LEFT)){
									if(buttonOption == 1){
										buttonOption = 0;
										btnForgive.updateSprite("Data/Graphics/Buttons/button_forgive_selected.png");
										btnFight.updateSprite("Data/Graphics/Buttons/button_fight.png");
									}
								}
								if(Keyboard.isKeyPressed(Keyboard.Key.D) || Keyboard.isKeyPressed(Keyboard.Key.RIGHT)){
									if(buttonOption == 0){
										buttonOption = 1;
										btnForgive.updateSprite("Data/Graphics/Buttons/button_forgive.png");
										btnFight.updateSprite("Data/Graphics/Buttons/button_fight_selected.png");
									}
								}
								if(Keyboard.isKeyPressed(Keyboard.Key.RETURN) || Keyboard.isKeyPressed(Keyboard.Key.SPACE)){
									if(buttonOption == 0){
										textI++;
									}else{
										return false;
									}
								}
							}else{
								Bullet one = new Bullet(315,boss.y,0f,4f,0,"Data/Graphics/Bullets/B3.png",false);
								Bullet two = new Bullet(285,boss.y,0f,4f,0,"Data/Graphics/Bullets/B3.png",false);
								while(true){
									window.clear();
									b1.render(window);
									b2.render(window);
									boss.render(window);
									if(player1.getHealth() > 0){
										player1.render(window);
									}
									if(!playerTwoPath.equals("Data/Graphics/Spacecraft/spacecraft!.png")){
										if(player2.getHealth() > 0){
											player2.render(window);
											if(player1.getHealth() > 0){
												two.y = (two.y + 3);
												two.render(window);
											}
										}
									}
									
									one.y = (one.y + 3);
									one.render(window);
									window.display();
									if(one.y >= 400){
										break;
									}
								}
								window.clear();
								b1.render(window);
								b2.render(window);
								boss.render(window);
								gameOver.draw(window, RenderStates.DEFAULT);
								window.display();
								try{ Thread.sleep(600); }catch(Exception ex){ ex.printStackTrace(); }
								return true;
							}
							storyText.setString(text[textI]);
					}
				}
			}else{
				y+=3;
				if(y < 200){
					boss.y = y;
				}else{
					bossInPlace = true;
				}
			}
			window.display();
		}
	}




	private Boolean pauseMenu(RenderWindow window)
	{

		Text pause = new Text ("PAUSED", spaceAge, 60); pause.setColor(Color.WHITE); pause.setPosition(160, 100);
		Actor btnPlay = new Actor(300,400,0f,0f,0,"Data/Graphics/Buttons/button_play_selected.png", false);
		Actor btnExit = new Actor(300,500,0f,0f,0,"Data/Graphics/Buttons/button_exit.png", false);

		int buttonOption = 0;

		while (true)
		{
			pause.draw(window, RenderStates.DEFAULT);
			btnPlay.render(window);
			btnExit.render(window);

			window.display( );

			for (Event event : window.pollEvents())
			{
				switch(event.type)
				{
					case CLOSED:
						System.out.println("The user pressed the close button!");
						window.close();
						break;
					case KEY_PRESSED:
						if(Keyboard.isKeyPressed(Keyboard.Key.UP) || Keyboard.isKeyPressed(Keyboard.Key.W)){
							buttonOption = 0;
							btnPlay.updateSprite("Data/Graphics/Buttons/button_play_selected.png");
							btnExit.updateSprite("Data/Graphics/Buttons/button_exit.png");
							break;
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.DOWN) || Keyboard.isKeyPressed(Keyboard.Key.S)){
							buttonOption = 1;
							btnPlay.updateSprite("Data/Graphics/Buttons/button_play.png");
							btnExit.updateSprite("Data/Graphics/Buttons/button_exit_selected.png");
							break;
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.RETURN) || Keyboard.isKeyPressed(Keyboard.Key.SPACE)){
							if(buttonOption == 0){
								return false;
							}else if(buttonOption == 1){
								return true;
							}
							break;
						}
				}
			}
		}
	}

	public void leaderboard(RenderWindow window)
	{
		//Reading values
		String[] readValues = new String[20];
        try{
            BufferedReader br = new BufferedReader(new FileReader("Data/leaderboard.txt"));
            String currentLine = br.readLine();

            for(int i=0; i<20; i=i+2){
                if(currentLine != null){
                    String splitLine[] = currentLine.split(": ");
                    readValues[i] = splitLine[0];
                    readValues[i+1] = splitLine[1];
                    currentLine = br.readLine();
                }else{
                    readValues[i] = Integer.toString(i+1) + ": ";
                    readValues[i+1] = "0";
                }
            }
        }catch(Exception e){
            System.out.println("ERROR: " + e);
        }

		//Ordering Scores
		for(int i=0; i<20; i=i+2){
            if(leaderboardScore > Integer.parseInt(readValues[i+1])){
                String tempScore = readValues[i+1];
                String tempName = readValues[i];

                readValues[i+1] = Integer.toString(leaderboardScore);
                readValues[i] = Integer.toString(i);

                leaderboardScore = Integer.parseInt(tempScore);
                readValues[i] = tempName;
            }
        }

		//Writing to file
		try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("Data/leaderboard.txt"));
            String buffer;

            for(int i=0; i<20; i=i+2){
                buffer = readValues[i] + ": " + readValues[i+1];
                bw.write(buffer);
                bw.newLine();
            }
            bw.close();

        }catch(Exception e){
            System.out.println("ERROR: " + e);
        }

		leaderboardScore = seconds + userScore + 250;
		Text title = new Text ("LEADERBOARD", spaceAge, 60); title.setColor(Color.WHITE); title.setPosition(10, 50);
		Text t2 = new Text ("YOUR SCORE: "+leaderboardScore, spaceAge, 36); t2.setColor(Color.WHITE); t2.setPosition(50, 700);
		Text[] t1 = new Text[10];
		String leaderboardText = "";
		int count = 0;
		for(int i=0; i<readValues.length; i+=2){
			leaderboardText += readValues[i] + ": " + readValues[i+1];
			t1[count] =  new Text(leaderboardText, spaceAge, 60); t1[count].setColor(Color.WHITE); t1[count].setPosition(50, 80+(count+1)*50);
			leaderboardText = "";
			count++;
		}

		boolean shouldRun = true;
		while(shouldRun == true){
			window.clear();
			window.draw(title, RenderStates.DEFAULT);
			window.draw(t2, RenderStates.DEFAULT);
			for(int i=0; i<10; i++){
				window.draw(t1[i], RenderStates.DEFAULT);
			}
			window.display();

			for (Event event : window.pollEvents()){
				switch(event.type){
					case CLOSED:
						window.close();
						break;
					case KEY_PRESSED:
						shouldRun = false;
						break;
				}
			}
		}
	}

	public void endScreen(RenderWindow window)
	{
		int y = 100, y2 = 850;
		String scrollText = "WITH THE CAESAR DEAD AND\nHIS FORCES DESTROYED YOU\nHAVE BROUGHT PEACE TO THE\nGALAXY.\n\nFREE FROM TYRANY AT LAST\nTHE GALAXY REJOICES THE\nREBELIONS SUCCESS.\n\nYOU TURN TO YOUR GUNNER\nAND SMILE, IT IS DONE, THE\nCAESAR IS DEAD AND WITH\nHIM WENT THE NEED FOR YOU\nTOO RUN, YOU ESCAPED YOUR\nPAST AND OVERCAME ALL ODDS.\n\nCONGRATULATION!\n\nSCORE: " + Integer.toString(leaderboardScore);
		Text victory = new Text ("VICTORY", spaceAge, 60); victory.setColor(Color.WHITE); victory.setPosition(160, y);
		Text outtro = new Text (scrollText, spaceAge, 24); outtro.setColor(Color.WHITE); outtro.setPosition(70, y+720);
		Text title = new Text ("TYRIAN", spaceAge, 60); title.setColor(Color.WHITE); title.setPosition(160, 100);

		victory.draw(window, RenderStates.DEFAULT);
		window.display();
		try{ Thread.sleep(2000); }catch(Exception ex){ ex.printStackTrace(); }

		while(true){
			window.clear();
			victory.setPosition(160, y-=2);
			outtro.setPosition(70, (y2-=1));

			b1.render(window);
			b2.render(window);

			victory.draw(window, RenderStates.DEFAULT);
			outtro.draw(window, RenderStates.DEFAULT);
			window.display();

			if(y2<-440){
				for(int i=0; i<255; i++){
					window.clear();

					b1.render(window);
					b2.render(window);

					title.setColor(new Color(i,i,i));
					title.draw(window, RenderStates.DEFAULT);
					window.display();
				}
				break;
			}
		}

	}

}
