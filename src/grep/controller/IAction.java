package grep.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import grep.API.SongItem;
import Utils.Player;
import Utils.playTTS;

public interface IAction {
		public String run( );
}


class GetSongListAction implements IAction {
	
	String keyword=null;

	public  GetSongListAction(String key) {
		// TODO Auto-generated constructor stub
		this.keyword = key;
	}

	@Override
	public String run() {
		// TODO Auto-generated method stub
		//String result=Player.pause(); 
		WebGrabber wg= WebGrabber.instance();
		return wg.getSongJSonList(keyword);
	}
	
}


class playTTSAction implements IAction {
	
	String msg=null;

	public playTTSAction(String msg) {
		// TODO Auto-generated constructor stub
		this.msg = msg;
	}

	@Override
	public String run() {
		// TODO Auto-generated method stub
		//String result=Player.pause(); 
		//WebGrabber wg= WebGrabber.instance();
		playTTS.playMsg(msg);
		//return wg.getSongJSonItembyID(ID);
		return null;
			//return result;
	}
	
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


class trackSongChange implements IAction {


	public trackSongChange() {
		
	}
	@Override
	public String run() {
		
		return null;
		// TODO Auto-generated method stub
		//
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
		if (delta<0) 
			return Player.decrease();
		else return Player.increase();
	}
	
}