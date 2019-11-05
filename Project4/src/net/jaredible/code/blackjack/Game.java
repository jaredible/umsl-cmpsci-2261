package net.jaredible.code.blackjack;

import java.util.Scanner;

import net.jaredible.code.blackjack.persons.Player;

public class Game implements Runnable {
	public static final boolean DEBUG = false;

	private boolean running = false;
	private Table table;
	private Scanner theScanner = new Scanner(System.in);
	private Player player;

	private void init() {
		table = new Table(this);
		player = table.getPlayer();
	}

	public void startGame() {
		running = true;
		new Thread(this, "Blackjack main thread").start();
	}

	public void stopGame() {
		running = false;
		theScanner.close();
		Stats.display();
	}

	public void run() {
		init();

		Util.println("WELCOME TO BLACKJACK");

		do {
			runRound();

			if (player.getBalance() < Table.MIN_BET) {
				Util.println("GAME OVER");
				stopGame();
				break;
			}

			int decision = onDecision();
			if (decision == 0) {
				stopGame();
			}
		} while (running);
	}

	private int onDecision() {
		Util.print("Quit=0 PlayAgain=1 | Decision: ");

		int decision;
		do {
			while (!theScanner.hasNextInt()) {
				System.err.println("That's not a number!");
				theScanner.next();
			}

			decision = theScanner.nextInt();

			if (decision < 0 || decision > 1) {
				Util.printlnerr("Please choose a valid option!");
			}
		} while (decision < 0 || decision > 1);

		return decision;
	}

	public void runRound() {
		table.beginRound();
		table.doRound();
		table.endRound();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.startGame();
	}

	public Scanner getScanner() {
		return theScanner;
	}
}
