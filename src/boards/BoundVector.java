package boards;

import java.util.Objects;

public class BoundVector {
    private final Position origin;
    private final Position destination;

    /**
     * The BoundVector constructor
     * @param origin of displacement
     * @param destination of displacement 
     */
    BoundVector(Position origin, Position destination) {
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * The method returns destination
     * @return the destination
     */
    public Position destination() {
        return destination;
    }

    /**
     * The method returns origin
     * @return the origin
     */
    public Position origin(){
        return origin;
    }

    /**
     * The static method creating object of class BoundVector based on argument string
     * @param line String in the format LDLD L - letter, D - digit where first LD is a starting
     * position and the second one is final position
     * @return object of class BoundVector
     */
    public static BoundVector getBoundVector(String line) {
        if (!line.matches("([a-h][1-8]){2}"))
            throw new IllegalArgumentException("Incorrect boards.BoundVector format");
        Position origin = Position.getPosition(line.substring(0, 2));
        Position destination = Position.getPosition(line.substring(2, 4));
        return new BoundVector(origin, destination);
    }

    /**
     * The method returns FreeVector representing relative displacement of this BoundVector according to X and Y axis
     * @return object of class FreeVector based on this BoundVector
     */
    public FreeVector getFreeVector() {
        return new FreeVector(destination.x() - origin.x(), destination.y() - origin.y());
    }

    /**
     * The method returns all positions between origin and destination if the free vector
     * of this bound vector is orthogonal, diagonal or none, else it throws IllegalStateException
     *
     * @return the array of positions between origin and destination
     */
    public Position[] getStandardPath() {
        FreeVector freeVector = getFreeVector();
        if (!freeVector.isNone() && !freeVector.isOrthogonal() && !freeVector.isDiagonal()) {
            throw new IllegalStateException("Free vector of this bound vector is not orthogonal, diagonal or none");
        }
        int length = freeVector.distance() + 1;
        Position[] path = new Position[length];
        Position current = this.origin;
        FreeVector iteration = freeVector.iteration();
        for (int i = 0; i < path.length; i++) {
            path[i] = current;
            current = current.getPositionMovedBy(iteration);
        }
        return path;
    }

    /**
     * The method returns String representation of this BoundVector
     * @return String in the format LDLD L - letter, D - digit, where first LD is a starting position
     * and the last LD is the final position
     */
    @Override
    public String toString() {
        return origin.toString() + destination.toString();
    }

    /**
     * The method checks 2 objects for equality
     * @param o object checked for equality
     * @return true if both objects are of type BoundVector and their positions
     * of origin and destination are the same, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundVector that = (BoundVector) o;
        return origin.equals(that.origin) && destination.equals(that.destination);
    }

    /**
     * The method returns hashcode of this object
     * @return hashcode of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(origin, destination);
    }
}
