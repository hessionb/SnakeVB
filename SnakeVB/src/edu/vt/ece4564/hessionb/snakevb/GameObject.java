package edu.vt.ece4564.hessionb.snakevb;

public class GameObject {

	private int x_,y_;
	
	public GameObject() {
		x_ = 0;
		y_ = 0;
	}
	
	public GameObject(int x, int y) {
		x_ = x;
		y_ = y;
	}
	
	public void setX(int x) {
		x_ = x;
	}
	
	public void setY(int y) {
		y_ = y;
	}
	
	public int getX() {
		return x_;
	}
	
	public int getY() {
		return y_;
	}
}
