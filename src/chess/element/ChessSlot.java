package chess.element;

public record ChessSlot(ChessTile tile, ChessPiece piece) {

    public ChessSlot {
        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null");
        }
    }

    /**
     * @return character representing this chess slot
     */
    public char character() {
        return (piece == null) ? tile.character() : piece.character();
    }
}
