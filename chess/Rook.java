package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;
import chess.Chess.Player;

public class Rook extends Piece {

    // Track whether the rook has moved (useful for castling with the king)
    public boolean moved = false;

    public Rook(Player color, PieceFile file, int rank) {
        super(type(color), file, rank);
    }

    private static PieceType type(Player color) {
        return (color == Player.white) ? PieceType.WR : PieceType.BR;
    }

    @Override
    public ArrayList<String> valid_moves(Piece[][] board) {
        return super.valid_moves(board);
    }

    @Override
    public ArrayList<String> valid_moves_no_test(Piece[][] board) {
        ArrayList<String> moves = new ArrayList<>();

        int r = get_row();
        int c = get_col();

        // Rook rays: up, down, right, left
        scanRay(board, r, c, +1,  0, moves);
        scanRay(board, r, c, -1,  0, moves);
        scanRay(board, r, c,  0, +1, moves);
        scanRay(board, r, c,  0, -1, moves);

        return moves;
    }

    /**
     * Scan along a direction (dr, dc) from (r, c) adding empty squares; stop after
     * first non-empty square (add it only if it's an enemy).
     */
    protected void scanRay(Piece[][] board, int r, int c, int dr, int dc, ArrayList<String> out) {
        boolean meIsWhite = isWhite(this.retpiece.pieceType);
        int nr = r + dr, nc = c + dc;
        while (nr >= 0 && nr < 8 && nc >= 0 && nc < 8) {
            Piece at = board[nr][nc];
            if (at == null) {
                out.add(square(nc, nr));
            } else {
                boolean themIsWhite = isWhite(at.retpiece.pieceType);
                if (themIsWhite != meIsWhite) {
                    out.add(square(nc, nr)); // capture
                }
                break; // stop on first piece
            }
            nr += dr; nc += dc;
        }
    }
}
