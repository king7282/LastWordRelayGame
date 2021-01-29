package Domain;
import java.io.Serializable;

public class Communication implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int type;
	private Serializable content;
	
	public Communication() {
		
	}
	
	public Communication(int type, Serializable content) {
		this.type = type;
		this.content = content;
	}
	
	public int getType() {
		return type;
	}
	
	public Serializable getContent() {
		return content;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setContent(Serializable content) {
		this.content = content;
	}
}
