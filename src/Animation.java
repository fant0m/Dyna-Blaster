import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Animation {

	private ArrayList<AFrame> frames;
	private int currentFrame;
	private long time;
	private long totalTime;
	private boolean moving;
	
	public Animation() {
		frames = new ArrayList<AFrame>();
		currentFrame = 0;
		time = 0;
		totalTime = 0;
		moving = false;
	}
	
	/**
	 * Pridanie �asu, ktor� uplynul od posledn�ho updatu + kontrola �i nie je treba zmeni� obr�zok v anim�cii.
	 * V pr�pade, �e �as prekro�il celkov� �as anim�cie tak sa anim�cia vr�ti na prv� obr�zok.
	 * @param addTime �as, ktor� uplynul
	 */
	public void update(int addTime) {
		time += addTime;
		
		if (time >= totalTime) {
			time = time % totalTime;
			currentFrame = 0;
		}
		
		if (time > getFrame(currentFrame).getDelay()) {
			currentFrame++;
		}
	}
	
	/**
	 * Getter pre obr�zok
	 * @param index
	 * @return
	 */
	public AFrame getFrame(int index) {
		return frames.get(index);
	}
	
	/**
	 * Pridanie obr�zku do anim�cie
	 * @param image
	 * @param delay �as zobrazenia obr�zku(ms)
	 */
	public void addFrame(BufferedImage image, long delay) {
		frames.add(new AFrame(image, totalTime+delay));
		totalTime += delay;
	}
	
	/**
	 * Vr�ti aktu�lny obr�zok pre vykreslenie
	 * @return
	 */
	public BufferedImage getCurrentImage() {
		if (!moving) {
			return frames.get(0).getImage();
		}
		return frames.get(currentFrame).getImage();
	}
	
	/**
	 * Zapnutie/vypnutie anim�cie
	 * @param moving
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
		if (!this.moving) {
			time = 0;
			currentFrame = 0;
		}
	}

	/**
	 * Trieda pre jednotliv� obr�zky anim�cie
	 * @author Dev
	 *
	 */
	public class AFrame {
		private BufferedImage image;
		private long delay;
		
		public AFrame(BufferedImage image, long delay) {
			this.image = image;
			this.delay = delay;
		}
		
		public BufferedImage getImage() {
			return image;
		}
		
		public long getDelay() {
			return delay;
		}
	}
	
}


