package dev.keryeshka.seeu.extra.protocol;

public record ClientOffer(
        int protocolVersion,
        boolean enabled,
        int maximumDistanceBlocks,
        int minimumDistanceBlocks,
        int entityViewScaleQ10
) {
    public ClientOffer {
        if (protocolVersion != ExtraProtocol.VERSION) {
            throw new IllegalArgumentException("Unsupported protocol version: " + protocolVersion);
        }
        if (maximumDistanceBlocks < 0 || maximumDistanceBlocks > ExtraProtocol.MAXIMUM_DISTANCE_BLOCKS) {
            throw new IllegalArgumentException("Maximum distance is outside the supported range");
        }
        if (minimumDistanceBlocks < 0 || minimumDistanceBlocks > maximumDistanceBlocks) {
            throw new IllegalArgumentException("Minimum distance exceeds the maximum distance");
        }
        if (entityViewScaleQ10 < ExtraProtocol.MIN_ENTITY_VIEW_SCALE_Q10
                || entityViewScaleQ10 > ExtraProtocol.MAX_ENTITY_VIEW_SCALE_Q10) {
            throw new IllegalArgumentException("Entity view scale is outside the supported range");
        }
    }
}
