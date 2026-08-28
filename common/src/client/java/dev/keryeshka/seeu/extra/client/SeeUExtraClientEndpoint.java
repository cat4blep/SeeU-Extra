package dev.keryeshka.seeu.extra.client;

import dev.keryeshka.seeu.extra.protocol.ClientOffer;
import dev.keryeshka.seeu.extra.protocol.EntitySnapshotPacket;
import dev.keryeshka.seeu.extra.protocol.ExtraPacketCodec;
import dev.keryeshka.voxyseeu.api.addon.AddonCloseReason;
import dev.keryeshka.voxyseeu.api.addon.ClientAddonEndpoint;
import dev.keryeshka.voxyseeu.api.addon.ClientAddonSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class SeeUExtraClientEndpoint implements ClientAddonEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger("SeeU Extra");

    private final ExtraEntityTracker tracker;
    private final ExtraEntityRenderer renderer;
    private final Supplier<ClientOffer> offerSupplier;
    private ClientAddonSession session;
    private ClientOffer lastSentOffer;
    private boolean loggedFirstSnapshot;

    public SeeUExtraClientEndpoint(
            ExtraEntityTracker tracker,
            ExtraEntityRenderer renderer,
            Supplier<ClientOffer> offerSupplier
    ) {
        this.tracker = tracker;
        this.renderer = renderer;
        this.offerSupplier = offerSupplier;
    }

    @Override
    public void onOpen(ClientAddonSession session, byte[] acknowledgementData) {
        clear();
        if (acknowledgementData.length != 0) {
            session.close();
            return;
        }
        this.session = session;
        this.lastSentOffer = null;
        LOGGER.info("Opened SeeU Extra addon session");
    }

    public ClientOffer synchronizeOffer() {
        ClientOffer currentOffer = offerSupplier.get();
        ClientAddonSession currentSession = session;
        if (currentSession == null || !currentSession.isOpen()) {
            return currentOffer;
        }

        if (currentOffer.equals(lastSentOffer)) {
            return currentOffer;
        }

        try {
            currentSession.send(ExtraPacketCodec.encodeClientOffer(currentOffer));
            lastSentOffer = currentOffer;
        } catch (RuntimeException | LinkageError failure) {
            if (currentSession.isOpen()) {
                currentSession.close();
            }
        }
        return currentOffer;
    }

    @Override
    public void onData(ClientAddonSession session, byte[] payload) {
        if (this.session != session) {
            return;
        }
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
        if (this.session == session) {
            clear();
        }
    }

    public void resetWorld() {
        tracker.clear();
        renderer.clear();
        loggedFirstSnapshot = false;
    }

    public void clear() {
        session = null;
        lastSentOffer = null;
        resetWorld();
    }
}
