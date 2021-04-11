package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.logging.*;

import game.Game;

public class ServerCore extends Thread {
	private int port;
	ServerSocket ss;
	private IChatLogger logger = null;

	public ServerCore(int port) throws IOException {
		this.port = port;
		logger = new TextChatLogger();
		logger.systemMessage("Server started...");
		this.start();
	}

	public void run() {
		try (ServerSocket ss = new ServerSocket(port)) {

			try {
				while (true) {
					try {
						//le nombre maximale est deux joueur pour chaque partie
						Game jeu = new Game();
						Game.Player player1 = jeu.new Player(ss.accept(), "player 1");
						Game.Player player2 = jeu.new Player(ss.accept(), "player 2");
						Game.setCurrentPlayer(player1);
						player1.setOpponent(player2);
						player2.setOpponent(player1);
						Game.currentPlayer = player1;

						// Afficher sur la sortie standard que le client est connecté
						logger.clientConnected(player1.getSocket().toString());
						logger.clientConnected(player2.getSocket().toString());
						
                        //créer un thread pour chaque joueur
						new Thread(new HandleClient(player1.getSocket(), logger, player1)).start();
						new Thread(new HandleClient(player2.getSocket(), logger, player2)).start();

					} catch (SocketTimeoutException ex) {

						System.out.println("nouveaux joueur:" + ex.toString());
					}
				}
			} finally {
				ss.close();
			}
		} catch (IOException e) {
			System.out.println("Could not bind port " + port);
			Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, e);
		}
	}

}
