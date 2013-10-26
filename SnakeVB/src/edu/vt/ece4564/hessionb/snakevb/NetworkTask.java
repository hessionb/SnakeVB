package edu.vt.ece4564.hessionb.snakevb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.TextView;

public class NetworkTask extends AsyncTask<String, Void, String> {

	private TextView scoresText_;
	
	public NetworkTask(TextView scoresText) {
		scoresText_ = scoresText;
	}
	
	@Override
	protected String doInBackground(String... args) {
		StringBuilder formattedRes = new StringBuilder();
		
		// Get arguments
		String domain = args[0];
		String port = args[1];
		String name = args[2];
		String score = args[3];
		
		// Error check null values
		if(domain == null || port == null || name == null || score == null) {
			return "null string";
		}
		
		// Concatenate URL
		String urlStr = "http://" + domain + ":" + port + "/scores?name=" + name + "&score=" + score;
		
		try {
			// Connect to server
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			
			// Get response
			StringBuilder res = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = in.readLine()) != null) {
				res.append(line);
			}
			
			// Disconnect
			conn.disconnect();
			
			// Parse json
			String json = res.toString();
			try {
				json = json.substring(2,json.length()-2); // trim off "[{" and "}]"
				String values[] = json.split(","); // Get all value pairs
				for(int i = 0; i < values.length; ++i) {
					String pair = values[i].trim(); // Trim spaces
					String split[] = pair.split(":"); // Separate key and value
					String pname = split[0].substring(1,split[0].length()-1); // Extract name and ut off quotations
					String pscore = split[1]; // Extract score
					formattedRes.append(String.format("%1$-14s ", pname) + pscore + "\n");
				}
			} catch (Exception e) {
				formattedRes = new StringBuilder();
				formattedRes.append("Invalid json");
			}
		} catch (IOException e) {
			formattedRes = new StringBuilder();
			formattedRes.append("Network connection failed");
		}
		
		return formattedRes.toString();
	}

	@Override
	protected void onPostExecute(String res) {
		scoresText_.setText(res);
	}
}
