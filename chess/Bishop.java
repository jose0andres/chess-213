package chess;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(ReturnPiece.PieceFile file, int rank, boolean isWhite) {
        super(isWhite ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB, file, rank);
    }

    @Override
    public ArrayList<String> valid_moves(Piece[][] board) {
        ArrayList<String> moves = new ArrayList<>();

        int r = get_row();
        int c = get_col();

        // scan in 4 diagonal directions
        scanRay(board, r, c, +1, +1, moves);
        scanRay(board, r, c, +1, -1, moves);
        scanRay(board, r, c, -1, +1, moves);
        scanRay(board, r, c, -1, -1, moves);

        return moves;
    }
}
