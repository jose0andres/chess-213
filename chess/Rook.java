package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class Rook extends Piece {

    public boolean moved = false; // for castling

    public Rook(Player color, PieceFile file, int rank)
    {
        super(type(color), file, rank);
    }

    private static PieceType type(Player color)
    {
        if(color == Player.black) return PieceType.BR;
        else return PieceType.WR;
    }

    public void move(Piece[][] board, String newPos) {
        super.move(board, newPos);
        moved = true;
    }

    @Override
    public ArrayList<String> valid_moves(Piece[][] board) {
        ArrayList<String> moves = new ArrayList<>();

        int r = get_row(); // 0..7, row in board
        int c = get_col(); // 0..7, col in board

        // scan in 4 orthogonal directions
        scanRay(board, r, c, +1,  0, moves); // down rows
        scanRay(board, r, c, -1,  0, moves); // up rows
        scanRay(board, r, c,  0, +1, moves); // right cols
        scanRay(board, r, c,  0, -1, moves); // left cols

        return moves;
    }
}
