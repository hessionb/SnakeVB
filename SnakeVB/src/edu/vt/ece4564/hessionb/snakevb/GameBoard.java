package edu.vt.ece4564.hessionb.snakevb;

public class GameBoard {

	private static final int WIDTH = 25;
	private static final int HEIGHT = 40;
	
	private boolean board_[][];
	
	public GameBoard() {
		board_ = new boolean[WIDTH][HEIGHT];
		for(int i = 0; i < board_.length; ++i) {
			for(int j = 0; j < board_[i].length; ++j) {
				board_[i][j] = false;
			}
		}
	}
	
	public boolean occupySpot(int x, int y) {
		if(x < 1 || x > WIDTH) return false;
		if(y < 1 || y > HEIGHT) return false;
		board_[x-1][y-1] = true;
		return true;
	}
	
	public boolean freeSpot(int x, int y) {
		if(x < 1 || x > WIDTH) return false;
		if(y < 1 || y > HEIGHT) return false;
		board_[x-1][y-1] = false;
		return true;
	}
	
	public boolean getSpot(int x, int y) {
		if(x < 1 || x > WIDTH) return true;
		if(y < 1 || y > HEIGHT) return true;
		return board_[x-1][y-1];
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
}
