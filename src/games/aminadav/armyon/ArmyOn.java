package games.aminadav.armyon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Locale;
//import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ArmyOn {

	//static Scanner in = new Scanner(System.in);
	static Vector<Army> armies = new Vector<Army>();
	static Army player = new Army();
	static GMap gameMap;
	static int delimeter = 0;
	static JTextArea output = new JTextArea(20, 30);
	static JLabel dataBar = new JLabel("");
	static JButton endTurn = new JButton("End Turn");
	static JButton lvlup = new JButton("Level Up your Soldiers");

	static void updateData() {
		dataBar.setText("Name: " + player.name + ", troops level: " + player.lvl + ", money: " + player.cash);
	}

	static String in(String str) {
		return JOptionPane.showInputDialog(str);
	}

	static void out(String str) {
		// System.out.println(str);
		output.append(str + "\n");
	}

	public static void main(String[] args) {
		MainFrame f = new MainFrame();
		JPanel top = new JPanel();

		f.addWindowStateListener(f);
		endTurn.addActionListener(f);

		lvlup.addActionListener(f);

		top.add(endTurn);
		top.add(lvlup);
		top.add(dataBar);

		output.setEditable(false);
		JScrollPane os = new JScrollPane(output);

		f.setMinimumSize(new Dimension(600, 600));
		f.setLayout(new BorderLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Army On 2D");
		// f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setLocale(Locale.getDefault());
		// main.setIconImage(null);
		f.setVisible(true);
		init();
		updateData();
		gameMap.addTo(f);

		f.add(top, BorderLayout.PAGE_START);
		f.add(os, BorderLayout.PAGE_END);
		f.pack();
		
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		gameMap.show();
            }
        });
		aiTurn();
	}

	private static void init() {
		final int MAX_PLAYERS = 6; // NO MORE THAN 12!!!!
		int aNum = 1 + (int) (Math.random() * MAX_PLAYERS);
		for (int i = 0; i < aNum; i++)
			addArmy();
		player.name = in("Please enter your name: ");
		armies.add(player);
		gameMap = new GMap(armies.size(), armies.size(), armies);
		ArmyOn.lvlup.setText("Level Up your Soldiers, Cost: "+(ArmyOn.player.lvl + 1) * (27500));
	}

	private static void addArmy() {
		Army toAdd = new Army();
		toAdd.name = "Army " + toAdd.pid;
		armies.add(toAdd);
	}

	static void aiTurn() {
		if (armies.contains(player)) {
			for (int i = 0; i < armies.size() + delimeter - 1; i++) {
				Army cArmy = armies.get(i);
				cArmy.doTurn();
			}
			delimeter = 0;
			if (armies.size() == 1 && armies.contains(player))
				winGame();
		} else
			loseGame();
	}

	private static void loseGame() {
		JOptionPane.showMessageDialog(null, "You have Lost the game!");
		out("You have Lost the game!");
		// main(null);
		// System.exit(0);
	}

	private static void winGame() {
		JOptionPane.showMessageDialog(null, "You have Won the game!");
		out("You have Won the game!");
		// main(null);
		// System.exit(0);
	}
}

class Army {
	static int id = 0;
	String name;
	int pid, cash, lvl;
	boolean attacked = false;

	public Army() {
		pid = Army.id;
		Army.id++;
		cash = 3000;
		lvl = 0;
	}

	public void doTurn() {
		if (attacked)
			randomBorder().train(cash / 25);
		else {
			if (Math.random() * 100 < 39)
				randomArea().lvlUp();
			if (Math.random() * 100 < 49)
				lvlUp();
			randomBorder().train(cash / 60);
		}
		int toAtk = (int) (Math.random() * ArmyOn.armies.size()) + 1;
		for (int i = 0; i < toAtk; i++) {
			if (Math.random() * 100 < 64) {
				GArea attacker = randomBorder();
				Vector<Point> enemies = ArmyOn.gameMap.getEnemiesOf(attacker);
				Point p = enemies.get((int) (Math.random() * enemies.size()));
				GArea attacked = ArmyOn.gameMap.areas[p.x][p.y];
				if (attacker.soldiers > attacked.soldiers)
					attacker.attack(p);
				else
					i--;
			}
		}
		attacked = false;
		endTurn();
	}

	private GArea randomArea() {
		Vector<GArea> myAreas = ArmyOn.gameMap.getAreas(this);
		return myAreas.get((int) (Math.random() * myAreas.size()));
	}

	private GArea randomBorder() {
		Vector<GArea> borders = ArmyOn.gameMap.getBordersOf(this);
		return borders.get((int) (Math.random() * borders.size()));
	}

	public Army(int soldiers, int cash, int lvl, int areas) {
		this.cash = cash;
		this.lvl = lvl;
	}

	void lvlUp() {
		int cost = (lvl + 1) * (27500);
		if (cash < cost)
			return;
		lvl++;
		cash -= cost;
		ArmyOn.out("System: Army " + name + " has leveled up his army.");
	}

	void endTurn() {
		for (GArea area : ArmyOn.gameMap.getAreas(this)) {
			cash += area.getIncome();
		}
	}

	public void kill() {
		ArmyOn.out(name + " Have been Killed!");
		ArmyOn.armies.remove(this);
		ArmyOn.delimeter--;
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		ArmyOn.gameMap.gMap.FONT_SIZE = (120 / ArmyOn.armies.size());
	}

	public int askNumToMove(GArea origin) {
		if (this.equals(ArmyOn.player)) {
			return Math.min(origin.soldiers, Integer.parseInt(JOptionPane.showInputDialog(
					"You have Conquered the territory! \n Choose how many soldiers will pass to it: ")));
		}
		return (int) (origin.soldiers / (Math.random() * 3 + 1));
	}
}