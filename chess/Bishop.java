package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class Bishop extends Piece {

    public Bishop(Player color, PieceFile file, int rank)
    {
        super(type(color), file, rank);
    }

    private static PieceType type(Player color)
    {
        if(color == Player.black) return PieceType.BB;
        else return PieceType.WB;
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
