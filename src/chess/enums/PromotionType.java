package chess.enums;

public enum PromotionType {
    FOR_WHITES, FOR_BLACKS, FOR_NONE;

    /**
     * The method returns a chesspiece color of the pawn that can be promoted on the tile that has this property
     *
     * @return color of a pawn to promote
     */
    public ChessPieceColor matchingColor() {
        return switch (this) {
            case FOR_WHITES -> ChessPieceColor.WHITE;
            case FOR_BLACKS -> ChessPieceColor.BLACK;
            case FOR_NONE -> null;
        };
    }
}
