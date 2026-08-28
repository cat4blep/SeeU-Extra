package dev.keryeshka.seeu.extra.server;

import dev.keryeshka.seeu.extra.protocol.EntityViewScale;

public final class VanillaEntityHandoff {
    public static final double PREFETCH_DISTANCE_BLOCKS = 16.0D;
    private static final double BASE_ENTITY_RENDER_DISTANCE = 64.0D;

    private VanillaEntityHandoff() {
    }

    public static SnapshotNeed classify(
            boolean trackedByVanilla,
            double distanceSquared,
            double boundingBoxSize,
            int entityViewScaleQ10
    ) {
        if (!trackedByVanilla) {
            return SnapshotNeed.REQUIRED;
        }

        double renderDistance = renderDistanceBlocks(boundingBoxSize, entityViewScaleQ10);
        double prefetchDistance = Math.max(0.0D, renderDistance - PREFETCH_DISTANCE_BLOCKS);
        if (distanceSquared < square(prefetchDistance)) {
            return SnapshotNeed.NONE;
        }
        if (distanceSquared < square(renderDistance)) {
            return SnapshotNeed.PREFETCH;
        }
        return SnapshotNeed.REQUIRED;
    }

    public static double renderDistanceBlocks(double boundingBoxSize, int entityViewScaleQ10) {
        double safeSize = Double.isNaN(boundingBoxSize) ? 1.0D : Math.max(0.0D, boundingBoxSize);
        return safeSize * BASE_ENTITY_RENDER_DISTANCE * EntityViewScale.decode(entityViewScaleQ10);
    }

    private static double square(double value) {
        return value * value;
    }

    public enum SnapshotNeed {
        REQUIRED,
        PREFETCH,
        NONE
    }
}
