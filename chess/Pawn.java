package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceType;

public class Pawn extends Piece {
    //to track if pawn just moved two squares forward//
    public boolean double_moved = false;

    public Pawn(Player color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public PieceType getType() {
        return (color == Player.white) ? PieceType.WP : PieceType.BP;
    }

    //normal pawn moves//
    @Override
    public ArrayList<int[]> valid_moves_no_test(Piece[][] board) {
        ArrayList<int[]> moves = new ArrayList<>();

        final int r = this.row;
        final int c = this.col;
        final boolean isWhite = (this.color == Player.white);
        final int dir = isWhite ? -1 : +1; 
        int r1 = r + dir;
        if (inside(r1, c) && board[r1][c] == null) {
            moves.add(new int[]{r1, c});

            //can take two tiles up if on start//
            boolean onStart = (isWhite && r == 6) || (!isWhite && r == 1);
            int r2 = r + 2 * dir;

            if (onStart && inside(r2, c) && board[r2][c] == null) {
                moves.add(new int[]{r2, c});
            }
        }

        // Diagonal captures (normal)//
        for (int dc : new int[]{-1, +1}) {

            int nc = c + dc;
            int nr = r + dir;
            if (!inside(nr, nc)) continue;
            Piece at = board[nr][nc];

            if (at != null && at.color != this.color) {
                moves.add(new int[]{nr, nc});
            }
        }
        addEnPassantMoves(board, moves);
        return moves;
    }

    @Override
    public ArrayList<int[]> valid_moves(Piece[][] board) {
        ArrayList<int[]> legal = new ArrayList<>();
        for (int[] to : valid_moves_no_test(board)) {
            if (test_move(board, to[0], to[1])) {
                legal.add(new int[]{to[0], to[1]});
            }
        }
        return legal;
    }

    //helper for en passant game logic//
    private void addEnPassantMoves(Piece[][] board, ArrayList<int[]> out) {
        final int r = this.row;
        final int c = this.col;
        final boolean isWhite = (this.color == Player.white);
        //necessary rank for en passant//
        if ((isWhite && r != 3) || (!isWhite && r != 4)) return;

        final int dir = isWhite ? -1 : +1;

        for (int d : new int[]{-1, +1}) {
            int adjC = c + d;
            if (!inside(r, adjC)) continue;

            Piece adj = board[r][adjC];

            if (adj instanceof Pawn && adj.color != this.color) {
                Pawn enemy = (Pawn) adj;

                if (enemy.double_moved) {
                    int tr = r + dir;
                    
                    if (inside(tr, adjC) && board[tr][adjC] == null) {
                        out.add(new int[]{tr, adjC});
                    }
                }
            }
        }
    }


    private static boolean inside(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
