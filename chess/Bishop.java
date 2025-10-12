package chess;

import java.util.ArrayList;
import chess.ReturnPiece.PieceType;
import chess.Chess.Player;

public class Bishop extends Piece {
    public Bishop(Player color, int row, int col) { 
        super(color,row,col); 
    }

    @Override
    public PieceType getType() {
        return (color==Player.white) ? PieceType.WB : PieceType.BB;
    }

    @Override
    public ArrayList<int[]> valid_moves_no_test(Piece[][] board) {
        //move diagonally//
        ArrayList<int[]> m = new ArrayList<>();
            addRay(board,color,row,col,1,1,m);
            addRay(board,color,row,col,1,-1,m);
            addRay(board,color,row,col,-1,1,m);
            addRay(board,color,row,col,-1,-1,m);
        return m;
    }
}