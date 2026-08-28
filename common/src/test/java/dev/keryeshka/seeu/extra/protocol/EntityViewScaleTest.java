package dev.keryeshka.seeu.extra.protocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityViewScaleTest {
    @Test
    void matchesVanillaRenderDistanceFormula() {
        assertEquals(1024, EntityViewScale.fromOptions(8, 1.0D));
        assertEquals(1536, EntityViewScale.fromOptions(12, 1.0D));
        assertEquals(1920, EntityViewScale.fromOptions(12, 1.25D));
    }

    @Test
    void clampsToSupportedVanillaOptionExtremes() {
        assertEquals(
                ExtraProtocol.MIN_ENTITY_VIEW_SCALE_Q10,
                EntityViewScale.fromOptions(2, 0.5D)
        );
        assertEquals(
                ExtraProtocol.MAX_ENTITY_VIEW_SCALE_Q10,
                EntityViewScale.fromOptions(32, 5.0D)
        );
    }

    @Test
    void rejectsNonFiniteEntityDistanceScaling() {
        assertThrows(
                IllegalArgumentException.class,
                () -> EntityViewScale.fromOptions(12, Double.NaN)
        );
    }
}
