package net.jaredible.code.blackjack;

public class Stats {
	private static int NUM_ROUNDS = 0;
	private static int NUM_PLAYER_WINS = 0;
	private static int NUM_DEALER_WINS = 0;
	private static int PLAYER_TOTAL_WINNINGS = 0;
	private static int PLAYER_TOTAL_LOSINGS = 0;

	public static void display() {
		Util.println("NumRounds:" + NUM_ROUNDS + " NumPlayerWins:" + NUM_PLAYER_WINS + " NumDealerWins:" + NUM_DEALER_WINS + " PlayerTotalWinnings:$" + PLAYER_TOTAL_WINNINGS + " PlayerTotalLosings:$" + PLAYER_TOTAL_LOSINGS);
	}

	public static void incrementRounds() {
		NUM_ROUNDS++;
	}

	public static void incrementPlayerWins() {
		NUM_PLAYER_WINS++;
	}

	public static void incrementDealerWins() {
		NUM_DEALER_WINS++;
	}

	public static void addPlayerTotalWinnings(int amount) {
		PLAYER_TOTAL_WINNINGS += amount;
	}

	public static void addPlayerTotalLosings(int amount) {
		PLAYER_TOTAL_LOSINGS += amount;
	}

	public static void reset() {
		NUM_ROUNDS = 0;
		NUM_PLAYER_WINS = 0;
		NUM_DEALER_WINS = 0;
		PLAYER_TOTAL_WINNINGS = 0;
		PLAYER_TOTAL_LOSINGS = 0;
	}
}
