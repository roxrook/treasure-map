package Game;

import GraphUtility.*;

public class Node {
	private Integer nodeId;
	private Point position;
	private Integer foodTax;
	private Integer hasTreasureMap;
	private Integer currentPlayer;

	public Node(Integer nodeId, Point position, Integer foodTax, Integer hasTreasureMap, Integer currentPlayer) {
		this.nodeId = nodeId;
		this.position = position;
		this.foodTax = foodTax;
		this.hasTreasureMap = hasTreasureMap;
		this.currentPlayer = currentPlayer;
	}

	public Integer getNodeId() {
		return nodeId;
	}

	public Point getPosition() {
		return position;
	}

	public Integer getFoodTax() {
		return foodTax;
	}

	public Integer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setFoodTax(Integer foodTax) {
		this.foodTax = foodTax;
	}

	public Integer getHasTreasureMap() {
		return hasTreasureMap;
	}

	public void setHasTreasueMap(Integer hasTreasureMap) {
		this.hasTreasureMap = hasTreasureMap;
	}

	public void setCurrentPlayer(Integer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public String toString() {
		return "NodeId = " + nodeId + ", Position = " + position + ", FoodTax = " + foodTax + ", HasTreasureMap = " + hasTreasureMap + "\n";
	}
}
