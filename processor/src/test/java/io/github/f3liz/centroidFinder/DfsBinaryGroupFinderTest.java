package io.github.f3liz.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;

public class DfsBinaryGroupFinderTest {

    @Test
    public void testDfsBinaryGroupFinder_Basic() {
        int[][] image = new int[][]{
            {1, 0, 0, 1},
            {0, 1, 0, 0},
            {0, 0, 0, 0},
            {1, 0, 1, 1}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(5, actual.size());
    }

    // test null
    @Test
    public void testDfsBinaryGroupFinder_NullArray() {
        int[][] image = new int[][]{null};

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        Exception exception = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);;
        });
        assertEquals("Null array or subarray", exception.getMessage());
       
    }
    
    // test null subarray
    @Test
    public void testDfsBinaryGroupFinder_NullSubArray() {
        int[][] image = new int[][]{
            {1, 0, 0, 1},
            {0, 1, 0, 0},
            null,
            {1, 0, 1, 1},
            {1, 1, 0, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        Exception exception = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);;
        });
        assertEquals("Null array or subarray", exception.getMessage());
       
    }

    // test empty image/no pixels
    @Test
    public void testDfsBinaryGroupFinder_EmptySize() {
        int[][] image = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(0, actual.size());
    }
    
    // test empty image [0][0]
    @Test
    public void testDfsBinaryGroupFinder_EmptyArray() {
        int[][] image = new int[0][0];

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            finder.findConnectedGroups(image);;
        });
        assertEquals("Invalid array", exception.getMessage());
       
    }

    @Test
    public void testDfsBinaryGroupFinder_SizeAndCentroid() {
        int[][] image = new int[][]{
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(1, actual.size());

        Group group = actual.get(0);
        assertEquals(3, group.size());

        assertEquals(1, group.centroid().x());
        assertEquals(1, group.centroid().y());
    }

    @Test
    public void testDfsBinaryGroupFinder_MultipleGroups() {
        int[][] image = new int[][]{
            {1, 0, 1, 0, 1},
            {0, 0, 1, 0, 0},
            {1, 1, 1, 1, 1},
            {0, 0, 1, 0, 0},
            {1, 0, 1, 0, 1},
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(5, actual.size());

        Group group = actual.get(0);
        assertEquals(9, group.size());

        assertEquals(2, group.centroid().x());
        assertEquals(2, group.centroid().y());
    }

    @Test
    public void testDfsBinaryGroupFinder_BigTest() {
        int[][] image = new int[][]{
            {0, 1, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 0, 0, 0, 0, 1},
            {0, 1, 0, 1, 1, 1, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 1, 1, 0, 0, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 1, 1, 1, 1, 1},
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(6, actual.size());

        // biggest group
        Group group1 = actual.get(0);
        assertEquals(15, group1.size());

        assertEquals(7, group1.centroid().x());
        assertEquals(6, group1.centroid().y());

        // 2nd group
        Group group2 = actual.get(1);
        assertEquals(7, group2.size());

        assertEquals(4, group2.centroid().x());
        assertEquals(2, group2.centroid().y());

        // 3rd group
        Group group3 = actual.get(2);
        assertEquals(5, group3.size());

        assertEquals(1, group3.centroid().x());
        assertEquals(1, group3.centroid().y());

        // 4th group
        Group group4 = actual.get(3);
        assertEquals(3, group4.size());

        assertEquals(9, group4.centroid().x());
        assertEquals(1, group4.centroid().y());

        // 5th group
        Group group5 = actual.get(4);
        assertEquals(3, group5.size());

        assertEquals(1, group5.centroid().x());
        assertEquals(5, group5.centroid().y());

        // 6th group
        Group group6 = actual.get(5);
        assertEquals(1, group6.size());

        assertEquals(0, group6.centroid().x());
        assertEquals(7, group6.centroid().y());
    }
}
