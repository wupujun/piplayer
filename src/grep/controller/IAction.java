package grep.controller;

import grep.API.SongItem;
import Utils.Player;

public interface IAction {
		public String run( );
}



class GetSongItemAction implements IAction {
	
	String ID=null;

	public GetSongItemAction(String ID) {
		// TODO Auto-generated constructor stub
		this.ID = ID;
	}

	@Override
	public String run() {
		// TODO Auto-generated method stub
		//String result=Player.pause(); 
		WebGrabber wg= WebGrabber.instance();
		return wg.getSongJSonItembyID(ID);
			//return result;
	}
	
}

class PauseAction implements IAction {

	public PauseAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String run() {
		// TODO Auto-generated method stub
		String result=Player.pause(); 
		return result;
	}
	
}

class PlayAction implements IAction {

	String filename=null;
	public PlayAction(String filename) {
		// TODO Auto-generated constructor stub
		this.filename=filename;
	}

	@Override
	public String run() {
		// TODO Auto-generated method stub
		String result=Player.playFile(filename);
		return result;
	}
	
}


class VolAdjustAction implements IAction {

	int delta=0;
	public VolAdjustAction (int delta) {
		this.delta = delta;
	}
	@Override
	public String run() {
		// TODO Auto-generated method stub
		return Player.decrease();
	}
	
}