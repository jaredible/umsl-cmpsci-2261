package net.jaredible.code.blackjack.persons;

import java.util.ArrayList;
import java.util.List;

import net.jaredible.code.blackjack.Table;
import net.jaredible.code.blackjack.Util;
import net.jaredible.code.blackjack.decks.cards.Card;
import net.jaredible.code.blackjack.persons.hands.Hand;

public abstract class Person {
	protected Table table;
	protected List<Hand> hands = new ArrayList<Hand>();
	protected Hand currentHand;
	protected boolean turnOver;

	public Person(Table table) {
		this.table = table;
	}

	public void onRoundBegin() {
		turnOver = false;
		addHand();
	}

	public void onRoundEnd() {
		hands.clear();
		currentHand = null;
	}

	public void doTurn() {
		turnOver = true;
	}

	protected void addHand() {
		hands.add(new Hand(this, getNumHands() + 1));
		currentHand = hands.get(0);
	}

	public void changeHand(boolean hit) {
		int index = hands.indexOf(currentHand) + 1;

		if (getNumHands() == 2) {
			if (currentHand.isBust()) {
				if (currentHand.getHandNum() == 1) {
					Util.println("Your first hand went bust with a sum of " + currentHand.getSum() + "!");
				} else if (currentHand.getHandNum() == 2) {
					Util.println("Your second hand went bust with a sum of " + currentHand.getSum() + "!");
				}

				currentHand.discardCards();
				hands.remove(currentHand);
			}

			if (hit) {
				return;
			}

			if (index >= hands.size() - 1) {
				currentHand = hands.get(hands.size() - 1);
			} else {
				currentHand = hands.get(index);
			}
		}
	}

	protected void nextHand() {
		int index = hands.indexOf(currentHand);
		if (getNumHands() > index + 1) {
			currentHand = hands.get(index + 1);
		}
	}

	public void discardAllCards() {
		for (Hand h : hands) {
			h.discardCards();
		}
	}

	public Card draw() {
		Card card = table.draw();
		currentHand.addCard(card);
		return card;
	}

	public void displayTable() {
		Util.println("");
		Util.println("------" + getName() + "'s Turn------");
		table.display();
		Util.println("");
	}

	public void display() {
		Util.println(getName());
		displayHands();
	}

	private void displayHands() {
		boolean isDealer = this instanceof Dealer;
		for (Hand h : hands) {
			if (!isDealer) {
				if (h.equals(currentHand) && !turnOver) {
					if (getNumHands() > 1) {
						Util.println(" => Hand " + h.getHandNum());
					} else {
						Util.println("Hand " + h.getHandNum(), 4);
					}
				} else {
					Util.println("Hand " + h.getHandNum(), 4);
				}
			}
			h.displayHand();
		}
	}

	protected boolean hasBlackjack() {
		for (Hand h : hands) {
			if (h.hasBlackjack()) {
				return true;
			}
		}
		return false;
	}

	protected void onBlackjack() {
		Util.println("!!!BLACKJACK!!!");
	}

	public int getSum() {
		return currentHand.getSum();
	}

	public int getBestSum() {
		int max = 0;
		for (Hand h : hands) {
			if (h.getSum() > max) {
				max = h.getSum();
			}
		}
		return max;
	}

	public boolean isBust() {
		return currentHand.isBust();
	}

	public Table getTable() {
		return table;
	}

	public int getNumHands() {
		return hands.size();
	}

	public List<Hand> getHands() {
		return hands;
	}

	public Hand getCurrentHand() {
		return currentHand;
	}

	public int getCurrentHandIndex() {
		return hands.indexOf(currentHand);
	}

	public boolean isTurnOver() {
		return turnOver;
	}

	public Hand getLastHand() {
		return hands.get(getNumHands() - 1);
	}

	public abstract String getName();
}
