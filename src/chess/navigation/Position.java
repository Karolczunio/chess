package chess.navigation;

public record Position(int x, int y) {

    /**
     * The method returns Position created by moving from this position by the vector given as an argument.
     *
     * @param vector FreeVector representing displacement on the board from this position
     * @return the position on the board displaced by vector
     */
    public Position getPositionMovedBy(FreeVector vector) {
        return new Position(x + vector.x(), y + vector.y());
    }

    /**
     * The method returns String representation of this position.
     *
     * @return For valid positions in format LD L - letter, D - digit,
     * for invalid positions String "out"
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
