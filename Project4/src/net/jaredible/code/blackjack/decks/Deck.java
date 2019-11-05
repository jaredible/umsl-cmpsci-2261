package net.jaredible.code.blackjack.decks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.jaredible.code.blackjack.Game;
import net.jaredible.code.blackjack.decks.cards.Card;

public class Deck {
	protected Random random = new Random();
	private List<Card> cards = new ArrayList<Card>();

	public Deck() {
		try {
			for (int i = 0; i < getInitialSize() / Card.RANKS.length; i++) {
				for (int j = 0; j < Card.RANKS.length; j++) {
					cards.add((Card) Class.forName("net.jaredible.code.blackjack.decks.cards.Card" + Card.RANKS[j]).newInstance());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void shuffle() {
		initAllCards();

		int len = 10000 + random.nextInt(10) * 100;
		for (int i = 0; i < len; i++) {
			Collections.shuffle(cards);
		}

		if (Game.DEBUG) {
			// TODO insert cards at specific locations
		}
	}

	private void initAllCards() {
		for (Card c : cards) {
			c.init();
		}
	}

	public Card draw() {
		return cards.remove(0);
	}

	public void addCards(List<Card> cards) {
		this.cards.addAll(cards);
	}

	public int getInitialSize() {
		return 52;
	}

	public int getSize() {
		return cards.size();
	}
}
