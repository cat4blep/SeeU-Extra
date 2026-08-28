package dev.keryeshka.seeu.extra.protocol;

public final class EntityViewScale {
    private EntityViewScale() {
    }

    public static int fromOptions(int effectiveRenderDistanceChunks, double entityDistanceScaling) {
        if (!Double.isFinite(entityDistanceScaling)) {
            throw new IllegalArgumentException("Entity distance scaling must be finite");
        }

        double renderDistanceScale = clamp(effectiveRenderDistanceChunks / 8.0D, 1.0D, 2.5D);
        int quantized = (int) Math.round(
                renderDistanceScale
                        * entityDistanceScaling
                        * ExtraProtocol.ENTITY_VIEW_SCALE_DENOMINATOR
        );
        return clamp(
                quantized,
                ExtraProtocol.MIN_ENTITY_VIEW_SCALE_Q10,
                ExtraProtocol.MAX_ENTITY_VIEW_SCALE_Q10
        );
    }

    public static double decode(int entityViewScaleQ10) {
        return entityViewScaleQ10 / (double) ExtraProtocol.ENTITY_VIEW_SCALE_DENOMINATOR;
    }

    private static int clamp(int value, int minimum, int maximum) {
        return Math.max(minimum, Math.min(value, maximum));
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(value, maximum));
    }
}
