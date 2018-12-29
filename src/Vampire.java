
class Vampire extends Enemy{
	public Vampire(int x, int y) {
		super(180, 30, 25, 'V');
		setxPos(x);
		setyPos(y);
	}
	
	public int attack(Character c) {
		int damage = (int) Math.ceil(100.0 / (100.0 + (double) c.getDef())) * this.getAtk();
		c.setHP(c.getHP() - damage);
		this.setHP(this.getHP() + (damage / 10));
		System.out.println("Vampire restores " + damage / 10 +" health");
		return damage;		
	}
}
