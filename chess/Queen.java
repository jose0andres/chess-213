package chess;

import java.util.ArrayList;
import chess.ReturnPiece.PieceType;
import chess.Chess.Player;

public class Queen extends Piece {
    public Queen(Player color, int row, int col) {
        super(color,row,col); 
    }

    @Override
    public PieceType getType() {
        return (color==Player.white) ? PieceType.WQ : PieceType.BQ;
    }

    @Override
    public ArrayList<int[]> valid_moves_no_test(Piece[][] board) {
        //move in any direction//
        ArrayList<int[]> m = new ArrayList<>();
        addRay(board,color,row,col,1,0,m);
        addRay(board,color,row,col,-1,0,m);
        addRay(board,color,row,col,0,1,m);
        addRay(board,color,row,col,0,-1,m);
        addRay(board,color,row,col,1,1,m);
        addRay(board,color,row,col,1,-1,m);
        addRay(board,color,row,col,-1,1,m);
        addRay(board,color,row,col,-1,-1,m);
        return m;
    }
}
