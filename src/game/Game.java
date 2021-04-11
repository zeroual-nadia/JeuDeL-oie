package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Game {
	// on rempli le board en fonction des tours de nos joueurs
	private static Player[] board = { null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null };

	public static Player getCurrentPlayer() {
		return currentPlayer;
	}

	// cette fonction permets aux nouveaux joueurs de jouer sur une nouvelle carte
	// vide
	public static void finish() {
		for (int i = 0; i < 59; i++) {
			board[i] = null;
		}
	}

	// à la fin de chaque tour le joueur actuel change avec cette fonction
	public static void setCurrentPlayer(Player currentPlayer) {
		Game.currentPlayer = currentPlayer;
	}

	// current player
	public static Player currentPlayer;

	// winner
	public static boolean hasWinner() {

		return

		(currentPlayer.getScore() >= 59 || currentPlayer.getOpponent().getScore()>=59 );
	}

	// no empty squares
	public static boolean boardFilledUp() {
		if (board[59] != null)
			return false;
		return true;
	}

	// thread when player tries a move
	public synchronized static boolean legalMove(int location, Player player) {
		System.out.println("legal move");
		if (player == currentPlayer) {
			board[location] = currentPlayer;
			currentPlayer = currentPlayer.opponent;
			currentPlayer.otherPlayerMoved(location);
			System.out.println("legal move22222222");
			return true;
		}
		System.out.println("legal return fnalse");
		return false;
	}

	// on crée un thread pour chaque nouveau thread
	public class Player extends Thread {
		private String mark; // nom du joueur
		private Player opponent; // joueur adverse
		private Socket socket;
		private BufferedReader input;
		private PrintWriter output;
		private int score = 0;// le score du joueur

		// thread handler to initialize stream fields
		public Player(Socket socket, String mark) {
			this.socket = socket;
			this.mark = mark;
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				output.println("WELCOME " + mark);
				if (mark == "player 1")
					output.println("MESSAGE Waiting for opponent to connect");
				if (mark == "player 2")
					// lorsque les deux clients sont connecté le second joueur sera notifié
					output.println("MESSAGE WAIT FOR YOUR TURN");

			} catch (IOException e) {
				System.out.println("Player disconnected: " + e);
			}
		}

		// Accepts notification of who the opponent is.
		public void setOpponent(Player opponent) {
			this.opponent = opponent;
		}

		// Handles the otherPlayerMoved message.
		public void otherPlayerMoved(int location) {
			output.println("OPPONENT_MOVED " + location);
			output.println(Game.hasWinner() ? "DEFEAT" : "");
		}

		

		public String getMark() {
			return mark;
		}

		public void setMark(String mark) {
			this.mark = mark;
		}

		public BufferedReader getInput() {
			return input;
		}

		public void setInput(BufferedReader input) {
			this.input = input;
		}

		public PrintWriter getOutput() {
			return output;
		}

		public void setOutput(PrintWriter output) {
			this.output = output;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int resultat) {
			this.score = resultat + score;
			System.out.println("score: " + score);
		}

		public Player getOpponent() {
			return opponent;
		}

		public void setSocket(Socket socket) {
			this.socket = socket;
		}

		public Socket getSocket() {
			return socket;
		}
	}

}
