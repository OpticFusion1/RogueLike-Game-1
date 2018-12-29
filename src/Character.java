import java.util.Random;

abstract class Character {
	private int HP;
	private int Atk;
	private int Def;
	private int xPos;
	private int yPos;
	private char symbol;

	Character(int hP, int atk, int def, char symbol) {
		super();
		HP = hP;
		Atk = atk;
		Def = def;
		this.symbol = symbol;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hp) {
		HP = hp;
	}

	public int getAtk() {
		return Atk;
	}

	public void setAtk(int atk) {
		Atk = atk;
	}

	public int getDef() {
		return Def;
	}

	public void setDef(int def) {
		Def = def;
	}

	public char getSymbol() {
		return symbol;
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

	public int attack(Character c) {
		int damage = 0;
		if (new Random().nextInt(5) == 0) return damage;
		
		else {
			damage = (int) Math.ceil(100.0 / (100.0 + (double) c.Def)) * this.Atk;
			c.setHP(c.getHP() - damage);
			return damage;
		}
	}

	public void move(Map map, String cmd, Boolean dig) {
		
		switch(cmd) {
		case("n"): move(map, xPos - 1, yPos, dig); break;
		case("s"): move(map, xPos + 1, yPos, dig); break;
		case("e"): move(map, xPos, yPos + 1, dig); break;
		case("w"): move(map, xPos, yPos - 1, dig); break;
		case("ne"): move(map, xPos - 1, yPos + 1, dig); break;
		case("se"): move(map, xPos + 1, yPos + 1, dig); break;
		case("nw"): move(map, xPos - 1, yPos - 1, dig); break;
		case("sw"): move(map, xPos + 1, yPos - 1, dig); break;
		
		}
	}

	void move(Map map, int x, int y, Boolean dig) { // movement to 1 surrounding tile only
		Boolean validMove = false;

		for (int i = xPos - 1; i <= xPos + 1; i++) {
			for (int j = yPos - 1; j <= yPos + 1; j++) {
					if (x == i && y == j && dig.equals(map.getMap()[x][y].getBlocked())) validMove = true;
			}
		}

		if (!validMove && this instanceof Player)
			System.out.println("Invalid move!");

		if (validMove) {
			map.getMap()[xPos][yPos].setCharacter(null);
			map.getMap()[xPos][yPos].setBlocked(false);
			map.getMap()[x][y].setCharacter(this);
			map.getMap()[x][y].updateSymbol();
			xPos = x;
			yPos = y;
			if (dig) map.getMap()[xPos][yPos].setBlocked(false);
			if (this instanceof Player) System.out.println("Your " + map.getMap()[xPos][yPos].getCharacter().getClass().getName() + " has moved.");
		}
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

}
