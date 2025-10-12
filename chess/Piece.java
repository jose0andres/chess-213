package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;
import chess.Chess.Player;

public abstract class Piece {
    protected Player color;
    protected int row; // 0..7 (rank-1)
    protected int col; // 0..7 (file 'a'->0)
    protected boolean moved = false;

    public Piece(Player color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public Player get_color() { return color; }

    public abstract PieceType getType();

    public ReturnPiece toReturnPiece() {
        ReturnPiece rp = new ReturnPiece();
        rp.pieceType = getType();
        rp.pieceFile = PieceFile.values()[col];
        rp.pieceRank = row + 1;
        return rp;
    }

    public abstract ArrayList<int[]> valid_moves_no_test(Piece[][] board);

    /** simulate-and-filter self-check */
    public ArrayList<int[]> valid_moves(Piece[][] board) {
        ArrayList<int[]> res = new ArrayList<>();
        for (int[] mv : valid_moves_no_test(board)) {
            if (test_move(board, mv[0], mv[1])) {
                res.add(mv);
            }
        }
        return res;
    }

    /** return true if moving this piece to (r,c) does NOT leave own king in check */
    public boolean test_move(Piece[][] board, int r, int c) {
        // save
        Piece captured = board[r][c];
        int or = row, oc = col;
        board[or][oc] = null;
        board[r][c] = this;
        this.row = r; this.col = c;

        boolean ok = !Chess.inCheck(board, color);

        // undo
        this.row = or; this.col = oc;
        board[or][oc] = this;
        board[r][c] = captured;
        return ok;
    }

    protected static boolean inBounds(int r, int c) {
        return r>=0 && r<8 && c>=0 && c<8;
    }

    protected static void addRay(Piece[][] board, Player me, int r, int c, int dr, int dc, ArrayList<int[]> acc) {
        int nr=r+dr, nc=c+dc;
        while (inBounds(nr,nc)) {
            Piece p = board[nr][nc];
            if (p==null) {
                acc.add(new int[]{nr,nc});
            } else {
                if (p.color != me) acc.add(new int[]{nr,nc});
                break;
            }
            nr+=dr; nc+=dc;
        }
    }
}
