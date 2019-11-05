package net.jaredible.code.blackjack.persons;

import net.jaredible.code.blackjack.Table;

public class Dealer extends Person {
	public Dealer(Table table) {
		super(table);
	}

	public void doTurn() {
		currentHand.unhideAllCards();

		Player player = table.getPlayer();
		if (!player.isBust()) {
			if (player.hasBlackjack()) {
				if (hasBlackjack()) {
				} else {
					// do stuff
				}
			} else if (hasBlackjack()) {
				onBlackjack();
			} else {
				int sum = getSum();
				while (sum < 17) {
					draw();
					sum = getSum();
					if (isBust()) {
						break;
					}
				}
			}
		}

		displayTable();

		super.doTurn();
	}

	public String getName() {
		return "Dealer";
	}
}
