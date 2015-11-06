import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Gameboard extends JFrame {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private ChessBoard chessBoard;
	private DisplayBoard displayBoard;
	private ImageIcon background;
	private JPanel imagePanel;

	private int roundCount;

	final static String FN_BG = "C:\\Users\\Alex\\Pictures\\background.jpg";

	public Gameboard() {

		this.setTitle("Reversi");

		chessBoard = new ChessBoard();
		displayBoard = new DisplayBoard();
		 background = new ImageIcon(FN_BG);
		background = new ImageIcon(Gameboard.class
				.getResource("resource\\background.jpg"));

		JLabel picLabel = new JLabel(background);
		picLabel.setBounds(0, 0, background.getIconWidth(), background
				.getIconHeight());

		imagePanel = (JPanel) this.getContentPane();
		imagePanel.add(picLabel);
		imagePanel.setOpaque(false);
		imagePanel.setLayout(null);

		displayBoard.setBounds(670, 50, 300, 600);

		this.getLayeredPane().setLayout(null);
		this.getLayeredPane().add(chessBoard);
		this.getLayeredPane().add(displayBoard);

		this
				.setBounds(
						(Toolkit.getDefaultToolkit().getScreenSize().width - background
								.getIconWidth()) / 2,
						(Toolkit.getDefaultToolkit().getScreenSize().height - background
								.getIconHeight()) / 2, background
								.getIconWidth(), background.getIconHeight());
		this.setResizable(false);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void initGame() {

		roundCount = 1;

		chessBoard.setChess(3, 3, 0);
		chessBoard.setChess(3, 4, 1);
		chessBoard.setChess(4, 3, 1);
		chessBoard.setChess(4, 4, 0);

		displayBoard.setNumber(0, 2);
		displayBoard.setNumber(1, 2);
	}

	private class ChessBoard extends JPanel implements MouseListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		final static String filename = "C:\\Users\\Alex\\Pictures\\chessbord.PNG";
		final static String chessBlack = "C:\\Users\\Alex\\Pictures\\black.PNG";
		final static String chessWhite = "C:\\Users\\Alex\\Pictures\\white.PNG";

		final static int BOARD_SIZE = 8;

		private JLabel[][] chessMatrixLabel = new JLabel[BOARD_SIZE][BOARD_SIZE];
		private int chessMatrix[][] = new int[BOARD_SIZE][BOARD_SIZE];

		private int blackCount;
		private int whiteCount;

		private int right;

		public ChessBoard() {
			blackCount = 0;
			whiteCount = 0;

			this.setLayout(null);

			for (int i = 0; i < BOARD_SIZE; i++) {
				for (int j = 0; j < BOARD_SIZE; j++) {
					chessMatrix[i][j] = -1;
					chessMatrixLabel[i][j] = new JLabel();
					chessMatrixLabel[i][j].setBounds(i * 78 + 15, j * 78 + 15,
							75, 75);
					chessMatrixLabel[i][j].addMouseListener(this);
					this.add(chessMatrixLabel[i][j]);
				}
			}

			this.setVisible(true);
			this.setSize(650, 650);
		}

		private void countChess() {
			blackCount = 0;
			whiteCount = 0;
			for (int i = 0; i < BOARD_SIZE; i++) {
				for (int j = 0; j < BOARD_SIZE; j++) {
					if (chessMatrix[i][j] == 1) {
						blackCount++;
					} else if (chessMatrix[i][j] == 0) {
						whiteCount++;
					}
				}
			}
		}

		public void setChess(int x, int y, int c) {
			if (c == 1) {
				// chessMatrixLabel[x][y].setIcon(new ImageIcon(chessBlack));
				chessMatrixLabel[x][y].setIcon(new ImageIcon(Gameboard.class
						.getResource("resource//black.PNG")));
			} else if (c == 0) {
				// chessMatrixLabel[x][y].setIcon(new ImageIcon(chessWhite));
				chessMatrixLabel[x][y].setIcon(new ImageIcon(Gameboard.class
						.getResource("resource//white.PNG")));
			}

			chessMatrix[x][y] = c;
		}

		private void repaintChessboard() {
			for (int i = 0; i < BOARD_SIZE; i++) {
				for (int j = 0; j < BOARD_SIZE; j++) {
					if (chessMatrix[i][j] == 1) {
						// chessMatrixLabel[i][j].setIcon(new
						// ImageIcon(chessBlack));
						chessMatrixLabel[i][j].setIcon(new ImageIcon(
								Gameboard.class
										.getResource("resource//black.PNG")));
					} else if (chessMatrix[i][j] == 0) {
						// chessMatrixLabel[i][j].setIcon(new
						// ImageIcon(chessWhite));
						chessMatrixLabel[i][j].setIcon(new ImageIcon(
								Gameboard.class
										.getResource("resource//white.PNG")));
					}
				}
			}
		}

		private boolean endGame(int matrix[][]) {
			String end;
			if (Algorithm.checkDoable(1, matrix)) {
				//System.out.println("0000");
				return false;
			}
			if (Algorithm.checkDoable(0, matrix)) {
				//System.out.println("11111");
				return false;
			}
			if (blackCount > whiteCount) {
				end = "Black Wins!";
			} else if (whiteCount > blackCount) {
				end = "White Wins!";
			} else {
				end = "Drawn Game!";
			}

			System.out.println("-----------" + end + "--------------");
			return true;

		}

		@Override
		public void paintComponent(Graphics g) {
			// ImageIcon icon = new ImageIcon(filename);
			ImageIcon icon = new ImageIcon(Gameboard.class
					.getResource("resource//chessbord.PNG"));
			g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int judge = 0;
			for (int i = 0; i < BOARD_SIZE; i++) {
				for (int j = 0; j < BOARD_SIZE; j++) {
					if (chessMatrixLabel[i][j].equals((JLabel) e.getSource())
							&& chessMatrix[i][j] == -1) {

						if (Algorithm.checkValid(i, j, roundCount % 2,
								chessMatrix, true)) {
							judge = 1;
							setChess(i, j, roundCount % 2);

							repaintChessboard();
							roundCount++;
							countChess();
							displayBoard.setNumber(0, whiteCount);
							displayBoard.setNumber(1, blackCount);
							if (endGame(chessMatrix) == true) {
								System.out
										.println("==========Game Over==========");
								judge=2;
							}
						}

						if (!Algorithm.checkDoable(roundCount % 2, chessMatrix)&& judge!=2) {
							System.out.println("++++++++++" + "White No Move!"
									+ "++++++++++");
							judge = 0;
							roundCount++;
						}
					}
				}
			}

			while (judge == 1) {
				Integer[] value = Ai.tree((roundCount) % 2, chessMatrix);
				int m = value[0];
				int n = value[1];
				System.out.println(value[0] + "," + value[1]);

				if (Algorithm.checkValid(m, n, roundCount % 2, chessMatrix,
						true)) {
					setChess(m, n, roundCount % 2);

					System.out.println(m + "," + n);

					repaintChessboard();
					roundCount++;
					countChess();
					displayBoard.setNumber(0, whiteCount);
					displayBoard.setNumber(1, blackCount);
					if (endGame(chessMatrix) == true) {
						System.out.println("==========Game Over==========");
						judge=2;
					}
					else
					{
						judge = 0;
					}
					
				}

				if (!Algorithm.checkDoable(roundCount % 2, chessMatrix) && judge!=2) {
					System.out.println("++++++++++" + "Black No Move!"
							+ "++++++++++");
					roundCount++;
					judge = 1;
				}
			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private class DisplayBoard extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;



		final static String FN_HB = "resource\\HumanBlack.png";
		final static String FN_HW = "resource\\HumanWhite.png";
		final static String FN_CB = "resource\\ComputerBlack.png";
		final static String FN_CW = "resource\\ComputerWhite.png";

		private PlayerLable jlPlayer1;
		private PlayerLable jlPlayer2;

		public DisplayBoard() {

			jlPlayer1 = new PlayerLable(FN_HB, 1);
			jlPlayer2 = new PlayerLable(FN_HW, 2);

			this.add(jlPlayer1);
			this.add(jlPlayer2);

			jlPlayer1.setBounds(0, 0, 300, 100);
			jlPlayer2.setBounds(0, 450, 300, 100);

			this.setOpaque(false);
			this.setLayout(null);
			this.setSize(300, 650);
			this.setVisible(true);
		}

		public void setNumber(int c, int number) {
			if (c == 1) {
				jlPlayer1.setNumberLabel(number);
			} else {
				jlPlayer2.setNumberLabel(number);
			}
		}

	}

	private class PlayerLable extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ImageIcon playerImage;
		private JLabel numberLabel;

		public PlayerLable(String playerFile, int color) {

			// playerImage = new ImageIcon(playerFile);
			playerImage = new ImageIcon(Gameboard.class.getResource(playerFile));
			numberLabel = new JLabel("00");

			numberLabel.setBounds(30, 35, 30, 30);
			numberLabel.setFont(new Font("arial", Font.BOLD, 24));

			if (color == 1) {
				numberLabel.setForeground(Color.white);
			} else {
				numberLabel.setForeground(Color.black);
			}

			this.add(numberLabel);
			this.setOpaque(false);
			this.setLayout(null);
			this.setSize(playerImage.getIconWidth(), playerImage
					.getIconHeight());
			this.setVisible(true);
		}

		public void setNumberLabel(int number) {
			numberLabel.setText(String.format("%02d", number));
		}

		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(playerImage.getImage(), 0, 0, getWidth(), getHeight(),
					this);
		}

	}

	private class TimeLable extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}

	public static void main(String[] args) {
		Gameboard gb = new Gameboard();

		gb.initGame();

	}

}
