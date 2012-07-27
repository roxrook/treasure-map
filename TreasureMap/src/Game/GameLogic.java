package Game;

import java.util.*;

public class GameLogic {
	static public void changeTurnLogicTest() {
		Vector<FakePlayer> players = new Vector<FakePlayer>();
		players.add(new FakePlayer(4));
		players.add(new FakePlayer(12));
		players.add(new FakePlayer(8));
		players.add(new FakePlayer(6));

		int count = 0;
		int i;
		do {
			i = count % 4;
			if (players.elementAt(i).isStillPlaying() == false) {
				System.out.println("Player " + i + " is done.");
				count++;
			}
			else {
				System.out.println(i);
				players.elementAt(i).play();
				count++;
			}

		}
		while (!isAllDone(players));

		if (players.elementAt(1).isStillPlaying() == false)
			System.out.println("Player " + 1 + " is done.");
	}

	private static boolean isAllDone(Vector<FakePlayer> players) {
		for (FakePlayer fp : players) {
			if (fp.isStillPlaying())
				return false;
		}
		return true;
	}
}
