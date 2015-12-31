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
	 * Pridanie èasu, ktorý uplynul od posledného updatu + kontrola èi nie je treba zmeni obrázok v animácii.
	 * V prípade, že èas prekroèil celkový èas animácie tak sa animácia vráti na prvý obrázok.
	 * @param addTime èas, ktorý uplynul
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
	 * Getter pre obrázok
	 * @param index
	 * @return
	 */
	public AFrame getFrame(int index) {
		return frames.get(index);
	}
	
	/**
	 * Pridanie obrázku do animácie
	 * @param image
	 * @param delay èas zobrazenia obrázku(ms)
	 */
	public void addFrame(BufferedImage image, long delay) {
		frames.add(new AFrame(image, totalTime+delay));
		totalTime += delay;
	}
	
	/**
	 * Vráti aktuálny obrázok pre vykreslenie
	 * @return
	 */
	public BufferedImage getCurrentImage() {
		if (!moving) {
			return frames.get(0).getImage();
		}
		return frames.get(currentFrame).getImage();
	}
	
	/**
	 * Zapnutie/vypnutie animácie
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
	 * Trieda pre jednotlivé obrázky animácie
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


