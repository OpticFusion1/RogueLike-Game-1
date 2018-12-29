
class PoisonHP extends Potion {

	public void pickUp(Player p) {

		if (p instanceof Elf) p.pickUp(new RestoreHP());
		
		else {
			int poisonedHP = p.getHP() - 10;
			
			if (poisonedHP <= 0) p.setHP(1);
			else p.setHP(poisonedHP);
		}
	}

}
