package edu.vt.ece4564.hessionb.snakevbserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServerScores extends HttpServlet {

	private static final long serialVersionUID = -1618783980286518659L;
	
	private static Map<String, Integer> scores_;

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		
		WebAppContext context = new WebAppContext();
		context.setWar("war");
		context.setContextPath("/");
		server.setHandler(context);
		
		scores_ = new HashMap<String, Integer>();
		
		server.start();
		server.join();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String name = req.getParameter("name");
		String scoreStr = req.getParameter("score");
		
		try {
			if(name != null && scoreStr != null) {
				int score = Integer.parseInt(scoreStr);
				scores_.put(name, score);
			}
		} catch (Exception e) {
			resp.getWriter().write("Invalid parameter");
		}
		
		resp.getWriter().write(toJson());
	}
	
	public String toJson() {
		StringBuilder json = new StringBuilder();
		String[] keys = new String[scores_.size()];
		
		json.append("[{");
		if(keys.length > 0) {
			scores_.keySet().toArray(keys);
		
			json.append("\"" + keys[0] + "\":" + scores_.get(keys[0]));
			for(int i = 1; i < keys.length; ++i) {
				json.append(", \"" + keys[i] + "\":" + scores_.get(keys[i]));
			}
		}
		json.append("}]");
		
		return json.toString();
	}
}
