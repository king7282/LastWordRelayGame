import java.io.IOException;
import java.util.ArrayList;

import Domain.Communication;
import Domain.Dictionary;
import Domain.Turn;

public class GameThread extends Thread{
	private ArrayList<String> ids;
	private String prevString;
	private int curIdIndex;
	private volatile boolean correctAnswer;
	
	public GameThread(ArrayList<String> ids) {
		this.ids = ids;
		this.prevString = "r";
		this.correctAnswer = false;
		this.curIdIndex = 0;
	}
	
	@Override
	public void run() {
		try {
			long startTime = 0;
			PlayerList playerList = PlayerList.getInstance();
			Communication output = new Communication();
			
			while(true) {
				output.setType(10);
				output.setContent(new Turn(ids.get(curIdIndex), prevString));
				
				for(int i = 0; i < ids.size(); i++) {
					playerList.writeSocket(ids.get(i), output);
				}
				
				startTime = System.currentTimeMillis();
				correctAnswer = false;
				
				while(System.currentTimeMillis() - startTime <= 31000 && !correctAnswer); // 31초간 기다리며 중간에 answer가 바뀌면 빠져나옴
				
				System.out.println("Result : " + correctAnswer);
				if(correctAnswer == true) {
					curIdIndex = (curIdIndex + 1) % ids.size();
				}
				else {
					output.setType(11);
					output.setContent(ids.get(curIdIndex));
					for(int i = 0; i < ids.size(); i++) {
						playerList.writeSocket(ids.get(i), output);
					}
					break;
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getCurrentTurnAnswer(String answer) {
		if(answer != null && answer.length() > 0 &&
		   answer.charAt(0) == prevString.charAt(prevString.length() - 1) && Dictionary.isExistString(answer)) {
			synchronized(this) {
				correctAnswer = true;
				prevString = answer;
			}
			return true;
		}
		return false;
	}
	

}
