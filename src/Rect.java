
class Rect {
	private int x1;
	private int y1;
	private int x2;
	private int y2;

	Rect(int x, int y, int w, int h) {
		this.x1 = x;
		this.y1 = y;
		this.x2 = x + w;
		this.y2 = y + h;
	}

	int[] center() {
		int centerX = (x1 + x2) / 2;
		int centerY = (y1 + y2) / 2;
		int[] temp = { centerX, centerY };
		return temp;
	}

	Boolean intersect(Rect r) {
		// returns true if this rectangle intersects with another one
		return (this.x1 <= r.x2 && this.x2 >= r.x1 && 
				this.y1 <= r.y2 && this.y2 >= r.y1);
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}
}