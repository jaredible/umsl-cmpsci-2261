package net.jaredible.code.towerofhanoi;

import java.util.Stack;

public class Main implements Runnable {
	public static int NUM_TOWERS = 3;
	@SuppressWarnings("unchecked")
	public Stack<Integer>[] towers = new Stack[NUM_TOWERS];
	private int numDisks;
	private int numMoves = 0;

	public Main(int numDisks) {
		this.numDisks = numDisks;

		for (int i = 0; i < NUM_TOWERS; i++) {
			towers[i] = new Stack<Integer>();
		}
	}

	public void start() {
		new Thread(this, "Tower of Hanoi main thread").start();
	}

	public void run() {
		for (int disk = numDisks; disk > 0; disk--) {
			towers[0].push(disk);
		}
		display();

		move(numDisks, 0, 1, 2);
		System.out.println("NumMoves: " + numMoves);
	}

	public void move(int numDisks, int t0, int t1, int t2) {
		if (numDisks > 0) {
			move(numDisks - 1, t0, t2, t1);
			towers[t2].push(towers[t0].pop());
			numMoves++;
			display();
			move(numDisks - 1, t1, t0, t2);
		}
	}

	public void display() {
		System.out.println("MoveNum: " + numMoves);
		System.out.println("-----------------");

		for (int disk = numDisks - 1; disk >= 0; disk--) {
			String d0 = " ", d1 = " ", d2 = " ";

			try {
				d0 = String.valueOf(towers[0].get(disk));
			} catch (Exception e) {
			}

			try {
				d1 = String.valueOf(towers[1].get(disk));
			} catch (Exception e) {
			}

			try {
				d2 = String.valueOf(towers[2].get(disk));
			} catch (Exception e) {
			}

			System.out.println("  " + d0 + "  |  " + d1 + "  |  " + d2 + "  ");
		}

		System.out.println("-----------------");
		System.out.println("  A  |  B  |  C  ");
		System.out.println("-----------------");

		System.out.println();
	}

	public static void main(String[] args) {
		Main main = new Main(7);
		main.start();
	}
}
