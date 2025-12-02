package io.github.f3liz.centroidFinder;

/**
 * Represents a group of contiguous pixels in an image.
 * The top-left cell of the array (row:0, column:0) is considered to be coordinate (x:0, y:0).
 * Y increases downward and X increases to the right. For example, (row:4, column:7)
 * corresponds to (x:7, y:4).
 * 
 * A group's size indicates the number of pixels in the group.
 * The centroid of the group is computed as the average of the pixel coordinates in each
 * dimension using integer division.
 * This means the x coordinate of the centroid is the sum of all x values divided by the
 * number of pixels in the group, and similarly for the y coordinate.
 * 
 * Groups are naturally comparable. The comparison is done first by the group's size,
 * then by the x coordinate of the centroid, and finally by the y coordinate.
 * In a method that returns groups, they should be sorted in this natural order.
 */
public record Group(int size, Coordinate centroid) implements Comparable<Group> {

    /**
     * Compares this group with the specified group for order.
     * The comparison is based on the group's size first, then the x coordinate of the centroid,
     * and finally on the y coordinate of the centroid.
     *
     * @param other the group to be compared with this group
     * @return a negative integer, zero, or a positive integer if this group is less than,
     *         equal to, or greater than the specified group
     */
    @Override
    public int compareTo(Group other) {
        int comp = Integer.compare(this.size(), other.size());
        if (comp != 0) {
            return comp;
        }
        comp = Integer.compare(this.centroid().x(), other.centroid().x());
        if (comp != 0) {
            return comp;
        }
        return Integer.compare(this.centroid().y(), other.centroid().y());
    }

    /**
     * Returns a string representing this group in comma-separated values (CSV) format.
     * The format is "size,x,y", where size is the group's size and x and y are the
     * centroid coordinates.
     *
     * @return a CSV row string representing the group's size and centroid coordinates
     */
    public String toCsvRow() {
        return String.format("%d,%d,%d", this.size(), this.centroid().x(), this.centroid().y());
    }
}
