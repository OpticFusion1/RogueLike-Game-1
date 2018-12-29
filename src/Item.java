
abstract class Item {
	private char symbol;

	public char getSymbol() {
		return symbol;
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
	
	public abstract void pickUp(Player player);
	
}
