import java.util.ArrayList;
import java.util.Random;

public class Map {
	private final int MAXROW = 20;
	private final int MAXCOL = 50;
	private final int ROOM_MAXSIZE = 10;
	private final int ROOM_MINSIZE = 6;
	private final int MAXROOMS = 4;
	private final int MAXMONSTERS = 3 + getFloor();
	private final int MAXITEMS = 7 - getFloor();
	private Tile[][] map = new Tile[MAXROW][MAXCOL];
	private int floor = 1;
	private ArrayList<Rect> rooms = new ArrayList<Rect>();
	private static Map instance = new Map(); // singleton

	private Map() {
		makeMap();
	}

	public void moveEnemies(Player p) {

		for (int i = 0; i < MAXROW; i++) {
			for (int j = 0; j < MAXCOL; j++) {

				if (map[i][j].getCharacter() instanceof Enemy) {
					Enemy e = (Enemy) map[i][j].getCharacter();

					Boolean nextToPlayer = false;

					for (int x = e.getxPos() - 1; x <= e.getxPos() + 1; x++) {
						for (int y = e.getyPos() - 1; j <= e.getyPos() + 1; j++) {
							if (map[x][y].getSymbol() == '@') {
								nextToPlayer = true;
							}
						}
					}

					if (nextToPlayer) {
						int damage = e.attack(p);
						if (damage == 0) System.out.println("Attack misses!");
						System.out.println(e.getClass().getName() + " dealt " + damage + " damage to you!");
					} else if (!(e instanceof Dragon)) {
						int[][] moves = { { e.getxPos() - 1, e.getyPos() }, { e.getxPos() + 1, e.getyPos() },
								{ e.getxPos(), e.getyPos() + 1 }, { e.getxPos(), e.getyPos() - 1 },
								{ e.getxPos() - 1, e.getyPos() + 1 }, { e.getxPos() + 1, e.getyPos() + 1 },
								{ e.getxPos() - 1, e.getyPos() - 1 }, { e.getxPos() + 1, e.getyPos() - 1 } };

						Random rand = new Random();
						int[] chosenMove = moves[rand.nextInt(moves.length)];
						e.move(this, chosenMove[0], chosenMove[1], false);
					}
				}

			}
		}
	}

	private void makeMap() {
		for (int i = 0; i < MAXROW; i++) {
			for (int j = 0; j < MAXCOL; j++) {
				map[i][j] = new Tile(true);
				map[i][j].updateSymbol();
			}
		}
		generateDungeon();
	}

	private void generateDungeon() {
		rooms.clear();

		int numRooms = 0;

		for (int i = 0; i < MAXROOMS; i++) {

			Random rand = new Random();

			// get random ints between min and max room sizes
			int width = rand.ints(1, ROOM_MINSIZE, ROOM_MAXSIZE).findFirst().getAsInt();
			int height = rand.ints(1, ROOM_MINSIZE, ROOM_MAXSIZE).findFirst().getAsInt();

			int x = rand.nextInt(MAXROW - width);
			int y = rand.nextInt(MAXCOL - height);

			Rect newRoom = new Rect(x, y, width, height);

			Boolean failed = false;

			for (Rect room : rooms) {
				if (newRoom.intersect(room)) {
					failed = true;
					break;
				}
			}

			// create the room and connect to previous one with tunnels
			if (!failed) {
				createRoom(newRoom);
				rooms.add(newRoom);
				populateRoom(newRoom);
				numRooms++;

				if (numRooms > 1) {
					int[] newCenter = newRoom.center();

					int[] prevCenter = rooms.get(numRooms - 2).center();

					if (rand.nextBoolean()) {
						createHorTunnel(prevCenter[0], newCenter[0], prevCenter[1]);
						createVerTunnel(prevCenter[1], newCenter[1], newCenter[0]);
					}

					else {
						createVerTunnel(prevCenter[1], newCenter[1], prevCenter[0]);
						createHorTunnel(prevCenter[0], newCenter[0], newCenter[1]);
					}
				}

			}

		}

		// place stairs at center of a random room
		Rect randRoom = rooms.get(new Random().nextInt(rooms.size()));
		map[randRoom.center()[0]][randRoom.center()[1]].setSymbol('<');

	}

	private void populateRoom(Rect room) {
		Random rand = new Random();
		int numMonster = rand.nextInt(MAXMONSTERS);

		for (int i = 0; i < numMonster; i++) {
			int x = rand.ints(1, room.getX1() + 1, room.getX2() - 1).findFirst().getAsInt();
			int y = rand.ints(1, room.getY1() + 1, room.getY2() - 1).findFirst().getAsInt();

			if (!map[x][y].getBlocked()) {
				if (rand.nextInt(9) < floor) {
					map[x][y].setBlocked(true);
					map[x][y].setCharacter(new Troll(x, y));
					continue;
				}

				if (rand.nextInt(9) < floor) {
					map[x][y].setBlocked(true);
					map[x][y].setCharacter(new Phoenix(x, y));
					continue;
				}


				if (rand.nextInt(18) < (3 * floor)) {
					map[x][y].setBlocked(true);
					map[x][y].setCharacter(new Vampire(x, y));
					continue;
				}

				if (rand.nextInt(9) < (2 * floor)) {
					map[x][y].setBlocked(true);
					map[x][y].setCharacter(new Werewolf(x, y));
					continue;
				}

				else {
					map[x][y].setBlocked(true);
					map[x][y].setCharacter(new Goblin(x, y));
				}
			}

		}

		int numItems = rand.nextInt(MAXITEMS);
		Potion[] potions = { new RestoreHP(), new PoisonHP(), new WoundATK(), new WoundDEF(), new BoostATK(),
				new BoostDEF() };

		for (int i = 0; i < numItems; i++) {
			int x = rand.ints(1, room.getX1() + 1, room.getX2() - 1).findFirst().getAsInt();
			int y = rand.ints(1, room.getY1() + 1, room.getY2() - 1).findFirst().getAsInt();

			if (!map[x][y].getBlocked()) {
				if (rand.nextBoolean())
					map[x][y].setItem(potions[rand.nextInt(potions.length)]);
				else {
					Gold g = new Gold();
					if (g.getDragonHoard()) {
						for (int index = x - 1; index <= x + 1; index++) {
							for (int j = y - 1; j <= y + 1; j++) {
								if (index != x && j != y) { // ignore the center tile
									if (!map[index][j].getBlocked()) {
										map[index][j].setCharacter(new Dragon(index, j));
									}
								}
							}
						}
					}
					map[x][y].setItem(g);
				}
			}

		}

	}

	private void createHorTunnel(int x1, int x2, int y) {
		for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
			map[x][y].setBlocked(false);
		}
	}

	private void createVerTunnel(int y1, int y2, int x) {
		for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
			map[x][y].setBlocked(false);
		}
	}

	private void createRoom(Rect room) {
		for (int i = room.getX1() + 1; i < room.getX2(); i++) {
			for (int j = room.getY1() + 1; j < room.getY2(); j++) {
				map[i][j].setBlocked(false);
			}
		}
	}

	public void createPlayer(Player p) {
		Random rand = new Random();
		Rect randRoom = rooms.get(rand.nextInt(rooms.size()));
		
		// keep trying if trying to place player on stairs
		while (map[randRoom.center()[0]][randRoom.center()[1]].getSymbol() == '<') {
			randRoom = rooms.get(rand.nextInt(rooms.size()));
		}
		
		map[randRoom.center()[0]][randRoom.center()[1]].setCharacter(p);
		p.setxPos(randRoom.center()[0]);
		p.setyPos(randRoom.center()[1]);
		
	}

	public void nextFloor() {
		floor++;
		makeMap();
	}

	public String toString() {
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < MAXROW; i++) {
			for (int j = 0; j < MAXCOL; j++) {
				map[i][j].updateSymbol();
				s.append(map[i][j].getSymbol());
			}
			s.append("\n");
		}

		return s.toString();
	}

	public int getMAXROW() {
		return MAXROW;
	}

	public int getMAXCOL() {
		return MAXCOL;
	}

	public int getROOM_MAXSIZE() {
		return ROOM_MAXSIZE;
	}

	public int getROOM_MINSIZE() {
		return ROOM_MINSIZE;
	}

	public int getMAXROOMS() {
		return MAXROOMS;
	}

	public int getMAXHEIGHT() {
		return MAXCOL;
	}

	public Tile[][] getMap() {
		return map;
	}

	public void setMap(Tile[][] map) {
		this.map = map;
	}

	public int getMAXWIDTH() {
		return MAXROW;
	}

	static Map getInstance() {
		return instance;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getMAXMONSTERS() {
		return MAXMONSTERS;
	}

	public int getMAXITEMS() {
		return MAXITEMS;
	}

}
