class Tile {
	private Boolean blocked;
	private Character character = null;
	private Item item = null;
	private char symbol;
	
	Tile(Boolean blocked) {
		setBlocked(blocked);
	}
	
	void updateSymbol() {
		if (symbol == '<') setSymbol('<');
		else if (character != null) setSymbol(character.getSymbol());
		else if (item != null) setSymbol(item.getSymbol());
		else if (blocked) setSymbol('#');
		else setSymbol('.');
	}

	public Boolean getBlocked() {	
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
		blocked = true;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
		blocked = true;
	}

	public char getSymbol() {
		return symbol;
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
}
