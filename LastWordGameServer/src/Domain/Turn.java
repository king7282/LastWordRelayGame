package Domain;

import java.io.Serializable;

public class Turn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String curId, prevString;
	
	public Turn(String curId, String prevString) {
		this.curId = curId;
		this.prevString = prevString;
	}
	
	public String getCurId() {
		return curId;
	}
	
	public String getPrevString() {
		return prevString;
	}
}	
