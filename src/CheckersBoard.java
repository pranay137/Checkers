package checkers;

public class CheckersBoard {
	public Piece[][] board;

    public CheckersBoard() {
        board = new Piece[8][8];
        setUpGame();
    }
    
    public Piece getPiece(int row, int col) {
        return board[row][col];
    }
    
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board[fromRow][fromCol];
        board[fromRow][fromCol] = new Piece(Piece.EMPTY);
        board[toRow][toCol] = piece;
    }


    public void setUpGame() {
    	for (int row = 0; row < 8; row++) {
    	for (int col = 0; col < 8; col++) {
    		  if (row % 2 == col % 2) {
                  if (row < 3) {
                      Piece piece = new Piece(Piece.BLACK);
                      board[row][col] = piece;
                  } else if (row > 4) {
                      Piece piece = new Piece(Piece.RED);
                      board[row][col] = piece;
                  } else {
                      Piece piece = new Piece(Piece.EMPTY);
                      board[row][col] = piece;
                  }
              } else {
                  Piece piece = new Piece(Piece.EMPTY);
                  board[row][col] = piece;
              }
          }
      }
    }
    public boolean isSquareOccupied(int row, int col) {
	return getPiece(row, col).getPieceType() != Piece.EMPTY;
    }
}
   
    	              