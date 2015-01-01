package uk.ac.cam.ss2249;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.GridGUI;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;
import uk.ac.cam.ss2249.boggle.Engine;
import uk.ac.cam.ss2249.boggle.FoundWord;
import uk.ac.cam.ss2249.boggle.UI;

/**
 * GUI handles the GUI of the game such as the window and the layout.
 * Certain inputs then call functions from the game engine
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
public class GUI implements UI{

	private Engine e;
	
	private GridGUI gui;
	private JLabel scoreLabel;
	private JFrame frame;
	private JPanel sideBar;
	
	/**
	 * Starts the game and lays out the GUI
	 * 
	 */
	public void startGame() {
		e = new Engine(this);
		frame = new JFrame("Java Word Game");

		JPanel controls = new JPanel();
		JPanel wordEntry = new JPanel();

		
		wordEntry.setLayout(new BorderLayout());
		controls.setLayout(new BorderLayout());

		

		frame.setTitle("Java Word Game");

		frame.getContentPane().setLayout(new BorderLayout());
		resetGui();
		frame.getContentPane().add(getButtons(), BorderLayout.SOUTH);
		frame.getContentPane().add(getScorePanel(), BorderLayout.NORTH);
		frame.pack();
		frame.setResizable(false);
		frame.toFront();

		frame.setBackground(Color.lightGray);
		frame.setVisible(true);
		
		e.guiReady();
	}
	
	/**
	 * Resets the GUI for a new game
	 * 
	 */
	public void resetGui(){
		e.reset();
		
		if(gui != null)
			frame.getContentPane().remove(gui);
		gui = new GridGUI(e.getGrid());
		frame.getContentPane().add(gui, BorderLayout.WEST);
		
		gui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Tile source = (Tile) actionEvent.getSource();
				e.tileClicked(source);
			}
		});
		
		if(sideBar != null)
			frame.getContentPane().remove(sideBar);
		sideBar = getSideBar();
		frame.getContentPane().add(sideBar, BorderLayout.EAST);
		
		frame.getContentPane().validate();
	}

	/*
	 * Methods from the UI interface, see that interface for JavaDocs
	 * 
	 */
	@Override
	public void uncheckTileAtPoint(Point p) {
		gui.locateTileGUI(p).setUnchecked();
	}

	@Override
	public void checkTileAtPoint(Point p) {
		gui.locateTileGUI(p).setChecked();
	}
	
	@Override
	public void usedTileAtPoint(Point p){
		gui.locateTileGUI(p).setInactive();
	}
	
	@Override
	public void onError(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("There was an error!\n");
		sb.append(message);
		showAlert(sb.toString());
	}

	@Override
	public void showAlert(String message) {
		JOptionPane.showMessageDialog(gui, message);
	}
	
	/**
	 * Gets the buttons used at the bottom of the window
	 * 
	 * @return the buttons JPanel
	 */
	private JPanel getButtons(){
		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		JButton checkWord = new JButton();
		checkWord.setText("Check Word");
		checkWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				e.tryForWord();
			}
		});
		buttons.add(checkWord, BorderLayout.WEST);
		
		
		JButton bestWord = new JButton();
		bestWord.setText("Best Word");
		bestWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				e.selectBestWord();
			}
		});
		buttons.add(bestWord, BorderLayout.EAST);
		return buttons;
	}
	
	/**
	 * Gets the sidebar JPanel
	 * 
	 * @return sidebar JPanel
	 */
	private JPanel getSideBar(){
		JPanel s = new JPanel();
		s.setLayout(new BoxLayout(s, BoxLayout.Y_AXIS));
		
		
		
		JLabel usedWordsLabel = new JLabel();
		usedWordsLabel.setText("Your used words:");
		s.add(usedWordsLabel);
		
		return s;
	}
	
	/**
	 * Gets the score JPanel
	 * 
	 * @return score JPanel
	 */
	private JPanel getScorePanel(){
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new FlowLayout());
		
		JLabel scoreDescLabel = new JLabel();
		scoreDescLabel.setText("Your score is: ");
		scorePanel.add(scoreDescLabel);
		
		scoreLabel = new JLabel();
		scoreLabel.setText("0");
		scorePanel.add(scoreLabel);
		
		return scorePanel;
	}

	/**
	 * Updates the sidebar words from the engine
	 * 
	 */
	@Override
	public void updateWords() {
		if(scoreLabel != null)
			scoreLabel.setText(String.valueOf(e.getScore()));
		FoundWord word = e.getRecentWord();
		if(word != null){
			StringBuilder sb = new StringBuilder();
			sb.append(word.getWord());
			sb.append(" (");
			sb.append(word.getScore());
			sb.append(")");
			JLabel wordLabel = new JLabel();
			wordLabel.setText(sb.toString());
			sideBar.add(wordLabel);
		}
	}

}
