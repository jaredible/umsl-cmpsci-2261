package net.jaredible.code.blackjack.decks.cards;

public class CardA extends Card {
	private int currentValue;

	@Override
	public void init() {
		currentValue = 11;
	}

	@Override
	public void update() {
		currentValue = 1;
	}

	@Override
	public String getRank() {
		return "A";
	}

	@Override
	public int getValue() {
		return currentValue;
	}
}
