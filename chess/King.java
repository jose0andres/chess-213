package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class King extends Piece
{

    boolean moved = false; //for castling

    public King(Player color, PieceFile file, int rank)
    {
        super(type(color), file, rank);
    }

    private static PieceType type(Player color)
    {
        if(color == Player.black) return PieceType.BK;
        else return PieceType.WK;
    }

    @Override 
    public void move(Piece[][] board, String newPos)
    {
        int x = super.get_col();
        int y = super.get_row();
        
        super.move(board, newPos);

        int newx = super.get_col();

        if(!moved)
        {
            moved = true;

            // move rook while castling 
            if(super.get_color() == Player.black)
            {
                if(newx == (x + 2)) board[y][x+3].move(board, "f8"); // black king side castle
                else if(newx == (x - 3)) board[y][x-4].move(board, "c8"); // black queen side castle
            }

            else 
            {
                if(newx == (x + 2)) board[y][x+3].move(board, "f1"); // white king side castle
                else if(newx == (x - 3)) board[y][x-4].move(board, "c1"); // white queen side castle
            }

        } 
    }

    public boolean in_check(Piece[][] board)
    {
        return false;
    }

    public ArrayList<String> valid_moves(Piece[][] board) 
    {
        int x = this.get_col(); //column, file
        int y = this.get_row(); //row, rank

        ArrayList<String> arr = new ArrayList<String>();

        //add surrounding squares
        for(int r = y - 1; r <= y + 1; r++)
        {
            for(int c = x - 1; c <= x + 1; c++)
            {
                if(r >= 0 && r < 8 && c >= 0 && c < 8)
                {
                    if(!(r == y && c == x)) //not the same spot
                    {
                        if(board[c][r] == null) arr.add(Piece.int_to_file(c) + "" + Piece.int_to_rank(r));
                        else if(board[c][r].get_color() != this.get_color()) arr.add(Piece.int_to_file(c) + "" + Piece.int_to_rank(r));
                    }
                }
            }
        }

        //castling
        if(!moved)
        {
            //king side
            if(board[x+3][y] != null && (board[x+3][y].get_type() == PieceType.BR || board[x+3][y].get_type() == PieceType.WR))
            {
                if(!((Rook) board[x+3][y]).moved)
                {
                    if(board[x+1][y] == null && board[x+2][y] == null) arr.add(Piece.int_to_file(x+2) + "" + Piece.int_to_rank(y));
                }
            }

            //queen side
            if(board[x-4][y] != null && (board[x-4][y].get_type() == PieceType.BR || board[x-4][y].get_type() == PieceType.WR))
            {
                if(!((Rook) board[x-4][y]).moved)
                {
                    if(board[x-1][y] == null && board[x-2][y] == null && board[x-3][y] == null) arr.add(Piece.int_to_file(x-2) + "" + Piece.int_to_rank(y));
                }
            }
        }

        //remove invalid moves that would cause check
        for(int i = 0; i < arr.size(); i++)
        {
            if(!this.test_move(board, arr.get(i))) 
            {
                arr.remove(i);
                i--;
            }
        }

        return arr;
    }

    public ArrayList<String> valid_moves_no_test(Piece[][] board) 
    {
        int x = this.get_col(); //column, file
        int y = this.get_row(); //row, rank

        ArrayList<String> arr = new ArrayList<String>();

        //add surrounding squares
        for(int r = y - 1; r <= y + 1; r++)
        {
            for(int c = x - 1; c <= x + 1; c++)
            {
                if(r >= 0 && r < 8 && c >= 0 && c < 8)
                {
                    if(!(r == y && c == x)) //not the same spot
                    {
                        if(board[c][r] == null) arr.add(Piece.int_to_file(c) + "" + Piece.int_to_rank(r));
                        else if(board[c][r].get_color() != this.get_color()) arr.add(Piece.int_to_file(c) + "" + Piece.int_to_rank(r));
                    }
                }
            }
        }

        //castling
        if(!moved)
        {
            //king side
            if(board[x+3][y] != null && (board[x+3][y].get_type() == PieceType.BR || board[x+3][y].get_type() == PieceType.WR))
            {
                if(!((Rook) board[x+3][y]).moved)
                {
                    if(board[x+1][y] == null && board[x+2][y] == null) arr.add(Piece.int_to_file(x+2) + "" + Piece.int_to_rank(y));
                }
            }

            //queen side
            if(board[x-4][y] != null && (board[x-4][y].get_type() == PieceType.BR || board[x-4][y].get_type() == PieceType.WR))
            {
                if(!((Rook) board[x-4][y]).moved)
                {
                    if(board[x-1][y] == null && board[x-2][y] == null && board[x-3][y] == null) arr.add(Piece.int_to_file(x-2) + "" + Piece.int_to_rank(y));
                }
            }
        }
        
        return arr;
    }
}
