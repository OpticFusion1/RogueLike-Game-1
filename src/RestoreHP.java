

class RestoreHP extends Potion {

	public void pickUp(Player p) {
		int restoredHP = p.getHP() + 10;
		
		if (restoredHP >= p.getMaxHP()) p.setHP(p.getMaxHP());
		else p.setHP(restoredHP);
	}

}
