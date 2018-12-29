
import java.util.Random;

class Gold extends Item {
	private int amount;
	private Boolean dragonHoard = false;
	
	public Gold() {
		amountGenerator();
		setSymbol('$');
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Boolean getDragonHoard() {
		return dragonHoard;
	}

	public void setDragonHoard(Boolean dragonHoard) {
		this.dragonHoard = dragonHoard;
	}
	
	private void amountGenerator() {
		Random rand = new Random();
		
		Boolean normalHoard = rand.nextInt(8) < 5;
		if (normalHoard) setAmount(4);

		Boolean smallHoard = rand.nextInt(4) == 0;
		if (smallHoard) setAmount(10);
		
		Boolean dragon = rand.nextInt(8) == 0;
		if (dragon) {
			setAmount(1000);
			setDragonHoard(true);
		}
		
	}

	public void pickUp(Player p) {
		if (p instanceof Dwarf) p.setGold(p.getGold() + 2 * amount);
		if (p instanceof Orc) p.setGold(p.getGold() + (amount / 2));
		else p.setGold(p.getGold() + amount);
	}
}
