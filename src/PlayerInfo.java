import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class PlayerInfo {
	
	private int score;
	private String name;

	/**
	 * Inform�cie o hr��ovi
	 * @param score sk�re hr��a
	 * @param name meno hr��a
	 */
	public PlayerInfo(int score, String name) {
		this.score = score;
		this.name = name;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Zv��enie sk�re
	 * @param score
	 */
	public void increaseScore(int score) {
		this.score += score;
	}
	
	/**
	 * Zn�enie sk�re
	 * @param score
	 */
	public void decreaseScore(int score) {
		this.score -= score;
		if (this.score < 0) this.score = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Zap�sanie sk�re do s�boru
	 */
	public void writeScore() {
		if (score > 0) {
			try {
	        	BufferedWriter out = new BufferedWriter(new FileWriter("stats.txt", true));
				out.write(name + "-" + score);
				out.newLine();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
