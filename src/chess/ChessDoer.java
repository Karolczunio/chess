package chess;

import chess.navigation.BoundVector;
import chess.navigation.Position;
import chess.element.ChessPiece;
import chess.enums.ChessPieceColor;
import chess.enums.ChessType;

public interface ChessDoer {
    /**
     * The method performs a standard chess move
     *
     * @param chessBoard  chessboard the move takes place on
     * @param boundVector represents the move
     * @param provider    represents a way to handle pawn promotion
     * @return modified chessboard with move made
     */
    static ChessBoard makeStandardChessMove(ChessBoard chessBoard, BoundVector boundVector, PromotionTypeProvider provider) {
        ChessPiece chessPiece = chessBoard.getElement(boundVector.origin()).piece();
        ChessPieceColor colorToPromote = chessBoard.getElement(boundVector.destination()).tile().promotionType().matchingColor();
        if (colorToPromote != null && chessPiece.type() == ChessType.PAWN && colorToPromote == chessPiece.color()) {
            chessPiece = new ChessPiece(provider.getPromotionType(), chessPiece.color(), true);
        } else {
            chessPiece = new ChessPiece(chessPiece.type(), chessPiece.color(), true);
        }
        return chessBoard.getChessBoardWithChangedChessPiece(boundVector.origin(), null)
                .getChessBoardWithChangedChessPiece(boundVector.destination(), chessPiece)
                .getChessBoardWithFlippedColor();
    }

    /**
     * The method translates String object into BoundVector object
     *
     * @param move String object representing move on the chessboard
     * @return BoundVector object representing move ont the chessboard
     */
    static BoundVector getBoundVector(String move) {
        if (!move.matches("([a-h][1-8]){2}")) {
            throw new IllegalArgumentException("No such bound vector");
        }
        return new BoundVector(getPosition(move.substring(0, 2)), getPosition(move.substring(2)));
    }

    /**
     * The method translates String object into Position object
     *
     * @param position String object representing position on the chessboard
     * @return Position object representing position on the chessboard
     */
    static Position getPosition(String position) {
        if (!position.matches("[a-h][1-8]")) {
            throw new IllegalArgumentException("No such position");
        }
        return new Position(position.charAt(0) - 'a', position.charAt(1) - '1');
    }
}
