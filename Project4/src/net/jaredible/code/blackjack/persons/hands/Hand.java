package net.jaredible.code.blackjack.persons.hands;

import java.util.ArrayList;
import java.util.List;

import net.jaredible.code.blackjack.Util;
import net.jaredible.code.blackjack.decks.cards.Card;
import net.jaredible.code.blackjack.decks.cards.CardA;
import net.jaredible.code.blackjack.persons.Dealer;
import net.jaredible.code.blackjack.persons.Person;

public class Hand {
	private Person person;
	private int handNum;
	private List<Card> cards = new ArrayList<Card>();
	private int sum = 0;
	private boolean bust = false;

	public Hand(Person person, int handNum) {
		this.person = person;
		this.handNum = handNum;
	}

	public void discardCards() {
		person.getTable().discardCards(cards);
		cards.clear();
	}

	public void displayHand() {
		String sums = getSumDisplayString();
		String cards = getCardsDisplayString();
		Util.println(sums + " " + cards, person instanceof Dealer ? 4 : 8);
	}

	private String getSumDisplayString() {
		StringBuffer sums = new StringBuffer();

		sums.append("Sum:");
		if (isAtLeastOneCardHidden()) {
			sums.append("-");
		} else {
			sums.append(sum);
		}

		return sums.toString();
	}

	private String getCardsDisplayString() {
		StringBuffer cards = new StringBuffer();

		for (Card c : this.cards) {
			cards.append(c.getDisplayString() + " ");
		}

		return cards.toString();
	}

	public void unhideAllCards() {
		for (Card c : cards) {
			c.unhide();
		}
	}

	public void addCard(Card card) {
		cards.add(card);

		if (person instanceof Dealer && getNumCards() == 2) {
			card.hide();
		}

		calculateBestSum();
	}

	public Card removeCard() {
		Card removedCard = cards.remove(cards.size() - 1);
		calculateBestSum();
		return removedCard;
	}

	private void calculateBestSum() {
		List<Card> aces = new ArrayList<Card>();

		for (Card c : cards) {
			c.init();
			if (c instanceof CardA) {
				aces.add(c);
			}
		}

		while (true) {
			sum = 0;

			for (Card c : cards) {
				sum += c.getValue();
			}

			if (sum > 21 && !aces.isEmpty()) {
				aces.remove(0).update();
			} else {
				break;
			}
		}

		bust = sum > 21;
	}

	public int getSum() {
		return sum;
	}

	public boolean isBust() {
		return bust;
	}

	public int getNumCards() {
		return cards.size();
	}

	public List<Card> getCards() {
		return cards;
	}

	public int getCardValueAtIndex(int handIndex) {
		return cards.get(handIndex).getValue();
	}

	public boolean containsAce() {
		for (Card c : cards) {
			if (c instanceof CardA) {
				return true;
			}
		}
		return false;
	}

	public boolean isAtLeastOneCardHidden() {
		for (Card c : cards) {
			if (c.isHidden()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasBlackjack() {
		return sum == 21 && getNumCards() == 2;
	}

	public boolean isFirstCardAce() {
		return cards.get(0) instanceof CardA;
	}

	public Card getFirstCard() {
		return cards.size() < 1 ? null : cards.get(0);
	}

	public Card getSecondCard() {
		return cards.size() < 2 ? null : cards.get(1);
	}

	public int getHandNum() {
		return handNum;
	}
}
