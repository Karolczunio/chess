package chess.navigation;

public record BoundVector(Position origin, Position destination) {

    /**
     * The method returns FreeVector representing relative displacement of this BoundVector according to X and Y axis
     *
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
        if (freeVector.distance() != 0 && !freeVector.isOrthogonal() && !freeVector.isDiagonal()) {
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
     * The method returns String representation of this bound vector
     *
     * @return String representation of this bound vector
     */
    @Override
    public String toString() {
        return "{" + origin.toString() + "," + destination.toString() + "}";
    }

}
