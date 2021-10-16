package boards;

import java.util.Objects;

public class FreeVector {

    private final int x;
    private final int y;

    /**
     * The FreeVector constructor
     * @param x relative displacement on X axis
     * @param y relative displacement on Y axis
     */
    FreeVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * The method returns x coordinate representing relative displacement on X axis
     * @return x coordinate
     */
    public int x() {
        return x;
    }

    /**
     * The method returns y coordinate representing relative displacement on Y axis
     * @return y coordinate
     */
    public int y() {
        return y;
    }

    /**
     * The method checks if the FreeVector represents displacement that is zero on both X and Y axis
     * @return true if displacement is none, otherwise false
     */
    public boolean isNone() {
        return x == 0 && y == 0;
    }

    /**
     * The method checks if this FreeVector represents orthogonal displacement
     * @return true if displacement is orthogonal, otherwise false
     */
    public boolean isOrthogonal() {
        return !isNone() && (x == 0 || y == 0);
    }

    /**
     * The method checks if this FreeVector represents displacement of magnitude at most 1 in any direction
     * @return true if displacement is at most 1 in any direction, otherwise false
     */
    public boolean isOneTiled() {
        return !isNone() && Math.abs(x) <= 1 && Math.abs(y) <= 1;
    }
    
    /**
     * The method checks if this FreeVector represents displacement towards positive values on Y axis
     * @return true if displacement is towards positive values on Y axis, otherwise false
     */
    public boolean isTowardsPositiveY() {
        return y > 0;
    }

    /**
     * The method checks if this FreeVector represents displacement towards negative values on Y axis
     * @return true if displacement is towards negative values on Y axis, otherwise false
     */
    public boolean isTowardsNegativeY() {
        return y < 0;
    }

    /**
     * The method checks if this FreeVector represents displacement towards positive values on X axis
     * @return true if displacement is towards positive values on X axis, otherwise false
     */
    public boolean isTowardsPositiveX() {
        return x > 0;
    }

    /**
     * The method checks if this FreeVector represents displacement towards negative values on X axis
     * @return true if displacement is towards negative values on X axis, otherwise false
     */
    public boolean isTowardsNegativeX() {
        return x < 0;
    }

    /**
     * The method checks if FreeVector represents diagonal displacement
     * @return true if displacement is diagonal, otherwise false
     */
    public boolean isDiagonal() {
        return !isNone() && Math.abs(x) == Math.abs(y);
    }

    /**
     * The method checks if FreeVector represents displacement that is 2 tiles in one direction
     * and 1 tile in another direction
     * @return true if displacement is 1 tile in one direction and 2 tiles in another direction, otherwise false
     */
    public boolean isLShaped() {
        return (Math.abs(x) == 2 && Math.abs(y) == 1)
                || (Math.abs(x) == 1 && Math.abs(y) == 2);
    }

    /**
     * The method returns Chebyshev distance of this FreeVector
     * @return distance of this FreeVector
     */
    public int distance() {
        return Math.max(Math.abs(x), Math.abs(y));
    }

    /**
     * The method adds argument FreeVector to this FreeVector and returns this sum
     * @param vector FreeVector to be added to this FreeVector
     * @return sum of this FreeVector and argument Freevector
     */
    public FreeVector add(FreeVector vector) {
        return new FreeVector(this.x() + vector.x(), this.y() + vector.y());
    }

    /**
     * The method subtracts argument FreeVector from this FreeVector and returns this difference
     * @param vector FreeVector to be subtracted from this FreeVector
     * @return difference of this FreeVector and argument Freevector
     */
    public FreeVector subtract(FreeVector vector) {
        return new FreeVector(this.x() - vector.x(), this.y() - vector.y());
    }

    /**
     * The method returns FreeVector that represents general direction of its displacement but without magnitude
     * @return general direction of displacement as FreeVector
     */
    public FreeVector iteration() {
        return new FreeVector(x != 0 ? x / Math.abs(x) : 0, y != 0 ? y / Math.abs(y) : 0);
    }

    /**
     * The method returns String representation of this FreeVector
     * @return String representation of this FreeVector.
     * First number is x coordinate, the second one is y coordinate.
     */
    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }

    /**
     * The method checks two objects for equality
     * @param o object checked for equality
     * @return true if both objects are of type FreeVector and have the same x and y coordinates
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreeVector that = (FreeVector) o;
        return x == that.x && y == that.y;
    }

    /**
     * The method returns hascode of this object
     * @return hashcode of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
