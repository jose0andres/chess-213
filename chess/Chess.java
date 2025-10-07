// Simon Kowerski 	sk2790	
// Jose Lamela		jal601

package chess;

import java.util.ArrayList;
import chess.ReturnPiece.*;
import chess.Piece;


public class Chess 
{

    enum Player { white, black }

	private static Piece[][] board = new Piece[8][8];
	private static Player currentPlayer;
    
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) // can assume move properly formatted, may have leading/trailing whitespace
	{
		ReturnPlay ret = new ReturnPlay();

		move = move.strip();
		PieceFile file = to_file(move.charAt(0));
		int rank = Integer.parseInt(move.charAt(1) + "");

		int row = 8 - rank;
		int col = file.ordinal();

		Piece toMove = board[row][col];

		if(toMove.get_color() != currentPlayer) return build_return(ReturnPlay.Message.ILLEGAL_MOVE);

		ArrayList<String> valid = new ArrayList<String>();
		valid = toMove.valid_moves(board);

		if(!valid.contains(move.substring(3))) return build_return(ReturnPlay.Message.ILLEGAL_MOVE);

		toMove.move(board, move.substring(3));

		// other conditions - check, checkmate, etc
		
		return build_return(null);
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() 
	{
		currentPlayer = Player.white;

		//pawns and empty squares
		for(int r = 0; r < 8; r++)
		{
			for(int f = 0; f < 8; f++)
			{
				if(r == 1) board[r][f] = new Pawn(Player.black, Piece.int_to_file(f), Piece.int_to_rank(r)); // black pawns
				else if(r == 6) board[r][f] = new Pawn(Player.white, Piece.int_to_file(f), Piece.int_to_rank(r)); // white pawns
				else if(r > 1 && r < 6) board[r][f] = null;
			}
		}

		//black pieces
		board[0][0] = new Rook(Player.black, PieceFile.a, 8);
		board[0][1] = new Knight(Player.black, PieceFile.b, 8);
		board[0][2] = new Bishop(Player.black, PieceFile.c, 8);
		board[0][3] = new Queen(Player.black, PieceFile.d, 8);
		board[0][4] = new King(Player.black, PieceFile.e, 8);
		board[0][5] = new Bishop(Player.black, PieceFile.f, 8);
		board[0][6] = new Knight(Player.black, PieceFile.g, 8);
		board[0][7] = new Rook(Player.black, PieceFile.h, 8);

		//white pieces
		board[7][0] = new Rook(Player.white, PieceFile.a, 1);
		board[7][1] = new Knight(Player.white, PieceFile.b, 1);
		board[7][2] = new Bishop(Player.white, PieceFile.c, 1);
		board[7][3] = new Queen(Player.white, PieceFile.d, 1);
		board[7][4] = new King(Player.white, PieceFile.e, 1);
		board[7][5] = new Bishop(Player.white, PieceFile.f, 1);
		board[7][6] = new Knight(Player.white, PieceFile.g, 1);
		board[7][7] = new Rook(Player.white, PieceFile.h, 1);
	}

	public static void next_player()
	{
		if(currentPlayer == Player.black) currentPlayer = Player.white;
		else currentPlayer = Player.black;
	}

	public static PieceFile to_file(char c)
	{
		switch(c)
		{
			case 'a': 
				return PieceFile.a;
			case 'b': 
				return PieceFile.b;
			case 'c': 
				return PieceFile.c;
			case 'd': 
				return PieceFile.d;
			case 'e': 
				return PieceFile.e;
			case 'f': 
				return PieceFile.f;
			case 'g': 
				return PieceFile.g;
			case 'h': 
				return PieceFile.h;
		}

		return null;
	}

	public static ReturnPlay build_return(ReturnPlay.Message message)
	{

		next_player();
		return null;
	}
}
