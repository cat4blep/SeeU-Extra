package dev.keryeshka.seeu.extra.server;

final class SnapshotVelocity {
    private SnapshotVelocity() {
    }

    static double vertical(double velocityY, boolean onGround) {
        return onGround && velocityY < 0.0D ? 0.0D : velocityY;
    }
}
