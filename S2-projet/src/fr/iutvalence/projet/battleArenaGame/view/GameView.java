package fr.iutvalence.projet.battleArenaGame.view;

import fr.iutvalence.projet.battleArenaGame.Board;
import fr.iutvalence.projet.battleArenaGame.GameController;
import fr.iutvalence.projet.battleArenaGame.pawn.Pawn;
import fr.iutvalence.projet.battleArenaGame.spell.SpellPage;

/**
 * Handle the interaction with the user when the game is running
 */

public interface GameView {
	
	//Ask 
	
	/**
	 * Ask the player to pick between every possible actions choice
	 * @param currentPlayerIndex 
	 */
	public void askActionChoice(int currentPlayerIndex);
	
	/**
	 * ask the player to pick a spell
	 * @param currentPlayerIndex the index of the user chosen by the controller
	 */
	public void askSpell(int currentPlayerIndex);
	
	/**
	 * Ask the player to pick a page for his pawn
	 * @param currentPlayerIndex the index of the user chosen by the controller
	 */
	public void askPageSelection(int currentPlayerIndex);
	
	/**
	 * Displays the board 
	 * @param myBoard
	 */
	
	public void askMove(int currentPlayerIndex);
	
	
	//Display
	
	/**
	 * Display to the player what he have to do in order to select a spell
	 */
	public void displaySpellSelection();
		
	/**
	 * Display the board to user
	 * @param myBoard
	 * @param nbPlayer
	 */
	public void displayBoard(Board myBoard, int nbPlayer);

	/**
	 * Display a status messages based on StatusMessages enumeration 
	 */
	public void displayStatus(StatusMessages msg);
	
	/**
	 * Display the owned spell pages
	 */
	public void displaySpellPage();
	
	/**
	 * Display the actions that the player can move
	 */
	public void displayChoiceAction();
	
	/**
	 * Display a ending message for the game
	 * @param Pstat status of the Game (victory,defeat,draw)
	 * @param teamId display the team linked to the status
	 */
	public void displayEnd(String winTeam);
	
	/**
	 * Display that the turn has changed
	 */
	public void displayNextTurn(int numPlayer);
	
	/**
	 * Ensure the player that his pawn has been moved
	 */
	public void displayMoveDone();

	/**
	 * Display every attributes of every spell in a page
	 * @param pPage the described page
	 */
	public void displaySpellPageDetail(SpellPage pPage);
	
	/**
	 * say Size is to small for the amount of pawns
	 */
	public void diplaySizeError();
	
	/**
	 * Show for which pawn you have to select a page
	 * @param Pawn
	 */
	public void displaySelectForThisPawn(String pawnName);
	
	/**
	 * Show to the player what he need to do to move
	 */
	public void displayMoveSelection();
	
	
	//Setter
	public void setGameController(GameController GC);
}
