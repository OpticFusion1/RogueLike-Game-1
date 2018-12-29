import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Map map = Map.getInstance();
		
		Scanner in = new Scanner(System.in);

		String randGameNo = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
		System.out.println("Welcome to RogueLike No. " + randGameNo.substring(0, new Random().nextInt(randGameNo.length())) );

		String race = null;
		Player p = null;
		do {
			System.out.println("What race do you want to play as: (H)uman, (D)warf [Double gold], (O)rc [Half gold], (E)lf [No negative potions]");
			race = in.nextLine().toUpperCase();

			switch (race) {
			case ("H"):
				p = new Human();
				map.createPlayer(p);
				break;
			case ("D"):
				p = new Dwarf();
				map.createPlayer(p);
				break;
			case ("O"):
				p = new Orc();
				map.createPlayer(p);
				break;
			case ("E"):
				p = new Elf();
				map.createPlayer(p);
				break;
			}
		} while (!(race.equals("H") || race.equals("D") || race.equals("O") || race.equals("E")));

		while (true) {
			
			if (p.getHP() <= 0) {
				System.out.println("Game over! You died...");
				in.close();
				break;
			}
			
			
			System.out.println("Displaying map. Press enter.");
			in.nextLine();
			System.out.println("Floor: " + map.getFloor());
			System.out.println(map);

			System.out.println("Race: " + p.getClass().getName());
			System.out.println("HP: " + p.getHP() + "/" + p.getMaxHP());
			System.out.println("ATK: " + p.getAtk());
			System.out.println("DEF: " + p.getDef());
			System.out.println("Gold: " + p.getGold());

			System.out.println("Enter a valid command: ");
			System.out.println("Move: n, s, e, w, nw, ne, sw, se");
			System.out.println("Dig: d");
			System.out.println("Pick up item: p");
			System.out.println("Attack: a");
			System.out.println("Climb stairs: <");
			System.out.print("Command: ");
			
			playerTurn(p, in, map);

			enemyTurn(map, p);

		}
	}

	private static void playerTurn(Player p, Scanner in, Map map) {

		String cmd = in.nextLine().toLowerCase();

		if (cmd.equals("n") || cmd.equals("s") || cmd.equals("e") || cmd.equals("w") || cmd.equals("nw")
				|| cmd.equals("ne") || cmd.equals("sw") || cmd.equals("se")) {
			map.getMap()[p.getxPos()][p.getyPos()].getCharacter().move(map, cmd, false);
		}

		if (cmd.equals("d")) {
			System.out.println("Enter the direction to dig: ");
			cmd = in.nextLine();
			if (cmd.equals("n") || cmd.equals("s") || cmd.equals("e") || cmd.equals("w") || cmd.equals("nw")
					|| cmd.equals("ne") || cmd.equals("sw") || cmd.equals("se")) {
				map.getMap()[p.getxPos()][p.getyPos()].getCharacter().move(map, cmd, true);
				map.moveEnemies(p);
			}
		}

		if (cmd.equals("p")) {
			for (int i = p.getxPos() - 1; i <= p.getxPos() + 1; i++) {
				for (int j = p.getyPos() - 1; j <= p.getyPos() + 1; j++) {
					if (map.getMap()[i][j].getItem() != null) {
						System.out.println("Picked up " + map.getMap()[i][j].getItem().getClass().getName());
						p.pickUp(map.getMap()[i][j].getItem());
						map.getMap()[i][j].setItem(null);
						map.getMap()[i][j].setBlocked(false);
					}
				}
			}
		}

		if (cmd.equals("a")) {
			for (int i = p.getxPos() - 1; i <= p.getxPos() + 1; i++) {
				for (int j = p.getyPos() - 1; j <= p.getyPos() + 1; j++) {
					if (map.getMap()[i][j].getCharacter() instanceof Enemy) {
						System.out.println("Attacking " + map.getMap()[i][j].getCharacter().getClass().getName());
						int damage = p.attack(map.getMap()[i][j].getCharacter());
						if (damage == 0) System.out.println("Attack missed!");
						System.out.println("Dealt " + damage + " damage!");
						if ((map.getMap()[i][j].getCharacter().getHP() <= 0)) {
							map.getMap()[i][j].setCharacter(null);
							map.getMap()[i][j].setBlocked(false);
							System.out.println("Enemy killed!");
						}
						break;
					}
				}
			}
		}

		if (cmd.equals("<")) {
			for (int i = p.getxPos() - 1; i <= p.getxPos() + 1; i++) {
				for (int j = p.getyPos() - 1; j <= p.getyPos() + 1; j++) {
					if (map.getMap()[i][j].getSymbol() == '<') {
						if (map.getFloor() == 5) {
							System.out.println("You've finished the game. Great job!");
							in.next();
							System.exit(0);
						}
						map.nextFloor();
						map.createPlayer(p);
					}
				}
			}
		}

	}

	private static void enemyTurn(Map map, Player p) {
		
		map.moveEnemies(p);

	}
	
}
