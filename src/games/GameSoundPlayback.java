package games;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.Control;

public class GameSoundPlayback implements Runnable{
	protected static File gameOverSound = new File("sound\\GameOverSound.wav");
	protected static File noMatchSound = new File("sound\\NoMatchSound.wav");
	protected static File matchSound = new File("sound\\MatchSound.wav");
	protected static Clip gameOverSoundClip;
	protected static Clip noMatchSoundClip;
	protected static Clip matchSoundClip;
	protected AudioInputStream gameOverAudioInputStream;
	protected AudioInputStream noMatchAudioInputStream;
	protected AudioInputStream matchAudioInputStream;
	protected static Control[] noMatchSoundClipControls;
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			gameOverAudioInputStream = AudioSystem.getAudioInputStream(gameOverSound);
			gameOverSoundClip = AudioSystem.getClip();
			gameOverSoundClip.open(gameOverAudioInputStream);
			noMatchAudioInputStream = AudioSystem.getAudioInputStream(noMatchSound);
			noMatchSoundClip = AudioSystem.getClip();
			noMatchSoundClip.open(noMatchAudioInputStream);
			matchAudioInputStream = AudioSystem.getAudioInputStream(matchSound);
			matchSoundClip = AudioSystem.getClip();
			matchSoundClip.open(matchAudioInputStream);			
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
