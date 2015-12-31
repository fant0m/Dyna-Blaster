import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class PlayerInfo {
	
	private int score;
	private String name;

	/**
	 * Informácie o hráèovi
	 * @param score skóre hráèa
	 * @param name meno hráèa
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
	 * Zvýšenie skóre
	 * @param score
	 */
	public void increaseScore(int score) {
		this.score += score;
	}
	
	/**
	 * Zníženie skóre
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
	 * Zapísanie skóre do súboru
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
