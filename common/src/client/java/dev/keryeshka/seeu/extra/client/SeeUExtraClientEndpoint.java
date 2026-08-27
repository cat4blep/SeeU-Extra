package dev.keryeshka.seeu.extra.client;

import dev.keryeshka.seeu.extra.protocol.EntitySnapshotPacket;
import dev.keryeshka.seeu.extra.protocol.ExtraPacketCodec;
import dev.keryeshka.voxyseeu.api.addon.AddonCloseReason;
import dev.keryeshka.voxyseeu.api.addon.ClientAddonEndpoint;
import dev.keryeshka.voxyseeu.api.addon.ClientAddonSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SeeUExtraClientEndpoint implements ClientAddonEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger("SeeU Extra");

    private final ExtraEntityTracker tracker;
    private final ExtraEntityRenderer renderer;
    private boolean loggedFirstSnapshot;

    public SeeUExtraClientEndpoint(ExtraEntityTracker tracker, ExtraEntityRenderer renderer) {
        this.tracker = tracker;
        this.renderer = renderer;
    }

    @Override
    public void onOpen(ClientAddonSession session, byte[] acknowledgementData) {
        clear();
        if (acknowledgementData.length != 0) {
            session.close();
            return;
        }
        LOGGER.info("Opened SeeU Extra addon session");
    }

    @Override
    public void onData(ClientAddonSession session, byte[] payload) {
        try {
            EntitySnapshotPacket packet = ExtraPacketCodec.decodeSnapshotPacket(payload);
            tracker.apply(packet);
            if (!loggedFirstSnapshot) {
                LOGGER.info(
                        "Received first SeeU Extra snapshot: dimension={}, entities={}",
                        packet.dimensionKey(),
                        packet.snapshots().size()
                );
                loggedFirstSnapshot = true;
            }
        } catch (IllegalArgumentException exception) {
            session.close();
        }
    }

    @Override
    public void onClose(ClientAddonSession session, AddonCloseReason reason) {
        clear();
    }

    public void clear() {
        tracker.clear();
        renderer.clear();
        loggedFirstSnapshot = false;
    }
}
