
class WoundATK extends Potion {

	public void pickUp(Player p) {
		if (p instanceof Elf) p.pickUp(new BoostATK());
		else {
			int lower = p.getAtk() - 2;
			if (lower <= 0) p.setAtk(1);
			else p.setAtk(lower);
		}
	}

}
