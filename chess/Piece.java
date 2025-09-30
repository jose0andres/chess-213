package chess;

import chess.ReturnPiece.*;
import chess.Chess.Player;
import java.util.ArrayList;

public abstract class Piece 
{
    ReturnPiece retpiece;  

    public Piece(PieceType type, PieceFile file, int rank)
    {
        retpiece = new ReturnPiece();
        retpiece.pieceType = type;
        retpiece.pieceFile = file;
        retpiece.pieceRank = rank;
    }
    
    // returns locations the piece can move to (ex. "e4", "h6")
    public abstract ArrayList<String> valid_moves(Piece[][] board); 



    //Does NOT check if the move is valid
    public void move(Piece[][] board, String newPos)
    {
        int x = this.get_col();
        int y = this.get_row();

        board[x][y] = null;

        retpiece.pieceRank = Integer.parseInt("" + newPos.charAt(1));

        switch(newPos.charAt(0))
        {
            case 'a':
                retpiece.pieceFile = PieceFile.a;
                break;
            case 'b':
                retpiece.pieceFile = PieceFile.b;
                break;
            case 'c':
                retpiece.pieceFile = PieceFile.c;
                break;
            case 'd':
                retpiece.pieceFile = PieceFile.d;
                break;
            case 'e':
                retpiece.pieceFile = PieceFile.e;
                break;
            case 'f':
                retpiece.pieceFile = PieceFile.f;
                break;
            case 'g':
                retpiece.pieceFile = PieceFile.g;
                break;
            case 'h':
                retpiece.pieceFile = PieceFile.h;
                break;
        }

        x = this.get_col();
        y = this.get_row();

        board[x][y] = this;
    }

    public ReturnPiece get_piece()
    {
        return retpiece;
    }

    public PieceType get_type()
    {
        return retpiece.pieceType;
    }

    public Player get_color()
    {
        switch(retpiece.pieceType)
        {
            //WP, WR, WN, WB, WQ, WK,  BP, BR, BN, BB, BK, BQ
            case WP:
            case WR:
            case WN:
            case WB:
            case WQ:
            case WK:
                return Player.white;
            case BP:
            case BR:
            case BN:
            case BB:
            case BQ:
            case BK:
                return Player.black;
            default:
                return null;         
        }
    }

    // get 2d-arr coordinates from retpiece
    public int get_row()
    {
        return 8 - retpiece.pieceRank;
    }

    public int get_col()
    {
        return retpiece.pieceFile.ordinal();
    }

    public int get_rank()
    {
        return retpiece.pieceRank;
    }

    public PieceFile get_file()
    {
        return retpiece.pieceFile;
    }

    //converts from coordinate to PieceFile/rank accordingly
    public static PieceFile int_to_file(int x)
    {
        switch(x)
        {
            case 0: return PieceFile.a;
            case 1: return PieceFile.b;
            case 2: return PieceFile.c;
            case 3: return PieceFile.d;
            case 4: return PieceFile.e;
            case 5: return PieceFile.f;
            case 6: return PieceFile.g;
            case 7: return PieceFile.h;
            default: return null;
        }
    }

    public static int int_to_rank(int x)
    {
        return 8 - x;
    }
}
