package Game;

import java.util.*;

import GraphUtility.*;
import Character.*;
import Media.*;
import Graph.*;

public class Player {
	private Integer playerId;
	private Integer wealth;
	private Integer strength;
	private Integer start;
	private Integer goal;
	private Integer currentPosition;
	private Integer currentGoal;
	private Vector<Integer> path;
	private TreeMap<Integer, Integer> treasureIds;
	private Integer currentMoveIndex;

	public Player(Integer playerId, Integer wealth, Integer strength, Integer currentPosition, Integer currentGoal) {
		this.playerId = playerId;
		this.wealth = wealth;
		this.strength = strength;
		this.currentPosition = currentPosition;
		this.currentGoal = currentGoal;
		this.start = currentPosition;
		this.goal = currentGoal;
		this.path = new Vector<Integer>();
		this.treasureIds = new TreeMap<Integer, Integer>();
		this.currentMoveIndex = 0;
	}

	public void set(Player rhs) {
		this.playerId = rhs.playerId;
		this.wealth = rhs.wealth;
		this.strength = rhs.strength;
		this.start = rhs.start;
		this.goal = rhs.goal;
		this.currentPosition = rhs.currentPosition;
		this.path = rhs.path;
		this.treasureIds = rhs.treasureIds;
		this.currentMoveIndex = rhs.currentMoveIndex;
	}

	public boolean isTreasureVisited(Integer treasureId) {
		if (treasureIds.isEmpty()) {
			return false;
		}
		else {
			if (treasureIds.get(treasureId) == null)
				return false;
		}
		return true;
	}

	public boolean isStillPlaying() {
		if (currentPosition.compareTo(goal) == 0 || strength.compareTo(new Integer(0)) < 0)
			return false;
		return true;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public Integer getWealth() {
		return wealth;
	}

	public Integer getStrength() {
		return strength;
	}

	public Integer getStart() {
		return start;
	}

	public Integer getGoal() {
		return goal;
	}

	public Integer getCurrentGoal() {
		return currentGoal;
	}

	public Integer getCurrentPosition() {
		return currentPosition;
	}

	public Vector<Integer> getPath() {
		return path;
	}

	public void setWealth(Integer wealth) {
		this.wealth = wealth;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public void setCurrentPosition(Integer currentPosition) {
		this.currentPosition = currentPosition;
	}

	public void setCurrentGoal(Integer currentGoal) {
		this.currentGoal = currentGoal;
	}

	public void setPath(Vector<Integer> path) {
		this.path = path;
		// whenever we have a new path, reset
		this.currentMoveIndex = 0;
	}

	public void addTreasureId(Integer treasureId, Integer goldValue) {
		treasureIds.put(treasureId, goldValue);
	}

	public void setCurrentMoveIndex(int currentMoveIndex) {
		this.currentMoveIndex = currentMoveIndex;
	}

	public Integer getCurrentMoveIndex() {
		return currentMoveIndex;
	}

	public void move() {
		// move to the next node in current path
		currentMoveIndex = currentMoveIndex + 1;
		currentPosition = path.elementAt(currentMoveIndex);
	}

	public String toString() {
		return "Player[" + playerId + "]" + ", Wealth = " + wealth + ", Strength = " + strength + ", Start = " + start + ", Goal = " + goal + ", Current Position = " + currentPosition + ", Current Goal = " + currentGoal + "\n";
	}
}
