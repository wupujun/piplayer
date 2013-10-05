package Utils;


import grep.API.ExecResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.google.gson.Gson;




class LineRedirecter extends Thread {
    /** The input stream to read from. */
    private InputStream in;
    /** The output stream to write to. */
    private OutputStream out;


    /**
     * @param in the input stream to read from.
     * @param out the output stream to write to.
     * @param prefix the prefix used to prefix the lines when outputting to the logger.
     */
    LineRedirecter(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
   
    }

    public void run()
    {
        try {
            // creates the decorating reader and writer
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            PrintStream printStream = new PrintStream(out);
            String line;

            // read line by line
            while ( (line = reader.readLine()) != null) {
                printStream.println(line);
            }
        } catch (IOException ioe) {
            //ioe.printStackTrace();
        	logger.Log("Borken play process!");
        }
    }

}


class OutputHandler extends Thread {
    /** The input stream to read from. */
    private BufferedReader err;
    /** The output stream to write to. */
 

    /**
     * @param in the input stream to read from.
     * @param out the output stream to write to.
     * @param prefix the prefix used to prefix the lines when outputting to the logger.
     */
    OutputHandler(BufferedReader err) {
    	this.err=err;
    }

    public void run()
    {
    	String answer=null;
    	try {
		    while ((answer = err.readLine()) != null) {
		        if (answer.startsWith("ANS_length=")) {
		            break;
		        }
		        logger.Log(answer);
		    }
		}
		catch (IOException e) {
		}
    }

}


class PlayThread extends Thread {
    

    public void run()
    {
    	Player.play();
    }

}

class startListenThread extends Thread {
    

    public void run()
    {
    	Player.beginListen();
    }

}

public class Player {
	static private PrintStream playIn=null;
	
	
	
	public static void main(String[] args) {
		
	}
	
	public static String pause() {
		playIn.print("pause");
		playIn.print("\n");
		playIn.flush();
		logger.Log("Pause playing file");
		
		ExecResult ret=new ExecResult("pause");
		ret.setMsg("Pause playing");
		return ret.toJason();
		
	}
	
	public static String increase() {
		playIn.print("volume 10");
		playIn.print("\n");
		playIn.flush();
		logger.Log("Volume +10");
		
		
		ExecResult ret=new ExecResult("tune");
		ret.setMsg("increase volume +10");
		return ret.toJason();

	}
	
	public static String decrease() {
		playIn.print("volume -10");
		playIn.print("\n");
		playIn.flush();
		logger.Log("volume -10");
		
		ExecResult ret=new ExecResult("tune");
		ret.setMsg("increase volume -10");
		return ret.toJason();

	}
	
	public static String quit() {
		
		if (playIn!=null) {
		playIn.print("quit");
		playIn.print("\n");
		playIn.flush();
		logger.Log("Quit Mplayer!!!");
		
		ExecResult ret=new ExecResult("quit");
		ret.setMsg("Quite Mplayer!");
		return ret.toJason();
		}
		return null;
		
	}
	
	public static String playFile(String filename) {
		
		if (playIn==null) { 
			startListen();
		}
		
		playIn.print("loadfile "+filename);
		playIn.print("\n");
		playIn.flush();
		logger.Log("开始播放媒体文件：" + filename);
		
		ExecResult ret=new ExecResult("play");
		ret.setMsg("Strat playing file:" + filename );
		
		return ret.toJason();
	}
	
	public static void playAsync() {
		logger.Log("Start play thread in background!");
		new PlayThread().start();
	}
	
	public static void startListen() {
		new startListenThread().start();
	}
	
	static void beginListen() {
		try{			
//		 create the piped streams where to redirect the standard output and error of MPlayer
//		 specify a bigger pipesize than the default of 1024
			PipedInputStream  readFrom = new PipedInputStream(256*1024);
			PipedOutputStream writeTo = new PipedOutputStream(readFrom);
			BufferedReader mplayerOutErr = new BufferedReader(new InputStreamReader(readFrom));
			Process mplayerProcess = Runtime.getRuntime().exec("mplayer -quiet -slave -idle");//-quiet -idle -slave  
						
			new LineRedirecter(mplayerProcess.getInputStream(), writeTo).start();
			new LineRedirecter(mplayerProcess.getErrorStream(), writeTo).start();
			new OutputHandler(mplayerOutErr).start();
			
//		 the standard input of MPlayer
			PrintStream mplayerIn = new PrintStream(mplayerProcess.getOutputStream());
			playIn=mplayerIn;
			String readLine=null;			
			do {
				Scanner in=new Scanner(System.in);
				readLine = in.nextLine(); 
				logger.Log("Get input: " + readLine);
				
				mplayerIn.print(readLine);
				mplayerIn.print("\n");
				mplayerIn.flush();
				        
				
			} while (!readLine.equals("q"));
			
			mplayerProcess.destroy();
		}catch (Exception e) {
		}	
		
	}
	
	public static void play() {
		
		//String mediafile="http://zhangmenshiting.baidu.com/data2/music/42361105/7319778151200.mp3?xcode=4097b07ab945f7a00e0e2f5086974641fa98aee792386a1e";
		String mediafile="-playlist /Users/pujunwu/Documents/workspace/BDPlayer/mylist.txt";
		try{
			
//		 create the piped streams where to redirect the standard output and error of MPlayer
//		 specify a bigger pipesize than the default of 1024
			PipedInputStream  readFrom = new PipedInputStream(256*1024);
			PipedOutputStream writeTo = new PipedOutputStream(readFrom);
			BufferedReader mplayerOutErr = new BufferedReader(new InputStreamReader(readFrom));
			Process mplayerProcess = Runtime.getRuntime().exec("mplayer -quiet -slave -idle "+mediafile);//-quiet -idle -slave  
//		 create the threads to redirect the standard output and error of MPlayer
			//Process mplayerProcess = Runtime.getRuntime().exec("mplayer");//-quiet -idle -s
			
			new LineRedirecter(mplayerProcess.getInputStream(), writeTo).start();
			new LineRedirecter(mplayerProcess.getErrorStream(), writeTo).start();
			new OutputHandler(mplayerOutErr).start();
			
//		 the standard input of MPlayer
			PrintStream mplayerIn = new PrintStream(mplayerProcess.getOutputStream());

			/*
			mplayerIn.print("set_property time_pos 300");
			mplayerIn.print("\n");
			mplayerIn.flush();
			
			
			mplayerIn.print("loadfile \""+ mediafile +"\" 0");
			mplayerIn.print("\n");
			mplayerIn.flush();
			
			
			mplayerIn.print("pause");
			mplayerIn.print("\n");
			mplayerIn.flush();
			
			
			mplayerIn.print("get_property length");
			mplayerIn.print("\n");
			mplayerIn.flush();
			
			*/
	/*		
			String answer;
			int totalTime = -1;
			try {
			    while ((answer = mplayerOutErr.readLine()) != null) {
			        if (answer.startsWith("ANS_length=")) {
			            totalTime = Integer.parseInt(answer.substring("ANS_length=".length()));
			            break;
			        }
			        logger.Log(answer);
			    }
			}
			catch (IOException e) {
			}
			logger.Log("========"+totalTime);
			
	*/		
			String readLine=null;			
			do {
				Scanner in=new Scanner(System.in);
				readLine = in.nextLine(); 
				logger.Log("Get input: " + readLine);
				
				mplayerIn.print(readLine);
				mplayerIn.print("\n");
				mplayerIn.flush();
				        
				
			} while (!readLine.equals("q"));
			
			mplayerProcess.destroy();
			/*		
			try {
			    mplayerProcess.waitFor();
			}
			catch (InterruptedException e) {}
			 */	
		}catch (Exception e) {
		}
	}
}


