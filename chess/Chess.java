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
		/* FILL IN THIS METHOD */
		
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		return null;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() 
	{
		currentPlayer = Player.white;

		for(int r = 0; r < 8; r++)
		{
			for(int f = 0; f < 8; f++)
			{
				if(r == 1) board[r][f] = new Pawn(Player.black, Piece.int_to_file(f), Piece.int_to_rank(r)); // black pawns
				else if(r == 6) board[r][f] = new Pawn(Player.white, Piece.int_to_file(f), Piece.int_to_rank(r)); // white pawns
				else if(r > 1 && r < 6) board[r][f] = null;
			}
		}
	}
}
