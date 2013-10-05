package grep.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Utils.Player;

class Forwarder {
	
	public static IAction forward(HttpServletRequest request  ) {
		//return null;
	
		String filename=request.getParameter("filename");
		String action= request.getParameter("action");
		
		IAction Act=null;
		
		if (filename!=null) {

			Act= new PlayAction( filename);
			//Player.playFile(filename);
		}
		else if (action!=null) {
			if (action.equals("stop")) {
					Act= new PauseAction(); 
			}
			else if (action.equals("inc")) {
				Act= new VolAdjustAction(10);
				//Player.increase(); 
			}
			else if (action.equals("dec")) {
				Act= new VolAdjustAction(-10);
				//Player.decrease(); 
			}
			else if (action.equals("getSongInfo")) {
				String id=request.getParameter("id");
				Act = new GetSongItemAction(id);
			}
			
			
		}
		return Act;

	}
}



/**
 * Servlet implementation class playerCtl
 */
@WebServlet("/playerCtl")
public class playerCtl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public playerCtl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		IAction action=Forwarder.forward(request);
		
		if (action==null) return;
		
		String json= action.run();
		//response.setContentType("application/json");
		response.setContentType("application/json;charset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = response.getWriter();
		// Assuming your json object is **jsonObject**, perform the following, it will return your json object  
		out.print(json);
		out.flush();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}



