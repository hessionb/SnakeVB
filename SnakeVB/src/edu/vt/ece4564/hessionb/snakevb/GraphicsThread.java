package edu.vt.ece4564.hessionb.snakevb;

import android.os.Handler;
import android.widget.TextView;

public class GraphicsThread extends Handler {

	private GameView gameView_;
	private Runnable runnable_;
	private long delay_;
	private Player player_;
	private TextView scoreText_;
	
	public GraphicsThread(GameView gameView, long delay, Player player, TextView scoreText) {
		gameView_ = gameView;
		delay_ = delay;
		player_ = player;
		scoreText_ = scoreText;
		runnable_ = new Runnable() {
			@Override
			public void run() {
				long startTime = System.nanoTime();
				synchronized(player_) {
					scoreText_.setText("Score: " + player_.getScore());
				}
				gameView_.redraw();
				long elapsedTime = (System.nanoTime() - startTime)/1000000;
				if(elapsedTime < delay_)
					GraphicsThread.this.postDelayed(runnable_, delay_ - elapsedTime);
				else
					GraphicsThread.this.post(runnable_);
			}
		};
	}
	
	public void start() {
		post(runnable_);
	}
	
	public void stop() {
		removeCallbacks(runnable_);
	}
}
