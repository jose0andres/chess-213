package chess;

import chess.ReturnPiece.*;
import chess.Chess.Player;
import java.util.ArrayList;

public abstract class Piece 
{
    ReturnPiece retpiece;  

    public Piece(PieceType type, PieceFile file, int rank)
    {
        retpiece = new ReturnPiece();
        retpiece.pieceType = type;
        retpiece.pieceFile = file;
        retpiece.pieceRank = rank;
    }

    // ---------- Public API expected by the project ----------

    /** Generate legal moves for this piece, filtering out moves that leave own king in check. */
    public ArrayList<String> valid_moves(Piece[][] board)
    {
        ArrayList<String> pseudo = valid_moves_no_test(board);
        ArrayList<String> legal = new ArrayList<>();
        for (String sq : pseudo) {
            int[] rc = parseSquare(sq);
            if (rc != null && test_move(board, rc[0], rc[1])) {
                legal.add(sq);
            }
        }
        return legal;
    }

    /** Generate all moves for the piece ignoring self-check (pseudo-legal). */
    public abstract ArrayList<String> valid_moves_no_test(Piece[][] board);

    /**
     * Simulate moving this piece to (toRow, toCol). Return true if the position
     * after the move does NOT have our own king in check.
     */
    public boolean test_move(Piece[][] board, int toRow, int toCol)
    {
        int fromRow = get_row();
        int fromCol = get_col();

        // Bounds check
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) return false;

        Piece moving = this;
        Piece captured = board[toRow][toCol];

        // Apply move on board
        board[fromRow][fromCol] = null;
        board[toRow][toCol] = moving;

        // Save old coords to restore
        PieceFile oldFile = moving.retpiece.pieceFile;
        int oldRank = moving.retpiece.pieceRank;

        // Update the piece's internal coordinates
        moving.retpiece.pieceFile = int_to_file(toCol);
        moving.retpiece.pieceRank = int_to_rank(toRow);

        boolean meIsWhite = isWhite(moving.retpiece.pieceType);
        Player me = meIsWhite ? Player.white : Player.black;

        boolean inCheck = isKingInCheck(board, me);

        // Undo move
        board[toRow][toCol] = captured;
        board[fromRow][fromCol] = moving;
        moving.retpiece.pieceFile = oldFile;
        moving.retpiece.pieceRank = oldRank;

        return !inCheck;
    }

    // ---------- Check detection helpers (no recursion on valid_moves) ----------

    /** Returns true if the specified side's king is attacked in the current board. */
    protected boolean isKingInCheck(Piece[][] board, Player side)
    {
        int kingRow = -1, kingCol = -1;
        // Locate the king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null) continue;
                if ((side == Player.white && p.retpiece.pieceType == PieceType.WK) ||
                    (side == Player.black && p.retpiece.pieceType == PieceType.BK)) {
                    kingRow = r; kingCol = c;
                    break;
                }
            }
        }
        if (kingRow == -1) return false; // should not happen

        String kingSq = square(kingCol, kingRow);
        // Scan opponent moves (pseudo-legal only!)
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null) continue;
                boolean white = isWhite(p.retpiece.pieceType);
                if ((side == Player.white && white) || (side == Player.black && !white)) continue;
                ArrayList<String> attacks = p.valid_moves_no_test(board);
                for (String s : attacks) {
                    if (s.equals(kingSq)) return true;
                }
            }
        }
        return false;
    }

    // ---------- Coordinates & utilities ----------

    public static PieceFile int_to_file(int x) //x is column [0..7]
    {
        if(x == 0) return PieceFile.a;
        else if(x == 1) return PieceFile.b;
        else if(x == 2) return PieceFile.c;
        else if(x == 3) return PieceFile.d;
        else if(x == 4) return PieceFile.e;
        else if(x == 5) return PieceFile.f;
        else if(x == 6) return PieceFile.g;
        else return PieceFile.h;
    }

    public static int int_to_rank(int y) //y is row index [0..7] -> rank [1..8]
    {
        return y + 1;
    }

    public int get_row()  // returns 0..7
    {
        return retpiece.pieceRank - 1;
    }

    public int get_col()  // returns 0..7
    {
        return retpiece.pieceFile.ordinal();
    }

    protected boolean isWhite(ReturnPiece.PieceType t) {
        return t == ReturnPiece.PieceType.WP || t == ReturnPiece.PieceType.WR ||
               t == ReturnPiece.PieceType.WN || t == ReturnPiece.PieceType.WB ||
               t == ReturnPiece.PieceType.WQ || t == ReturnPiece.PieceType.WK;
    }

    protected String square(int col, int row) {
        return String.valueOf(int_to_file(col)) + int_to_rank(row);
    }

    /** Parse squares like "e4" into (row,col). Returns null if invalid. */
    protected int[] parseSquare(String sq) {
        if (sq == null || sq.length() < 2) return null;
        char file = sq.charAt(0);
        int col = file - 'a';
        int rank;
        try {
            rank = Integer.parseInt(sq.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }
        int row = rank - 1;
        if (col < 0 || col > 7 || row < 0 || row > 7) return null;
        return new int[]{row, col};
    }
}
