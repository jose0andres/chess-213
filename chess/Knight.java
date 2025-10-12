package chess;

import java.util.ArrayList;
import chess.ReturnPiece.PieceType;
import chess.Chess.Player;

public class Knight extends Piece {
    public Knight(Player color, int row, int col) { super(color,row,col); }

    @Override
    public PieceType getType() {
        return (color==Player.white) ? PieceType.WN : PieceType.BN;
    }

    @Override
    public ArrayList<int[]> valid_moves_no_test(Piece[][] board) {
        //logic for L shape moves//
        ArrayList<int[]> m = new ArrayList<>();
        
        int[][] d = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
        for (int[] x:d) {

            int nr=row+x[0], nc=col+x[1];
            if (!inBounds(nr,nc)) continue;
            Piece p=board[nr][nc];
            if (p==null || p.color!=this.color) m.add(new int[]{nr,nc});
        }
        
        return m;
    }
}
