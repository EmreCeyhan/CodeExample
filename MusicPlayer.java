import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MusicPlayer
{
    SoundBuffer soundBuff = new SoundBuffer();
    Sound sound;
    float volume = 40;


	public MusicPlayer(String SongName)
    {
        try
        {

			Path path = Paths.get("Data/Audio/"+SongName);
			soundBuff.loadFromFile(path);
			sound = new Sound(soundBuff);
			sound.setVolume(volume);
            sound.setLoop(true);
		}
        catch(IOException e)
        {
            System.out.println("Error retreiving sound.\n" + e);
        }

	}
	public void play()
    {
        sound.play();
	}
    public void stop()
    {
        sound.stop();
	}
	public void setVolume(float selectedVolume)
    {
        volume = selectedVolume;
        sound.setVolume(volume);
    }
    public void stop(float selectedVolume)
    {
        sound.stop();
    }
}
//https://jsfml.sfmlprojects.org/javadoc/org/jsfml/audio/Sound.html#isLoop()
