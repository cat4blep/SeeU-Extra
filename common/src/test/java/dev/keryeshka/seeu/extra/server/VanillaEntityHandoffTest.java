package dev.keryeshka.seeu.extra.server;

import dev.keryeshka.seeu.extra.protocol.EntityViewScale;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VanillaEntityHandoffTest {
    @Test
    void zombieHandoffMatchesVanillaAtTwelveChunks() {
        int viewScale = EntityViewScale.fromOptions(12, 1.0D);
        double zombieBoundingBoxSize = (0.6D + 1.95D + 0.6D) / 3.0D;

        assertEquals(
                100.8D,
                VanillaEntityHandoff.renderDistanceBlocks(zombieBoundingBoxSize, viewScale),
                0.0001D
        );
        assertEquals(
                VanillaEntityHandoff.SnapshotNeed.NONE,
                classifyAtDistance(true, 84.0D, zombieBoundingBoxSize, viewScale)
        );
        assertEquals(
                VanillaEntityHandoff.SnapshotNeed.PREFETCH,
                classifyAtDistance(true, 90.0D, zombieBoundingBoxSize, viewScale)
        );
        assertEquals(
                VanillaEntityHandoff.SnapshotNeed.REQUIRED,
                classifyAtDistance(true, 101.0D, zombieBoundingBoxSize, viewScale)
        );
    }

    @Test
    void entitiesOutsideVanillaTrackingAreAlwaysRequired() {
        assertEquals(
                VanillaEntityHandoff.SnapshotNeed.REQUIRED,
                classifyAtDistance(false, 1.0D, 1.0D, 1024)
        );
    }

    @Test
    void nanBoundingBoxUsesVanillaFallbackSize() {
        assertEquals(
                64.0D,
                VanillaEntityHandoff.renderDistanceBlocks(Double.NaN, 1024),
                0.0001D
        );
    }

    private static VanillaEntityHandoff.SnapshotNeed classifyAtDistance(
            boolean trackedByVanilla,
            double distance,
            double boundingBoxSize,
            int viewScale
    ) {
        return VanillaEntityHandoff.classify(
                trackedByVanilla,
                distance * distance,
                boundingBoxSize,
                viewScale
        );
    }
}
