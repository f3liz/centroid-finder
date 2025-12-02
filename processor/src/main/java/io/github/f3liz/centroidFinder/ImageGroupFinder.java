package io.github.f3liz.centroidFinder;

import java.util.List;
import java.awt.image.BufferedImage;

public interface ImageGroupFinder {
    /**
     * Finds connected groups in an image.
     * 
     * The groups are sorted in DESCENDING order according to Group's compareTo method.
     * @param image
     * @return connected groups in an image sorted in descending order
     */
    public List<Group> findConnectedGroups(BufferedImage image);
}
