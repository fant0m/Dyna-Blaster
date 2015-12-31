import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;



public class Map {
	
	private boolean ready;
	private int time;
	private int score;
	
	private BufferedImage sprites;
	private ArrayList<Enemy> enemies;
	private Collision collision;
	private BufferedReader in;
	
	public Map(Collision collision) {
		this.ready = false;
		this.enemies = new ArrayList<Enemy>();
		this.collision = collision;
	}
	
	/**
	 * Na��tanie levela
	 * @param level level, ktor� sa m� na��ta�
	 * @return
	 * @throws IOException
	 */
	public Tile[][] loadMap(int level) {
		
		try {
			sprites = ImageIO.read(getClass().getClassLoader().getResource("data/sprites.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        in = new BufferedReader(new InputStreamReader(Map.class.getResourceAsStream("/maps/" + level + ".txt")));

		String row = null;
		try {
			row = in.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String[] data = row.split("x");
		Tile[][] output = new Tile[Integer.parseInt(data[1])][Integer.parseInt(data[0])];
		
		time = Integer.parseInt(data[2]);
		score = Integer.parseInt(data[3]);
		
		try {
			row = in.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String[] mapEnemies = row.split("x");
		for (String s : mapEnemies) {
			String[] enemyData = s.split("-");
			Animation dyingEnemy = new Animation();
			dyingEnemy.addFrame(sprites.getSubimage(442, 233 , 16, 18), 200);
			dyingEnemy.addFrame(sprites.getSubimage(458, 233, 16, 18), 200);
			Enemy e = new Enemy(Integer.parseInt(enemyData[1]) * 32, Integer.parseInt(enemyData[0]) * 32, collision, dyingEnemy);
			enemies.add(e);
		}
		
		
		int yIndex = -1;
		int xIndex = -1;
		ArrayList<Block> blocks = new ArrayList<Block>();
		
        try {
			while((row = in.readLine()) != null) {
				yIndex++;
				xIndex = -1;
				for (int i = 0; i < row.length(); i++){
					xIndex++;
				    char c = row.charAt(i);
			        if (c == '0') {
			        	output[yIndex][xIndex] = new Tile(xIndex, yIndex, 0, true);
			        } else if (c == '1') {
			        	output[yIndex][xIndex] = new Tile(xIndex, yIndex, 1, false);
			        } else if (c == '2') {
			        	output[yIndex][xIndex] = new Block(xIndex, yIndex, 2, false);
			        	blocks.add((Block) output[yIndex][xIndex]);
			        }
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        Random random = new Random();
        int exit = random.nextInt(blocks.size());
        blocks.get(exit).setHasExit(true);
        

        try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return output;
	}
	
	/**
	 * Getter pre arraylist nepriate�ov
	 */
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	/**
	 * Odpo��tavanie �asu do kedy mus� hr�� vyhra� dan� level
	 */
	public void countdown() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				time--;
				if (time < 0) {
			        timer.cancel();
			        timer.purge();
			        return;
				}
				
			}
		};
		timer.schedule(task, 0L, 1000L);
	}
	
	/**
	 * Vr�ti aktu�lne zost�vaj�ci �as
	 * @return
	 */
	public int getSeconds() {
		return time;
	}
	
	/**
	 * Vr�ti aktu�lne zostav�ci �as, ktor� sa vypisuje v hre
	 * @return String
	 */
	public String getTime() {
		int minutes = time / 60;
		int seconds = time - minutes * 60;
		String sec;
		if (seconds < 10) {
			sec = "0" + Integer.toString(seconds);
		} else {
			sec = Integer.toString(seconds);
		}
		return "0" + Integer.toString(minutes) + ":" + sec;
	}

	/**
	 * Je mapa naozaj na��tan� a m��e sa vykres�ova�?
	 * @return
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Nastavenie, �e sa mapa m��e vykres�ova�
	 * @param ready
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	/**
	 * Vr�ti po�et bodov, ktor� hr�� z�ska za prejdenie dan�ho levela
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
}
