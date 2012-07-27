import java.awt.Color;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import Media.Picture;
import Media.PictureFilePath;
import Character.*;
import Game.Game;
import Game.GameLogic;
import Game.Player;
import GraphUtility.Converter;

public class Program {
	public static Game loadDatabase() {
		System.out.println("Loading data from database. Please wait for a few minutes ...\n\n");
		Game game = new Game();
		return game;
	}

	public static void main(String[] args) {
		Game game = loadDatabase();
		String userResponse = new String();
		Scanner in = new Scanner(System.in);
		game.runGameInstruction();
		
		System.out.println("Do you want to start the game now? ( Y / N )");
		System.out.println(", or you can run SSH Secure Shell to see the database before running the game.");
		System.out.print(" -> ");
		
		userResponse = in.nextLine();
		if (userResponse.charAt(0) == 'y' || userResponse.charAt(0) == 'Y') {
			game.run();
			game.showWinner();
		}

		in.close();
	}
}
