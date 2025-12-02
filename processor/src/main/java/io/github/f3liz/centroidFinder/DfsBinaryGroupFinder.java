package io.github.f3liz.centroidFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {

    // Directions to move in the grid: up, down, left, right
    private final int[][] directions = new int[][] {
            { -1, 0 }, // up
            { 1, 0 }, // down
            { 0, -1 }, // left
            { 0, 1 } // right
    };

    /**
     * Finds connected pixel groups of 1s in an integer array representing a binary
     * image.
     * 
     * The input is a non-empty rectangular 2D array containing only 1s and 0s.
     * If the array or any of its subarrays are null, a NullPointerException
     * is thrown. If the array is otherwise invalid, an IllegalArgumentException
     * is thrown.
     *
     * Pixels are considered connected vertically and horizontally, NOT diagonally.
     * The top-left cell of the array (row:0, column:0) is considered to be
     * coordinate
     * (x:0, y:0). Y increases downward and X increases to the right. For example,
     * (row:4, column:7) corresponds to (x:7, y:4).
     *
     * The method returns a list of sorted groups. The group's size is the number
     * of pixels in the group. The centroid of the group
     * is computed as the average of each of the pixel locations across each
     * dimension.
     * For example, the x coordinate of the centroid is the sum of all the x
     * coordinates of the pixels in the group divided by the number of pixels in
     * that group.
     * Similarly, the y coordinate of the centroid is the sum of all the y
     * coordinates of the pixels in the group divided by the number of pixels in
     * that group.
     * The division should be done as INTEGER DIVISION.
     *
     * The groups are sorted in DESCENDING order according to Group's compareTo
     * method
     * (size first, then x, then y). That is, the largest group will be first, the
     * smallest group will be last, and ties will be broken first by descending
     * y value, then descending x value.
     * 
     * @param image a rectangular 2D array containing only 1s and 0s
     * @return the found groups of connected pixels in descending order
     */
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        // Validation the image
        if (image == null) throw new NullPointerException("Null array or subarray");
        if (image.length == 0) throw new IllegalArgumentException("Invalid array");

        // Validation for subarrays
        for(int[] row : image) {
            if (row == null) throw new NullPointerException("Null array or subarray");
        }

        if (image[0].length == 0) throw new IllegalArgumentException("Invalid array");

        List<Group> groups = new ArrayList<>();
        boolean[][] visited = new boolean[image.length][image[0].length];

        // Iterate through each pixel in the image
        for (int r = 0; r < image.length; r++) {
            for (int c = 0; c < image[0].length; c++) {
                // Check for if the pixel is a part of a group
                if (image[r][c] == 1 && !visited[r][c]) {
                    List<int[]> pixelatedGroup = new ArrayList<>();
                    // Perform DFS to all connected pixels for this group
                    findConnectedGroups(image, new int[] { r, c }, visited, pixelatedGroup);
                    // Convert the list of pixels to a Group and adding to the result
                    groups.add(createGroup(pixelatedGroup));
                }
            }
        }
        // Sort the groups in descending order
        groups.sort(Collections.reverseOrder());

        return groups;
    }

    // /**
    //  * Locates connected groups of pixels in the image from a given location.
    //  * 
    //  * @param image a rectangular 2D array containing only 1s and 0s
    //  * @param location the starting coordinates (row, column) for the search
    //  * @param visited the boolean array used to track visited pixels
    //  * @param pixelatedGroup a list that contains the found connected pixels
    //  */
    // private void findConnectedGroups(int[][] image, int[] location, boolean[][] visited, List<int[]> pixelatedGroup) {
    //     int curR = location[0];
    //     int curC = location[1];

    //     // Validate coordinates to ensure they are within the boundaries of image
    //     if (curR < 0 || curR >= image.length || curC < 0 || curC >= image[0].length)
    //         return;

    //     // Skip if the current pixel has already been visited or is a '0'
    //     if (visited[curR][curC] || image[curR][curC] == 0)
    //         return;

    //     // Mark the pixel as visited and add it to the group
    //     visited[curR][curC] = true;
    //     pixelatedGroup.add(location);

    //     // Exploring all four directions (up, down, left, right)
    //     for (int[] direction : directions) {
    //         int newR = curR + direction[0];
    //         int newC = curC + direction[1];

    //         findConnectedGroups(image, new int[] { newR, newC}, visited, pixelatedGroup);
    //     }
    // }

    // iterative approach (wrote in case our dfs didn't work later on for video processing)
    private void findConnectedGroups(int[][] image, int[] location, boolean[][] visited, List<int[]> pixelatedGroup) {
        Stack<int[]> stack = new Stack<>();

        stack.push(location);

        while(!stack.isEmpty()) {
            int[] current = stack.pop();

            int curR = current[0];
            int curC = current[1];

            // Validate coordinates to ensure they are within the boundaries of image
            if (curR < 0 || curR >= image.length || curC < 0 || curC >= image[0].length)
                continue;

            // Skip if the current pixel has already been visited or is a '0'
            if (visited[curR][curC] || image[curR][curC] == 0)
                continue;

            // Mark the pixel as visited and add it to the group
            visited[curR][curC] = true;
            pixelatedGroup.add(new int[]{curR, curC});

            for (int[] direction : directions) {
                int newR = curR + direction[0];
                int newC = curC + direction[1];

                stack.push(new int[]{newR, newC});
            }
        }
    }

    /**
     * Converts a list of pixel coordinates into a Group. 
     * The centroid is calculated by averaging the pixel coordinates.
     * 
     * @param pixelatedGroup a list of pixel coordinates that form a connected group
     * @return a Group representing the connected group of pixels
     */
    private Group createGroup(List<int[]> pixelatedGroup) {
        int totalXPixels = 0;
        int totalYPixels = 0;

        int size = pixelatedGroup.size();

        // Calculate the total of the x and y coordinates for the centroid
        for (int[] coord : pixelatedGroup) {
            totalXPixels += coord[1];
            totalYPixels += coord[0];
        }

        // Return a new Group with the size and the calculated centroid
        return new Group(size, new Coordinate(totalXPixels / size, totalYPixels / size));
    }
}
