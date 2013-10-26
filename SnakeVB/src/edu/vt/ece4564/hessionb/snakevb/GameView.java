package edu.vt.ece4564.hessionb.snakevb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

	private static final int WIDTH = 27;
	private static final int HEIGHT = 42;

	private Paint paint_;
	private int width_, height_, size_;

	private Player player_ = new Player();
	private GameObject apple_ = new GameObject();
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint_ = new Paint();
		width_ = 0;
		height_ = 0;
		size_ = 0;
	}
	
	public void setApple(GameObject apple) {
		apple_ = apple;
	}
	
	public void setPlayer(Player player) {
		player_ = player;
	}

	@Override
	public void onMeasure(int width, int height) {

		int w = View.MeasureSpec.getSize(width);
		int h = View.MeasureSpec.getSize(height);

		w /= WIDTH;
		h /= HEIGHT;
		size_ = (w < h) ? w : h;
		width_ = WIDTH * size_;
		height_ = HEIGHT * size_;

		// Set width and height
		super.onMeasure(View.MeasureSpec.makeMeasureSpec(width_,
				View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
				height_, View.MeasureSpec.EXACTLY));
	}

	@Override
	public void onSizeChanged(int width, int height,
			int old_width, int old_height) {

		int w = View.MeasureSpec.getSize(width);
		int h = View.MeasureSpec.getSize(height);

		w /= WIDTH;
		h /= HEIGHT;
		size_ = (w < h) ? w : h;
		width_ = WIDTH * size_;
		height_ = HEIGHT * size_;

		// Set width and height
		super.onSizeChanged(View.MeasureSpec.makeMeasureSpec(width_,
				View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
				height_, View.MeasureSpec.EXACTLY), old_width, old_height);
	}

	@Override
	public void onDraw(Canvas canvas) {

		// Get background color
		int background;
		synchronized(player_) {
			background = player_.getColor();
		}
		
		// Draw background
		paint_.setColor(background);
		paint_.setAlpha(255);
		paint_.setStrokeWidth(1);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint_);

		// Calculate circle parameters
		int offset = size_ / 2;
		int radius = size_ / 2 - 2;

		// Get border color
		if(background == Color.BLACK)
			paint_.setColor(Color.GRAY);
		else
			paint_.setColor(Color.BLUE);
		
		// Draw border
		for (int i = 0; i < WIDTH; ++i) {
			canvas.drawCircle(i * size_ + offset, offset, radius, paint_);
			canvas.drawCircle(i * size_ + offset, getHeight() - offset, radius,
					paint_);
		}
		for (int i = 0; i < HEIGHT; ++i) {
			canvas.drawCircle(offset, i * size_ + offset, radius, paint_);
			canvas.drawCircle(getWidth() - offset, i * size_ + offset, radius,
					paint_);
		}
		
		// Draw apple
		paint_.setColor(Color.RED);
		synchronized(apple_) {
			canvas.drawCircle(apple_.getX() * size_ + offset, apple_.getY() * size_ + offset, radius, paint_);
		}
		
		// Get player color
		if(background == Color.BLACK)
			paint_.setColor(Color.YELLOW);
		else
			paint_.setColor(Color.MAGENTA);
		
		// Draw player
		synchronized(player_) {
			for(int i = 0; i < player_.getSize(); ++i) {
				GameObject piece = player_.getPlayer().get(i);
				canvas.drawCircle(piece.getX() * size_ + offset, piece.getY() * size_ + offset, radius, paint_);
			}
		}
	}
	
	// Only redraw portions of the screen that change
	public void redraw() {
		int offset = size_*2; // Extra offset in case frames are skipped
		int minX = player_.getX()*size_-offset;
		int minY = player_.getY()*size_-offset;
		int maxX = player_.getX()*size_+size_+offset;
		int maxY = player_.getY()*size_+size_+offset;
		invalidate(minX, minY, maxX, maxY);
		
		offset = size_*2; // Extra offset in case frames are skipped
		minX = player_.getEndX()*size_-offset;
		minY = player_.getEndY()*size_-offset;
		maxX = player_.getEndX()*size_+size_+offset;
		maxY = player_.getEndY()*size_+size_+offset;
		invalidate(minX, minY, maxX, maxY);

		// Just redraw apple (no need to erase old one, since the player should be on top of it)
		minX = apple_.getX()*size_;
		minY = apple_.getY()*size_;
		maxX = apple_.getX()*size_+size_;
		maxY = apple_.getY()*size_+size_;
		invalidate(minX, minY, maxX, maxY);
	}
}
