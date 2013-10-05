package grep.API;

import com.google.gson.Gson;

public class SongItem {
	public String title;
	public String url;
	public String key;
	public String albumImgURL;
	public String lrcText;
	public String lrcURL;
	public String songName;
	public String albumName;
	public int rate;
	public int size;
	public String ID;
	
	public SongItem(String ID) {
		this.ID = ID;
	}
	

	
	public String toJason() {
		Gson jsonObj = new Gson();
		return jsonObj.toJson(this);
	}
		
}