package edu.vt.ece4564.hessionb.snakevb;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

public class Game extends AsyncTask<Void, Void, Integer> {

	private Activity parent_;
	
	private GameView gameView_;
	private GameBoard gameBoard_;
	private Player player_;
	private GameObject apple_;
	private TextView scoreText_;
	
	private GraphicsThread graphics_;
	private LogicThread logic_;
	
	private long delay_;
	
	public Game(Activity parent, GameView gameView, TextView scoreText, long delay) {
		parent_ = parent;
		
		gameView_ = gameView;
		player_ = new Player(1,1,1,0);
		scoreText_ = scoreText;
		delay_ = delay;
		initialize();
	}
	
	private void initialize() {
		gameBoard_ = new GameBoard();
		apple_ = new GameObject(1,1);
		scoreText_.setText(R.string.directions);

		player_.resetScore();
		player_.clearTail();
		player_.setX(1);
		player_.setY(1);
		player_.setVX(0);
		player_.setVY(1);
		player_.setDirection(Player.DOWN);
		
		gameView_.setPlayer(player_);
		gameView_.setApple(apple_);
		
		logic_ = new LogicThread(gameBoard_, player_, apple_, delay_);
		graphics_ = new GraphicsThread(gameView_, delay_, player_, scoreText_);
	}
	
	public Player getPlayer() {
		return player_;
	}
	
	public void redraw() {
		synchronized(gameView_) {
			gameView_.invalidate();
		}
	}
	
	public String getBoardString() {
		return gameBoard_.toString();
	}
	
	public void start() {
		logic_.start();
		graphics_.start();
	}
	
	public void stop() {
		logic_.terminate();
		graphics_.stop();
	}

	@Override
	protected Integer doInBackground(Void... params) {
		int score = 0;
		
		start();
		
		// Wait for game to stop
		try {
			logic_.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		graphics_.stop(); // kill graphics thread
		
		// Get score
		synchronized(player_) {
			score = player_.getScore();
		}
		
		return score;
	}
	
	@Override
	protected void onPostExecute(Integer score) {
		Intent i = new Intent(parent_.getApplicationContext(),ScoreActivity.class);
		i.putExtra("score", score);
		parent_.startActivity(i);
		parent_.finish();
	}
}
