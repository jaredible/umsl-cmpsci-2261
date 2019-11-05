package net.jaredible.code.mtmm.tests;

import net.jaredible.code.mtmm.MTMM;

public class MM100 {
	private static final boolean PRINT = false;

	public static void main(String[] args) {
		MTMM mtmm = new MTMM(100);
		mtmm.calculate();
		if (PRINT || MMAll.PRINT) mtmm.printMatrix();
		mtmm.cleanup();
		mtmm = null;
	}
}
