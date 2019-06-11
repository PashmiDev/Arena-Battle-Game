package fr.iutvalence.projet.battleArenaGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import fr.iutvalence.projet.battleArenaGame.move.Coordinate;
import fr.iutvalence.projet.battleArenaGame.network.GameClientHandler;
import fr.iutvalence.projet.battleArenaGame.spell.Effect;
import fr.iutvalence.projet.battleArenaGame.spell.Shape;
import fr.iutvalence.projet.battleArenaGame.spell.Spell;
import fr.iutvalence.projet.battleArenaGame.spell.SpellPage;
import fr.iutvalence.projet.battleArenaGame.view.DisplayMessage;
import fr.iutvalence.projet.battleArenaGame.view.GameView;
import fr.iutvalence.projet.battleArenaGame.view.MenuChoices;
import fr.iutvalence.projet.battleArenaGame.view.PlayerConsole;
import fr.iutvalence.projet.battleArenaGame.view.PlayerWindow;
import fr.iutvalence.projet.battleArenaGame.view.UserView;
/**
 *Handle users before they start a game 
 */
public class User implements UserController
{
	/**
	 * IHM used to interact with the user
	 */
	private UserView userView;
	private ArrayList<Shape> gameShapes;
	private ArrayList<SpellPage> myPages;
	
	
	
	public User(UserView userV)
	{
		this.userView = userV;
		this.userView.setController(this);
		createDefaultShapes();
		this.myPages = new ArrayList<SpellPage>();
		this.myPages.add(new SpellPage("DefaultPage",new Spell(Effect.Fire,this.gameShapes.get(0)),new Spell(Effect.Ice,this.gameShapes.get(1)),new Spell(Effect.Electricity,this.gameShapes.get(2))));
	}
	public void launch()
	{
		while(true)
		{
			userView.display(DisplayMessage.MENU);
			userView.askChoiceMenu();	
		}
		
	}

	@Override
	public void choiceMenuRequest(MenuChoices choice)
	{
		switch(choice)
		{
		case CREATE_SPELL_PAGE:
			this.userView.display(DisplayMessage.PAGE_CREATION);
			this.userView.displayElementChoice();
			this.userView.displayShapeChoice();
			this.userView.askPageCreation();	
			break;
		case JOIN_GAME:
			userView.displayListServer();
			userView.askServerConnection();
			break;
		case HOST_GAME:
			userView.display(DisplayMessage.SERVER_CONFIG);
			userView.askServerConfig();
			break;
		case LOCAL_GAME:
			userView.display(DisplayMessage.LOCAL_CONFIG);
			userView.askLocalConfig();
			break;
		}
	}
	
	@Override
	public void localConfigRequest(int nbPlayers, int nbPawns, int boardSize, int nbConsoleView)
	{
		if(nbPlayers<1)
			{
				this.userView.display(DisplayMessage.WRONG_PLAYER_NUMBER);
				return;
			}
		if(nbPawns<1)
		{
			this.userView.display(DisplayMessage.WRONG_PAWN_NUMBER);
			return;
		}
		if(boardSize*boardSize<nbPlayers*nbPawns)
		{
			this.userView.display(DisplayMessage.WRONG_BOARD_SIZE);
			boardSize = (int)Math.sqrt(nbPlayers*nbPawns)+1;
		}
		ArrayList<GameView> listPlayer = new ArrayList<GameView>();
		for(int consoleCount = 0; consoleCount < nbConsoleView; consoleCount ++)
			listPlayer.add(new PlayerConsole(this));
		for(int windowCount = 0;windowCount < nbPlayers-nbConsoleView;windowCount++)
			listPlayer.add(new PlayerWindow());
		
		new Game(listPlayer,nbPlayers,nbPawns,boardSize).play();		
	}
	
	@Override
	public void serverConfigRequest(int nbPlayers, int nbPawns, int boardSize)
	{
		ClientConnectionInfo[] clients = new GameLauncherServerClientHandler(int 12000,int nbPlayers).getClients;
		new Thread(() ->  {
			GameView gch = new GameClientHandler(clients);
			ArrayList<GameView> gchList = new ArrayList<GameView>();
			gchList.add(gch);
			this.userView.displayServerLaunched(gch.getServerIp(),gch.getPort()); 
			new Game(gchList,nbPlayers,nbPawns,boardSize).play();
		}).start();
		
	}
	
	
	@Override
	public void createDefaultShapes()
	{
		this.gameShapes = new ArrayList<Shape>();
		//Ball
		HashSet<Coordinate> ballShape = new HashSet<Coordinate>();
		ballShape.addAll(Arrays.asList(new Coordinate(0,0)));
		this.gameShapes.add(new Shape("Ball",10,2,5,3,ballShape));
		//Fist
		HashSet<Coordinate> fistShape = new HashSet<Coordinate>();
		fistShape.addAll(Arrays.asList(new Coordinate(0,0)));
		this.gameShapes.add(new Shape("Fist",15,1,1,2,fistShape));
		//Cross
		HashSet<Coordinate> crossShape = new HashSet<Coordinate>();
		crossShape.addAll(Arrays.asList(new Coordinate(0,0),new Coordinate(-2,0),new Coordinate(-1,0),new Coordinate(1,0),new Coordinate(2,0),new Coordinate(0,-2),new Coordinate(0,-1),new Coordinate(0,1),new Coordinate(0,2)));
		this.gameShapes.add(new Shape("Cross",10,3,5,4,crossShape));
		//Square
		HashSet<Coordinate> squareShape = new HashSet<Coordinate>();
		squareShape.addAll(Arrays.asList(new Coordinate(0,0),new Coordinate(0,-1),new Coordinate(0,1),new Coordinate(-1,0),new Coordinate(-1,-1),new Coordinate(-1,1),new Coordinate(1,0),new Coordinate(1,-1),new Coordinate(1,1)));
		this.gameShapes.add(new Shape("Square",10,3,4,4,squareShape));
		//Sword
		HashSet<Coordinate> swordShape = new HashSet<Coordinate>();
		swordShape.addAll(Arrays.asList(new Coordinate(-1,-1),new Coordinate(-1,0),new Coordinate(-1,1),new Coordinate(0,-1),new Coordinate(0,1),new Coordinate(1,-1),new Coordinate(1,0),new Coordinate(1,1)));
		this.gameShapes.add(new Shape("Sword",8,2,1,3,swordShape));
		//Beam
		HashSet<Coordinate> beamShape = new HashSet<Coordinate>();
		beamShape.addAll(Arrays.asList(new Coordinate(0,1),new Coordinate(0,2),new Coordinate(0,3),new Coordinate(0,4),new Coordinate(0,5)));
		this.gameShapes.add(new Shape("Beam",10,3,1,4,beamShape));
	
	}
	@Override
	public ArrayList<Shape> getGameShapes()
	{
		return this.gameShapes;
	}
	@Override
	public ArrayList<SpellPage> getSpellPages()
	{
		return this.myPages;
	}
	
	@Override
	public void createSpellPageRequest(String name, Spell spell1, Spell spell2, Spell spell3)
	{
		this.myPages.add(new SpellPage(name,spell1,spell2,spell3));
		this.userView.display(DisplayMessage.PAGE_CREATED);
	}
	
	
	


	
}
