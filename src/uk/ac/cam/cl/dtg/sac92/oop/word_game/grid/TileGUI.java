package uk.ac.cam.cl.dtg.sac92.oop.word_game.grid;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * TileGUI
 *
 * This class generates a GUI element that represents a tile in the game grid.
 * 
 * @author Stephen Cummins
 * @version 1.0 Released 11/10/2005
 */
public class TileGUI extends JButton {

	private static final long serialVersionUID = 1L;
	private Tile model;
	

	/**
	 * The constructor accepts a tile and generates the button for the grid.
	 * 
	 * @param tile
	 */
	public TileGUI(Tile tile) {
		super();
		model = tile;
		this.setContentAreaFilled(true);
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setUnchecked();
		
		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.CENTER);
	}

	private String setTwoDigits(String string) {
		if (string.length() == 1) {
			return "0" + string;
		} else if (string.length() == 2) {
			return string;
		} else {
			throw new IllegalArgumentException("TileGUI:setTwoDigits(" + string + "): more than two digits");
		}
	}

	/**
	 * This will allow you to change the foreground colour of the tile.
	 * 
	 * @param A
	 *            colour (Color)
	 */
	@Override
	public void setForeground(Color colour) {
		super.setForeground(colour);
		if (model != null) {
			this.setText(makeHTML(colour, model));
		}
	}
	
	public void setInactive(){
		this.setIcon(new ImageIcon(this.getClass().getResource("/images/blankscrabbleblack.png")));
		setForeground(Color.darkGray);
	}
	
	public void setChecked(){
		this.setIcon(new ImageIcon(this.getClass().getResource("/images/blankscrabblegreen.png")));
		setForeground(Color.black);
	}
	
	public void setUnchecked(){
		this.setIcon(new ImageIcon(this.getClass().getResource("/images/blankscrabble.png")));
		setForeground(Color.black);
	}

	/**
	 * This method will get you the tile object which is being represented by
	 * this GUI element
	 * 
	 * @return a Tile
	 */
	public Tile getTile() {
		return model;
	}
	
	private String makeHTML(Color colour, Tile tile) {
		String redString = Integer.toHexString(colour.getRed());
		String greenString = Integer.toHexString(colour.getGreen());
		String blueString = Integer.toHexString(colour.getBlue());
		redString = setTwoDigits(redString);
		greenString = setTwoDigits(greenString);
		blueString = setTwoDigits(blueString);

		StringBuffer result = new StringBuffer();
		result.append("<html><font color=\"#");
		result.append(redString);
		result.append(greenString);
		result.append(blueString);
		result.append("\" size=\"+5\">" + tile.letter() + "</font><sub>" + tile.value() + "</sub>");
		return result.toString();
	}
}
