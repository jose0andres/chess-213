package chess;

import java.util.ArrayList;

import chess.ReturnPiece.*;
import chess.Chess.Player;

public class Queen extends Piece
{

    public Queen(Player color, PieceFile file, int rank)
    {
        super(type(color), file, rank);
    }

    private static PieceType type(Player color)
    {
        if(color == Player.black) return PieceType.BQ;
        else return PieceType.WQ;
    }

    public ArrayList<String> valid_moves(Piece[][] board) 
    {
        int x = this.get_col(); //column, file
        int y = this.get_row(); //row, rank

        ArrayList<String> arr = new ArrayList<String>();

        int c = 1;
        boolean straight = true, ldiag = true, rdiag = true; 
        for(int r = y - 1; r >= 0 && (straight || ldiag || rdiag); r--)
        {
            //Straight up 
            if(straight)
            {
                if(board[x][r] == null) arr.add(Piece.int_to_file(x) + "" + Piece.int_to_rank(r));
                else
                {
                    if(board[x][r].get_color() != this.get_color()) arr.add(Piece.int_to_file(x) + "" + Piece.int_to_rank(r));
                    straight = false;
                }
            }

            //Left, Up Diag
            if(ldiag)
            {
                if(x - c >= 0 && board[x-c][r] == null) arr.add(Piece.int_to_file(x-c) + "" + Piece.int_to_rank(r));
                else if(x - c >= 0)
                {
                    if(board[x-c][r].get_color() != this.get_color()) arr.add(Piece.int_to_file(x-c) + "" + Piece.int_to_rank(r));
                    ldiag = false;
                }
            }

            //Right, Up Diag
            if(rdiag)
            {
                if(x + c < 8 && board[x+c][r] == null) arr.add(Piece.int_to_file(x+c) + "" + Piece.int_to_rank(r));
                else if(x + c < 8)
                {
                    if(board[x+c][r].get_color() != this.get_color()) arr.add(Piece.int_to_file(x+c) + "" + Piece.int_to_rank(r));
                    rdiag = false;
                }
            }

            c++;
        }

        c = 1;
        straight = true; ldiag = true; rdiag = true; 
        for(int r = y + 1; r < 8 && (straight || ldiag || rdiag); r++)
        {
            //Straight down 
            if(straight)
            {
                if(board[x][r] == null) arr.add(Piece.int_to_file(x) + "" + Piece.int_to_rank(r));
                else
                {
                    if(board[x][r].get_color() != this.get_color()) arr.add(Piece.int_to_file(x) + "" + Piece.int_to_rank(r));
                    straight = false;
                }
            }

            //Left, Down Diag
            if(ldiag)
            {
                if(x - c >= 0 && board[x-c][r] == null) arr.add(Piece.int_to_file(x-c) + "" + Piece.int_to_rank(r));
                else if(x - c >= 0)
                {
                    if(board[x-c][r].get_color() != this.get_color()) arr.add(Piece.int_to_file(x-c) + "" + Piece.int_to_rank(r));
                    ldiag = false;
                }
            }

            //Right, Down Diag
            if(rdiag)
            {
                if(x + c < 8 && board[x+c][r] == null) arr.add(Piece.int_to_file(x+c) + "" + Piece.int_to_rank(r));
                else if(x + c < 8)
                {
                    if(board[x+c][r].get_color() != this.get_color()) arr.add(Piece.int_to_file(x+c) + "" + Piece.int_to_rank(r));
                    rdiag = false;
                }
            }

            c++;
        }

        for(c = x - 1; c >= 0; c--) //left
        {
            if(board[c][y] == null) arr.add(Piece.int_to_file(c) + "" + Piece.int_to_rank(y));
            else 
            {
                if(board[c][y].get_color() != this.get_color()) arr.add(Piece.int_to_file(c) + "" + Piece.int_to_rank(y));
                break;
            }
        }

        for(c = x + 1; c < 8; c++) //right
        {
            if(board[c][y] == null) arr.add(Piece.int_to_file(c) + "" + Piece.int_to_rank(y));
            else 
            {
                if(board[c][y].get_color() != this.get_color()) arr.add(Piece.int_to_file(c) + "" + Piece.int_to_rank(y));
                break;
            }
        }

        return arr;
    } 

}
