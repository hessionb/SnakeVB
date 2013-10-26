package edu.vt.ece4564.hessionb.snakevb;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class GameActivity extends Activity {

private static final long DELAY = 100;
	
	private GameView gameView_;
	private Game game_;
	
	private TextView scoreText_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_main);
		
		gameView_ = (GameView)findViewById(R.id.gameBoard);
		scoreText_ = (TextView)findViewById(R.id.scoreText);
		
		game_ = new Game(this, gameView_, scoreText_, DELAY);
	
		new Controller(this, game_, DELAY); // Start controller
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
