package net.jaredible.code.connect4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main {
	public static String[][] createPattern() {
		String[][] pattern = new String[6 + 1][7 + 8];

		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[i].length; j++) {
				if (j % 2 == 0) {
					pattern[i][j] = "|";
				} else {
					pattern[i][j] = " ";
				}

				if (i == 6) {
					pattern[i][j] = "-";
				}
			}
		}

		return pattern;
	}

	public static void printPattern(String[][] pattern) {
		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[i].length; j++) {
				System.out.print(pattern[i][j]);
			}
			System.out.println();
		}
	}

	public static void dropPattern(String[][] pattern, String color, Scanner scanner) {
		System.out.println("Drop a " + color + " disk at column (0-6): ");

		int col = -1;

		do {
			try {
				col = scanner.nextInt();
				if (col < 0 || col > 6) {
					throw new Exception("Testing");
				} else {
					col *= 2;
					col++;
					break;
				}
			} catch (Exception e) {
				System.out.print("Invalid input!");
				scanner.nextLine();
			}
		} while (true);

		if (color == "red") {
			for (int i = 5; i >= 0; i--) {
				if (pattern[i][col] == " ") {
					pattern[i][col] = "R";
					break;
				}
			}
		} else if (color == "yellow") {
			for (int i = 5; i >= 0; i--) {
				if (pattern[i][col] == " ") {
					pattern[i][col] = "Y";
					break;
				}
			}
		}
	}

	public static String getWinner(String[][] f) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j += 2) {
				if ((f[i][j + 1] != " ") && (f[i][j + 3] != " ") && (f[i][j + 5] != " ") && (f[i][j + 7] != " ")
						&& (f[i][j + 1] == f[i][j + 3]) && (f[i][j + 3] == f[i][j + 5])
						&& (f[i][j + 5] == f[i][j + 7])) {
					return f[i][j + 1];
				}
			}
		}

		for (int i = 1; i < 15; i += 2) {
			for (int j = 0; j < 3; j++) {
				if ((f[j][i] != " ") && (f[j + 1][i] != " ") && (f[j + 2][i] != " ") && (f[j + 3][i] != " ")
						&& (f[j][i] == f[j + 1][i]) && (f[j + 1][i] == f[j + 2][i]) && (f[j + 2][i] == f[j + 3][i])) {
					return f[j][i];
				}
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 1; j < 9; j += 2) {
				if ((f[i][j] != " ") && (f[i + 1][j + 2] != " ") && (f[i + 2][j + 4] != " ") && (f[i + 3][j + 6] != " ")
						&& (f[i][j] == f[i + 1][j + 2]) && (f[i + 1][j + 2] == f[i + 2][j + 4])
						&& (f[i + 2][j + 4] == f[i + 3][j + 6])) {
					return f[i][j];
				}
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 7; j < 15; j += 2) {
				if ((f[i][j] != " ") && (f[i + 1][j - 2] != " ") && (f[i + 2][j - 4] != " ") && (f[i + 3][j - 6] != " ")
						&& (f[i][j] == f[i + 1][j - 2]) && (f[i + 1][j - 2] == f[i + 2][j - 4])
						&& (f[i + 2][j - 4] == f[i + 3][j - 6])) {
					return f[i][j];
				}
			}
		}

		return null;
	}

	public static void sendJson(String json) {
		try {
			URL myurl = new URL("http://test.jaredible.net/connect4");
			HttpURLConnection connection = (HttpURLConnection) myurl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);

			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Method", "POST");
			OutputStream os = connection.getOutputStream();
			os.write(json.toString().getBytes("UTF-8"));
			os.close();

			StringBuilder sb = new StringBuilder();
			int HttpResult = connection.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				System.out.println("" + sb.toString());

			} else {
				System.out.println(connection.getResponseCode());
				System.out.println(connection.getResponseMessage());
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {
		// sendJson(""); // TODO

		Scanner scanner = new Scanner(System.in);

		String[][] pattern = createPattern();
		boolean running = true;

		int turn = 0;
		printPattern(pattern);

		while (running) {
			dropPattern(pattern, turn++ % 2 == 0 ? "red" : "yellow", scanner);
			printPattern(pattern);

			if (getWinner(pattern) != null) {
				if (getWinner(pattern) == "R") {
					System.out.println("The red player won.");
				} else if (getWinner(pattern) == "Y") {
					System.out.println("The yellow player won.");
				}

				running = false;
			}
		}

		scanner.close();
	}
}
