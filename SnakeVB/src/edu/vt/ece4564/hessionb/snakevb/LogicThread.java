package edu.vt.ece4564.hessionb.snakevb;

import android.util.Log;

/***
 * Task
 * 
 * This is the logic loop that handles all the game mechanics.
 * 
 * @author Brian
 *
 */
class Task implements Runnable {

	private volatile boolean running_ = true;
	
	private long delay_;
	
	private GameBoard gameBoard_;
	private Player player_;
	private GameObject apple_;
	
	public Task(GameBoard gameBoard, Player player, GameObject apple, long delay) {
		gameBoard_ = gameBoard;
		player_ = player;
		apple_ = apple;
		delay_ = delay;
	}
	
	public void terminate() {
		running_ = false;
	}
	
	@Override
	public void run() {
		generateApple(); // initial apple generation
		while(running_) {
			long startTime = System.nanoTime();
			
			// Logic
			synchronized(player_) {
				
				// Save old x,y for later, then update player
				int oldx = player_.getEndX();
				int oldy = player_.getEndY();
				player_.update();
				
				// If player is on an occupied spot
				if(gameBoard_.getSpot(player_.getX(), player_.getY())) {
					// Game over
					running_ = false;
					break;
				}
				
				// Update board
				gameBoard_.occupySpot(player_.getX(), player_.getY());
				gameBoard_.freeSpot(oldx, oldy);
				
				// If player is on an apple
				boolean extendTail;
				synchronized(apple_) {
					extendTail = onApple();
				}
				
				// Extend tail
				if(extendTail) {
					player_.increaseTail();
					generateApple();
				}
			}

			// Sleep for the rest of the "delay"
			long elapsedTime = (System.nanoTime() - startTime)/1000000;
			if(elapsedTime < delay_) {
				try {
					Thread.sleep(delay_ - elapsedTime, 0);
				} catch (InterruptedException e) {
					Log.e("Snake","InterruptedException");
					e.printStackTrace();
					running_ = false;
				}
			}
		}
	}
	
	private void generateApple() {
		int x,y;
		
		do {
			x = (int)(Math.random() * gameBoard_.getWidth()) + 1;
			y = (int)(Math.random() * gameBoard_.getHeight()) + 1;
		} while(gameBoard_.getSpot(x,y));
		
		synchronized(apple_) {
			apple_.setX(x);
			apple_.setY(y);
		}
	}
	
	private boolean onApple() {
		return (player_.getX() == apple_.getX() && player_.getY() == apple_.getY());
	}
}

/***
 * LogicThread
 * 
 * This just starts the logic loop in its own thread.
 * 
 * @author Brian
 *
 */
public class LogicThread extends Thread {
	
	private static Task task_;
	
	public LogicThread(GameBoard gameBoard, Player player, GameObject apple, long delay) {
		super(task_ = new Task(gameBoard,player,apple,delay));
	}
	
	public void terminate() {
		task_.terminate();
	}
}
