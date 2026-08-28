package dev.keryeshka.seeu.extra.protocol;

public final class ExtraProtocol {
    public static final int VERSION = 2;
    public static final int MAX_SNAPSHOTS = 1024;
    public static final int MAXIMUM_DISTANCE_BLOCKS = 32_768;
    public static final int MAX_UPDATE_INTERVAL_TICKS = 1200;
    public static final int MAX_PACKET_BYTES = 1_048_576;
    public static final int MAX_RESOURCE_IDENTIFIER_BYTES = 256;
    public static final int MAX_POSE_BYTES = 64;
    public static final int MAX_ITEM_COUNT = 99;
    public static final double MAX_ABSOLUTE_COORDINATE = 30_000_000.0D;
    public static final double MAX_ABSOLUTE_VELOCITY = 4096.0D;
    public static final int ENTITY_VIEW_SCALE_DENOMINATOR = 1024;
    public static final int MIN_ENTITY_VIEW_SCALE_Q10 = 512;
    public static final int MAX_ENTITY_VIEW_SCALE_Q10 = 12_800;

    private ExtraProtocol() {
    }
}
