package chess;

import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(ReturnPiece.PieceFile file, int rank, boolean isWhite) {
        super(isWhite ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR, file, rank);
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

    private void scanRay(Piece[][] board, int r, int c, int dr, int dc, ArrayList<String> out) {
        boolean meIsWhite = isWhite(this.retpiece.pieceType);
        int nr = r + dr, nc = c + dc;

        while (0 <= nr && nr < 8 && 0 <= nc && nc < 8) {
            Piece at = board[nr][nc];
            if (at == null) {
                out.add(square(nc, nr));
            } else {
                boolean themIsWhite = isWhite(at.retpiece.pieceType);
                if (themIsWhite != meIsWhite) out.add(square(nc, nr));
                break; // blocked either way
            }
            nr += dr; nc += dc;
        }
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
