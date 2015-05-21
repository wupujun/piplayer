package grep.controller;

import grep.API.SongItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import Utils.Player;
import Utils.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;



public class WebGrabber {
/*
	public class songItem {
		public String title;
		public String url;
		public String key;
		public JSONObject obj;
		
	}*/
	private ArrayList<SongItem> list=null;
	private WebGrabber() {}
	private static WebGrabber ins=null;
	public static WebGrabber instance() {
		if (ins==null) ins= new WebGrabber();	
		return ins;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length<1) {
			logger.Log("输入查询关键字");
			return;
		}
		
		//logger.Log(args[0]);
		
		//WebGrabber wg= new WebGrabber();
		WebGrabber wg= WebGrabber.instance();
		String encodedKey="good";
		try {
			encodedKey = URLEncoder.encode(args[0],"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "http://music.baidu.com/search?key="+encodedKey;//%E7%8E%8B%E8%8F%B2";
		
		
		logger.Log("开始解析 URL:" + url);
		wg.createPlayList(wg.getSongListbyJsoup(url));
	    	        // p.getPageContent(url, "post", 100500);//第二种方法
		//String msg= wg.getPageContent(url, "get", 2*1024*1024);
		//logger.Log(msg);
		
		//Player.play();
	}
	
	//public String createPlayList(HashMap<String,String> songList) {
	protected String createPlayList(ArrayList<SongItem> songList) {
		String fname= "mylist.txt";//UUID.randomUUID().toString();
		File f=new File(fname);
		
		try{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname)));
			
			
			//for(String song:songList.keySet()) 
			for(SongItem item: songList)
			{
				String url=item.url; //=songList.get(song);
				bw.write(url); //+System.getProperty("line.separator"));
				bw.newLine();				
			}
			
			bw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		logger.Log("创建播放列表，文件长度（字节）：" + f.length());
		return fname;
	}
	
		
	
	

	private String getPageHTTPClient ( String url) {
	     CloseableHttpClient httpclient = HttpClients.createDefault();	     
	     
 
	     String responseBody=null;
	        try {
	            HttpGet httpget = new HttpGet(url);

	            //logger.Log("executing request " + httpget.getURI());
	           
	            // Create a custom response handler
	            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

	                public String handleResponse(
	                        final HttpResponse response) throws ClientProtocolException, IOException {
	                    int status = response.getStatusLine().getStatusCode();
	                    if (status >= 200 && status < 300) {
	                        HttpEntity entity = response.getEntity();
	                        Header header=entity.getContentEncoding();
	                        return entity != null ? EntityUtils.toString(entity,"UTF-8") : null;
	                    } else {
	                        throw new ClientProtocolException("Unexpected response status: " + status);
	                    }
	                }

	            };
	            responseBody = httpclient.execute(httpget, responseHandler);
	        }
	         catch (Exception e) {
	        	 e.printStackTrace();
	         }
	            
	        return responseBody;
	            	
	}
	
	protected String getResFromID(String ID, SongItem item) throws IOException {
		
		String resURL=null;
		String url="http://ting.baidu.com/data/music/links?songIds=" + ID;
		String msg= this.getPageHTTPClient(url);

		JSONObject obj=null;
		try {
			obj = new JSONObject(msg);
			//resURL=(String) obj.get("songLink");
			JSONArray songList=obj.getJSONObject("data").getJSONArray("songList");
			JSONObject songItem= (JSONObject) songList.getJSONObject(0);
			
			
			String songLink=songItem.getString("songLink");
			item.songName=songItem.getString("songName");
			item.albumImgURL= songItem.getString("songPicBig");
			item.albumName = songItem.getString("albumName");
			item.rate=songItem.getInt("rate");
			item.size=songItem.getInt("size");
			item.lrcText = "";
			item.lrcURL = songItem.getString("lrcLink");
			if (!item.lrcURL.startsWith("http://")) {
				item.lrcURL="http://ting.baidu.com"+item.lrcURL;
			}
			
			//logger.Log(lrcLink);
			//logger.Log(songLink);
			
			resURL=songLink;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		item.url= resURL;
		logger.Log("解析媒体资源文件：" + resURL);
		return resURL;
	}
	protected ArrayList<SongItem> getSongResList (ArrayList<SongItem> songList) {
		//HashMap<String,String> resList= new HashMap<String,String> ();
		
		//for(String songName: songList.keySet()) 
		for(SongItem item: songList)
		{
			String id= item.key;
			String url;
			try {
				url = getResFromID(id,item);
				//item.url=url;
				//resList.put(songName, url);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return songList;
	}
	
	public SongItem getSongItembyID (String id) {
		
		SongItem rec=null;
		
		if (id==null && list!=null) rec=list.get(0);
		for(SongItem item:list) {
			
			if (item.key.equals(id)) {
				rec=item;
				break;
			}
		}
		
		return rec;
	}
	
	//public HashMap<String,String> getSongListbyJsoup(String url) {
	public ArrayList<SongItem> getSongListbyJsoup(String url) {
		
		list =this.getSongResList(this.getSongIDList(url));
		
		Player.setSongList(list);
		
		return list;
	}
	
	public ArrayList<SongItem> getCurrentSongList() {
		return list;
	}
	
	protected ArrayList<SongItem> getSongIDList(String url) {
		//HashMap<String,String> songList= new HashMap<String,String> ();
		ArrayList<SongItem> songList = new ArrayList<SongItem>();
		try
		{
			Document doc = Jsoup.connect(url).get();
					//parse(input, "UTF-8", url);
			Elements links= doc.select("a");
			//Element content = doc.getElementById("content");
			//Elements links = content.getElementsByTag("a");
			for (Element link : links) {
			  String linkHref = link.attr("href");
			  String linkText = link.text();
			  if (linkHref.startsWith("/song/")) {
				  String id=linkHref.substring(6);
				  if (id.contains("#")) {
					  id=id.substring(0, id.indexOf("#"));
				  }
				  //logger.Log("ID="+id);
				  logger.Log("添加歌曲:" + linkText +",key="+id);
				  SongItem item= new SongItem(id);
				  item.key=id;
				  item.title=linkText;
				  
				  songList.add (item);
			  }
			  
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return songList;
	}
	public String getSongJSonItembyID(String iD) {
		// TODO Auto-generated method stub
		SongItem item=this.getSongItembyID(iD);
		
		if ((item.lrcText==null || item.lrcText.length()<1) && item.lrcURL.length()>5  ){
			String lrc=this.getPageHTTPClient(item.lrcURL);
			item.lrcText = lrc;
			logger.Log("Fetch LRC, length=" + item.lrcText.length());
		}
		
		Gson gson=new Gson();
		return gson.toJson(item);
		//return null;
	}
	public String getSongJSonList(String keyword) {
		// TODO Auto-generated method stub
		if(keyword==null) keyword="";
		
		String encodedKey=keyword;
		try {
			encodedKey = URLEncoder.encode(encodedKey,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "http://music.baidu.com/search?key="+encodedKey;//%E7%8E%8B%E8%8F%B2";
		
		Gson gson=new Gson();
		return gson.toJson(getSongListbyJsoup(url));
	
	}
}
