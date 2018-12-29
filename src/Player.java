abstract class Player extends Character {
	private int gold;
	private final int MaxHP;
	
	Player(int HP, int Atk, int Def) {
		super(HP, Atk, Def, '@');
		this.MaxHP = HP;
	}
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getMaxHP() {
		return MaxHP;
	}
	
	public void pickUp(Item i) {
		i.pickUp(this);
	}

}
