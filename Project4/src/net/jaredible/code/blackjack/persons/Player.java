package net.jaredible.code.blackjack.persons;

import java.util.Scanner;

import net.jaredible.code.blackjack.Game;
import net.jaredible.code.blackjack.Stats;
import net.jaredible.code.blackjack.Table;
import net.jaredible.code.blackjack.Util;
import net.jaredible.code.blackjack.decks.cards.CardA;
import net.jaredible.code.blackjack.persons.hands.Hand;

// TODO Splitting should be scalable!
public class Player extends Person {
	public static final int STAND = 0;
	public static final int HIT = 1;
	public static final int DOUBLE_DOWN = 2;
	public static final int SPLIT = 3;
	public static final int INSURE = 1;

	private int balance = 1000;
	private boolean insured;
	private boolean firstTurn;

	public Player(Table table) {
		super(table);
	}

	public void onWin() {
		int winnings = table.getTotalBet();

		if (hasBlackjack()) {
			winnings += winnings * 3 / 2;
		} else {
			winnings += winnings;
		}

		balance += winnings;

		Util.println("You won $" + winnings + "! | Balance: $" + balance);
		Stats.incrementPlayerWins();
		Stats.addPlayerTotalWinnings(winnings);
	}

	public void onLose() {
		int losings = table.getTotalBet();

		if (table.getDealer().hasBlackjack()) {
			int n = losings / 2;
			losings += n;

			if (balance - losings < Table.MIN_BET) {
				losings = table.getInitialBet();
			}

			if (insured && !isBust()) {
				int insurance = table.getInitialBet() / 2;
				n -= insurance;
				losings -= insurance;
				balance += insurance;
				Stats.addPlayerTotalWinnings(insurance);
				Util.println("You were insured $" + insurance + ".");
			}

			if (balance - n < Table.MIN_BET) {
				balance = 0;
			} else {
				balance -= n;
			}
		}

		Util.println("You lost $" + losings + (isBroke() ? " and you're broke" : "") + "! | Balance: $" + balance);
		Stats.incrementDealerWins();
		Stats.addPlayerTotalLosings(losings);
	}

	public void onPush() {
		balance += table.getTotalBet();
		Util.println("Push! | Balance: $" + balance);
	}

	public void onRoundBegin() {
		super.onRoundBegin();
		insured = false;
		firstTurn = true;
	}

	public void onRoundEnd() {
		super.onRoundEnd();
	}

	public void doTurn() {
		displayTable();

		if (hasBlackjack()) {
			onBlackjack();
		} else {
			if (canInsure()) {
				if (insure() == INSURE) {
					onInsure();
				}
			}

			while (true) {
				Hand previousHand = currentHand;
				int decision = onDecision(ask());

				if (decision != HIT) {
					displayTable();
				}

				if (hasBlackjack()) {
					onBlackjack();
					break;
				} else if (isBust() || ((decision == STAND || decision == DOUBLE_DOWN) && previousHand == getLastHand()) || getSum() == 21) {
					break;
				}

				firstTurn = false;
			}
		}

		super.doTurn();
	}

	public int onInitialBet() {
		Scanner sc = table.getGame().getScanner();
		int bet;
		do {
			Util.print("Balance: $" + balance + " | How much would you like to bet? ($" + Table.MIN_BET + " to $" + Table.MAX_BET + ") | Bet: $");

			while (!sc.hasNextInt()) {
				Util.printlnerr("That's not a number!");
				sc.next();
			}

			bet = sc.nextInt();

			if (bet < Table.MIN_BET) {
				Util.printlnerr("Minimum bet is $" + Table.MIN_BET + "!");
			} else if (bet > Table.MAX_BET) {
				Util.printlnerr("Maximum bet is $" + Table.MAX_BET + "!");
			} else if (bet - balance > 0) {
				Util.printlnerr("You don't have enough!");
			}
		} while (bet < Table.MIN_BET || bet > Table.MAX_BET || bet - balance > 0);

		return bet;
	}

	public void bet(int betAmount) {
		balance -= betAmount;
		table.bet(betAmount);
		Util.println("You have bet $" + table.getTotalBet() + ".");
	}

	private int ask() {
		Scanner sc = table.getGame().getScanner();
		int decision;
		StringBuffer options = new StringBuffer();

		if (canStand()) {
			options.append("Stand=" + STAND);
			options.append(" ");
		}
		if (canHit()) {
			options.append("Hit=" + HIT);
			options.append(" ");
		}
		if (canDoubleDown()) {
			options.append("Doubledown=" + DOUBLE_DOWN);
			options.append(" ");
		}
		if (canSplit()) {
			options.append("Split=" + SPLIT);
			options.append(" ");
		}

		Util.print(options.toString() + "| Decision: ");

		boolean retry = false;
		do {
			while (!sc.hasNextInt()) {
				Util.printlnerr("That's not a number!");
				sc.next();
			}

			decision = sc.nextInt();

			if (decision == STAND && !canStand()) {
				Util.printlnerr("Cannot stand!");
				retry = true;
			} else if (decision == HIT && !canHit()) {
				Util.printlnerr("Cannot hit!");
				retry = true;
			} else if (decision == DOUBLE_DOWN && !canDoubleDown()) {
				Util.printlnerr("Cannot doubledown!");
				retry = true;
			} else if (decision == SPLIT && !canSplit()) {
				Util.printlnerr("Cannot split!");
				retry = true;
			} else if (decision < 0) {
				Util.printlnerr("Minimum value is 0!");
			} else if (decision > 3) {
				Util.printlnerr("Maximum value is 3!");
			} else {
				retry = false;
			}
		} while (retry || decision < 0 || decision > 3);

		return decision;
	}

	private int onDecision(int decision) {
		switch (decision) {
		case STAND:
			if (canStand()) {
				onStand();
			}
			break;
		case HIT:
			if (canHit()) {
				onHit();
			}
			break;
		case DOUBLE_DOWN:
			if (canDoubleDown()) {
				onDoubleDown();
			}
			break;
		case SPLIT:
			if (canSplit()) {
				onSplit();
			}
			break;
		default:
			break;
		}

		return decision;
	}

	private boolean canStand() {
		return true;
	}

	private void onStand() {
		changeHand(false);
		nextHand();
	}

	private boolean canHit() {
		return getSum() < 21;
	}

	private void onHit() {
		draw();
		changeHand(true);
		displayTable();
	}

	private boolean canDoubleDown() {
		return currentHand.getNumCards() == 2 && getSum() >= 9 && getSum() <= 11 && !((table.getInitialBet() - balance) > 0) || Game.DEBUG;
	}

	private void onDoubleDown() {
		bet(table.getInitialBet());
		draw();
		changeHand(false);
	}

	private boolean canSplit() {
		return firstTurn && currentHand.getNumCards() == 2 && (currentHand.getFirstCard().getRank().equals(currentHand.getSecondCard().getRank()) || currentHand.getFirstCard().getValue() == currentHand.getSecondCard().getValue()) && getNumHands() < 2 || Game.DEBUG;
	}

	private void onSplit() {
		addHand();
		getLastHand().addCard(currentHand.removeCard());
		for (Hand h : hands) {
			h.addCard(table.draw());
		}
	}

	private boolean canInsure() {
		Hand dealerHand = table.getDealer().getCurrentHand();
		return dealerHand.getFirstCard() instanceof CardA && dealerHand.getNumCards() == 2 || Game.DEBUG;
	}

	private void onInsure() {
		insured = true;
	}

	private int insure() {
		Util.print("Insurance? | Decline=0 Accept=1 | Decision: ");

		Scanner sc = table.getGame().getScanner();
		int decision;
		do {
			while (!sc.hasNextInt()) {
				System.err.println("That's not a number!");
				sc.next();
			}

			decision = sc.nextInt();

			if (decision < 0 || decision > 1) {
				Util.printlnerr("Please choose a valid option!");
			}
		} while (decision < 0 || decision > 1);

		return decision;
	}

	public int getBalance() {
		return balance;
	}

	public boolean isInsured() {
		return insured;
	}

	public boolean isBroke() {
		return balance == 0;
	}

	public String getName() {
		return "Player";
	}
}
