package games.aminadav.armyon;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JFrame;

public class GMap {
	GArea[][] areas;
	Screen gMap;

	public GMap(int widthNum, int heightNum, Vector<Army> armies) {
		areas = new GArea[widthNum][heightNum];
		int areasToArmy = (widthNum * heightNum) / (armies.size());
		int cAreas = 0, cArmy = 0;
		for (int i = 0; i < widthNum; i++) {
			for (int j = 0; j < heightNum; j++) {
				if (cAreas >= areasToArmy) {
					cArmy++;
					cAreas = 0;
				}
				if (cArmy >= armies.size()) {
					cArmy = 0;
					cAreas = 0;
					areasToArmy = 1;
				}
				Army current = armies.get(cArmy);
				areas[i][j] = new GArea(current);
				cAreas++;
			}
		}
		gMap = new Screen(this);
	}

	public void addTo(JFrame frame) {
		//gMap.FONT_SIZE = (120 / areas.length - 5);
		gMap.FONT_SIZE = 20 - ArmyOn.armies.size();
		frame.add(gMap);
	}

	public Vector<GArea> getAreas(Army owner) {
		Vector<GArea> ownerAreas = new Vector<GArea>();
		for (int i = 0; i < areas.length; i++) {
			for (int j = 0; j < areas[i].length; j++) {
				if (areas[i][j].ruler.equals(owner)) {
					ownerAreas.add(areas[i][j]);
				}
			}
		}
		return ownerAreas;
	}

	public Vector<GArea> getBordersOf(Army owner) {
		Vector<GArea> borderAreas = new Vector<GArea>();

		for (int i = 0; i < areas.length; i++) {
			for (int j = 0; j < areas[i].length; j++) {
				if (areas[i][j].ruler.equals(owner)) {
					if (!areas[i - 1 < 0 ? areas.length - 1 : i - 1][j - 1 < 0 ? areas[i].length - 1 : j - 1].ruler
							.equals(owner)
							|| !areas[i - 1 < 0 ? areas.length - 1 : i - 1][j].ruler.equals(owner)
							|| !areas[i - 1 < 0 ? areas.length - 1 : i - 1][j + 1 > areas[i].length - 1 ? 0
									: j + 1].ruler.equals(owner)
							|| !areas[i][j + 1 > areas[i].length - 1 ? 0 : j + 1].ruler.equals(owner)
							|| !areas[i][j - 1 < 0 ? areas[i].length - 1 : j - 1].ruler.equals(owner)
							|| !areas[i + 1 > areas.length - 1 ? 0 : i + 1][j - 1 < 0 ? areas[i].length - 1
									: j - 1].ruler.equals(owner)
							|| !areas[i + 1 > areas.length - 1 ? 0 : i + 1][j].ruler.equals(owner)
							|| !areas[i + 1 > areas.length - 1 ? 0 : i + 1][j + 1 > areas[i].length - 1 ? 0
									: j + 1].ruler.equals(owner)) {
						borderAreas.add(areas[i][j]);
					}
				}
			}
		}
		return borderAreas;
	}

	public Vector<GArea> getNearBordersOf(Army owner) {
		Vector<GArea> borderAreas = new Vector<GArea>();

		for (int i = 0; i < areas.length; i++) {
			for (int j = 0; j < areas[i].length; j++) {
				if (areas[i][j].ruler.equals(owner)) {
					if (!areas[i - 1 < 0 ? areas.length - 1 : i - 1][j - 1 < 0 ? areas[i].length - 1 : j - 1].ruler
							.equals(owner))
						borderAreas.add(
								areas[i - 1 < 0 ? areas.length - 1 : i - 1][j - 1 < 0 ? areas[i].length - 1 : j - 1]);
					if (!areas[i - 1 < 0 ? areas.length - 1 : i - 1][j].ruler.equals(owner))
						borderAreas.add(areas[i - 1 < 0 ? areas.length - 1 : i - 1][j]);
					if (!areas[i - 1 < 0 ? areas.length - 1 : i - 1][j + 1 > areas[i].length - 1 ? 0 : j + 1].ruler
							.equals(owner))
						borderAreas.add(
								areas[i - 1 < 0 ? areas.length - 1 : i - 1][j + 1 > areas[i].length - 1 ? 0 : j + 1]);
					if (!areas[i][j + 1 > areas[i].length - 1 ? 0 : j + 1].ruler.equals(owner))
						borderAreas.add(areas[i][j + 1 > areas[i].length - 1 ? 0 : j + 1]);
					if (!areas[i][j - 1 < 0 ? areas[i].length - 1 : j - 1].ruler.equals(owner))
						borderAreas.add(areas[i][j - 1 < 0 ? areas[i].length - 1 : j - 1]);
					if (!areas[i + 1 > areas.length - 1 ? 0 : i + 1][j - 1 < 0 ? areas[i].length - 1 : j - 1].ruler
							.equals(owner))
						borderAreas.add(
								areas[i + 1 > areas.length - 1 ? 0 : i + 1][j - 1 < 0 ? areas[i].length - 1 : j - 1]);
					if (!areas[i + 1 > areas.length - 1 ? 0 : i + 1][j].ruler.equals(owner))
						borderAreas.add(areas[i + 1 > areas.length - 1 ? 0 : i + 1][j]);
					if (!areas[i + 1 > areas.length - 1 ? 0 : i + 1][j + 1 > areas[i].length - 1 ? 0 : j + 1].ruler
							.equals(owner))
						borderAreas.add(
								areas[i + 1 > areas.length - 1 ? 0 : i + 1][j + 1 > areas[i].length - 1 ? 0 : j + 1]);
				}
			}
		}
		return borderAreas;
	}

	public Vector<Point> getEnemiesOf(GArea me) {
		Vector<Point> enemies = new Vector<Point>();

		for (int i = 0; i < areas.length; i++) {
			for (int j = 0; j < areas[i].length; j++) {
				if (areas[i][j].equals(me)) {
					if (!areas[i - 1 < 0 ? areas.length - 1 : i - 1][j - 1 < 0 ? areas[i].length - 1 : j - 1].ruler
							.equals(me.ruler))
						enemies.add(new Point(i - 1 < 0 ? areas.length - 1 : i - 1,
								j - 1 < 0 ? areas[i].length - 1 : j - 1));
					if (!areas[i - 1 < 0 ? areas.length - 1 : i - 1][j].ruler.equals(me.ruler))
						enemies.add(new Point(i - 1 < 0 ? areas.length - 1 : i - 1, j));
					if (!areas[i - 1 < 0 ? areas.length - 1 : i - 1][j + 1 > areas[i].length - 1 ? 0 : j + 1].ruler
							.equals(me.ruler))
						enemies.add(new Point(i - 1 < 0 ? areas.length - 1 : i - 1,
								j + 1 > areas[i].length - 1 ? 0 : j + 1));
					if (!areas[i][j + 1 > areas[i].length - 1 ? 0 : j + 1].ruler.equals(me.ruler))
						enemies.add(new Point(i, j + 1 > areas[i].length - 1 ? 0 : j + 1));
					if (!areas[i][j - 1 < 0 ? areas[i].length - 1 : j - 1].ruler.equals(me.ruler))
						enemies.add(new Point(i, j - 1 < 0 ? areas[i].length - 1 : j - 1));
					if (!areas[i + 1 > areas.length - 1 ? 0 : i + 1][j - 1 < 0 ? areas[i].length - 1 : j - 1].ruler
							.equals(me.ruler))
						enemies.add(new Point(i + 1 > areas.length - 1 ? 0 : i + 1,
								j - 1 < 0 ? areas[i].length - 1 : j - 1));
					if (!areas[i + 1 > areas.length - 1 ? 0 : i + 1][j].ruler.equals(me.ruler))
						enemies.add(new Point(i + 1 > areas.length - 1 ? 0 : i + 1, j));
					if (!areas[i + 1 > areas.length - 1 ? 0 : i + 1][j + 1 > areas[i].length - 1 ? 0 : j + 1].ruler
							.equals(me.ruler))
						enemies.add(new Point(i + 1 > areas.length - 1 ? 0 : i + 1,
								j + 1 > areas[i].length - 1 ? 0 : j + 1));
				}
			}
		}
		return enemies;
	}

	public void show() {
		gMap.paintMap();
	}
}

class GArea {
	Army ruler;
	Rectangle2D.Double rect;
	boolean highlight = false;
	int soldiers, lvl;

	public GArea(Army army) {
		ruler = army;
		soldiers = 250;
		lvl = 0;
	}

	public int getIncome() {
		return (int) (2150 * (1 + lvl * 0.55)) - (soldiers * 7);
	}

	void lvlUp() {
		int cost = (int) ((lvl + 0.95) * (9500));
		if (ruler.cash < cost)
			return;
		lvl++;
		ruler.cash -= cost;
		ArmyOn.out("System: " + ruler.name + " has leveled up a territory.");
		ArmyOn.gameMap.gMap.stringBoldArea(this);
	}

	void train(int num) {
		if (num < 0 || ruler.cash < 0)
			return;
		int cost = num * 25;
		if (ruler.cash < cost) {
			num = ruler.cash / 25;
			cost = num * 25;
		}
		soldiers += num;
		ruler.cash -= cost;
		ArmyOn.out("System: " + ruler.name + " has recruited " + num + " soldiers in a territory.");
		ArmyOn.gameMap.gMap.stringBoldArea(this);
	}

	void attack(Point p) {
		GArea enemyArea = ArmyOn.gameMap.areas[p.x][p.y];
		enemyArea.ruler.attacked = true;
		ArmyOn.out("System: " + ruler.name + " has attacked a territory of " + enemyArea.ruler.name);

		int attackerLoses = soldiers, defenderLoses = enemyArea.soldiers;
		while (soldiers > 0 && enemyArea.soldiers > 0) {
			int myPower = (int) (soldiers * (ruler.lvl * 0.5 + 1) * 0.1);
			int enemyPower = (int) (enemyArea.soldiers * (enemyArea.ruler.lvl * 0.5 + 1) * 0.1);

			int myAL = Math.min(soldiers, enemyPower);
			int enemyAL = Math.min(enemyArea.soldiers, myPower);

			soldiers -= myAL;
			enemyArea.soldiers -= enemyAL;
		}
		attackerLoses -= soldiers;
		defenderLoses -= enemyArea.soldiers;

		if (ruler.equals(ArmyOn.player))
			ArmyOn.gameMap.gMap.stringBoldArea(this);
		else
			ArmyOn.gameMap.gMap.stringArea(this);
		ArmyOn.gameMap.gMap.stringArea(enemyArea);

		ArmyOn.out("Loses: the attacker has lost " + attackerLoses + " soldiers, and the defender has lost "
				+ defenderLoses + " soldiers.");

		if (soldiers > 0 && enemyArea.soldiers <= 0) {
			ArmyOn.out("System: " + ruler.name + " has Conquered a territory of " + enemyArea.ruler.name);
			annex(p);
		} else
			ArmyOn.out(
					"System: " + ruler.name + " failed in his try to conquere a territory of " + enemyArea.ruler.name);
	}

	private void annex(Point p) {
		GArea enemyArea = ArmyOn.gameMap.areas[p.x][p.y];
		Army enemy = enemyArea.ruler;
		enemyArea.ruler = ruler;
		ArmyOn.gameMap.gMap.syncArea(p.x, p.y, ruler);
		int soldiersToMove = ruler.askNumToMove(this);
		soldiers -= soldiersToMove;
		enemyArea.soldiers += soldiersToMove;
		ArmyOn.gameMap.gMap.stringBoldArea(this);
		ArmyOn.gameMap.gMap.stringArea(enemyArea);
		if (ArmyOn.gameMap.getAreas(enemy).size() < 1) {
			enemy.kill();
		}
	}
}