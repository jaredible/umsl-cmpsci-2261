package net.jaredible.code;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class Main1019 {
	public static final boolean TRY_FAST = true;

	public static final BigInteger BIG_TWO = BigInteger.valueOf(2);

	public static boolean isPrime(BigInteger bn) {
		int c = 5;
		if (TRY_FAST) {
			return bn.isProbablePrime(c);
		} else {
			if (!bn.isProbablePrime(c)) {
				return false;
			}

			// check if even
			if (!BIG_TWO.equals(bn) && BigInteger.ZERO.equals(bn.mod(BIG_TWO))) {
				return false;
			}

			// find divisor if any from 3 to bn
			for (BigInteger bi = BigInteger.valueOf(3); bi.multiply(bi).compareTo(bn) < 1; bi = bi.add(BIG_TWO)) {
				if (BigInteger.ZERO.equals(bn.mod(bi))) { // check if bi is divisor of bn
					return false;
				}
			}

			// booyah! PRIME
			return true;
		}
	}

	// TODO: predict execution times
	public static void main(String[] args) {
		System.out.println("   p     2^p - 1");
		System.out.println("--------------------------------------------------------");
		for (int p = 2; p <= 100; p++) {
			BigInteger val = BIG_TWO.pow(p).subtract(BigInteger.ONE);
			if (isPrime(val)) {
				System.out.println("   " + p + (p < 10 ? " " : "") + "     "
						+ NumberFormat.getNumberInstance(Locale.US).format(val));
			}
		}
	}
}
