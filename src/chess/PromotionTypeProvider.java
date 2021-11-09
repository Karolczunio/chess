package chess;

import chess.enums.ChessType;

@FunctionalInterface
public interface PromotionTypeProvider {
    /**
     * The method should provide one of the following chesstypes: ROOK, KNIGHT, BISHOP, QUEEN
     * It will be used when the pawn is meant to be promoted
     *
     * @return chesstype the pawn should promote to
     */
    ChessType getPromotionType();
}
