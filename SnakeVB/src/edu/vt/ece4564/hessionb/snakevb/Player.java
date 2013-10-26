package edu.vt.ece4564.hessionb.snakevb;

import java.util.ArrayList;

import android.graphics.Color;

public class Player extends GameObject {

	public static final int UP = 4;
	public static final int RIGHT = 5;
	public static final int DOWN = 6;
	public static final int LEFT = 7;
	public static final int NONE = 8;
	
	private int vx_,vy_,size_;
	private ArrayList<GameObject> trail_;
	private int currentDir_, nextDir_;
	
	private int color_;
	private int score_;
	
	public Player() {
		super(1,1);
		vx_ = 0;
		vy_ = 0;
		
		currentDir_ = DOWN;
		nextDir_ = DOWN;
		
		size_ = 1;
		score_ = 0;
		trail_ = new ArrayList<GameObject>();
		trail_.add(new GameObject(getX(),getY()));

		color_ = Color.WHITE;
	}
	
	public Player(int x, int y) {
		super(x,y);
		vx_ = 0;
		vy_ = 0;
		
		currentDir_ = DOWN;
		nextDir_ = DOWN;
		
		size_ = 1;
		score_ = 0;
		trail_ = new ArrayList<GameObject>();
		trail_.add(new GameObject(getX(),getY()));

		color_ = Color.WHITE;
	}
	
	public Player(int x, int y, int vx, int vy) {
		super(x,y);
		vx_ = vx;
		vy_ = vy;
		
		currentDir_ = DOWN;
		nextDir_ = DOWN;
		
		size_ = 1;
		score_ = 0;
		trail_ = new ArrayList<GameObject>();
		trail_.add(new GameObject(getX(),getY()));
		
		color_ = Color.WHITE;
	}
	
	public void update() {
		setVelocity();
		setX(getX() + vx_);
		setY(getY() + vy_);
		trail_.add(new GameObject(getX(), getY()));
		trail_.remove(0);
	}
	
	public void setColor(int color) {
		color_ = color;
	}
	
	public int getColor() {
		return color_;
	}
	
	public void resetScore() {
		score_ = 0;
	}
	
	public int getScore() {
		return score_;
	}
	
	public void increaseTail() {
		++size_;
		score_ += 10;
		GameObject tail = trail_.get(0);
		trail_.add(0, new GameObject(tail.getX(), tail.getY()));
	}
	
	public void clearTail() {
		size_ = 1;
		trail_.clear();
		trail_.add(new GameObject(getX(),getY()));
	}

	public void turnRight() {
		nextDir_ = ((nextDir_ + 1) % 4) + 4;
	}
	
	public void turnLeft() {
		nextDir_ = ((nextDir_ - 1) % 4) + 4;
	}
	
	public void setNextDirection(int dir) {
		nextDir_ = dir;
	}
	
	public void setDirection(int dir) {
		nextDir_ = currentDir_ = dir;
	}
	
	public int getDirection() {
		return currentDir_;
	}
	
	public void setVX(int vx) {
		vx_ = vx;
	}
	
	public void setVY(int vy) {
		vy_ = vy;
	}
	
	public int getVX() {
		return vx_;
	}
	
	public int getVY() {
		return vy_;
	}
	
	public ArrayList<GameObject> getPlayer() {
		return trail_;
	}
	
	public int getSize() {
		return size_;
	}
	
	public int getEndX() {
		return trail_.get(0).getX();
	}
	
	public int getEndY() {
		return trail_.get(0).getY();
	}

	private void setVelocity() {
		switch(nextDir_) {
		case UP:
			setVX(0);
			setVY(-1);
			break;
		case DOWN:
			setVX(0);
			setVY(1);
			break;
		case LEFT:
			setVX(-1);
			setVY(0);
			break;
		case RIGHT:
			setVX(1);
			setVY(0);
			break;
		}
		currentDir_ = nextDir_;
	}
}
