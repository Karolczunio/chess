package boards;

import java.util.Objects;

public class Position {
    private final static int xLimit = 8;
    private final static int yLimit = 8;
    private final int x;
    private final int y;

    /**
     * The Position constructor takes as arguments x and y
     * representing position on X axis and Y axis
     * @param x position on X axis
     * @param y position on Y axis
     */
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns position on X axis
     * @return position on X axis
     */
    public int x() {
        return x;
    }

    /**
     * Returns position on Y axis
     * @return position on Y axis
     */
    public int y() {
        return y;
    }

    /**
     * The method checks if x is between 0 and xLimit - 1 and if y is between 0 and yLimit - 1.
     * @return true if both x and y are within specified limits, otherwise false
     */
    public boolean isValid() {
        return (x >= 0 && x <= xLimit - 1) && (y >= 0 && y <= yLimit - 1);
    }

    /**
     * The static method creating object of class Position based on argument string.
     * The string should be composed of letter from a to h followed by a digit from 1 to 8.
     * Letters from a to h are mapped to position on X axis from 0 to 7.
     * Digits from 1 to 8 are mapped to position on Y axis from 0 to 7.
     * The method throws an exception if the string does not satisfy above conditions.
     * @param position String object representing position
     * @return object of class Position
     */
    public static Position getPosition(String position) {
        if (position == null) {
            throw new NullPointerException("String should not be null");
        }
        if (!position.matches("[a-h][1-8]")) {
            throw new IllegalArgumentException("Incorrect format of string representing position");
        }
        return new Position(position.charAt(0) - 'a', position.charAt(1) - '1');
    }

    /**
     * The method returns Position created by moving from this position by the vector given as an argument.
     * @param vector FreeVector representing displacement on the board from this position
     * @return the position on the board displaced by vector
     */
    public Position getPositionMovedBy(FreeVector vector) {
        return new Position(x + vector.x(), y + vector.y());
    }

    /**
     * The method returns String representation of this position.
     * @return For valid positions in format LD L - letter, D - digit,
     * for invalid positions String "out"
     */
    @Override
    public String toString() {
        return isValid()
                ? String.valueOf((char) ('a' + x)) + (char) ('1' + y)
                : "out";
    }

    /**
     * The method checks if two objects are equal
     * @param o Object to check for equality
     * @return true if both objects are of type Position and have the same x and y coordinates, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    /**
     * The method returns hashcode of an object
     * @return hashcode of an object
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
