package Game;

import java.util.*;
import GraphUtility.Point;

public class Treasure {
	Integer treasureId;
	private Integer position;
	private Integer goldValue;

	public Treasure(Integer treasureId, Integer position, Integer goldValue) {
		this.treasureId = treasureId;
		this.position = position;
		this.goldValue = goldValue;
	}

	public Treasure(Treasure rhs) {
		this.treasureId = rhs.treasureId;
		this.position = rhs.position;
		this.goldValue = rhs.goldValue;
	}

	public Integer getTreasureId() {
		return treasureId;
	}

	public Integer getPosition() {
		return position;
	}

	public Integer getGoldValue() {
		return goldValue;
	}

	public void setGoldValue(Integer goldValue) {
		this.goldValue = goldValue;
	}

	public String toString() {
		return "TreasureId = " + treasureId + ", Position = " + position + ", Goal Value = " + goldValue;
	}
}
