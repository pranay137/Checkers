package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;


public class MainFram {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares;
    private CheckersBoard checkersBoard;
    private Checkers game;
    private Piece selectedPiece;
    
    public Piece getSelectedPiece() {
        return selectedPiece;
    }
    
    public void setSelectedPiece(int row, int col) {
    	this.selectedPiece = checkersBoard.getPiece(row, col);
    }
    
    public void clearSelectedPiece() {
        selectedPiece = null;
    }
    private void highlightSquare(int row, int col) {
        squares[row][col].setBackground(Color.YELLOW);
    }
    private void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = game.getCheckersBoard().getPiece(row, col);
                if (piece != null) {
                    ImageIcon icon = getPieceIcon(piece);
                    squares[row][col].setIcon(icon);
                } else {
                    squares[row][col].setIcon(null);
                }
            }
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setBackground(getSquareColor(row, col));
            }
        }
    }


    public boolean isGameOver() {
        int redPieceCount = 0;
        int blackPieceCount = 0;
        
        // Initialize the checkersBoard object
        checkersBoard = game.getCheckersBoard();

        // Count the number of remaining red and black pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = checkersBoard.getPiece(row, col);
                if (piece.getPieceType() == Piece.RED || piece.getPieceType() == Piece.RED_KING) {
                    redPieceCount++;
                } else if (piece.getPieceType() == Piece.BLACK || piece.getPieceType() == Piece.BLACK_KING) {
                    blackPieceCount++;
                }
            }
        }

        // Check if any player has no remaining pieces or unable to make a move
        if (redPieceCount == 0 || blackPieceCount == 0 || game.getLegalMoves(Piece.RED) == null || game.getLegalMoves(Piece.BLACK) == null) {
            return true; // Game over
        }

        return false; // Game is not over
    }
  
    
    public MainFram() {
    	checkersBoard = new CheckersBoard();
    	game = new Checkers();
        frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        boardPanel = new JPanel(new GridLayout(8, 8));
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);

        squares = new JButton[8][8];
        initializeBoard();        
        frame.pack();
        frame.setVisible(true);
    }
   
    
    private ImageIcon getPieceIcon(Piece piece) {
        switch (piece.getPieceType()) {
            case Piece.RED:
                return new ImageIcon("src/4284016_3_15.png");  // Replace "red_piece.png" with the actual file path or resource name for the red piece icon.
            case Piece.BLACK:
                return new ImageIcon("src/4281223_15.png");  // Replace "black_piece.png" with the actual file path or resource name for the black piece icon.
            case Piece.RED_KING:
                return new ImageIcon("src/4284016_3_15.png");  // Replace "red_king_piece.png" with the actual file path or resource name for the red king piece icon.
            case Piece.BLACK_KING:
                return new ImageIcon("src/4281223_15.png");  // Replace "black_king_piece.png" with the actual file path or resource name for the black king piece icon.
            default:
                return null;
        }
    }

  
    
    private void initializeBoard() {
    	boardPanel = new JPanel(new GridLayout(8, 8)); // Set GridLayout for boardPanel
    	for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton();
                squares[row][col].setBackground(getSquareColor(row, col));
                squares[row][col].setOpaque(true);
                squares[row][col].setBorderPainted(false);
                squares[row][col].addActionListener(new SquareButtonListener(row, col));

                Piece piece = checkersBoard.getPiece(row, col); // Access the board directly
                 if (piece != null) {
                    ImageIcon icon = getPieceIcon(piece);
                    squares[row][col].setIcon(icon);
                }

                boardPanel.add(squares[row][col]);
            }
        }
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER); // Add boardPanel to the frame
    }



    private Color getSquareColor(int row, int col) {
        if ((row + col) % 2 == 0) {
            return Color.RED;
        } else {
            return Color.BLACK;
        }
    }
    private class SquareButtonListener implements ActionListener {
    	private int row;
        private int col;
        private boolean isPieceSelected;
        private static final int squareSize = 80;

        public SquareButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
            this.isPieceSelected = false;
        }


        public void actionPerformed(ActionEvent e) {
            clearHighlights(); // Clear any existing highlights
            JButton button = (JButton) e.getSource();

            System.out.println("Button clicked: (" + row + ", " + col + ")");
            System.out.println("isPieceSelected before: " + isPieceSelected);

            if (!isPieceSelected) {
                Piece clickedPiece = checkersBoard.getPiece(row, col);
                System.out.println("Clicked piece type: " + clickedPiece.getPieceType());
                System.out.println("Piece.EMPTY: " + Piece.EMPTY);
                System.out.println("Current player: " + game.getCurrentPlayer());

                if (clickedPiece.getPieceType() != Piece.EMPTY && clickedPiece.getPieceType() == game.getCurrentPlayer()) {
                    selectedPiece = clickedPiece;
                    highlightSquare(row, col);
                    isPieceSelected = true;

                    System.out.println("Piece selected: (" + row + ", " + col + ")");
                }
                System.out.println(isPieceSelected);
            }

            if (isPieceSelected) {
                int selectedRow = selectedPiece.getRow();
                int selectedCol = selectedPiece.getCol();
                System.out.println("Selected: (" + selectedRow + ", " + selectedCol + ")");
                System.out.println("Clicked: (" + row + ", " + col + ")");

                if (row == selectedRow && col == selectedCol) {
                    // Deselect the selected piece
                    selectedPiece = null;
                    clearHighlights();
                    isPieceSelected = false;
                } else {
                    boolean moveSuccessful = game.makeMove(selectedRow, selectedCol, row, col);

                    if (moveSuccessful) {
                        // Animate piece movement
                        animatePieceMovement(selectedRow, selectedCol, row, col);
                        // Update the GUI board
                        updateBoard();
                        frame.repaint();
                        return; // Exit the method after triggering the animation
                    } else {
                        // Reset selected piece and highlights
                        selectedPiece = null;
                        clearHighlights();
                        isPieceSelected = false;
                    }
                }
            }

            System.out.println("End of actionListener method");
        }


        private void animatePieceMovement(int startRow, int startCol, int endRow, int endCol) {
        	System.out.println("nigger");
            int numSteps = 10;
            int dx = (endCol - startCol) * squareSize / numSteps;
            int dy = (endRow - startRow) * squareSize / numSteps;

            int startX = squares[startRow][startCol].getX();
            int startY = squares[startRow][startCol].getY();

            Timer timer = new Timer(50, null);
            AtomicInteger step = new AtomicInteger(0);

            timer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int x = startX + step.get() * dx;
                    int y = startY + step.get() * dy;

                    squares[startRow][startCol].setLocation(x, y);
                    frame.repaint();

                    if (step.incrementAndGet() > numSteps) {
                        timer.stop();

                        squares[endRow][endCol].setIcon(squares[startRow][startCol].getIcon());
                        squares[startRow][startCol].setIcon(null);

                        squares[startRow][startCol].setLocation(startX, startY);

                        frame.repaint();

                        JOptionPane.showMessageDialog(frame, "Animation complete");

                        // Update the GUI board
                        updateBoard();

                        // Update the piece position during each step
                        squares[startRow][startCol].setLocation(x, y);
                        frame.repaint();
                    }
                }
            });

            timer.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFram();
            }
        });
    }
    }
