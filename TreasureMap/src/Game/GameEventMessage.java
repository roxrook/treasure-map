package Game;

import java.util.*;
import java.sql.*;
import Game.*;
import GraphUtility.*;
import Media.PictureFilePath;

public class GameEventMessage {

	private Converter utility;

	public GameEventMessage() {
		utility = new Converter(PictureFilePath.heightMapFilePath);
	}

	public void showGameInstruction(Vector<Player> players) {
		System.out.println(" * Game Introduction : There are 4 players in this game. All players have one");
		System.out.println("	starting point and one destination, and they all start with wealth = 100, strength = 100.");
		System.out.println("   The player ends his game if either his strength goes below 0 or he gets to his destination.");
		System.out.println("   The game ends when there are no more playing players. When the game ends the winner is the ");
		System.out.println("   player at their goal with the most wealth. If no player is at their goal, the winner is the ");
		System.out.println("   player with the most wealth.");
		System.out.println("		<> Green turtle represents for player 0 at position [ " + utility.getPointFromInteger(players.elementAt(0).getStart()).getX() + ", " + utility.getPointFromInteger(players.elementAt(0).getStart()).getY() + " ]");
		System.out.println("		<> Red turtle represents for player 1 at position [ " + utility.getPointFromInteger(players.elementAt(1).getStart()).getX() + ", " + utility.getPointFromInteger(players.elementAt(1).getStart()).getY() + " ]");
		System.out.println("		<> Blue turtle represents for player 2 at position [ " + utility.getPointFromInteger(players.elementAt(2).getStart()).getX() + ", " + utility.getPointFromInteger(players.elementAt(2).getStart()).getY() + " ]");
		System.out.println("		<> Pink turtle represents for player 3 at position [ " + utility.getPointFromInteger(players.elementAt(3).getStart()).getX() + ", " + utility.getPointFromInteger(players.elementAt(3).getStart()).getY() + " ]");
		System.out.println("  + Yellow rectangle represents for treasure.");
		System.out.println("  + Red circle represents for node that has treasure map.");
	}

	public void showPlayerInfo(Player p) {
		System.out.println(p);
	}

	public void showPlayerDeterminingPath(Player p) {
		System.out.println("\nPlayer[" + p.getPlayerId() + "] has changed his path.");
		System.out.println("And the new path is: ");
		for (Integer i : p.getPath()) {
			Point point = utility.getPointFromInteger(i);
			System.out.print("( " + point.getX() + ", " + point.getY() + " ) -> ");
		}
		System.out.println(" end path.\n");
	}

	public void showPlayerBecomeNonPlaying(Player player) {
		Point playerFinalPosition = utility.getPointFromInteger(player.getCurrentPosition());
		System.out.println("\n *** Player[" + player.getPlayerId() + "] has ended his game with the following result:");
		System.out.println(" Wealth: " + player.getWealth() + ", Strength: " + player.getStrength() + " at postion : ( " + playerFinalPosition.getX() + ", " + playerFinalPosition.getY() + " )\n");
	}

	public void showPlayerArrivingAtNodeWithAnotherPlayer(Player p, Player e) {
		System.out.println("The battle of player[" + p.getPlayerId() + "] vs player[" + e.getPlayerId() + "] begin.");
		if (p.getStrength().compareTo(e.getStrength()) == 0) {
			System.out.println("This is a tie.");
		}
		if (p.getStrength().compareTo(e.getStrength()) > 0) {
			System.out.println("The winner is player[" + p.getPlayerId() + "] !");
		}
		if (p.getStrength().compareTo(e.getStrength()) < 0) {
			System.out.println("The winner is player[" + e.getPlayerId() + "] !");
		}

	}

	public void showTreasureBeingFoundForTheFirstTime(Player player, Integer goldValue) {
		System.out.println("\nCongratulation player[" + player.getPlayerId() + "] !!!");
		System.out.println("You have won " + goldValue + "$");
	}

	public void showWinner(Player player) {
		System.out.println("\n * After a long tournament, we proudly announce our winner today is : ");
		System.out.println("Player[" + player.getPlayerId() + "] is the champion of this year !!!");
	}

	public void showAllPlayersAfterGameOver(Vector<Player> players) {
		System.out.println("\n\n");
		System.out.println("  	------------------------------------------");
		System.out.println("  	--- The final result of all players is ---");
		System.out.println("  	------------------------------------------");
		for (Player p : players) {
			System.out.println("Player[" + p.getPlayerId() + "]" + ", wealth: " + p.getWealth() + ", strength: " + p.getStrength());
			System.out.println(", starting point: ( " + utility.getPointFromInteger(p.getStart()) + " )" + ", goal: ( " + utility.getPointFromInteger(p.getGoal()) + " )" + ", current position: ( " + utility.getPointFromInteger(p.getCurrentPosition()) + " ) \n");
		}
	}
}
