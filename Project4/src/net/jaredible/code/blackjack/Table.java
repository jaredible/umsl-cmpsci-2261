package net.jaredible.code.blackjack;

import java.util.ArrayList;
import java.util.List;

import net.jaredible.code.blackjack.decks.BlackjackDeck;
import net.jaredible.code.blackjack.decks.cards.Card;
import net.jaredible.code.blackjack.persons.Dealer;
import net.jaredible.code.blackjack.persons.Player;

public class Table {
	public static final int MIN_BET = 1;
	public static final int MAX_BET = 10000;

	private Game game;
	private BlackjackDeck shoe = new BlackjackDeck();
	private List<Card> discard = new ArrayList<Card>();
	private Dealer dealer = new Dealer(this);
	private Player player = new Player(this);
	private int initialBet;
	private int totalBet;

	public Table(Game game) {
		this.game = game;

		shoe.shuffle();
	}

	public void beginRound() {
		initialBet = 0;
		totalBet = 0;
		player.onRoundBegin();
		dealer.onRoundBegin();
	}

	public void doRound() {
		if (onInitialBet()) {
			dealCards();
			doTurns();
		} else {
			Util.print("Sorry, you're broke!");
			game.stopGame();
		}
	}

	public void endRound() {
		determineWinnerLoser();
		player.onRoundEnd();
		dealer.onRoundEnd();
		reshuffle();
		Stats.incrementRounds();
	}

	private boolean onInitialBet() {
		int bet = player.onInitialBet();

		if (player.getBalance() - bet < 0) {
			return false;
		} else {
			initialBet += bet;
			player.bet(bet);
			return true;
		}
	}

	public void bet(int betAmount) {
		totalBet += betAmount;
	}

	private void dealCards() {
		while (player.getCurrentHand().getNumCards() < 2 && dealer.getCurrentHand().getNumCards() < 2) {
			player.draw();
			dealer.draw();
		}
	}

	private void doTurns() {
		player.doTurn();
		dealer.doTurn();
	}

	private void determineWinnerLoser() {
		int playerBestSum = player.getBestSum();
		int dealerBestSum = dealer.getBestSum();
		if (playerBestSum > dealerBestSum) { // TODO currently goes off of currentHand. Needs multihand checking!
			if (player.isBust()) {
				player.onLose();
			} else {
				player.onWin();
			}
		} else if (playerBestSum < dealerBestSum) {
			if (dealer.isBust()) {
				player.onWin();
			} else {
				player.onLose();
			}
		} else {
			player.onPush();
		}
	}

	private void reshuffle() {
		if (shoe.getSize() < BlackjackDeck.MIN_SIZE_REQUIRED_TO_RESHUFFLE) {
			shoe.addCards(discard);
			discard.clear();
			shoe.shuffle();
			shoe.reset();
		}
	}

	public Card draw() {
		Card card = shoe.draw();
		discard.add(card);
		return card;
	}

	public void discardCards(List<Card> cards) {
		discard.addAll(cards);
	}

	public void display() {
		Util.println("----------Table----------");
		dealer.display();
		player.display();
		Util.println("-------------------------");
	}

	public int getInitialBet() {
		return initialBet;
	}

	public int getTotalBet() {
		return totalBet;
	}

	public Dealer getDealer() {
		return dealer;
	}

	public Player getPlayer() {
		return player;
	}

	public BlackjackDeck getShoe() {
		return shoe;
	}

	public Game getGame() {
		return game;
	}
}
