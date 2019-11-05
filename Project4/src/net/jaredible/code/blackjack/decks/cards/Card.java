package net.jaredible.code.blackjack.decks.cards;

public abstract class Card {
	public static final String[] RANKS = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

	protected boolean hidden = false;

	public void init() {
	}

	public void update() {
	}

	public abstract String getRank();

	public abstract int getValue();

	public void hide() {
		hidden = true;
	}

	public void unhide() {
		hidden = false;
	}

	public boolean isHidden() {
		return hidden;
	}

	public String getDisplayString() {
		StringBuffer result = new StringBuffer();

		if (hidden) {
			result.append("-:-");
		} else {
			result.append(getRank() + ":" + getValue());
		}

		return result.toString();
	}
}
