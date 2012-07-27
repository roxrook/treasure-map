package Game;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import Graph.Graph;
import GraphUtility.*;
import Media.*;
import Character.*;

public class Game {
	public final double INFINITY = Double.MAX_VALUE;

	private Connection conn;
	private Statement stmt;
	private Vector<Player> players;
	private Vector<Treasure> treasures;
	private Vector<Node> nodes;
	private GameEventMessage eventMessages;
	private TreasureMap playMap;

	public Game() {
		eventMessages = new GameEventMessage();
		// connect with database
		stmt = DatabaseManager.connect();
		// transfer data from picture file
		playMap = new TreasureMap();
		// after initialize all collections
		players = new Vector<Player>();
		treasures = new Vector<Treasure>();
		nodes = new Vector<Node>();

		// then, generate random data for Player, Node, Treasure
		generateRandomNodesAndTreasures();
		initializeFourPlayers();
		// load from database
		loadDataFromDatabase();
	}

	private void loadDataFromDatabase() {
		// only insert the first time
		// DatabaseManager.insertPlayersIntoDatabase( stmt, players );
		// DatabaseManager.insertTreasuresIntoDatabase( stmt, treasures );
		// DatabaseManager.insertNodesIntoDatabase( stmt, nodes );

		DatabaseManager.updatePlayersFromDatabase(stmt, players);
		DatabaseManager.updateTreasuresFromDatabase(stmt, treasures);
		DatabaseManager.updateNodesFromDatabase(stmt, nodes);

		// load data
		DatabaseManager.loadPlayersFromDatabase(stmt, players);
		DatabaseManager.loadTreasuresFromDatabase(stmt, treasures);
		DatabaseManager.loadNodesFromDatabase(stmt, nodes);

		// update node database first time
		DatabaseManager.updateCurrentPlayerFromNodeWhereNodeId(stmt, players.elementAt(0).getStart(), players.elementAt(0).getPlayerId());
		DatabaseManager.updateCurrentPlayerFromNodeWhereNodeId(stmt, players.elementAt(1).getStart(), players.elementAt(1).getPlayerId());
		DatabaseManager.updateCurrentPlayerFromNodeWhereNodeId(stmt, players.elementAt(2).getStart(), players.elementAt(2).getPlayerId());
		DatabaseManager.updateCurrentPlayerFromNodeWhereNodeId(stmt, players.elementAt(3).getStart(), players.elementAt(3).getPlayerId());
	}

	private void delay(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void runGameInstruction() {
		eventMessages.showGameInstruction(players);
	}

	public void run() {
		Vector<Turtle> turtles = createTurtles(players);
		drawTreasuresAndNodeHasTreasureMap();
		getPathForAllPlayers();
		play(turtles);
		finalUpdateForPlayersWealth();
		eventMessages.showAllPlayersAfterGameOver(players);
	}

	private void finalUpdateForPlayersWealth() {
		for (Player p : players) {
			if (p.getGoal().compareTo(p.getCurrentPosition()) == 0) {
				p.setWealth(p.getStrength() + p.getWealth());
				p.setStrength(0);
			}
		}
		// update database
		DatabaseManager.updatePlayersFromDatabase(stmt, players);
	}

	public void showWinner() {
		Vector<Player> finalPlayers = new Vector<Player>();
		Vector<Player> preWinners = new Vector<Player>();
		DatabaseManager.loadPlayersFromDatabase(stmt, finalPlayers);
		Integer greatestWealth = new Integer(-1);
		Player winner = null;
		// check to see winner get to their goal
		for (Player p : finalPlayers) {
			if (p.getCurrentPosition().compareTo(p.getGoal()) == 0) {
				preWinners.add(p);
			}
		}

		// nobody get to their goal
		if (preWinners.isEmpty()) {
			preWinners = finalPlayers;
		}

		for (Player p : preWinners) {
			if (greatestWealth.compareTo(p.getWealth()) < 0) {
				greatestWealth = p.getWealth();
				winner = p;
			}
		}

		eventMessages.showWinner(winner);
	}

	private void generateRandomNodesAndTreasures() {
		Converter mapInfo = playMap.getPictureToPointOnGraphUtility();
		// create a random generator
		Random randomGenerator = new Random();
		// get all points
		Vector<Point> pogVector = mapInfo.getVectorOfValidNodes();
		// the total number valid nodes ( 420 )
		int totalNodes = pogVector.size();

		// 'lookUp' tree map holds all treasure indices
		TreeMap<Integer, Integer> lookUp = new TreeMap<Integer, Integer>();
		// a dummy variable for TreeMap
		Integer dummy = new Integer(0);
		// we have 24 treasures
		for (int i = 0; i < 24; ++i) {
			int randIdx = randomGenerator.nextInt(totalNodes);
			// goldValue range from 100 - 200
			int randGoldValue = 100 + randomGenerator.nextInt(100);
			treasures.add(new Treasure(new Integer(i), new Integer(randIdx), new Integer(randGoldValue)));
			lookUp.put(randIdx, dummy);
		}

		// first, we set all nodes to hasTreasureMap = -1, then we choose 100
		// nodes that are not treasure nodes with HasTreasureMap = v where v =
		// 0, 1, ... 23
		// also 2/3 of the nodes charge 2 wealth to feed 4 strengths
		for (int i = 0; i < totalNodes; ++i) {
			nodes.add(new Node(new Integer(i), new Point(pogVector.elementAt(i)), new Integer(4), // foodTax
																											// =
																											// 4
			-1, // no treasure map
			-1)); // no player
		}
		// now we set 1/3 of the nodes to foodTax = free and feed 10 strengths
		int oneThird = totalNodes / 3;
		for (int i = 0; i < oneThird; ++i) {
			int randIdx = randomGenerator.nextInt(totalNodes);
			nodes.elementAt(randIdx).setFoodTax(10);
		}
		// now for 100 nodes that can view the treasure map
		// the hasTreasureMap will store the closet treasure
		int cnt = 0;
		do {
			int randIdx = randomGenerator.nextInt(totalNodes);
			// if this is not at treasure position, then set it
			if (lookUp.get(randIdx) == null) {
				cnt++;
				Integer theCloset = findClosetTreasures(randIdx, treasures);
				nodes.elementAt(randIdx).setHasTreasueMap(theCloset);
				// also mark this randIdx
				lookUp.put(randIdx, dummy);
			}
		}
		while (cnt < 100);
	}

	private Integer findClosetTreasures(Integer src, Vector<Treasure> treasures) {
		Graph<Integer> telescope = playMap.getGraph();
		telescope.dijkstra(src);
		double minCost = INFINITY;
		Integer theCloset = 0;
		for (Treasure t : treasures) {
			if (minCost > telescope.getCost(t.getPosition())) {
				minCost = telescope.getCost(t.getPosition());
				theCloset = t.getTreasureId();
			}
		}
		return theCloset;
	}

	private void initializeFourPlayers() {
		Converter mapInfo = playMap.getPictureToPointOnGraphUtility();
		players.add(new Player(0, 100, 100, mapInfo.getIntegerFromPoint(new Point(20, 20, 88)), mapInfo.getIntegerFromPoint(new Point(500, 260, 74))));
		players.add(new Player(1, 100, 100, mapInfo.getIntegerFromPoint(new Point(500, 20, 72)), mapInfo.getIntegerFromPoint(new Point(260, 500, 55))));
		players.add(new Player(2, 100, 100, mapInfo.getIntegerFromPoint(new Point(500, 500, 66)), mapInfo.getIntegerFromPoint(new Point(20, 260, 72))));
		players.add(new Player(3, 100, 100, mapInfo.getIntegerFromPoint(new Point(20, 500, 58)), mapInfo.getIntegerFromPoint(new Point(260, 20, 103))));
	}

	private void getPathForAllPlayers() {
		for (Player p : players) {
			Vector<Integer> path = getVectorPath(p.getStart(), p.getGoal());
			p.setPath(path);
		}
	}

	private void play(Vector<Turtle> turtles) {
		boolean[] markAsNonPlayer = { false, false, false, false };
		int i = 0;
		int count = 0;
		do {
			i = count % 4;
			if (players.elementAt(i).isStillPlaying() == false) {
				// if this is the first time
				if (markAsNonPlayer[i] == false) {
					eventMessages.showPlayerBecomeNonPlaying(players.elementAt(i));
					markAsNonPlayer[i] = true;
				}
				count++;
			}
			else {
				Player player = players.elementAt(i);
				Turtle turtle = turtles.elementAt(i);
				movePlayer(player, turtle);
				fightEnemyIfAny(player);
				encounterNodeHasTreasureMap(player);
				foundTreasure(player);
				// delay( 100 );
				count++;
			}
		}
		while (!isGameOver());
	}

	private void movePlayer(Player player, Turtle turtle) {
		Integer playerId = player.getPlayerId();
		Integer previousPosition = player.getCurrentPosition();
		player.move();
		Integer currentPosition = player.getCurrentPosition();
		Point pointOnPicture = DatabaseManager.selectPositionFromNodeWhereNodeId(stmt, currentPosition);
		turtle.moveTo(pointOnPicture.getX(), pointOnPicture.getY());
		// update player strength
		updatePlayerStrengthWealth(player, previousPosition, currentPosition);
		// update database
		DatabaseManager.updateCurrentPlayerFromNodeWhereNodeId(stmt, previousPosition, new Integer(-1));
		DatabaseManager.updateCurrentPositionFromPlayerWherePlayerId(stmt, playerId, currentPosition);
		// update in case meet another player
		// Database.updateCurrentPlayerFromNodeWhereNodeId( stmt,
		// currentPosition, playerId );
	}

	private void updatePlayerStrengthWealth(Player player, Integer previousPos, Integer currentPos) {
		Integer amount = getCostBetweenTwoNodes(previousPos, currentPos);
		player.setStrength(player.getStrength() - amount);
		Integer foodTax = DatabaseManager.selectFoodTaxFromNodeWhereNodeId(stmt, player.getPlayerId());
		if (foodTax.compareTo(new Integer(4)) == 0) {
			player.setWealth(player.getWealth() - 2);
			player.setStrength(player.getStrength() + foodTax);
		}
		else {
			player.setStrength(player.getStrength() + foodTax);
		}
		// update database
		Integer playerId = player.getPlayerId();
		DatabaseManager.updateStrengthFromPlayerWherePlayerId(stmt, playerId, player.getStrength());
		DatabaseManager.updateWealthFromPlayerWherePlayerId(stmt, playerId, player.getWealth());
	}

	private void fightEnemyIfAny(Player player) {
		Integer playerId = player.getPlayerId();
		Integer playerPosition = player.getCurrentPosition();
		// get the enemyId from database
		Integer enemyId = DatabaseManager.selectCurrentPlayerFromNodeWhereNodeId(stmt, playerPosition);
		// if enemyId != -1, then there must be an enemy at this position
		if (enemyId.compareTo(new Integer(-1)) != 0) {
			// get the real enemy
			Player enemy = players.elementAt(enemyId);
			if (enemy.isStillPlaying() == true) {
				if (player.getStrength().compareTo(enemy.getStrength()) > 0) {
					player.setStrength(player.getStrength() + 10);
					enemy.setStrength(enemy.getStrength() - 10);
				}
				else if (player.getStrength().compareTo(enemy.getStrength()) < 0) {
					player.setStrength(player.getStrength() - 10);
					enemy.setStrength(enemy.getStrength() + 10);
				}
				else {
					// they have equal strength, do nothing
				}
				// update local data
				players.elementAt(enemyId).set(enemy);
				// update database
				DatabaseManager.updateStrengthFromPlayerWherePlayerId(stmt, playerId, player.getStrength());
				DatabaseManager.updateStrengthFromPlayerWherePlayerId(stmt, enemyId, enemy.getStrength());
				DatabaseManager.updateCurrentPlayerFromNodeWhereNodeId(stmt, playerPosition, playerId);
				// show event message
				eventMessages.showPlayerArrivingAtNodeWithAnotherPlayer(player, enemy);
			}
		}
		// update database
		DatabaseManager.updateCurrentPlayerFromNodeWhereNodeId(stmt, playerPosition, playerId);
	}

	private void encounterNodeHasTreasureMap(Player player) {
		// get the player current position
		Integer currentPosition = player.getCurrentPosition();
		// get the mapValue of this node
		Integer mapValue = DatabaseManager.selectHasTreasureMapFromNodeWhereNodeId(stmt, currentPosition);
		// if mapValue != 0, then this is a node has treasure map
		if (mapValue.compareTo(new Integer(-1)) != 0) {
			Integer theClosetTreasure = mapValue;
			// if player has not visited this treasure
			if (player.isTreasureVisited(theClosetTreasure) == false) {
				// get the position of the closet treasure
				Integer theClosetTreasurePosition = DatabaseManager.selectPositionFromTreasureWhereTreasureId(stmt, theClosetTreasure);
				// set new path for player to this treasure
				player.setPath(getVectorPath(currentPosition, theClosetTreasurePosition));
				// set new current goal
				player.setCurrentGoal(theClosetTreasurePosition);
				// update database
				DatabaseManager.updateCurrentGoalFromPlayerWherePlayerId(stmt, player.getPlayerId(), theClosetTreasurePosition);
				// show message
				eventMessages.showPlayerDeterminingPath(player);
			}
		}
	}

	private void foundTreasure(Player player) {
		// if player has a treasure goal
		if (player.getCurrentGoal().compareTo(player.getGoal()) != 0) {
			// if this is treasure or the end of the current path
			if (player.getCurrentPosition().compareTo(player.getCurrentGoal()) == 0) {
				// get the player's current position to find the gold value
				Integer currentPosition = player.getCurrentPosition();
				// get treasure id of this position
				Integer treasureId = DatabaseManager.selectIdFromTreasureWherePosition(stmt, currentPosition);
				// get its gold value
				Integer goldValue = DatabaseManager.selectGoldValueFromTreasureWhereTreasureId(stmt, treasureId);
				// put this treasure into the set of visited treasures
				player.addTreasureId(treasureId, goldValue);
				// update wealth
				player.setWealth(player.getWealth() + goldValue);
				// change the current path back to original goal
				player.setPath(getVectorPath(currentPosition, player.getGoal()));
				// change current goal to original goal
				player.setCurrentGoal(player.getGoal());
				// update database;
				DatabaseManager.updateWealthFromPlayerWherePlayerId(stmt, player.getPlayerId(), player.getWealth());
				DatabaseManager.updateCurrentGoalFromPlayerWherePlayerId(stmt, player.getPlayerId(), player.getGoal());
				DatabaseManager.updateGoldValueFromTreasureWhereTreasureId(stmt, treasureId, 0);
				// show event message for get the treasure first time
				if (goldValue.compareTo(0) != 0) {
					eventMessages.showTreasureBeingFoundForTheFirstTime(player, goldValue);
				}
			}
		}
	}

	private Integer getCostBetweenTwoNodes(Integer src, Integer des) {
		Converter mapInfo = playMap.getPictureToPointOnGraphUtility();
		return Formula.calculateHeightDifference(mapInfo.getPointFromInteger(src), mapInfo.getPointFromInteger(des));
	}

	private Vector<Integer> getVectorPath(Integer src, Integer des) {
		Graph<Integer> graph = playMap.getGraph();
		// run Dijsktra
		graph.dijkstra(src);
		// get path as string ( e.g 3-2-1.. )
		String pathStr = graph.printPathString(des);
		// transfer to integer path ( 1, 2, 3... )
		return getVectorPathHelper(pathStr);
	}

	private Vector<Integer> getVectorPathHelper(String pathStr) {
		String[] path = pathStr.split("-");
		Vector<Integer> temp = new Vector<Integer>();
		for (int i = path.length - 1; i >= 0; --i) {
			temp.add(Integer.parseInt(path[i]));
		}
		return temp;
	}

	private boolean isGameOver() {
		for (Player p : players) {
			if (p.isStillPlaying())
				return false;
		}
		return true;
	}

	private Vector<Turtle> createTurtles(Vector<Player> players) {
		// add turtles to that map
		World world = playMap.getMapHandle();
		Vector<Turtle> characters = new Vector<Turtle>();
		Color[] colorArray = { Color.green, Color.red, Color.blue, Color.pink };
		int idx = 0;
		for (Player p : players) {
			Point start = DatabaseManager.selectPositionFromNodeWhereNodeId(stmt, p.getStart());
			Point goal = DatabaseManager.selectPositionFromNodeWhereNodeId(stmt, p.getGoal());
			Turtle t = new Turtle(start.getX(), start.getY(), world);
			t.setColor(colorArray[idx]);
			t.setWidth(12);
			t.setHeight(12);
			t.penDown();
			characters.add(t);
			SimpleGraphics.drawGoal(playMap.getPicture(), goal, colorArray[idx]);
			idx++;
		}
		return characters;
	}

	private void drawTreasuresAndNodeHasTreasureMap() {
		for (Treasure t : treasures) {
			SimpleGraphics.drawTreasureOnPictureAt(playMap.getPicture(), playMap.getPictureToPointOnGraphUtility().getPointFromInteger(t.getPosition()));
		}
		for (Node n : nodes) {
			if (n.getHasTreasureMap().compareTo(new Integer(-1)) != 0) {
				SimpleGraphics.drawNodeHasTreasureMap(playMap.getPicture(), playMap.getPictureToPointOnGraphUtility().getPointFromInteger(n.getNodeId()));
			}
		}
	}
}