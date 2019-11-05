package net.jaredible.code.mtmm.tests;

import net.jaredible.code.mtmm.MTMM;

public class MM1000 {
	private static final boolean PRINT = false;

	public static void main(String[] args) {
		MTMM mtmm = new MTMM(1000);
		mtmm.calculate();
		if (PRINT || MMAll.PRINT) mtmm.printMatrix();
		mtmm.cleanup();
		mtmm = null;
	}
}
