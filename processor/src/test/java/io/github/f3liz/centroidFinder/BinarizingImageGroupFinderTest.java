package io.github.f3liz.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BinarizingImageGroupFinderTest {

    // fakes are simplified versions of classes, aka test doubles. for testing a
    // component without needing full implementation of it's dependencies

    // mocks are objects you can control during a test to see how it was used

    // fake ImageBinarizer
    private static class FakeImageBinarizer implements ImageBinarizer {
        boolean called = false;

        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            called = true;
            return new int[][] {
                    { 1, 0, 1 },
                    { 0, 0, 0 },
                    { 1, 1, 1 },
            };
        }

        // not using for tests
        @Override
        public BufferedImage toBufferedImage(int[][] image) {
            throw new UnsupportedOperationException("Not needed in this test");
        }
    }

    // fake BinaryGroupFinder
    private static class FakeBinaryGroupFinder implements BinaryGroupFinder {
        boolean called = false;
        List<Group> output;

        public FakeBinaryGroupFinder() {
            output = new ArrayList<>();
            output.add(new Group(3, new Coordinate(1, 2)));
            output.add(new Group(1, new Coordinate(0, 0)));
            output.add(new Group(1, new Coordinate(2, 0)));
        }

        @Override
        public List<Group> findConnectedGroups(int[][] binaryImage) {
            called = true;
            return output;
        }
    }

    @Test
    public void testFindConnectedGroupsWithFakesandMocks() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer();

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder();

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);
        assertEquals(groupFinder.output, result);

        assertEquals(3, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals(new Coordinate(1, 2), result.get(0).centroid());
    }

    @Test
    public void testImageWithOnlyZeros() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[][] {
                        { 0, 0, 0 },
                        { 0, 0, 0 },
                        { 0, 0, 0 }
                };
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                return new ArrayList<>();
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testImageWithOneGiantGroup() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[][] {
                        { 1, 1, 1 },
                        { 1, 1, 1 },
                        { 1, 1, 1 }
                };
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                List<Group> groups = new ArrayList<>();
                groups.add(new Group(9, new Coordinate(1, 1)));
                return groups;
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);

        assertEquals(1, result.size());
        assertEquals(9, result.get(0).size());
        assertEquals(new Coordinate(1, 1), result.get(0).centroid());
    }

    @Test
    public void testImageWithMultipleGroups() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[][] {
                        { 1, 0, 1 },
                        { 0, 0, 0 },
                        { 1, 1, 1 }
                };
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                List<Group> groups = new ArrayList<>();
                groups.add(new Group(3, new Coordinate(1, 2)));
                groups.add(new Group(1, new Coordinate(0, 0)));
                groups.add(new Group(1, new Coordinate(2, 0)));
                return groups;
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);

        assertEquals(3, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals(new Coordinate(1, 2), result.get(0).centroid());
        assertEquals(new Coordinate(0, 0), result.get(1).centroid());
        assertEquals(new Coordinate(2, 0), result.get(2).centroid());
    }

    @Test
    public void testImageWithOneSmallGroup() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[][] {
                        { 0, 0, 0 },
                        { 0, 1, 0 },
                        { 0, 0, 0 }
                };
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                List<Group> groups = new ArrayList<>();
                groups.add(new Group(1, new Coordinate(1, 1)));
                return groups;
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(new Coordinate(1, 1), result.get(0).centroid());
    }

    @Test
    public void testSingleWhitePixelGroup() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[][] {
                        { 0, 0, 0 },
                        { 0, 1, 0 },
                        { 0, 0, 0 }
                };
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                List<Group> groups = new ArrayList<>();
                groups.add(new Group(1, new Coordinate(1, 1)));
                return groups;
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(new Coordinate(1, 1), result.get(0).centroid());
    }

    @Test
    public void testNonSquareImage() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(5, 3, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[][] {
                        { 1, 0, 1, 0, 1 },
                        { 0, 0, 0, 1, 0 },
                        { 1, 0, 1, 0, 1 }
                };
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                List<Group> groups = new ArrayList<>();
                groups.add(new Group(3, new Coordinate(0, 0)));
                groups.add(new Group(3, new Coordinate(1, 4)));
                return groups;
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);

        assertEquals(2, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals(new Coordinate(0, 0), result.get(0).centroid());
        assertEquals(new Coordinate(1, 4), result.get(1).centroid());
    }

    @Test
    public void testSingleRowImage() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(1, 5, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[][] {
                        { 1, 0, 1, 0, 1 }
                };
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                List<Group> groups = new ArrayList<>();
                groups.add(new Group(3, new Coordinate(0, 0)));
                return groups;
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals(new Coordinate(0, 0), result.get(0).centroid());
    }

    @Test
    public void testZeroSizeBinaryArray() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        // Uses a fake binary array to simulate an image of size 0x0

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[0][0];
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                return new ArrayList<>();
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMultipleIsolatedPixels() {
        // Arrange
        BufferedImage fakeImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        FakeImageBinarizer binarizer = new FakeImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                called = true;
                return new int[][] {
                        { 1, 0, 0 },
                        { 0, 1, 0 },
                        { 0, 0, 1 }
                };
            }
        };

        FakeBinaryGroupFinder groupFinder = new FakeBinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] binaryImage) {
                called = true;
                List<Group> groups = new ArrayList<>();
                groups.add(new Group(1, new Coordinate(0, 0)));
                groups.add(new Group(1, new Coordinate(1, 1)));
                groups.add(new Group(1, new Coordinate(2, 2)));
                return groups;
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> result = finder.findConnectedGroups(fakeImage);

        // Assert
        assertTrue(binarizer.called);
        assertTrue(groupFinder.called);
        assertEquals(3, result.size());
        assertEquals(new Coordinate(0, 0), result.get(0).centroid());
        assertEquals(new Coordinate(1, 1), result.get(1).centroid());
        assertEquals(new Coordinate(2, 2), result.get(2).centroid());
    }
}
