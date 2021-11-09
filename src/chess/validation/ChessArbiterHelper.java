package chess.validation;

import chess.ChessBoard;
import chess.element.ChessPiece;
import chess.element.ChessSlot;
import chess.enums.ChessPieceColor;
import chess.navigation.BoundVector;
import chess.navigation.FreeVector;
import chess.navigation.Position;

public interface ChessArbiterHelper {

    /**
     * The method checks if all tiles on a path described by boundVector are free from chesspieces
     *
     * @param chessBoard  chessboard the path is placed on
     * @param boundVector represents path
     * @return true if the path is empty, otherwise false
     */
    private static boolean isPathEmpty(ChessBoard chessBoard, BoundVector boundVector) {
        Position[] path = boundVector.getStandardPath();
        for (int i = 1; i < path.length - 1; i++) {
            if (chessBoard.getElement(path[i]).piece() != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks if given boundVector represents a valid pawn move given specified chessBoard
     * The method does not check if the chesspiece at the origin is null or if either origin or destination
     * of given boundVector are within bounds of the chessboard
     *
     * @param chessBoard        chessboard the piece moves on
     * @param boundVector       represents move
     * @param onlyAttackingMove if true the method ignores all non-attacking moves
     * @return true if the pawn move is legal otherwise false
     */
    private static boolean isLegalPawnMove(ChessBoard chessBoard, BoundVector boundVector, boolean onlyAttackingMove) {
        ChessSlot firstSlot = chessBoard.getElement(boundVector.origin());
        ChessSlot secondSlot = chessBoard.getElement(boundVector.destination());
        FreeVector freeVector = boundVector.getFreeVector();
        boolean correctDirection = switch (firstSlot.piece().color()) {
            case WHITE -> freeVector.y() > 0;
            case BLACK -> freeVector.y() < 0;
        };
        if (!correctDirection) {
            return false;
        }
        if (freeVector.isDiagonal() && freeVector.distance() == 1) {
            return secondSlot.piece() != null && secondSlot.piece().color() != firstSlot.piece().color();
        } else if (!onlyAttackingMove && freeVector.isOrthogonal() && freeVector.distance() == 2) {
            return !firstSlot.piece().wasMoved() && isPathEmpty(chessBoard, boundVector) && secondSlot.piece() == null;
        } else if (!onlyAttackingMove && freeVector.isOrthogonal() && freeVector.distance() == 1) {
            return secondSlot.piece() == null;
        } else {
            return false;
        }
    }

    /**
     * This method checks if given boundVector represents a valid king move given specified chessBoard
     * The method does not check if the chesspiece at the origin is null or if either origin or destination
     * of given boundVector are within bounds of the chessboard
     *
     * @param chessBoard  chessboard the piece moves on
     * @param boundVector represents move
     * @return true if the king move is legal otherwise false
     */
    private static boolean isLegalKingMove(ChessBoard chessBoard, BoundVector boundVector) {
        ChessSlot firstSlot = chessBoard.getElement(boundVector.origin());
        ChessSlot secondSlot = chessBoard.getElement(boundVector.destination());
        FreeVector freeVector = boundVector.getFreeVector();
        return (secondSlot.piece() == null || firstSlot.piece().color() != secondSlot.piece().color())
                && freeVector.distance() == 1;
    }

    /**
     * This method checks if given boundVector represents a valid knight move given specified chessBoard
     * The method does not check if the chesspiece at the origin is null or if either origin or destination
     * of given boundVector are within bounds of the chessboard
     *
     * @param chessBoard  chessboard the piece moves on
     * @param boundVector represents move
     * @return true if the knight move is legal otherwise false
     */
    private static boolean isLegalKnightMove(ChessBoard chessBoard, BoundVector boundVector) {
        ChessSlot firstSlot = chessBoard.getElement(boundVector.origin());
        ChessSlot secondSlot = chessBoard.getElement(boundVector.destination());
        FreeVector freeVector = boundVector.getFreeVector();
        return (secondSlot.piece() == null || firstSlot.piece().color() != secondSlot.piece().color())
                && freeVector.isLShaped();
    }


    /**
     * This method checks if given boundVector represents a valid rook move given specified chessBoard
     * The method does not check if the chesspiece at the origin is null or if either origin or destination
     * of given boundVector are within bounds of the chessboard
     *
     * @param chessBoard  chessboard the piece moves on
     * @param boundVector represents move
     * @return true if the rook move is legal otherwise false
     */
    private static boolean isLegalRookMove(ChessBoard chessBoard, BoundVector boundVector) {
        ChessSlot firstSlot = chessBoard.getElement(boundVector.origin());
        ChessSlot secondSlot = chessBoard.getElement(boundVector.destination());
        FreeVector freeVector = boundVector.getFreeVector();
        return (secondSlot.piece() == null || firstSlot.piece().color() != secondSlot.piece().color())
                && freeVector.isOrthogonal() && isPathEmpty(chessBoard, boundVector);
    }

    /**
     * This method checks if given boundVector represents a valid bishop move given specified chessBoard
     * The method does not check if the chesspiece at the origin is null or if either origin or destination
     * of given boundVector are within bounds of the chessboard
     *
     * @param chessBoard  chessboard the piece moves on
     * @param boundVector represents move
     * @return true if the bishop move is legal otherwise false
     */
    private static boolean isLegalBishopMove(ChessBoard chessBoard, BoundVector boundVector) {
        ChessSlot firstSlot = chessBoard.getElement(boundVector.origin());
        ChessSlot secondSlot = chessBoard.getElement(boundVector.destination());
        FreeVector freeVector = boundVector.getFreeVector();
        return (secondSlot.piece() == null || firstSlot.piece().color() != secondSlot.piece().color())
                && freeVector.isDiagonal() && isPathEmpty(chessBoard, boundVector);
    }

    /**
     * This method checks if given boundVector represents a valid queen move given specified chessBoard
     * The method does not check if the chesspiece at the origin is null or if either origin or destination
     * of given boundVector are within bounds of the chessboard
     *
     * @param chessBoard  chessboard the piece moves on
     * @param boundVector represents move
     * @return true if the queen move is legal otherwise false
     */
    private static boolean isLegalQueenMove(ChessBoard chessBoard, BoundVector boundVector) {
        ChessSlot firstSlot = chessBoard.getElement(boundVector.origin());
        ChessSlot secondSlot = chessBoard.getElement(boundVector.destination());
        FreeVector freeVector = boundVector.getFreeVector();
        return (secondSlot.piece() == null || firstSlot.piece().color() != secondSlot.piece().color())
                && (freeVector.isOrthogonal() || freeVector.isDiagonal()) && isPathEmpty(chessBoard, boundVector);
    }

    /**
     * The method checks if the move is valid without taking into account whether the chesspiece
     * moves at the right turn or whether the move would expose king to a check
     *
     * @param chessBoard    chessboard the piece moves on
     * @param boundVector   represnts move
     * @param onlyAttacking if true the method ignores the move
     *                      if it's legal except it cannot be used to attack another chesspiece
     * @return true if the move is legal otherwise false
     */
    static boolean isLooselyLegalMove(ChessBoard chessBoard, BoundVector boundVector, boolean onlyAttacking) {

        //this code checks if move is within board boundaries
        if (!chessBoard.isValidBoundVector(boundVector)) {
            return false;
        }

        ChessPiece chessPiece = chessBoard.getElement(boundVector.origin()).piece();

        //this code checks if we're actually moving any chess piece at all
        if (chessPiece == null) {
            return false;
        }

        return switch (chessPiece.type()) {
            case PAWN -> isLegalPawnMove(chessBoard, boundVector, onlyAttacking);
            case ROOK -> isLegalRookMove(chessBoard, boundVector);
            case KNIGHT -> isLegalKnightMove(chessBoard, boundVector);
            case BISHOP -> isLegalBishopMove(chessBoard, boundVector);
            case QUEEN -> isLegalQueenMove(chessBoard, boundVector);
            case KING -> isLegalKingMove(chessBoard, boundVector);
        };
    }

    /**
     * The method checks whether a given position might be under attack by a chesspiece of the specified color
     *
     * @param chessBoard chessboard on which the position and potential attacking chesspieces are located
     * @param position   position to check
     * @param by         color of chesspieces that might attack a given position
     * @return true if position is under attack by specified color, otherwise false
     */
    static boolean isPositionUnderAttack(ChessBoard chessBoard, Position position, ChessPieceColor by) {
        Position[] positions = chessBoard.getAllValidPositions();
        ChessPiece chessPiece;
        boolean isAttacked = false;
        for (Position origin : positions) {
            chessPiece = chessBoard.getElement(origin).piece();
            if (chessPiece != null && chessPiece.color() == by) {
                isAttacked = isLooselyLegalMove(chessBoard, new BoundVector(origin, position), true);
                if (isAttacked) {
                    break;
                }
            }
        }
        return isAttacked;
    }
}
