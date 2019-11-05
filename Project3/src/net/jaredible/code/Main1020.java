package net.jaredible.code;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Main1020 {
	public static final boolean DEBUG = false;

	public static BigInteger factorial(int n) {
		BigInteger result = BigInteger.ONE;

		for (int i = 1; i <= n; i++) {
			result = result.multiply(BigInteger.valueOf(i));
		}

		return result;
	}

	public static void main(String[] args) {
		BigDecimal e, f, one;

		int i, j;
		for (i = 100; i <= 1000; i += 100) {
			e = new BigDecimal(0.0);

			for (j = 0; j < i; j++) {
				one = new BigDecimal(1.0);
				f = new BigDecimal(factorial(j));

				if (DEBUG) {
					System.out.print("(" + one + "/" + j + "!)");
					if (j < i - 1) {
						System.out.print(" + ");
					}
				}

				f = one.divide(f, 25, BigDecimal.ROUND_UP);
				e = e.add(f);

				if (DEBUG) {
					if (j == i - 1) {
						System.out.print(" = " + e);
					}
				}
			}
			
			if (DEBUG) {
				System.out.println();
			}

			System.out.println("i = " + i + (i < 1000 ? " " : "") + "   e = " + e);
		}
	}
}
