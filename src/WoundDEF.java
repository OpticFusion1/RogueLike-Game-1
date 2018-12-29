
class WoundDEF extends Potion {

	public void pickUp(Player p) {
		if (p instanceof Elf) p.pickUp(new BoostDEF());
		else {
			int lower = p.getDef() - 2;
			if (lower <= 0) p.setDef(1);
			else p.setDef(lower);
		}
	}
}
