package net.jaredible.code.blackjack.decks;

public class BlackjackDeck extends Deck {
	public static final int NUM_DECKS = 6;
	public static int MIN_SIZE_REQUIRED_TO_RESHUFFLE;

	public BlackjackDeck() {
		reset();
	}

	public void reset() {
		MIN_SIZE_REQUIRED_TO_RESHUFFLE = random.nextInt(10 + 1) + 40;
	}

	public int getInitialSize() {
		return super.getInitialSize() * NUM_DECKS;
	}
}
