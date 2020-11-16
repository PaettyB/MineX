package de.keygalp.mineX.events;


public class OnTestEvent implements EventListener{

	@Event
	public void onTestEvent(TestEvent e) {
		System.out.println(e.toString()+ " :: "+ e.getMessage()+" , " + e.getNum());
		System.out.println("YA");
	}
	
}
