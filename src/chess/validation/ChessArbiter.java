package chess.validation;

import chess.ChessBoard;
import chess.ChessDoer;
import chess.enums.ChessPieceColor;
import chess.enums.ChessType;
import chess.navigation.BoundVector;
import chess.navigation.Position;

import java.util.Arrays;

import static chess.validation.ChessArbiterHelper.isLooselyLegalMove;
import static chess.validation.ChessArbiterHelper.isPositionUnderAttack;

public interface ChessArbiter {

    /**
     * This method checks whether the side that can now move has king in check
     *
     * @param chessBoard current chessboard
     * @return true if king is in check, otherwise false
     */
    static boolean isCurrentKingInCheck(ChessBoard chessBoard) {
        ChessPieceColor color = chessBoard.getCurrentColor();
        Position kingPosition = chessBoard.getChessPiecePosition(ChessType.KING, color);
        return isPositionUnderAttack(chessBoard, kingPosition, color.oppositeColor());
    }

    /**
     * This method checks whether the side opposite to the side that can now move has king in check
     *
     * @param chessBoard current chessboard
     * @return true if king is in check, otherwise false
     */
    private static boolean isOppositeKingInCheck(ChessBoard chessBoard) {
        ChessPieceColor color = chessBoard.getCurrentColor().oppositeColor();
        Position kingPosition = chessBoard.getChessPiecePosition(ChessType.KING, color);
        return isPositionUnderAttack(chessBoard, kingPosition, color.oppositeColor());
    }

    /**
     * The method checks whether a move represented by boundVector can be executed.
     * The method takes into account the way a specific chesspiece is moving,
     * whether color of the current chesspiece has its turn
     * and whether making that move would expose the king of the current color to enemy attacks
     * or that it wouldn't protect the king from check if he is currently in one
     *
     * @param chessBoard  current chessboard
     * @param boundVector representation of the move
     * @return true if the move is legal, otherwise not
     */
    static boolean isStrictlyLegalMove(ChessBoard chessBoard, BoundVector boundVector) {
        if (!isLooselyLegalMove(chessBoard, boundVector, false)) {
            return false;
        }
        //this code checks if both white chess and black chess move on the turn they should
        if (chessBoard.getCurrentColor() != chessBoard.getElement(boundVector.origin()).piece().color()) {
            return false;
        }
        ChessBoard test = ChessDoer.makeStandardChessMove(chessBoard, boundVector, () -> ChessType.QUEEN);
        return !isOppositeKingInCheck(test);
    }

    /**
     * The method returns array of bound vectors that represent
     * all the moves that can be made from specified position according to the method isStrictlyLegalMove
     * If there are no legal moves that can be made from this position the method returns an empty array
     *
     * @param chessBoard current chessboard
     * @param origin     the position where the move starts
     * @return all the moves allowed from origin
     */
    private static BoundVector[] strictlyLegalMoves(ChessBoard chessBoard, Position origin) {
        Position[] positions = chessBoard.getAllValidPositions();
        BoundVector[] moves = new BoundVector[positions.length];
        BoundVector boundVector;
        int count = 0;
        for (Position destination : positions) {
            boundVector = new BoundVector(origin, destination);
            if (isStrictlyLegalMove(chessBoard, boundVector)) {
                moves[count] = boundVector;
                count++;
            }
        }
        return Arrays.copyOf(moves, count);
    }

    /**
     * The method checks whether the current player has any legal moves
     * according to the method isStrictlyLegalMove
     *
     * @param chessBoard current chessboard
     * @return true if the player has 0 legal moves, otherwise false
     */
    static boolean currentPlayerHasNoStrictlyLegalMoves(ChessBoard chessBoard) {
        int possibleMoves = 0;
        Position[] chessPiecePositions = chessBoard.getChessPiecePositionsOfColor(chessBoard.getCurrentColor());
        for (Position chessPiecePosition : chessPiecePositions) {
            possibleMoves += strictlyLegalMoves(chessBoard, chessPiecePosition).length;
        }
        return possibleMoves == 0;
    }
}
