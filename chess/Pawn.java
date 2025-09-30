package chess;

import java.util.ArrayList;

import chess.ReturnPiece.*;
import chess.Chess.Player;

public class Pawn extends Piece 
{
    private boolean moved = false;
    public boolean double_moved = false; //for en passant

    public Pawn(Player color, PieceFile file, int rank)
    {
        super(type(color), file, rank);
    }

    private static PieceType type(Player color)
    {
        if(color == Player.black) return PieceType.BP;
        else return PieceType.WP;
    }

    public void move(Piece[][] board, String newPos)
    {

        int x = super.get_col(); //column, file
        int y = super.get_row(); //row, rank

        super.move(board, newPos);
        if(!moved) moved = true; 

        int newx = super.get_col();

        int direction;
        if(this.get_color() == Player.black) direction = 1; // black
        else direction = -1; // white

        //if double move was made
        if(Integer.parseInt(newPos.charAt(1) + "") == y + (2 * direction)) double_moved = true;
        else double_moved = false;

        //if en passant was made
        if(newx != x)
        {
            if(x - 1 >= 0 && board[x-1][y] != null && (board[x-1][y].get_type() == PieceType.WP || board[x-1][y].get_type() == PieceType.BP))
            {
                if(((Pawn) board[x-1][y]).double_moved) board[x-1][y] = null;
            }

            if(x + 1 < 8 && board[x+1][y] != null &&  (board[x-1][y].get_type() == PieceType.WP || board[x-1][y].get_type() == PieceType.BP))
            {
                if(((Pawn) board[x+1][y]).double_moved) board[x-1][y] = null;
            }
        }

        //promotion
        if(this.get_color() == Player.black && super.get_rank() == 8) board[super.get_col()][super.get_row()] = new Queen(Player.black, super.get_file(), super.get_rank());
        if(this.get_color() == Player.white && super.get_rank() == 1) board[super.get_col()][super.get_row()] = new Queen(Player.white, super.get_file(), super.get_rank());
    }

    public ArrayList<String> valid_moves(Piece[][] board)
    {
        ArrayList<String> arr = new ArrayList<String>();

        int direction;
        int x = super.get_col(); //column, file
        int y = super.get_row(); //row, rank

        if(this.get_color() == Player.black) //black
        {
            direction = 1;
            if(y == 4) //en passant
            {
                if(x - 1 >= 0 && board[x-1][y] != null && board[x-1][y].get_type() == PieceType.WP)
                {
                    if(((Pawn) board[x-1][y]).double_moved) arr.add(Piece.int_to_file(x-1) + "" + Piece.int_to_rank(y+direction));
                }

                if(x + 1 < 8 && board[x+1][y] != null && board[x+1][y].get_type() == PieceType.WP)
                {
                    if(((Pawn) board[x+1][y]).double_moved) arr.add(Piece.int_to_file(x+1) + "" + Piece.int_to_rank(y+direction));
                }
            }
        }

        else //white 
        {
            direction = -1;

            if(y == 5) //en passant
            {
                if(x - 1 >= 0 && board[x-1][y] != null && board[x-1][y].get_type() == PieceType.BP)
                {
                    if(((Pawn) board[x-1][y]).double_moved) arr.add(Piece.int_to_file(x-1) + "" + Piece.int_to_rank(y+direction));
                }

                if(x + 1 < 8 && board[x+1][y] != null && board[x+1][y].get_type() == PieceType.BP)
                {
                    if(((Pawn) board[x+1][y]).double_moved) arr.add(Piece.int_to_file(x+1) + "" + Piece.int_to_rank(y+direction));
                }
            }
        }

        //move one or two (if not moved) squares forward
        if(board[x][y+direction] == null) arr.add(Piece.int_to_file(x) + "" + Piece.int_to_rank(y+direction)); 
        if(!moved && board[x][y+direction] == null && board[x][y+(2*direction)] == null) arr.add(Piece.int_to_file(x) + "" + Piece.int_to_rank(y+(2*direction))); 
        
        //capture diagonally
        if(x - 1 >= 0 && board[x-1][y+direction] != null && board[x-1][y+direction].get_color() != this.get_color()) arr.add(Piece.int_to_file(x-1) + "" + Piece.int_to_rank(y+direction));
        if(x + 1 < 8 && board[x+1][y+direction] != null && board[x+1][y+direction].get_color() != this.get_color()) arr.add(Piece.int_to_file(x+1) + "" + Piece.int_to_rank(y+direction));
        
        return arr;
    }
}