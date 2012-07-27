package Game;

public class FakePlayer {
	private int value;
	private int bound;

	public FakePlayer(int bound) {
		this.value = 0;
		this.bound = bound;
	}

	public void play() {
		value++;
	}

	public boolean isStillPlaying() {
		if (value < bound)
			return true;
		return false;
	}
}
