package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceType;

public class King extends Piece {
    //disable castling after king moves//
    public boolean moved = false;
    public King(Player color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public PieceType getType() {
        return (color == Player.white) ? PieceType.WK : PieceType.BK;
    }

    //only moves possible are 8 squares around king//
    @Override
    public ArrayList<int[]> valid_moves_no_test(Piece[][] board) {
        
        ArrayList<int[]> moves = new ArrayList<>();
        
        final int r = this.row;
        final int c = this.col;
        final int[] DR = {-1,-1,-1, 0, 0, 1, 1, 1};
        final int[] DC = {-1, 0, 1,-1, 1,-1, 0, 1};

        for (int i = 0; i < 8; i++) {

            int nr = r + DR[i], nc = c + DC[i];
            if (!inside(nr, nc)) continue;
            Piece at = board[nr][nc];
            if (at == null || at.color != this.color) {
                moves.add(new int[]{nr, nc});
            }
        }
        return moves;
    }

    @Override
    public ArrayList<int[]> valid_moves(Piece[][] board) {
        
        ArrayList<int[]> legal = new ArrayList<>();

        final int kr = this.row;
        final int kc = this.col;

        for (int[] to : valid_moves_no_test(board)) {
            if (test_move(board, to[0], to[1])) {
                legal.add(new int[]{to[0], to[1]});
            }
        }

        final Chess.Player me  = this.color;
        final Chess.Player opp = (me == Chess.Player.white) ? Chess.Player.black : Chess.Player.white;
        boolean inCheckNow = Chess.squareAttacked(board, (char)('a' + kc), 8 - kr, opp);
            
        //no castling if in check now//
        if (inCheckNow) {
                return legal; 
            }
            boolean kingOnStart = (color == Player.white && kr == 7 && kc == 4) || (color == Player.black && kr == 0 && kc == 4);
        
            if (kingOnStart && !this.moved) {
                //kingside castling//
                if (pathEmpty(board, kr, kc+1, kc+2)) {

                Piece rook = board[kr][7];

                if (rook instanceof Rook && rook.color == this.color && !((Rook)rook).moved) {
                    if (test_move(board, kr, kc+1) && test_move(board, kr, kc+2)) {
                        
                        legal.add(new int[]{kr, kc+2});
                    }
                }
            }
            //queenside castling//
            if (pathEmpty(board, kr, kc-1, kc-2, kc-3)) {

                Piece rook = board[kr][0];
                
                if (rook instanceof Rook && rook.color == this.color && !((Rook)rook).moved) {
                    if (test_move(board, kr, kc-1) && test_move(board, kr, kc-2)) {
                        
                        legal.add(new int[]{kr, kc-2});
                    }
                }
            }
        }

        return legal;
    }

    private static boolean inside(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    private static boolean pathEmpty(Piece[][] board, int row, int... cols) {
        for (int c : cols) {
            
            if (c < 0 || c >= 8) return false;
            if (board[row][c] != null) return false;
        }
        
        return true;
    }
}
