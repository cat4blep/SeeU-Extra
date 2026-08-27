package dev.keryeshka.seeu.extra.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SnapshotVelocityTest {
    @Test
    void groundedGravityDoesNotExtrapolateBelowTheSurface() {
        assertEquals(0.0, SnapshotVelocity.vertical(-0.0784, true));
    }

    @Test
    void airborneFallKeepsItsVerticalVelocity() {
        assertEquals(-0.0784, SnapshotVelocity.vertical(-0.0784, false));
    }

    @Test
    void upwardLaunchIsNotSuppressed() {
        assertEquals(0.42, SnapshotVelocity.vertical(0.42, true));
    }
}
