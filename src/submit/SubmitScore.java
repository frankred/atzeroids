package submit;


import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.myorg.mygame.MyGame;

public class SubmitScore implements Runnable{

	private static String URL = "http://atzenroidz.mynotiz.de";
	private int score;
	private String name;
	
	public SubmitScore(int score, String name ){
		this.score = score;
		this.name = name;
	}
	
	@Override
	public void run() {
		HttpClient client = new HttpClient();
		try {
			StringBuffer ranking = HttpClientHelper.getPageContentAsHttpGet(URL  + "/submitHighscore.php?name=" + this.name + "&score=" + this.score, client);
			if(MyGame.currentRankingEditable){
				MyGame.currentRanking = Integer.parseInt(ranking.toString());				
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
}