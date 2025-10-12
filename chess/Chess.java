// Simon Kowerski  sk2790
// Jose Lamela     jal601

package chess;

import java.util.ArrayList;
import chess.ReturnPiece.*;
import chess.Piece;

public class Chess {

    enum Player { white, black }

    /** 8x8 board where row 0 = rank 8, row 7 = rank 1; col 0 = file a, col 7 = file h */
    private static Piece[][] board = new Piece[8][8];
    private static Player     turn  = Player.white;
	
	//reset play//
    public static void start() {
        board = new Piece[8][8];
        turn  = Player.white;

        // --- White back rank (row 7 == rank 1) ---
        board[7][0] = new Rook  (Player.white, 7, 0);
        board[7][1] = new Knight(Player.white, 7, 1);
        board[7][2] = new Bishop(Player.white, 7, 2);
        board[7][3] = new Queen (Player.white, 7, 3);
        board[7][4] = new King  (Player.white, 7, 4);
        board[7][5] = new Bishop(Player.white, 7, 5);
        board[7][6] = new Knight(Player.white, 7, 6);
        board[7][7] = new Rook  (Player.white, 7, 7);

        // White pawns (row 6 == rank 2)
        for (int c = 0; c < 8; c++) {
            board[6][c] = new Pawn(Player.white, 6, c);
        }

        // --- Black back rank (row 0 == rank 8) ---
        board[0][0] = new Rook  (Player.black, 0, 0);
        board[0][1] = new Knight(Player.black, 0, 1);
        board[0][2] = new Bishop(Player.black, 0, 2);
        board[0][3] = new Queen (Player.black, 0, 3);
        board[0][4] = new King  (Player.black, 0, 4);
        board[0][5] = new Bishop(Player.black, 0, 5);
        board[0][6] = new Knight(Player.black, 0, 6);
        board[0][7] = new Rook  (Player.black, 0, 7);

        // Black pawns (row 1 == rank 7)
        for (int c = 0; c < 8; c++) {
            board[1][c] = new Pawn(Player.black, 1, c);
        }
    }

    /** Execute a move string like "e2 e4", "e2 e4 draw?", or "resign". */
    public static ReturnPlay play(String move) {
        if (move == null) return build_return(ReturnPlay.Message.ILLEGAL_MOVE);
        move = move.trim();

        // Resign
        if (move.equalsIgnoreCase("resign")) {
            return build_return(turn == Player.white
                    ? ReturnPlay.Message.RESIGN_BLACK_WINS
                    : ReturnPlay.Message.RESIGN_WHITE_WINS);
        }

        // Draw offer
        boolean drawOffer = move.endsWith(" draw?");
        if (drawOffer) move = move.substring(0, move.length() - 6).trim();

        String[] toks = move.split("\\s+");
        if (toks.length < 2) return build_return(ReturnPlay.Message.ILLEGAL_MOVE);

        int[] from = parseSquare(toks[0]);
        int[] to   = parseSquare(toks[1]);
        if (from == null || to == null) return build_return(ReturnPlay.Message.ILLEGAL_MOVE);

        int sr = from[0], sc = from[1];
        int dr = to[0],   dc = to[1];

        // Validate a piece exists and belongs to the side to move
        Piece moving = board[sr][sc];
        if (moving == null) return build_return(ReturnPlay.Message.ILLEGAL_MOVE);
        if ((turn == Player.white && moving.color != Player.white) ||
            (turn == Player.black && moving.color != Player.black)) {
            return build_return(ReturnPlay.Message.ILLEGAL_MOVE);
        }

        // Ensure destination is among legal moves (ArrayList<int[]>)
        ArrayList<int[]> legal = moving.valid_moves(board);
        boolean ok = false;
        for (int[] mv : legal) {
            if (mv[0] == dr && mv[1] == dc) { ok = true; break; }
        }
        if (!ok) return build_return(ReturnPlay.Message.ILLEGAL_MOVE);

		//en passant// 
        if (moving instanceof Pawn) {

            boolean diagonal = Math.abs(dc - sc) == 1;
            boolean forward  = (moving.color == Player.white) ? (dr == sr - 1) : (dr == sr + 1);
            boolean targetEmpty = (board[dr][dc] == null);
            	
			if (diagonal && forward && targetEmpty) {
                int capRow = (moving.color == Player.white) ? dr + 1 : dr - 1;
                
				if (capRow >= 0 && capRow < 8) {
                    board[capRow][dc] = null;
                }
            }
        }

        board[dr][dc] = moving;
        board[sr][sc] = null;
        // Keep piece's coordinates in sync (Piece exposes row/col)
        moving.row = dr;
        moving.col = dc;

		//castling logic//
        if (moving instanceof King) {
            King k = (King) moving;

            if (dc - sc == 2) {
                Piece rook = board[dr][7];
                board[dr][5] = rook;
                board[dr][7] = null;

                if (rook != null) {
                    rook.row = dr; rook.col = 5;
                    
					if (rook instanceof Rook) ((Rook) rook).moved = true;
                }
            }

            if (sc - dc == 2) {
                Piece rook = board[dr][0];
                board[dr][3] = rook;
                board[dr][0] = null;

                if (rook != null) {
                    rook.row = dr; rook.col = 3;

                    if (rook instanceof Rook) ((Rook) rook).moved = true;
                }
            }
            k.moved = true;
        } else if (moving instanceof Rook) {
            ((Rook) moving).moved = true;
        }

		//pawn promotion//
        if (moving instanceof Pawn) {
            boolean promote = (moving.color == Player.white && dr == 0)
                           || (moving.color == Player.black && dr == 7);
            if (promote) {
                board[dr][dc] = new Queen(moving.color, dr, dc); // Queen(Player,row,col)
            }
        }

		//flags for en passsant//
        boolean setEp = false;
        if (moving instanceof Pawn) {

            if (Math.abs(dr - sr) == 2) setEp = true;
        }
        clearAllEnPassantFlags();

        if (setEp && (board[dr][dc] instanceof Pawn)) {
            ((Pawn) board[dr][dc]).double_moved = true;
        }

		//being in check logic//
        Player opp = opponentOf(turn);
        boolean oppInCheck = inCheck(board, opp);

        ReturnPlay.Message msg = null;
        if (drawOffer) {

            msg = ReturnPlay.Message.DRAW;
        } else if (oppInCheck) {
            boolean any = sideHasAnyLegalMove(opp);
            
			if (!any) {
                msg = (turn == Player.white)
                        ? ReturnPlay.Message.CHECKMATE_WHITE_WINS
                        : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
            } else {
                msg = ReturnPlay.Message.CHECK;
            }
        }

        next_player();
        return build_return(msg);
    }

	//return object//
    public static ReturnPlay build_return(ReturnPlay.Message message) {
        ReturnPlay rp = new ReturnPlay();
        rp.message = message;
        rp.piecesOnBoard = new ArrayList<>();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                
				if (p != null) {
                    ReturnPiece rpce = new ReturnPiece();
                    rpce.pieceType = p.getType();
                    rpce.pieceFile = file_from_col(p.col);
                    rpce.pieceRank = 8 - p.row;
                    rp.piecesOnBoard.add(rpce);
                }
            }
        }
        return rp;
    }

	//helper functions//
    private static void next_player() {
        turn = (turn == Player.white) ? Player.black : Player.white;
    }

    private static Player opponentOf(Player p) {
        return (p == Player.white) ? Player.black : Player.white;
    }

    private static void clearAllEnPassantFlags() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] instanceof Pawn) {
                    ((Pawn) board[r][c]).double_moved = false;
                }
            }
        }
    }

    /** Convert file column index (0..7) to PieceFile enum (a..h). */
    private static PieceFile file_from_col(int col) {
        switch (col) {
            case 0: return PieceFile.a;
            case 1: return PieceFile.b;
            case 2: return PieceFile.c;
            case 3: return PieceFile.d;
            case 4: return PieceFile.e;
            case 5: return PieceFile.f;
            case 6: return PieceFile.g;
            case 7: return PieceFile.h;
            default: throw new IllegalArgumentException("col out of range: " + col);
        }
    }

    /** Parse algebraic like "e2" → {row,col} where row 0=rank8, col 0=file a. */
    private static int[] parseSquare(String s) {
        if (s == null || s.length() != 2) return null;
        char f = s.charAt(0);
        char r = s.charAt(1);
        if (f < 'a' || f > 'h') return null;
        if (r < '1' || r > '8') return null;
        int col = f - 'a';
        int row = 8 - (r - '0');   // '8'->0, '1'->7
        return new int[]{row, col};
    }

    /** True if square (file,rank) is attacked by byColor. */
    static boolean squareAttacked(Piece[][] b, char file, int rank, Player byColor) {
        int col = file - 'a';
        int row = 8 - rank;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = b[r][c];
                if (p == null || p.color != byColor) continue;

                if (p instanceof Pawn) {
                    int dir = (byColor == Player.white) ? -1 : +1;
                    int ar = r + dir;
                    int ac1 = c - 1, ac2 = c + 1;
                    if (ar == row && (ac1 == col || ac2 == col)) return true;
                } else if (p instanceof King) {
                    // 8 neighboring squares (do NOT call king move gen here)
                    int[] DR = {-1,-1,-1, 0, 0, 1, 1, 1};
                    int[] DC = {-1, 0, 1,-1, 1,-1, 0, 1};
                    for (int i = 0; i < 8; i++) {
                        int nr = r + DR[i], nc = c + DC[i];
                        if (nr == row && nc == col) return true;
                    }
                } else {
                    // Other pieces: use their geometric moves without self-check filtering
                    for (int[] mv : p.valid_moves_no_test(b)) {
                        if (mv[0] == row && mv[1] == col) return true;
                    }
                }
            }
        }
        return false;
    }

    /** True if the given side's king is in check. */
    static boolean inCheck(Piece[][] b, Player side) {
        int kr = -1, kc = -1;

        // Locate the king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = b[r][c];
                if (p == null || p.color != side) continue;
                PieceType t = p.getType();
                if ((side == Player.white && t == PieceType.WK) ||
                    (side == Player.black && t == PieceType.BK)) {
                    kr = r; kc = c;
                }
            }
        }
        if (kr < 0) return false; // should not happen

        char kf = (char) ('a' + kc);
        int  rk = 8 - kr;
        return squareAttacked(b, kf, rk, opponentOf(side));
    }

    /** True if the given side has at least one legal move. */
    private static boolean sideHasAnyLegalMove(Player side) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null || p.color != side) continue;
                ArrayList<int[]> mv = p.valid_moves(board);
                if (!mv.isEmpty()) return true;
            }
        }
        return false;
    }
}
