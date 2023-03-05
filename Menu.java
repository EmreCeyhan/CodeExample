import org.jsfml.window.WindowStyle;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.*;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.Font;
import org.jsfml.audio.Music;
import org.jsfml.audio.SoundStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Random;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Image;

public class Menu
{
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 800;

	private static String[] potentialShipPaths = {"Data/Graphics/Spacecraft/spacecraft!.png", "Data/Graphics/Spacecraft/spacecraft1.png",
													"Data/Graphics/Spacecraft/spacecraft2.png", "Data/Graphics/Spacecraft/spacecraft3.png",
													"Data/Graphics/Spacecraft/spacecraft4.png", "Data/Graphics/Spacecraft/spacecraft5.png",
													"Data/Graphics/Spacecraft/spacecraft6.png"};
	private int p1ShipIndex = 0;
	private int p2ShipIndex = 0;

	private Random rand = new Random();
	private ArrayList<Bullet> stars = new ArrayList<Bullet>();
	private Font spaceAge = new Font();
	private int volume = 60, musicVol = 26;
	private boolean background = true, story = true;
	private String difficulty = "MEDIUM";
	private MusicPlayer music;

	/**
	 * Loads menu window and all visual/ audio components of the menu.
	**/
	public Menu()
	{
		RenderWindow window = new RenderWindow();
		window.create(new VideoMode(WINDOW_WIDTH, WINDOW_HEIGHT), "TYRIAN", WindowStyle.DEFAULT);
		window.setVerticalSyncEnabled(true);
		Image image = new Image();
    		try{ image.loadFromFile(Paths.get("Data/Graphics/Spacecraft/spacecraft1.png"));
		}catch (IOException ex) { ex.printStackTrace(); }
		window.setIcon(image);

		preMenu(window);

		Actor btnPlay = new Actor(300,400,0f,0f,0,"Data/Graphics/Buttons/button_play_selected.png", false);
		Actor btnSettings = new Actor(300,500,0f,0f,0,"Data/Graphics/Buttons/button_settings.png", false);
		Actor btnHelp = new Actor(300,600,0f,0f,0,"Data/Graphics/Buttons/button_help.png", false);
		Actor btnExit = new Actor(300,700,0f,0f,0,"Data/Graphics/Buttons/button_exit.png", false);
		int buttonOption = 0;

		try{ spaceAge.loadFromFile(Paths.get("Data/Graphics/Font/SpaceAge.ttf"));
		}catch (IOException ex) { ex.printStackTrace(); }

		Text title = new Text ("TYRIAN", spaceAge, 60); title.setColor(Color.WHITE); title.setPosition(160, 100);
		Text t1 = new Text ("P1", spaceAge, 24); t1.setColor(Color.WHITE); t1.setPosition(85, 430);
		Text t2 = new Text ("P2", spaceAge, 24); t2.setColor(Color.WHITE); t2.setPosition(470, 430);
		Character p1Choice = new Character(100, 500, 0, "Data/Graphics/Spacecraft/spacecraft!.png", "", false,10);
		Character p2Choice = new Character(490, 500, 0, "Data/Graphics/Spacecraft/spacecraft!.png", "", false,10);

		music = new MusicPlayer("menuMusic.wav");
		music.setVolume(musicVol);
        music.play();
		while (window.isOpen( ))
		{
			window.clear();
			drawStars(window, stars);

			btnPlay.render(window);
			btnSettings.render(window);
			btnHelp.render(window);
            btnExit.render(window);

			title.draw(window, RenderStates.DEFAULT);
			t1.draw(window, RenderStates.DEFAULT);
			t2.draw(window, RenderStates.DEFAULT);
			p1Choice.render(window);
			p2Choice.render(window);

			window.display( );

			for (Event event : window.pollEvents())
			{
				switch(event.type)
				{
					case CLOSED:
						System.out.println("The user pressed the close button!");
						music.stop();
						window.close();
						break;
					case KEY_PRESSED:
						KeyEvent keyEvent = event.asKeyEvent();
						if(keyEvent.key.toString().equals("UP") || keyEvent.key.toString().equals("W")){
                            if (buttonOption != 0)
                                buttonOption -= 1;

                            if (buttonOption == 0)
                            {
                                btnPlay.updateSprite("Data/Graphics/Buttons/button_play_selected.png");
                                btnSettings.updateSprite("Data/Graphics/Buttons/button_settings.png");
                            }
                            if (buttonOption == 1)
                            {
                                btnSettings.updateSprite("Data/Graphics/Buttons/button_settings_selected.png");
                                btnHelp.updateSprite("Data/Graphics/Buttons/button_help.png");
                            }
							if (buttonOption == 2)
							{
								btnHelp.updateSprite("Data/Graphics/Buttons/button_help_selected.png");
								btnExit.updateSprite("Data/Graphics/Buttons/button_exit.png");
							}
						}else if(keyEvent.key.toString().equals("DOWN") || keyEvent.key.toString().equals("S")){
                            if (buttonOption != 3)
                                buttonOption += 1;

                            if (buttonOption == 1)
                            {
                                btnPlay.updateSprite("Data/Graphics/Buttons/button_play.png");
								btnSettings.updateSprite("Data/Graphics/Buttons/button_settings_selected.png");
                            }
                            if (buttonOption == 2)
                            {
                                btnSettings.updateSprite("Data/Graphics/Buttons/button_settings.png");
    							btnHelp.updateSprite("Data/Graphics/Buttons/button_help_selected.png");
                            }
							if (buttonOption == 3)
                            {
                                btnHelp.updateSprite("Data/Graphics/Buttons/button_help.png");
    							btnExit.updateSprite("Data/Graphics/Buttons/button_exit_selected.png");
                            }

						}else if(keyEvent.key.toString().equals("A")){
							if(p1ShipIndex-1 < 0){ p1ShipIndex = potentialShipPaths.length-1; }
							else{ p1ShipIndex--; }
						}else if(keyEvent.key.toString().equals("D")){
							if(p1ShipIndex+1 == potentialShipPaths.length){ p1ShipIndex = 0; }
							else{ p1ShipIndex++; }
						}else if(keyEvent.key.toString().equals("LEFT")){
							if(p2ShipIndex-1 < 0){ p2ShipIndex = potentialShipPaths.length-1; }
							else{ p2ShipIndex--; }
						}else if(keyEvent.key.toString().equals("RIGHT")){
							if(p2ShipIndex+1 == potentialShipPaths.length){ p2ShipIndex = 0; }
							else{ p2ShipIndex++; }
						}else if(keyEvent.key.toString().equals("RETURN")  || keyEvent.key.toString().equals("SPACE")){
							if(buttonOption == 0){
								if(p1ShipIndex != 0){
									playSound("Data/Audio/menuSound.wav",volume);
                                    music.stop();
									Gameplay G = new Gameplay(window, potentialShipPaths[p1ShipIndex], potentialShipPaths[p2ShipIndex], volume,musicVol,background,story,difficulty);
									System.out.println("BACK IN MENU");
								}
							}else if(buttonOption == 1){
								playSound("Data/Audio/menuSound.wav",volume);
								settingsMenu(window);
							}else if(buttonOption == 2){
								helpMenu(window);
							}else if(buttonOption == 3){
								music.stop();
                                window.close();
                            }
						}
				}
				p1Choice.updateSprite(potentialShipPaths[p1ShipIndex]);
				p2Choice.updateSprite(potentialShipPaths[p2ShipIndex]);
			}
		}
	}

	/**
	 * Setting Page of the Menu
	 * @param window window to present
	 */
	private void settingsMenu(RenderWindow window)
	{
		int buttonOption = 0;
		String backgroundString, storyString;

		Actor btnVolume = new Actor(150,100,0f,0f,0,"Data/Graphics/Buttons/button_volume_selected.png", false);
		Actor btnMusic = new Actor(150,200,0f,0f,0,"Data/Graphics/Buttons/button_music.png", false);
		Actor btnBackground = new Actor(150,300,0f,0f,0,"Data/Graphics/Buttons/button_background.png", false);
        Actor btnStory = new Actor(150,400,0f,0f,0,"Data/Graphics/Buttons/button_story.png", false);
		Actor btnDifficulty = new Actor(150,500,0f,0f,0,"Data/Graphics/Buttons/button_difficulty.png", false);
		Actor btnExit = new Actor(300,600,0f,0f,0,"Data/Graphics/Buttons/button_exit.png", false);

		if(background == true){ backgroundString = "ON"; }
		else{ backgroundString = "OFF"; }

		if(story == true){ storyString = "ON"; }
		else{ storyString = "OFF"; }

		Text txtVolume = new Text (Integer.toString(volume), spaceAge, 45); txtVolume.setColor(Color.WHITE); txtVolume.setPosition(300, 60);
		Text txtMusic = new Text (Integer.toString(musicVol), spaceAge, 45); txtMusic.setColor(Color.WHITE); txtMusic.setPosition(300, 160);
		Text txtBackground = new Text (backgroundString, spaceAge, 45); txtBackground.setColor(Color.WHITE); txtBackground.setPosition(300, 260);
		Text txtStory = new Text (storyString, spaceAge, 45); txtStory.setColor(Color.WHITE); txtStory.setPosition(300, 360);
		Text txtDifficulty = new Text (difficulty, spaceAge, 45); txtDifficulty.setColor(Color.WHITE); txtDifficulty.setPosition(300, 460);

		boolean exit = false;

		while(exit == false)
		{
			window.clear();

			txtVolume.draw(window, RenderStates.DEFAULT);
			txtMusic.draw(window, RenderStates.DEFAULT);
			txtBackground.draw(window, RenderStates.DEFAULT);
			txtStory.draw(window, RenderStates.DEFAULT);
			txtDifficulty.draw(window, RenderStates.DEFAULT);

			btnVolume.render(window);
			btnMusic.render(window);
			btnBackground.render(window);
			btnStory.render(window);
			btnDifficulty.render(window);
			btnExit.render(window);

			for (Event event : window.pollEvents())
			{
				switch(event.type)
				{
					case CLOSED:
						window.close();
						break;
					case KEY_PRESSED:
						KeyEvent keyEvent = event.asKeyEvent();
						if(keyEvent.key.toString().equals("UP") || keyEvent.key.toString().equals("W")){
							if (buttonOption != 0)
								buttonOption--;
							if (buttonOption == 0){
								btnVolume.updateSprite("Data/Graphics/Buttons/button_volume_selected.png");
								btnMusic.updateSprite("Data/Graphics/Buttons/button_music.png");
							}
							if (buttonOption == 1){
								btnMusic.updateSprite("Data/Graphics/Buttons/button_music_selected.png");
								btnBackground.updateSprite("Data/Graphics/Buttons/button_background.png");
							}
							if (buttonOption == 2){
								btnBackground.updateSprite("Data/Graphics/Buttons/button_background_selected.png");
								btnStory.updateSprite("Data/Graphics/Buttons/button_story.png");
							}
							if (buttonOption == 3){
								btnStory.updateSprite("Data/Graphics/Buttons/button_story_selected.png");
								btnDifficulty.updateSprite("Data/Graphics/Buttons/button_difficulty.png");
							}
							if (buttonOption == 4){
								btnDifficulty.updateSprite("Data/Graphics/Buttons/button_difficulty_selected.png");
								btnExit.updateSprite("Data/Graphics/Buttons/button_exit.png");
							}
						}
                        else if(keyEvent.key.toString().equals("DOWN") || keyEvent.key.toString().equals("S")){
                            if (buttonOption != 5)
                                buttonOption++;
                            if (buttonOption == 1){
								btnVolume.updateSprite("Data/Graphics/Buttons/button_volume.png");
								btnMusic.updateSprite("Data/Graphics/Buttons/button_music_selected.png");
                            }
							if (buttonOption == 2){
								btnMusic.updateSprite("Data/Graphics/Buttons/button_music.png");
								btnBackground.updateSprite("Data/Graphics/Buttons/button_background_selected.png");
							}
                            if (buttonOption == 3){
								btnBackground.updateSprite("Data/Graphics/Buttons/button_background.png");
								btnStory.updateSprite("Data/Graphics/Buttons/button_story_selected.png");
                            }
							if (buttonOption == 4){
								btnStory.updateSprite("Data/Graphics/Buttons/button_story.png");
								btnDifficulty.updateSprite("Data/Graphics/Buttons/button_difficulty_selected.png");
							}
							if (buttonOption == 5){
								btnDifficulty.updateSprite("Data/Graphics/Buttons/button_difficulty.png");
								btnExit.updateSprite("Data/Graphics/Buttons/button_exit_selected.png");
							}
						}else if(keyEvent.key.toString().equals("RIGHT") || keyEvent.key.toString().equals("D")){
							if (buttonOption == 0){
								if(volume != 100){ volume++; }
								txtVolume.setString(Integer.toString(volume));
							}else if (buttonOption == 1){
								if(musicVol != 100){ musicVol++; }
								txtMusic.setString(Integer.toString(musicVol));
								music.setVolume(musicVol);
							}else if (buttonOption == 2){
								if(background == true){ txtBackground.setString("OFF"); background = false; }
								else{ txtBackground.setString("ON"); background = true; }
							}else if (buttonOption == 3){
								if(story == true){ txtStory.setString("OFF"); story = false; }
								else{ txtStory.setString("ON"); story = true; }
							}else if (buttonOption == 4){
								if(difficulty.equals("MEDIUM")){ txtDifficulty.setString("HARD"); difficulty = "HARD"; }
								else if(difficulty.equals("EASY")){ txtDifficulty.setString("MEDIUM"); difficulty = "MEDIUM"; }
							}
						}else if(keyEvent.key.toString().equals("LEFT") || keyEvent.key.toString().equals("A")){
							if (buttonOption == 0){
								if(volume != 0){ volume--; }
								txtVolume.setString(Integer.toString(volume));
							}else if (buttonOption == 1){
								if(musicVol != 0){ musicVol--; }
								txtMusic.setString(Integer.toString(musicVol));
								music.setVolume(musicVol);
							}else if (buttonOption == 2){
								if(background == true){ txtBackground.setString("OFF"); background = false; }
								else{ txtBackground.setString("ON"); background = true; }
							}else if (buttonOption == 3){
								if(story == true){ txtStory.setString("OFF"); story = false; }
								else{ txtStory.setString("ON"); story = true; }
							}else if (buttonOption == 4){
								if(difficulty.equals("MEDIUM")){ txtDifficulty.setString("EASY"); difficulty = "EASY"; }
								else if(difficulty.equals("HARD")){ txtDifficulty.setString("MEDIUM"); difficulty = "MEDIUM"; }
							}
						}else if(keyEvent.key.toString().equals("RETURN") || keyEvent.key.toString().equals("SPACE")){
							if(buttonOption == 5){
								playSound("Data/Audio/menuSound.wav",volume);
								exit = true;
							}
						}
					break;
				}
			}
			window.display();
		}
	}

	/**
	 * Help page from the Menu
	 * @param window window to present
	 */
	private void helpMenu(RenderWindow window)
	{
		Font helpFont = new Font();
		try{ helpFont.loadFromFile(Paths.get("Data/Graphics/Font/LucidaSansRegular.ttf"));
		}catch (IOException ex) { ex.printStackTrace(); }

		String instructions = "\t\t\t\t\t\t\t\t------------------------------\n\t\t\t\t\t\t\t\t------------ HELP  ------------\n\t\t\t\t\t\t\t\t------------------------------\n\n\t\t\t\t\t\t\t\t  Welcome to the rebellion cadet!\n\n		Your mission is to seek and destroy any enemy ahead!\n		Make your shots count as the world depends on you!\n		Here is your mission brief:\n\n		1: Destory enemy ships ahead! We need the mothership\n		   exposed so that we can focus on it with full power!\n\n		2: Watch out! Destoying the mothership will not be the end!\n		   There are still two others! Show them why we are a strong\n		   force!\n\n		And cadet go make us proud, our future is on your hands!\n\n		POWERUPS:\n		1: F - This item will give you a weapon with a higher fire rate!\n		2: S - This item will upgrade you engine to give you a better speed!\n		3: X - This item will add a second weapon to your ship to help take\n		       out more enemies at a time!\n\n		OBSTACLES:\n		1: Ship remains - When enemy ship is wrecked there is a chance that\n		the remains could come and hurt you so watch out!\n\n		2: Comets - Comets in space will come in your way! Dodge them or\n		pay the price!\n\n		3: Black Holes - Do not collide with the black hole as this will hurt\n		you! It will also suck your bullets in and throw them in an\n		unpredictable way!";

		String controls1 = "\t\t\t\t\t\t\t\t------------------------------\n\t\t\t\t\t\t\t\t --------- CONTROLS ---------\n\t\t\t\t\t\t\t\t------------------------------\n\n\t\t\t\t\t\t\t\t  Welcome to the rebellion cadet!\n\n		Player 1:\n				UP:        W\n				DOWN:  S\n				LEFT:     A\n				RIGHT:  D\n				SHOOT: SPACE\n				SPEED:  L SHIFT\n\n		Menu:\n				Navigating: W,A,S,D & UP,LEFT,DOWN,RIGHT\n				Selection:   ENTER & SPACE\n\n		Gameplay:\n				Pause: P";
		String controls2 = "		Player 2:\n				UP:        UP ARROW\n				DOWN:  DOWN ARROW\n				LEFT:     LEFT ARROW\n				RIGHT:  RIGHT ARROW\n				SHOOT: ENTER\n				SPEED:   R SHIFT";

		Text t1 = new Text (instructions, helpFont, 16); t1.setColor(Color.WHITE); t1.setPosition(0, 30);
		Text t2 = new Text (controls2, helpFont, 16); t2.setColor(Color.WHITE); t2.setPosition(300, 140);
		boolean run = true;

		while(run == true){
			window.clear();
			t1.draw(window, RenderStates.DEFAULT);
			if(t1.getString().equals(controls1)){
				t2.draw(window, RenderStates.DEFAULT);
			}
			window.display();

			for (Event event : window.pollEvents())
			{
				switch(event.type)
				{
					case CLOSED:
						window.close();
						break;
					case KEY_PRESSED:
						if(t1.getString().equals(instructions)){
							t1.setString(controls1);
						}else{ run = false; break; }
				}
			}
		}
	}

	/**
	 * A scene to introduce to the game
	 * @param window window to present
	 */
	private void preMenu(RenderWindow window)
	{
		window.clear();
		Font spaceAge = new Font();
		try{ spaceAge.loadFromFile(Paths.get("Data/Graphics/Font/SpaceAge.ttf"));
		}catch (IOException ex) { ex.printStackTrace(); }

		Text t1 = new Text ("TYRIAN", spaceAge, 60); t1.setColor(Color.WHITE); t1.setPosition(160, 100);
		Bullet a = new Bullet(300, 700, 0f, -2f, 0, "Data/Graphics/Spacecraft/spacecraft4.png",true);
		Bullet e = new Bullet(300, 830, 0f, -1f, 0, "Data/Graphics/Boss/Boss 2 - UP.png",true);
		ArrayList<Bullet> dust = new ArrayList<Bullet>();
		ArrayList<Bullet> enemies = new ArrayList<Bullet>();

		for(int c=0; c<200; c++){
			int x = rand.nextInt(600);
			int y = rand.nextInt(800);
			stars.add(new Bullet(x, y, 0f, 0f, 0, "Data/Graphics/Obstacle/star.png",true));
		}

		for(int x=30, y=890, i=0; i < 10; i++, x+=50, y-=30){
			if(x==280){ x = 360; y+=30; }
			else if(x == 610){ x = 30; }
			if(x>360){ y+=60; }
			enemies.add(new Bullet(x,y,0,-1f,0,"Data/Graphics/Level1/No Border/CL1 (No Border).png",true));
		}

		a.render(window);

		for(int i=0; i<100; i++){
			window.clear();
			drawStars(window, stars);
			a.calcMove(); a.render(window);
			window.display();
		}
		t1.draw(window, RenderStates.DEFAULT);
		window.display();

		playSound("Data/Audio/warpCharge.wav",70);
		try{ Thread.sleep(500); }catch(Exception ex){ ex.printStackTrace(); }
		for(int i=254; i>0; i--){
			window.clear();
			drawStars(window, stars);
			a.render(window);
			t1.setColor(new Color(i,i,i)); t1.draw(window, RenderStates.DEFAULT);
			window.display();
		}

		playSound("Data/Audio/warpJump.wav",70);
		for(int i=0; i<300; i++){
			window.clear();
			if(i<40){
				createDust(dust, a, "Data/Graphics/Obstacle/dust1.png"); createDust(dust, a, "Data/Graphics/Obstacle/dust2.png");
				createDust(dust, a, "Data/Graphics/Obstacle/dust3.png"); createDust(dust, a, "Data/Graphics/Obstacle/dust4.png");
				a.setDY(-20f); a.calcMove(); a.render(window);
			}
			drawDust(window, dust);
			drawStars(window, stars);
			window.display();
		}
		try{ Thread.sleep(200); }catch(Exception ex){ ex.printStackTrace(); }
	}

	/**
	 * Music playing
	 * @param pathToAudio String path to audio
	 * @param volume Integer of the volume
	 */
	private void playSound(String pathToAudio, int volume)
	{
		try{
			SoundBuffer soundBuff = new SoundBuffer();
			Sound sound;
			Path path = Paths.get(pathToAudio);
			soundBuff.loadFromFile(path);
			sound = new Sound(soundBuff);
			sound.setVolume(volume);
			sound.play();
		}catch(IOException e){ System.out.println("Error retreiving sound.\n" + e); }
	}

	/**
	 * create background
	 * @param dust ArrayList of Bullet to be added in
	 * @param a Actor for information
	 * @param path String path of the image
	 */
	private void createDust(ArrayList<Bullet> dust, Actor a, String path)
	{
		float dx = rand.nextInt(8)-4;
		float dy = rand.nextInt(16)+4;
		dust.add(new Bullet(a.x, a.y, dx, dy, 0, path,false));
		dx = rand.nextInt(4)-2;
		dy = rand.nextInt(16)+4;
		dust.add(new Bullet(a.x, a.y, dx, dy, 0, path,false));
	}

	/**
	 * render stars
	 * @param window window to present
	 * @param stars Arraylist Bullet to render
	 */
	private void drawStars(RenderWindow window, ArrayList<Bullet> stars)
	{
		for(Bullet B: stars){
			B.render(window);
		}
	}

		/**
	 * draw moving dust
	 * @param window window present
	 * @param dust ArrayList of Bullet
	 */
	private void drawDust(RenderWindow window, ArrayList<Bullet> dust)
	{
		for(Bullet D: dust){
			D.calcMove();
			D.render(window);
		}
	}



}
