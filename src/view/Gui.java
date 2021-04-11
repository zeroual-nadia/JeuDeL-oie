package view;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.Image;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Model.Coodinate;
import game.Game;

/**
 * @author portable
 *
 */
public class Gui {

	// setBackground(Color.black);
	String joueur = "";
	private int width = 550;
	private int height = 550;
	private static final int tailleCellule = 40;
	private MyPannel map;
	private static ArrayList<Cellule> cellule;
	// MyPanel boardPanel=new MyPanel();
	private static JFrame frame = new JFrame();
	private JLabel messageLabel = new JLabel("");
	private static int PORT = 1234;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private JLabel monLabel;
	private ImageIcon monIcone;
	Coodinate cordActuel;
	Coodinate cordAdversaire;
	// pour avoir la position du joueur dans cette Carte
	private int position = 0;
	// les coordonn�e du joueur adversaire
	int adversaire = 0;
	// Constructs the client by connecting to a server, laying out the GUI and
	// registering GUI listeners.
	int positionnerMap = 0;
	private JTextField textField;
	private JLabel label;

	public Gui(String serverAddress) throws Exception {
		// pour recuperer le message du jouer
		
		cordActuel = new Coodinate(0, 0);
		cordAdversaire = new Coodinate(0, 0);
		monLabel = new JLabel();
		monIcone = new ImageIcon(this.getClass().getResource("images.jpg"));
		monLabel.setIcon(monIcone);
		monLabel.setBounds(0, 0, 600, 600);
		frame.getContentPane().add(monLabel);
		cellule = new ArrayList<Cellule>();
		dessinCellule();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		this.map = new MyPannel(this) {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				for (Cellule c : cellule) {

					g.setColor(c.getType().getCouleur());
					g.fillRect(c.getPos().getPosX() * tailleCellule, c.getPos().getPosY() * tailleCellule,
							tailleCellule, tailleCellule);

				}
				//int�gration des cellules sp�ciales pour mon joueur 
				for (int i = 0; i < cellule.size(); i++) {
					if (cellule.get(i).getType() == TypeCellule.Pont) {
						// g.setColor(cellule.get(i).getType().getCouleur());
						Image img = new ImageIcon(this.getClass().getResource("pont.jpg")).getImage();
						g.drawImage(img, cellule.get(i).getPos().getPosX() * tailleCellule,
								cellule.get(i).getPos().getPosY() * 40, 40, 40, null);
					} else if (cellule.get(i).getType() == TypeCellule.Hotel) {
						Image img = new ImageIcon(this.getClass().getResource("puit.jpg")).getImage();
						g.drawImage(img, cellule.get(i).getPos().getPosX() * tailleCellule,
								cellule.get(i).getPos().getPosY() * 40, 40, 40, null);
					} else if (cellule.get(i).getType() == TypeCellule.Mort) {
						Image img = new ImageIcon(this.getClass().getResource("mort.gif")).getImage();
						g.drawImage(img, cellule.get(i).getPos().getPosX() * tailleCellule,
								cellule.get(i).getPos().getPosY() * 40, 40, 40, null);
					} else if (cellule.get(i).getType() == TypeCellule.Retourne) {
						Image img = new ImageIcon(this.getClass().getResource("retourne.jpg")).getImage();
						g.drawImage(img, cellule.get(i).getPos().getPosX() * tailleCellule,
								cellule.get(i).getPos().getPosY() * 40, 40, 40, null);
					} else if (cellule.get(i).getType() == TypeCellule.Gagne) {
						Image img = new ImageIcon(this.getClass().getResource("gagne.jpg")).getImage();
						g.drawImage(img, cellule.get(i).getPos().getPosX() * tailleCellule,
								cellule.get(i).getPos().getPosY() * 40, 40, 40, null);
					} else if (cellule.get(i).getType() == TypeCellule.Puits) {
						Image img = new ImageIcon(this.getClass().getResource("puit.jpg")).getImage();
						g.drawImage(img, cellule.get(i).getPos().getPosX() * tailleCellule,
								cellule.get(i).getPos().getPosY() * 40, 40, 40, null);
					} else if (i % 2 == 0) {
						g.setColor(Color.orange);
						g.fillRect(cellule.get(i).getPos().getPosX() * tailleCellule,
								cellule.get(i).getPos().getPosY() * tailleCellule, tailleCellule, tailleCellule);

					}
				}
				if (joueur == "player1") {
				
					Image img = new ImageIcon(this.getClass().getResource("oie.gif")).getImage();
					// g.drawImage(img,10, 10, null);
					g.drawImage(img, cordActuel.getPosX() * 40, cordActuel.getPosY() * 40, 40, 40, null);
					Image img2 = new ImageIcon(this.getClass().getResource("image.gif")).getImage();

					// g.drawImage(img,10, 10, null);
					g.drawImage(img2, cordAdversaire.getPosX() * 40, cordAdversaire.getPosY() * 40, 40, 40, null);
					repaint();

				} else {
					Image img = new ImageIcon(this.getClass().getResource("image.gif")).getImage();
					// g.drawImage(img,10, 10, null);
					g.drawImage(img, cordActuel.getPosX() * 40, cordActuel.getPosY() * 40, 40, 40, null);
					Image img2 = new ImageIcon(this.getClass().getResource("oie.gif")).getImage();

					// g.drawImage(img,10, 10, null);
					g.drawImage(img2, cordAdversaire.getPosX() * 40, cordAdversaire.getPosY() * 40, 40, 40, null);
					repaint();
				}
			}
		};
		textField = new JTextField();

		map.add(textField);

		label = new JLabel("Rien pour le moment");
		Image img2 = new ImageIcon(this.getClass().getResource("images.jpg")).getImage();
		map.add(label);
		// Image img2 = new
		
		map.setBackground(Color.cyan);
		frame.getContentPane().add(map);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		// Setup networking
		socket = new Socket(serverAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Layout GUI
		messageLabel.setBackground(Color.lightGray);
		frame.getContentPane().add(messageLabel, "South");

		// boardPanel.setBackground(Color.black);
		// boardPanel.setLayout(new GridLayout(3, 3, 2, 2));

		JButton de;
		de = new JButton("lancement d�s");
		de.setVerticalTextPosition(AbstractButton.BOTTOM);
		de.setHorizontalTextPosition(AbstractButton.LEFT);
		de.setBounds(7, 0, 4, 4);

		// bouton pour r�cup�rer la valeur du d�
		de.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				System.out.println("jai clicker");
				Random r = new Random();

				int d1 = 1 + r.nextInt(6);
				int d2 = 1 + r.nextInt(6);
				int result = d1 + d2;
				JOptionPane.showMessageDialog(null, "d�1 = " + d1 + "    d�2 = " + d2 + "    (" + (d1 + d2) + ")", null,
						0);
				// g.setD�Result((d1+d2));
				System.out.println("d� " + result);

				out.println("MOVE");
				out.println(String.valueOf(result));
				position = position + result;
				//pour g�rer les bonus et minus 
		/*	  if(position==6) {
					System.out.println("posiitionnnnn 6");
					position=12;
				}else if(position==19) {
					System.out.println("posiitionnnnn 19");
					position=24;
				}else if(position==42) {
					System.out.println("posiitionnnnn 42");
					position=30;
				}else if(position==58) {
					System.out.println("posiitionnnnn 58");
					position=1;
				}else if(position==27) {
					System.out.println("posiitionnnnn 27");
					position=20;
				}*/
				deplacement(position, cordActuel);
			}
		});
		map.add(de);

		/// frame.getContentPane().add(boardPanel, "Center");
	}

	// * The main thread of the client will listen for messages from the server.
	// The first message will be a "WELCOME" message in which we receive our mark.
	// Then we go into a loop listening for:
	// --> "VALID_MOVE", --> "OPPONENT_MOVED", --> "VICTORY", --> "DEFEAT", -->
	// "TIE", --> "OPPONENT_QUIT, --> "MESSAGE" messages, and handling each message
	// appropriately.
	// The "VICTORY","DEFEAT" and "TIE" ask the user whether or not to play another
	// game.
	// If the answer is no, the loop is exited and the server is sent a "QUIT"
	// message. If an OPPONENT_QUIT message is recevied then the loop will exit and
	// the server will be sent a "QUIT" message also.
	public void play() throws Exception {
		String response;
		try {
			response = in.readLine();
			if (response.startsWith("WELCOME player 1")) {
				joueur = "player1";
				frame.setTitle("L'oie - " + response);
			}
			while (true) {
				response = in.readLine();
				if (response.startsWith("MOVE OK")) {
					System.out.print("Valid move, please wait");
					messageLabel.setText("Valid move, please wait");
				} else {
					if (response.startsWith("MESSAGE")) {
					messageLabel.setText(response.substring(8));
					} else {
						if (response.startsWith("OPPONENT_MOVED")) {
					int loc = Integer.parseInt(response.substring(15));
					this.adversaire = adversaire + loc;
					deplacement(adversaire, cordAdversaire);
					messageLabel.setText("Opponent moved, your turn");
						} else {
				   if (response.startsWith("VICTORY")) {
					messageLabel.setText("You win");
					break;
				   } else {
					if (response.startsWith("DEFEAT")) {
				    messageLabel.setText("You lose");
                    break;
							}
						}
					}
				}
			}
			}
			out.println("QUIT");
		} finally {
			socket.close();
		}
	}

	public boolean wantsToPlayAgain() {
		int response = JOptionPane.showConfirmDialog(frame, "Want to play again?", "JEU DE L'OIE",
				JOptionPane.YES_NO_OPTION);
		frame.dispose();
		return response == JOptionPane.YES_OPTION;
	}

	// Graphical square in the client window.
	static class Square extends JPanel {
		JLabel label = new JLabel((Icon) null);

		public Square() {
			setBackground(Color.white);
			add(label);
		}

		public void setIcon(Icon icon) {
			label.setIcon(icon);
		}

	}

	public JFrame getFrame() {
		return frame;
	}

	//dessiner toutes mes cellules
	public void dessinCellule() {
		for (int i = 1; i < 12; i++)
			cellule.add(new Cellule(new Coodinate(2, i), TypeCellule.Normal));
		for (int i = 4; i >= 2; i--)
			cellule.add(new Cellule(new Coodinate(i, 11), TypeCellule.Normal));
		for (int i = 1; i < 12; i++)
			cellule.add(new Cellule(new Coodinate(4, i), TypeCellule.Normal));
		for (int i = 6; i >= 4; i--)
			cellule.add(new Cellule(new Coodinate(i, 1), TypeCellule.Normal));
		for (int i = 1; i < 12; i++)
			cellule.add(new Cellule(new Coodinate(6, i), TypeCellule.Normal));
		for (int i = 8; i >= 6; i--)
			cellule.add(new Cellule(new Coodinate(i, 11), TypeCellule.Normal));
		for (int i = 1; i < 12; i++)
			cellule.add(new Cellule(new Coodinate(8, i), TypeCellule.Normal));
		for (int i = 10; i >= 8; i--)
			cellule.add(new Cellule(new Coodinate(i, 1), TypeCellule.Normal));
		for (int i = 1; i < 12; i++)
			cellule.add(new Cellule(new Coodinate(10, i), TypeCellule.Normal));
 //sp�cifier la position des cellultes sp�ciales
		cellule.get(5).setType(TypeCellule.Pont);
		cellule.get(19).setType(TypeCellule.Hotel);
		cellule.get(58).setType(TypeCellule.Mort);
		cellule.get(42).setType(TypeCellule.Retourne);
		cellule.get(27).setType(TypeCellule.Puits);
		cellule.get(66).setType(TypeCellule.Gagne);
		

	}

	// Fonction qui fait le lien entre le score et les coordon�e du joueur (ou
	// adversaire) dans la carte de jeu
	public void deplacement(int score, Coodinate cood) {
		// faire les deplacement d'apr�s le score obtenu par le joueur en chaneant les
		// coordonn�e du joueur et son adversaire
		if (score <= 11) {
			cood.setPosX(2);
			cood.setPosY(score);
		} else {
			if (score == 12) {
				cood.setPosX(3);
				cood.setPosY(11);
			} else {
				if (score == 13) {
				cood.setPosX(4);
				cood.setPosY(11);
				} else {
				if (score > 13 && score <= 23) {
				  positionnerMap = 0;
				for (int i = 23; i >= 14; i--) {
				positionnerMap = positionnerMap + 1;
				// Coodinate(6, i)
			     if (score == i) {
				 cood.setPosX(4);// new Coodinate(2, score);
    				cood.setPosY(positionnerMap);
					}
					}
					} else {
		    		if (score == 24) {
					cood.setPosX(5);// new Coodinate(2, score);
					cood.setPosY(1);
					} else {
				    if (score == 25) {
					cood.setPosX(6);// new Coodinate(2, score);
				    cood.setPosY(1);
					} else {
					if (score > 25 && score <= 35) {
					positionnerMap = 1;
					for (int i = 26; i <= 35; i++) {
					positionnerMap = positionnerMap + 1;
					// Coodinate(6, i)
					if (score == i) {
					cood.setPosX(6);// new Coodinate(2, score);
					cood.setPosY(positionnerMap);
					}
					}
					} else {
					if (score == 36) {
					cood.setPosX(7);// new Coodinate(2, score);
					cood.setPosY(11);
					} else {
					if (score >= 37 && score <= 47) {
					positionnerMap = 1;
					for (int i = 47; i > 37; i--) {
					positionnerMap = positionnerMap + 1;
					// Coodinate(8, i)
					if (score == i) {
					cood.setPosX(8);// new Coodinate(2, score);
					cood.setPosY(positionnerMap);
					}
					}
				    } else {
				    if (score == 48) {
				    cood.setPosX(9);// new Coodinate(2, score);
					cood.setPosY(1);
					} else {
					if (score >= 49 && score <= 59) {
					positionnerMap = 1;
					for (int i = 49; i <= 59; i++) {
					positionnerMap = positionnerMap + 1;
					// Coodinate(6, i)
					if (score == i) {
					cood.setPosX(10);// new Coodinate(2, score);
					cood.setPosY(positionnerMap);
					}
				   }
			       }
		          }
	             }
                 }
                 }
				}
			   }
			  }
	     	}
	     	}
     	}
	}

	public JTextField getTextField() {
		return textField;
	}

	public JLabel getLabel() {
		return label;
	}

}