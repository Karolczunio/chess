package chess.enums;

public enum ChessTileColor {
    WHITE, BLACK;

    /**
     * The method returns the opposite color to this one.
     * WHITE for BLACK and BLACK for WHITE
     *
     * @return the opposite color
     */
    public ChessTileColor oppositeColor() {
        return switch (this) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
        };
    }
}
