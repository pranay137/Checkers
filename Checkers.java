package checkers;
import java.util.ArrayList;


public class Checkers {
    private CheckersBoard checkersBoard;
    private int currentPlayer;
    private Piece selectedPiece;

    public Checkers() {
        checkersBoard = new CheckersBoard();
        currentPlayer = Piece.RED;
        selectedPiece = null;
    }

    public CheckersBoard getCheckersBoard() {
        return checkersBoard;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setSelectedPiece(int row, int col) {
        selectedPiece = checkersBoard.getPiece(row, col);
    }

    public void clearSelectedPiece() {
        selectedPiece = null;
    }

    private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {
        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
            return false; // (r3,c3) is off the board.
        if (checkersBoard.getPiece(r3, c3).getPieceType() != Piece.EMPTY)
            return false; // (r3,c3) already contains a piece.

        if (player == Piece.RED) {
            if (checkersBoard.getPiece(r1, c1).getPieceType() == Piece.RED && r3 > r1)
                return false; // Regular red piece can only move up.
            if (checkersBoard.getPiece(r2, c2).getPieceType() != Piece.BLACK && checkersBoard.getPiece(r2, c2).getPieceType() != Piece.BLACK_KING)
                return false; // There is no black piece to jump.
            return true; // The jump is legal.
        } else {
            if (checkersBoard.getPiece(r1, c1).getPieceType() == Piece.BLACK && r3 < r1)
                return false; // Regular black piece can only move down.
            if (checkersBoard.getPiece(r2, c2).getPieceType() != Piece.RED && checkersBoard.getPiece(r2, c2).getPieceType() != Piece.RED_KING)
                return false; // There is no red piece to jump.
            return true; // The jump is legal.
        }
    }

    private boolean canMove(int player, int r1, int c1, int r2, int c2) {
        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
            return false; // (r2,c2) is off the board.

        if (checkersBoard.getPiece(r2, c2).getPieceType() != Piece.EMPTY)
            return false; // (r2,c2) already contains a piece.

        if (player == Piece.RED) {
            if (checkersBoard.getPiece(r1, c1).getPieceType() == Piece.RED && r2 > r1)
                return false; // Regular red piece can only move down.
            return true; // The move is legal.
        } else {
            if (checkersBoard.getPiece(r1, c1).getPieceType() == Piece.BLACK && r2 < r1)
                return false; // Regular black piece can only move up.
            return true; // The move is legal.
        }
    }

    public static class CheckersMove {
        int fromRow, fromCol; // Position of piece to be moved.
        int toRow, toCol; // Square it is to move to.

        CheckersMove(int r1, int c1, int r2, int c2) {
            fromRow = r1;
            fromCol = c1;
            toRow = r2;
            toCol = c2;
        }
    }

    public CheckersMove[] getLegalMoves(int player) {
        if (player != Piece.RED && player != Piece.BLACK)
            return null;
        int playerKing; // The constant representing a King belonging to player.
        if (player == Piece.RED)
            playerKing = Piece.RED_KING;
        else
            playerKing = Piece.BLACK_KING;

        ArrayList<CheckersMove> moves = new ArrayList<>(); // Moves will be stored in this list.

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (checkersBoard.getPiece(row, col).getPieceType() == player || checkersBoard.getPiece(row, col).getPieceType() == playerKing) {
                    if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                        moves.add(new CheckersMove(row, col, row + 2, col + 2));
                    if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                        moves.add(new CheckersMove(row, col, row - 2, col + 2));
                    if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                        moves.add(new CheckersMove(row, col, row + 2, col - 2));
                    if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                        moves.add(new CheckersMove(row, col, row - 2, col - 2));
                }
            }
        }

        if (moves.isEmpty()) { // Jumps (captures) are mandatory in Checkers, make sure there are none available before proceeding.
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (checkersBoard.getPiece(row, col).getPieceType() == player || checkersBoard.getPiece(row, col).getPieceType() == playerKing) {
                        if (canMove(player, row, col, row + 1, col + 1))
                            moves.add(new CheckersMove(row, col, row + 1, col + 1));
                        if (canMove(player, row, col, row - 1, col + 1))
                            moves.add(new CheckersMove(row, col, row - 1, col + 1));
                        if (canMove(player, row, col, row + 1, col - 1))
                            moves.add(new CheckersMove(row, col, row + 1, col - 1));
                        if (canMove(player, row, col, row - 1, col - 1))
                            moves.add(new CheckersMove(row, col, row - 1, col - 1));
                    }
                }
            }
        }

        if (moves.isEmpty())
            return null;
        else
            return moves.toArray(new CheckersMove[moves.size()]);
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece selectedPiece = checkersBoard.getPiece(fromRow, fromCol);

        // Check if the selected piece is valid
        if (selectedPiece.getPieceType() == Piece.EMPTY || selectedPiece.getPieceType() != currentPlayer) {
            return false; // Invalid piece selection
        }

        // Check if the move is a valid jump/capture
        if (Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 2) {
            int jumpedRow = (fromRow + toRow) / 2; // Calculate the row of the jumped piece
            int jumpedCol = (fromCol + toCol) / 2; // Calculate the column of the jumped piece
            return canJump(currentPlayer, fromRow, fromCol, jumpedRow, jumpedCol, toRow, toCol);
        }

        // Check if the move is a valid regular move
        if (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 1) {
            return canMove(currentPlayer, fromRow, fromCol, toRow, toCol);
        }

        return false; // Invalid move
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (selectedPiece == null || !isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false; // Invalid move
        }

        checkersBoard.movePiece(fromRow, fromCol, toRow, toCol);
        currentPlayer = (currentPlayer == Piece.RED) ? Piece.BLACK : Piece.RED;

        return true; // Move successful
    }

    public void switchPlayers() {
        if (currentPlayer == Piece.RED)
            currentPlayer = Piece.BLACK;
        else
            currentPlayer = Piece.RED;
    }
}
