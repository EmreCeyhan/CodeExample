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
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
/**
 * 
 */
public class Story{
	private int height, width;
	private String playerSkin;
	/**
	 * Generate a scene of the game 
	 * @param window window to present
	 * @param playerSkin String of the imagepath
	 */
	public Story(RenderWindow window, String playerSkin){
		this.playerSkin = playerSkin;
		StoryBegin(window);
	}
	/**
	 * Method to start scene
	 * @param window window to present
	 */
	public void StoryBegin(RenderWindow window){

		Font spaceAge = new Font();
		try{ spaceAge.loadFromFile(Paths.get("Data/Graphics/Font/SpaceAge.ttf"));
		}catch (IOException ex) { ex.printStackTrace(); }

		ArrayList<Bullet> stars = new ArrayList<Bullet>();
		Random rand = new Random();

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
		Background b1 = new Background(300, 0 - height, 0, backgroundFile, height, width);
		Background b2 = new Background(300, 0, 0,backgroundFile, height, width);
		
		Bullet spaceStation = new Bullet(50,400,0f,0f,0, "Data/Graphics/Story/SpaceStation.png",true);

		String[] shipSkins = new String[]{"Data/Graphics/Spacecraft/spacecraft1.png",
										"Data/Graphics/Spacecraft/spacecraft2.png",
										"Data/Graphics/Spacecraft/spacecraft3.png",
										"Data/Graphics/Spacecraft/spacecraft4.png",
										"Data/Graphics/Spacecraft/spacecraft5.png",
										"Data/Graphics/Spacecraft/spacecraft6.png",
										"Data/Graphics/Level1/No Border/CL1 (No Border).png",
										"Data/Graphics/Level1/No Border/CL2 (No Border).png",
										"Data/Graphics/Level1/No Border/CL3 (No Border).png",
										"Data/Graphics/Level2/No Border/DL1 (No Border).png",
										"Data/Graphics/Level2/No Border/DL2 (No Border).png",
										"Data/Graphics/Level2/No Border/DL3 (No Border).png",
										"Data/Graphics/Level3/No Border/EL1 (No Border).png",
										"Data/Graphics/Level3/No Border/EL2 (No Border).png",
										"Data/Graphics/Level3/No Border/EL3 (No Border).png"};

		String[] text = new String[]{"AFTER ESCAPING IMPIRIAL PRISON ABOARD A \nSTOLEN SHIP, YOU TRAVEL THE GALAXY \nFEARFUL OF THE CAESARS RETRIBUTION.",
									"KNOWING THAT YOU'RE BEING HUNTED YOU \nKEEP A LOW PROFILE, LITTLE DO YOU \nKNOW THAT DOING SUCH WAS DRAWING \nTHE ATTENTION OF A DIFFERENT GROUP.",
									"AFTER A LONG NIGHT IN A TRULY VILE BAR\nON STATION  1NF0LA8 IT COMES TO YOUR\nATTENTION THAT YOU'RE BEING FOLLOWED,\nSPINNING QUICKLY AND DRAWING YOUR\nWEAPON YOU PREPARE TO FIGHT...",
									"HOLD YOUR FIRE!\nI'M HERE TO MAKE A PROPOSITION.",
									"TALK FAST I HAVE LITTLE TIME FOR IMPIRIAL\nSPIES.",
									"I'M A REBELION GUNNER, MY PILOT WAS\nKILLED AND I'M STRANDED HERE. YOU SEEM\nTO STAY IN THE SHADOWS TO, SO I GUESS\nYOU'RE NO FRIEND OF THE EMPIRE?",
									"AS IT HAPPENS YOU'RE RIGHT, BUT WHY\nSHOULD I HELP YOU?\nYOU SAID YOURSELF I'M TRYING TO STAY\nOUT OF THE EMPIRES WAY!",
									"WHY?\nTO GIVE THE EMPIRE WHAT IT DESERVES,\nAND STOP THE CAESARS REIGN OF TERROR!",
									"OK, BUT WE DO THIS MY WAY.",
									"AGREED!"};

		int count = 0, textI = 0;
		boolean textboxout = true, run = true;
		Bullet textBox = new Bullet(290,690,0f,0f,0, "Data/Graphics/Story/textbox2.png",true);
		Text storyText = new Text (text[textI], spaceAge, 18); storyText.setColor(Color.WHITE); storyText.setPosition(25,630);
		int spaceX = 300,spaceY=900;
		int charX = 150, charY = 800;

		while(run == true){
			window.clear();

			b1.render(window);
			b2.render(window);
			
			spaceStation.render(window);
			
			//stars
			if(count%23==0){
				int x = rand.nextInt(600);
				int direction = rand.nextInt(2);
				int shipSkin = rand.nextInt(shipSkins.length);
				if(direction == 0){
					Bullet B = new Bullet(x, -20, 0f, 4f, 0, shipSkins[shipSkin],true);
					if(shipSkin < 6){ B.actor.rotate(180); }
					stars.add(B);
				}else{
					Bullet B = new Bullet(x, 820, 0f, -4f, 0, shipSkins[shipSkin],true);
					if(shipSkin > 5){ B.actor.rotate(180); }
					stars.add(B);
				}
			}
			ArrayList<Integer> removelist=new ArrayList<Integer>();
			int removecount =0;
			for(Bullet B: stars){
				if(B.y>800){
					removelist.add(removecount);
				}
				removecount++;
			}
			for(Integer i : removelist){
				stars.remove(i);
			}

			if(spaceY>=400){
				spaceY-=3;
			}

			for(Bullet B: stars){
				B.calcMove();
				B.render(window);
			}
			if(textI==3 || textI==5 || textI==7 || textI==9){
				if(charY>=500){ charY-=5; }
				Bullet charac = new Bullet(charX,charY,0f,0f,0,"Data/Graphics/IntroCharacter/Captain.png",true);
				charac.render(window);
			}
			
			Bullet spaceship = new Bullet(spaceX,spaceY,0f,0f,0,playerSkin,true);
			spaceship.render(window);

			if(textboxout==true && spaceY<500){
				textBox.render(window);
				storyText.setString(text[textI]);
				storyText.draw(window, RenderStates.DEFAULT);
			}

			for (Event event : window.pollEvents()){
				switch(event.type){
					case CLOSED:
						System.out.println("The user pressed the close button!");
						window.close();
						break;
					case KEY_PRESSED:
						if(spaceY < 400){
							textI++;
						}
						//if(textI==5){ charY=800; }
				}
			}

			if(textI >= 10){
				System.out.println(spaceY);
				break;
			}
			window.display();
			count++;
		}
	}
}
