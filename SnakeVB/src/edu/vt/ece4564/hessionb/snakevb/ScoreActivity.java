package edu.vt.ece4564.hessionb.snakevb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ScoreActivity extends Activity {

	private TextView serverScores_;
	private EditText domainInput_, portInput_, nameInput_;
	private int score_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_main);
		
		Intent i = getIntent();
		Integer score = i.getIntExtra("score", 0);
		score_ = score;
		
		serverScores_ = (TextView)findViewById(R.id.serverScores);
		TextView scoreText = (TextView)findViewById(R.id.finalScore);
		scoreText.setText("Final score: " + score);
		
		domainInput_ = (EditText)findViewById(R.id.domainInput);
		portInput_ = (EditText)findViewById(R.id.portInput);
		nameInput_ = (EditText)findViewById(R.id.nameInput);

		Button postButton = (Button)findViewById(R.id.postButton);
		Button restartButton = (Button)findViewById(R.id.restartButton);
		Button exitButton = (Button)findViewById(R.id.exitButton);
		
		postButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String domain = domainInput_.getText().toString();
				String port = portInput_.getText().toString();
				String name = nameInput_.getText().toString();
				if(name.length() > 14) name = name.substring(0,15);
				
				if(name.indexOf(',') != -1 || name.indexOf(':') != -1) {
					serverScores_.setText("invalid name - cannot use ',' or ':'");
				}
				else {
					String args[] = new String[4];
					args[0] = domain;
					args[1] = port;
					args[2] = name;
					args[3] = "" + score_;
					
					new NetworkTask(serverScores_).execute(args);
				}
			}
		});
		
		restartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),GameActivity.class));
				finish();
			}
		});
		
		exitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
