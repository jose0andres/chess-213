package chess;

import chess.ReturnPiece.*;

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
    public abstract String[] valid_moves(Piece[][] board); 

    public void move(Piece[][] board, String newPos)
    {
        int x = get_x(retpiece);
        int y = get_y(retpiece);

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

        x = get_x(retpiece);
        y = get_y(retpiece);

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

    // get 2d-arr coordinates from retpiece
    public static int get_x(ReturnPiece piece)
    {
        return 8 - piece.pieceRank;
    }

    public static int get_y(ReturnPiece piece)
    {
        return piece.pieceFile.ordinal();
    }
}
