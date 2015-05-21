package Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class playTTS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			String myMsg="武枫辕，你好！";		
			playMsg(myMsg);
		}
	
		public static void playMsg(String myMsg){
			String mp3File="TTS_ZH.mp3";
			String url="http://translate.google.com/translate_tts?tl=zh_cn&q="+myMsg;
	
			System.out.println(url);
	
			HttpClient httpClient=new DefaultHttpClient();
			HttpGet httpGet=new HttpGet(url);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.52 Safari/537.17");
			try {
			HttpResponse httpResponse=httpClient.execute(httpGet);
			System.out.println(httpResponse.getStatusLine());
	
			if(httpResponse.getStatusLine().getStatusCode()==200){
			InputStream inputStream=httpResponse.getEntity().getContent();
			File file=new File(mp3File);
			FileOutputStream fileOutputStream=new FileOutputStream(file);
			byte[] buffer=new byte[1024];
			int count=0;
			while((count=inputStream.read(buffer))!=-1)
			fileOutputStream.write(buffer, 0, count);
	
			fileOutputStream.close();
			inputStream.close();
			}
			httpClient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
		// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
		// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		Player.playFile(mp3File);
	}

}


