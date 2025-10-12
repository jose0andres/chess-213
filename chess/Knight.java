package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;
import chess.Chess.Player;

public class Knight extends Piece {

    public Knight(Player color, PieceFile file, int rank) {
        super(type(color), file, rank);
    }

    private static PieceType type(Player color) {
        return (color == Player.white) ? PieceType.WN : PieceType.BN;
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

        int[][] deltas = {
            {+2, +1}, {+2, -1}, {-2, +1}, {-2, -1},
            {+1, +2}, {+1, -2}, {-1, +2}, {-1, -2}
        };

        boolean meIsWhite = isWhite(this.retpiece.pieceType);

        for (int[] d : deltas) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr < 0 || nr >= 8 || nc < 0 || nc >= 8) continue;

            Piece at = board[nr][nc];
            if (at == null) {
                moves.add(square(nc, nr));
            } else {
                boolean themIsWhite = isWhite(at.retpiece.pieceType);
                if (themIsWhite != meIsWhite) {
                    moves.add(square(nc, nr)); // capture allowed
                }
            }
        }

        return moves;
    }
}
