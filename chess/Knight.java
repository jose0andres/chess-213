package chess;

import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(ReturnPiece.PieceFile file, int rank, boolean isWhite) {
        super(isWhite ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN, file, rank);
    }

    @Override
    public ArrayList<String> valid_moves(Piece[][] board) {
        ArrayList<String> moves = new ArrayList<>();

        int r = get_row();
        int c = get_col();
        boolean meIsWhite = isWhite(this.retpiece.pieceType);

        int[][] deltas = {
            {+2, +1}, {+2, -1}, {-2, +1}, {-2, -1},
            {+1, +2}, {+1, -2}, {-1, +2}, {-1, -2}
        };

        for (int[] d : deltas) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (0 <= nr && nr < 8 && 0 <= nc && nc < 8) {
                Piece at = board[nr][nc];
                if (at == null) {
                    moves.add(square(nc, nr));
                } else {
                    boolean themIsWhite = isWhite(at.retpiece.pieceType);
                    if (themIsWhite != meIsWhite) {
                        moves.add(square(nc, nr)); // capture allowed
                    }
                    // no ray continuation for knights
                }
            }
        }

        return moves;
    }

    private boolean isWhite(ReturnPiece.PieceType t) {
        return t == ReturnPiece.PieceType.WP || t == ReturnPiece.PieceType.WR ||
               t == ReturnPiece.PieceType.WN || t == ReturnPiece.PieceType.WB ||
               t == ReturnPiece.PieceType.WQ || t == ReturnPiece.PieceType.WK;
    }

    private String square(int col, int row) {
        return String.valueOf(int_to_file(col)) + int_to_rank(row);
    }
}
