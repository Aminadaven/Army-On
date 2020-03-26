package games.aminadav.armyon;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Screen extends JPanel implements MouseListener, MouseMotionListener {
	int FONT_SIZE;
	Color[] colors;
	GMap map;
	GArea clicked, rclicked;
	CustomButton trainButton = new CustomButton("Train"), attackButton = new CustomButton("Attack!"),
			upgradeButton = new CustomButton("Upgrade");

	public Screen(GMap map) {
		super();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.map = map;
		FONT_SIZE = 16;
		colors = new Color[12];
		colors[0] = Color.BLUE;
		colors[1] = Color.CYAN;
		colors[2] = Color.DARK_GRAY;
		colors[3] = Color.GREEN;
		colors[4] = Color.MAGENTA;
		colors[5] = Color.PINK;
		colors[6] = Color.RED;
		colors[7] = Color.BLACK;
		colors[8] = Color.ORANGE;
		colors[9] = Color.YELLOW;
		// More Colors:
		colors[10] = Color.GRAY;
		colors[11] = Color.LIGHT_GRAY;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void paint(Graphics g) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		paintMap();

	}

	void syncArea(int width, int height, Army owner) {
		map.areas[width][height].ruler = owner;
		paintArea(map.areas[width][height]);
	}

	void paintMap() {
		fillBackground(Color.WHITE);
		initAreas();

		for (int j = 0; j < map.areas[0].length; j++) {
			for (int i = 0; i < map.areas.length; i++) {
				paintArea(map.areas[i][j]);
			}
		}
	}

	void repaintMap() {
		fillBackground(Color.WHITE);
		for (int j = 0; j < map.areas[0].length; j++) {
			for (int i = 0; i < map.areas.length; i++) {
				paintArea(map.areas[i][j]);
			}
		}
	}

	void paintArea(GArea area) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setColor(colors[area.ruler.pid]);
		g2.fill(area.rect);
		stringArea(area);
	}

	void stringArea(GArea area) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setColor(colors[area.ruler.pid]);
		g2.fillRect((int) area.rect.x, (int) area.rect.y, (int) area.rect.width, FONT_SIZE);
		Font f = new Font("Arial", 1, FONT_SIZE);
		g2.setFont(f);
		g2.setColor(Color.WHITE);
		g2.drawString("Level: " + area.lvl, (float) area.rect.x,
			(float) (area.rect.y + FONT_SIZE));
		g2.drawString("Soldiers: " + area.soldiers, (float) area.rect.x,
			(float) (area.rect.y + FONT_SIZE * 2));
	}

	void stringBoldArea(GArea area) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setColor(colors[area.ruler.pid].darker());
		g2.fillRect((int) area.rect.x, (int) area.rect.y, (int) area.rect.width, FONT_SIZE);
		Font f = new Font("Arial", 1, FONT_SIZE);
		g2.setFont(f);
		g2.setColor(Color.WHITE);
		g2.drawString("Level: " + area.lvl, (float) area.rect.x,
			(float) (area.rect.y + FONT_SIZE));
		g2.drawString("Soldiers: " + area.soldiers, (float) area.rect.x,
			(float) (area.rect.y + FONT_SIZE * 2));
	}

	Point calcAreaPoint(GArea area) {
		int x, y;
		int widthNum = map.areas.length, heightNum = map.areas[0].length;
		double unitWidth = getWidth() / widthNum, unitHeight = getHeight() / heightNum;
		x = (int) (area.rect.x / unitWidth);
		y = (int) (area.rect.y / unitHeight);
		return new Point(x, y);
	}

	private void initAreas() {
		int widthNum = map.areas.length, heightNum = map.areas[0].length;
		double unitWidth = getWidth() / widthNum, unitHeight = getHeight() / heightNum;
		double startX = 0, startY = 0;
		for (int i = 0; i < widthNum; i++) {
			for (int j = 0; j < heightNum; j++) {
				map.areas[i][j].rect = new Rectangle2D.Double(startX, startY, unitWidth, unitHeight);
				startY += unitHeight;
			}
			startY = 0;
			startX += unitWidth;
		}
	}

	private void fillBackground(Color c) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setColor(c);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (int j = 0; j < map.areas[0].length; j++) {
			for (int i = 0; i < map.areas.length; i++) {
				if (map.areas[i][j].rect != null && map.areas[i][j].rect.contains(e.getPoint())) {
					highlight(map.areas[i][j]);
				} else if (map.areas[i][j].highlight == true && !map.areas[i][j].equals(clicked)
						&& !map.areas[i][j].equals(rclicked)) {
					cancelHighlight(map.areas[i][j]);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3 && clicked != null) {
			for (int j = 0; j < map.areas[0].length; j++) {
				for (int i = 0; i < map.areas.length; i++) {
					if (map.areas[i][j].rect.contains(e.getPoint()) && (!map.areas[i][j].ruler.equals(ArmyOn.player))) {
						if (map.areas[i][j].equals(rclicked))
							return;
						if (map.getNearBordersOf(ArmyOn.player).contains(map.areas[i][j])) {
							rclick(map.areas[i][j]);
						}
						return;
					}
				}
			}
		} else {
			if (trainButton.rect != null)
				if (trainButton.rect.contains(e.getPoint())) {
					clicked.train(Integer.parseInt(JOptionPane.showInputDialog(this, "Enter number to train: ")));
					ArmyOn.updateData();
					return;
				}
			if (upgradeButton.rect != null && upgradeButton.rect.contains(e.getPoint())) {
				clicked.lvlUp();
				ArmyOn.updateData();
				return;
			}
			if (attackButton.rect != null && attackButton.rect.contains(e.getPoint())) {
				clicked.attack(calcAreaPoint(rclicked));
				ArmyOn.updateData();
				cancelRClick(rclicked);
				// cancelClick(clicked);
				return;
			}
			for (int j = 0; j < map.areas[0].length; j++) {
				for (int i = 0; i < map.areas.length; i++) {
					if (map.areas[i][j].rect.contains(e.getPoint()) && (!map.areas[i][j].ruler.equals(ArmyOn.player))) {
						if (map.areas[i][j].equals(rclicked))
							return;
					}
					if (map.areas[i][j].rect.contains(e.getPoint()) && (map.areas[i][j].ruler.equals(ArmyOn.player))) {
						if (map.areas[i][j].equals(clicked))
							return;
						click(map.areas[i][j]);
						return;
					}
				}
			}
		}
	}

	private void click(GArea gArea) {
		if (clicked != null)
			cancelClick(clicked);
		clicked = gArea;
		highlight(gArea);
		showMenuOf(gArea);
	}

	private void cancelClick(GArea lastClicked) {
		hideMenuOf(lastClicked);
		cancelHighlight(lastClicked);
	}

	private void rclick(GArea gArea) {
		if (rclicked != null)
			cancelRClick(clicked);
		rclicked = gArea;
		highlight(gArea);
		showAttackOn(gArea);
	}

	private void cancelRClick(GArea lastClicked) {
		hideAttackOn(lastClicked);
		cancelHighlight(lastClicked);
	}

	private void showMenuOf(GArea gArea) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		trainButton.rect = new Rectangle2D.Double(gArea.rect.x + gArea.rect.width / 2,
				gArea.rect.y + gArea.rect.height / 2, gArea.rect.width / 2, gArea.rect.height / 2);
		trainButton.paint(g2);
		upgradeButton.rect = new Rectangle2D.Double(gArea.rect.x, gArea.rect.y + gArea.rect.height / 2,
				gArea.rect.width / 2, gArea.rect.height / 2);
		upgradeButton.paint(g2);
	}

	private void hideMenuOf(GArea lastClicked) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setColor(colors[lastClicked.ruler.pid]);
		g2.fill(lastClicked.rect);
		trainButton.rect = null;
		upgradeButton.rect = null;
	}

	private void showAttackOn(GArea gArea) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		attackButton.rect = new Rectangle2D.Double(gArea.rect.x, gArea.rect.y + gArea.rect.height / 2, gArea.rect.width,
				gArea.rect.height / 2);
		attackButton.paint(g2);
	}

	private void hideAttackOn(GArea lastClicked) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setColor(colors[lastClicked.ruler.pid]);
		g2.fill(lastClicked.rect);
		attackButton.rect = null;
	}

	private void highlight(GArea gArea) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		if (gArea.highlight == true)
			return;
		gArea.highlight = true;
		g2.setColor(colors[gArea.ruler.pid].darker());
		g2.fill(gArea.rect);
		stringBoldArea(gArea);
	}

	private void cancelHighlight(GArea gArea) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		gArea.highlight = false;
		g2.setColor(colors[gArea.ruler.pid]);
		g2.fill(gArea.rect);
		stringArea(gArea);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}

class CustomButton {
	String label;
	Rectangle2D.Double rect;
	JComponent parent;

	public CustomButton() {
	}

	public CustomButton(String label) {
		this.label = label;
	}

	void paint(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.draw(rect);
		g2.setColor(Color.WHITE);
		g2.fill(rect);

		Font f = new Font("Arial", 1, 20);
		g2.setFont(f);
		g2.setColor(Color.BLACK);
		g2.drawString(label, (float) rect.x, (float) (rect.y + rect.height));
	}
}