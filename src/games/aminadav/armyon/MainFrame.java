package games.aminadav.armyon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener, WindowStateListener {

	public MainFrame() {
		// super();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(ArmyOn.endTurn)) {
			ArmyOn.output.setText("");
			ArmyOn.player.endTurn();
			ArmyOn.updateData();
			ArmyOn.aiTurn();
		}
		if (e.getSource().equals(ArmyOn.lvlup)) {
			ArmyOn.player.lvlUp();
			ArmyOn.lvlup.setText("Level Up your Soldiers, Cost: "+(ArmyOn.player.lvl + 1) * (27500));
			ArmyOn.updateData();
		}
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		/*
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		*/
		//ArmyOn.gameMap.gMap.paintMap();
		
	}
}