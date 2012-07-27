package Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import GraphUtility.Converter;
import GraphUtility.Point;
import Media.PictureFilePath;

public class DatabaseManager {
	private static Connection conn;
	private static Statement stmt;
	private static Converter mapInfo = new Converter(PictureFilePath.heightMapFilePath);

	public static Statement connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/chandb", "root", "chan");
			stmt = conn.createStatement();
		}
		catch (Exception e) {
			System.out.println("Connection Error:  " + e);
		}
		return stmt;
	}

	/*
	 * BEGIN ->[ TREASURE ]
	 */
	public static String formatInserStringForTreasure(Treasure t) {
		StringBuilder query = new StringBuilder("INSERT INTO Treasure (TreasureId, Position, GoldValue) VALUES (");
		query.append(t.getTreasureId().toString());
		query.append(", ");
		query.append(t.getPosition().toString());
		query.append(", ");
		query.append(t.getGoldValue().toString());
		query.append(")");
		return query.toString();
	}

	public static String formatUpdateStringForTreasure(Treasure t) {
		StringBuilder query = new StringBuilder("UPDATE Treasure SET Position = ");
		query.append(t.getPosition().toString());
		query.append(", ");
		query.append("GoldValue = ");
		query.append(t.getGoldValue().toString());
		query.append(" WHERE TreasureId = ");
		query.append(t.getTreasureId().toString());
		return query.toString();
	}

	static public void insertTreasuresIntoDatabase(Statement stmt, Vector<Treasure> treasures) {
		for (Treasure t : treasures) {
			String query = formatInserStringForTreasure(t);
			int result;
			try {
				result = stmt.executeUpdate(query);
			}
			catch (Exception e) {
				System.out.println("Insert Error:  " + e);
			}
		}
	}

	static public void loadTreasuresFromDatabase(Statement stmt, Vector<Treasure> treasures) {
		String query = "SELECT * FROM Treasure";
		ResultSet res;
		// clear old data
		if (!treasures.isEmpty())
			treasures.clear();

		try {
			res = stmt.executeQuery(query);
			while (res.next()) {
				Integer id = res.getInt("TreasureId");
				Integer position = res.getInt("Position");
				int goldValue = res.getInt("GoldValue");
				treasures.add(new Treasure(id, position, goldValue));
			}
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
	}

	public static Integer selectGoldValueFromTreasureWherePosition(Statement stmt, Integer position) {
		String query = "SELECT GoldValue FROM Treasure WHERE Position = " + position;
		ResultSet res;
		Integer goldValue = 0;
		try {
			res = stmt.executeQuery(query);
			res.next();
			goldValue = res.getInt("GoldValue");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return goldValue;
	}

	public static Integer selectGoldValueFromTreasureWhereTreasureId(Statement stmt, Integer treasureId) {
		String query = "SELECT GoldValue FROM Treasure WHERE TreasureId = " + treasureId;
		ResultSet res;
		Integer goldValue = 0;
		try {
			res = stmt.executeQuery(query);
			res.next();
			goldValue = res.getInt("GoldValue");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return goldValue;
	}

	public static Integer selectPositionFromTreasureWhereTreasureId(Statement stmt, Integer treasureId) {
		String query = "SELECT Position FROM Treasure WHERE TreasureId = " + treasureId;
		ResultSet res;
		Integer position = 0;
		try {
			res = stmt.executeQuery(query);
			res.next();
			position = res.getInt("Position");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return position;
	}

	public static Integer selectIdFromTreasureWherePosition(Statement stmt, Integer position) {
		String query = "SELECT TreasureId FROM Treasure WHERE Position = " + position.toString();
		ResultSet res;
		Integer treasureId = null;
		try {
			res = stmt.executeQuery(query);
			res.next();
			treasureId = res.getInt("TreasureId");
			res.close();
		}
		catch (Exception e) {
			return treasureId;
		}
		return treasureId;
	}

	static public void updateTreasuresFromDatabase(Statement stmt, Vector<Treasure> treasures) {
		for (Treasure t : treasures) {
			String query = formatUpdateStringForTreasure(t);
			int result;
			try {
				result = stmt.executeUpdate(query);
			}
			catch (Exception e) {
				System.out.println("Update error:  " + e);
			}
		}
	}

	public static void updateGoldValueFromTreasureWhereTreasureId(Statement stmt, Integer treasureId, Integer value) {
		String query = "UPDATE Treasure SET GoldValue = " + value.toString() + " WHERE TreasureId = " + treasureId.toString();
		try {
			stmt.executeUpdate(query);
		}
		catch (Exception e) {
			System.out.println("Update Error:  " + e);
		}
	}

	/*
	 * END ---------------------------------------------->[ TREASURE ]
	 */

	/*
	 * BEGIN ->[ NODE ]
	 */
	public static String formatInserStringForNode(Node n) {
		StringBuilder query = new StringBuilder("INSERT INTO Node (NodeId, Position, FoodTax, HasTreasureMap, CurrentPlayer) VALUES (");
		query.append(n.getNodeId().toString() + ", '");
		query.append(n.getPosition().toString() + "', ");
		query.append(n.getFoodTax().toString() + ", ");
		query.append(n.getHasTreasureMap().toString() + ", ");
		query.append(n.getCurrentPlayer() + ") ");
		return query.toString();
	}

	public static String formatUpdateStringForNode(Node n) {
		StringBuilder query = new StringBuilder("UPDATE Node SET Position = '" + n.getPosition().toString());
		query.append("', FoodTax = " + n.getFoodTax().toString());
		query.append(", HasTreasureMap = " + n.getHasTreasureMap());
		query.append(", CurrentPlayer = " + n.getCurrentPlayer());
		query.append(" WHERE NodeId = " + n.getNodeId().toString());
		return query.toString();
	}

	public static void insertNodesIntoDatabase(Statement stmt, Vector<Node> nodes) {
		for (Node n : nodes) {
			String query = formatInserStringForNode(n);
			try {
				stmt.executeUpdate(query);
			}
			catch (Exception e) {
				System.out.println("Insert error:  " + e);
			}
		}
	}

	static public void loadNodesFromDatabase(Statement stmt, Vector<Node> nodes) {
		String query = "SELECT * FROM Node";
		ResultSet res;
		// clear old data
		if (!nodes.isEmpty())
			nodes.clear();

		try {
			res = stmt.executeQuery(query);
			while (res.next()) {
				Integer id = res.getInt("NodeId");
				Point position = Point.parse(res.getString("Position"));
				Integer foodTax = res.getInt("FoodTax");
				Integer hasTreasureMap = res.getInt("HasTreasureMap");
				Integer currentPlayer = res.getInt("CurrentPlayer");
				nodes.add(new Node(id, position, foodTax, hasTreasureMap, currentPlayer));
			}
			res.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateNodesFromDatabase(Statement stmt, Vector<Node> nodes) {
		for (Node n : nodes) {
			String query = formatUpdateStringForNode(n);
			try {
				stmt.executeUpdate(query);
			}
			catch (Exception e) {
				System.out.println("Update error:  " + e);
			}
		}
	}

	public static void updateCurrentPlayerFromNodeWhereNodeId(Statement stmt, Integer nodeId, Integer currentPlayer) {
		String query = "UPDATE Node SET CurrentPlayer = " + currentPlayer.toString() + " WHERE NodeId = " + nodeId.toString();
		try {
			stmt.executeUpdate(query);
		}
		catch (Exception e) {
			System.out.println("Update Error:  " + e);
		}
	}

	public static void updateCurrentPlayerFromNodeWhereNodeIdToNull(Statement stmt, Integer nodeId) {
		String query = "UPDATE Node SET CurrentPlayer = NULL WHERE NodeId = " + nodeId.toString();
		try {
			stmt.executeUpdate(query);
		}
		catch (Exception e) {
			System.out.println("Update Error:  " + e);
		}
	}

	static public Point selectPositionFromNodeWhereNodeId(Statement stmt, Integer nodeId) {
		String query = "SELECT Position FROM Node WHERE NodeId =  " + nodeId.toString();
		ResultSet res;
		Point position = null;
		try {
			res = stmt.executeQuery(query);
			res.next();
			position = Point.parse(res.getString("Position"));
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection error where NodeId = " + nodeId);
			e.printStackTrace();
		}
		return position;
	}

	public static Integer selectHasTreasureMapFromNodeWhereNodeId(Statement stmt, Integer nodeId) {
		String query = "SELECT HasTreasureMap FROM Node WHERE NodeId = " + nodeId.toString();
		ResultSet res;
		Integer hasTreasureMap = -1;
		try {
			res = stmt.executeQuery(query);
			res.next();
			hasTreasureMap = res.getInt("HasTreasureMap");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return hasTreasureMap;
	}

	public static Integer selectFoodTaxFromNodeWherePosition(Statement stmt, Point p) {
		String query = "SELECT FoodTax FROM Node WHERE Position = '" + p.toString() + "'";
		ResultSet res;
		Integer foodTax = 0;
		try {
			res = stmt.executeQuery(query);
			res.next();
			foodTax = res.getInt("FoodTax");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return foodTax;
	}

	public static Integer selectFoodTaxFromNodeWhereNodeId(Statement stmt, Integer nodeId) {
		String query = "SELECT FoodTax FROM Node WHERE NodeId = " + nodeId.toString();
		ResultSet res;
		Integer foodTax = null;
		try {
			res = stmt.executeQuery(query);
			res.next();
			foodTax = res.getInt("FoodTax");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return foodTax;
	}

	public static Integer selectCurrentPlayerFromNodeWhereNodeId(Statement stmt, Integer nodeId) {
		String query = "SELECT CurrentPlayer FROM Node WHERE NodeId = " + nodeId.toString();
		ResultSet res;
		Integer currentPlayer = null;
		try {
			res = stmt.executeQuery(query);
			res.next();
			currentPlayer = res.getInt("CurrentPlayer");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return currentPlayer;
	}

	/*
	 * END --------------------------------------------> [ NODE ]
	 */

	/*
	 * BEGIN ->[ Player ]
	 */
	public static String formatInserStringForPlayer(Player p) {
		StringBuilder query = new StringBuilder("INSERT INTO Player (PlayerId, Wealth, Strength, CurrentPosition, CurrentGoal) VALUES (");
		query.append(p.getPlayerId().toString() + ", ");
		query.append(p.getWealth().toString() + ", ");
		query.append(p.getStrength().toString() + ", ");
		query.append(p.getCurrentPosition().toString() + ", ");
		query.append(p.getCurrentGoal().toString() + ")");
		return query.toString();
	}

	public static String formatUpdateStringForPlayer(Player p) {
		StringBuilder query = new StringBuilder("UPDATE Player SET Wealth = ");
		query.append(p.getWealth().toString());
		query.append(", Strength = " + p.getStrength().toString());
		query.append(", CurrentPosition = " + p.getCurrentPosition().toString());
		query.append(", CurrentGoal = " + p.getCurrentGoal().toString());
		query.append(" WHERE PlayerId = " + p.getPlayerId());
		return query.toString();
	}

	public static void insertPlayersIntoDatabase(Statement stmt, Vector<Player> players) {
		for (Player p : players) {
			String query = formatInserStringForPlayer(p);
			int result;
			try {
				result = stmt.executeUpdate(query);
			}
			catch (Exception e) {
				System.out.println("Insert error:  " + e);
			}
		}
	}

	public static void updatePlayersFromDatabase(Statement stmt, Vector<Player> players) {
		for (Player p : players) {
			String query = formatUpdateStringForPlayer(p);
			int result;
			try {
				result = stmt.executeUpdate(query);
			}
			catch (Exception e) {
				System.out.println("Update error:  " + e);
			}
		}
	}

	static public void loadPlayersFromDatabase(Statement stmt, Vector<Player> players) {
		String query = "SELECT * FROM Player";
		ResultSet res;
		// clear old data
		if (!players.isEmpty())
			players.clear();

		try {
			res = stmt.executeQuery(query);
			while (res.next()) {
				Integer id = res.getInt("PlayerId");
				Integer wealth = res.getInt("Wealth");
				Integer strength = res.getInt("Strength");
				Integer currentPosition = res.getInt("CurrentPosition");
				Integer currentGoal = res.getInt("CurrentGoal");
				players.add(new Player(id, wealth, strength, currentPosition, currentGoal));
			}
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
	}

	public static Integer selectStrengthFromPlayerWherePlayerId(Statement stmt, Integer playerId) {
		String query = "SELECT Strength FROM Player WHERE PlayerId = " + playerId.toString();
		ResultSet res;
		Integer strength = 0;
		try {
			res = stmt.executeQuery(query);
			res.next();
			strength = res.getInt("Strength");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return strength;
	}

	public static Integer selectWealthFromPlayerWherePlayerId(Statement stmt, Integer playerId) {
		String query = "SELECT Wealth FROM Player WHERE PlayerId = " + playerId.toString();
		ResultSet res;
		Integer wealth = 0;
		try {
			res = stmt.executeQuery(query);
			res.next();
			wealth = res.getInt("Wealth");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return wealth;
	}

	public static Integer selectCurrentPositionFromPlayerWherePlayerId(Statement stmt, Integer playerId) {
		String query = "SELECT CurrentPosition FROM Player WHERE PlayerId = " + playerId.toString();
		ResultSet res;
		Integer currentPosition = null;
		try {
			res = stmt.executeQuery(query);
			if (res != null) {
				res.next();
				res.close();
				return res.getInt("CurrentPosition");
			}
			else {
				res.close();
			}
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return null;
	}

	public static Integer selectCurrentGoalFromPlayerWherePlayerId(Statement stmt, Integer playerId) {
		String query = "SELECT CurrentGoal FROM Player WHERE PlayerId = " + playerId.toString();
		ResultSet res;
		Integer treasureGoal = 0;
		try {
			res = stmt.executeQuery(query);
			res.next();
			treasureGoal = res.getInt("CurrentGoal");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return treasureGoal;
	}

	public static Integer selectPlayerIdFromPlayerWhereCurrentPosition(Statement stmt, Point p) {
		String query = "SELECT PlayerId FROM Player WHERE CurrentPosition = '" + p.toString() + "'";
		ResultSet res;
		Integer id = 0;
		try {
			res = stmt.executeQuery(query);
			res.next();
			id = res.getInt("Id");
			res.close();
		}
		catch (Exception e) {
			System.out.println("Selection Error:  " + e);
		}
		return id;
	}

	public static void updateStrengthFromPlayerWherePlayerId(Statement stmt, Integer playerId, Integer value) {
		String query = "UPDATE Player SET Strength = " + value.toString() + " WHERE PlayerId = " + playerId.toString();
		try {
			stmt.executeUpdate(query);
		}
		catch (Exception e) {
			System.out.println("Update Error:  " + e);
		}
	}

	public static void updateWealthFromPlayerWherePlayerId(Statement stmt, Integer playerId, Integer value) {
		String query = "UPDATE Player SET Wealth = " + value.toString() + " WHERE PlayerId = " + playerId.toString();
		try {
			stmt.executeUpdate(query);
		}
		catch (Exception e) {
			System.out.println("Update Error:  " + e);
		}
	}

	public static void updateCurrentPositionFromPlayerWherePlayerId(Statement stmt, Integer playerId, Integer position) {
		String query = "UPDATE Player SET CurrentPosition = " + position.toString() + " WHERE PlayerId = " + playerId.toString();
		try {
			stmt.executeUpdate(query);
		}
		catch (Exception e) {
			System.out.println("Update Error:  " + e);
		}
	}

	public static void updateCurrentGoalFromPlayerWherePlayerId(Statement stmt, Integer playerId, Integer goalPosition) {
		String query = "UPDATE Player SET CurrentGoal = " + goalPosition.toString() + " WHERE PlayerId = " + playerId.toString();
		try {
			stmt.executeUpdate(query);
		}
		catch (Exception e) {
			System.out.println("Update Error:  " + e);
		}
	}
	/*
	 * END ---------------------------------------------->[ Player ]
	 */
}