package net.jaredible.code.blackjack.decks.cards;

public class CardQ extends Card {
	@Override
	public String getRank() {
		return "Q";
	}

	@Override
	public int getValue() {
		return 10;
	}
}
