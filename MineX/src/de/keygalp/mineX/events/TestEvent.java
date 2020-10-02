package de.keygalp.mineX.events;

public class TestEvent implements IEvent{

	private String message;
	private int num;
	
	public TestEvent(String message, int num) {
		this.message = message;
		this.num = num;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
