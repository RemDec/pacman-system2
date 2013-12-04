package model.event;

import java.util.ArrayList;

public class EventHandlerManager {

	private ArrayList<EventHandler> eventObjects;

	public void execute() {
		try{
			for(EventHandler e : this.eventObjects){
				e.perform();
			}
		}catch(RuntimeException ex){
			ex.printStackTrace();
		}	

	}

	public boolean register(EventHandler eH) {
		try{
			this.eventObjects.add(eH);

			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			
			return false;
		}
	}

}