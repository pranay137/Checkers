package checkers;

public class Piece {
	public static final int EMPTY = 0;
    public static final int RED = 1;
    public static final int BLACK = 2;
    public static final int RED_KING = 3;
    public static final int BLACK_KING = 4;

    private int pieceType;
    private int row;
    private int col;
    
    public Piece(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }

    public Piece(int pieceType) {
        this.pieceType = pieceType;
    }

    public int getPieceType() {
        return pieceType;
    }

    public void setPieceType(int pieceType) {
        this.pieceType = pieceType;
    }
}