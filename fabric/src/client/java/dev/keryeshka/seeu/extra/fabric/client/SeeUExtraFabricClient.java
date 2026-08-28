package dev.keryeshka.seeu.extra.fabric.client;

import dev.keryeshka.seeu.extra.SeeUExtra;
import dev.keryeshka.seeu.extra.client.ExtraEntityRenderer;
import dev.keryeshka.seeu.extra.client.ExtraEntityTracker;
import dev.keryeshka.seeu.extra.client.SeeUExtraClientConfig;
import dev.keryeshka.seeu.extra.client.SeeUExtraClientEndpoint;
import dev.keryeshka.seeu.extra.protocol.ClientOffer;
import dev.keryeshka.seeu.extra.protocol.ExtraPacketCodec;
import dev.keryeshka.voxyseeu.api.addon.SeeUClientAddons;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

public final class SeeUExtraFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SeeUExtraClientConfig config = SeeUExtraClientConfig.load(FabricLoader.getInstance().getConfigDir());
        ExtraEntityTracker tracker = new ExtraEntityTracker();
        ExtraEntityRenderer renderer = new ExtraEntityRenderer(tracker);
        Supplier<ClientOffer> offerSupplier = config::offer;
        SeeUExtraClientEndpoint endpoint = new SeeUExtraClientEndpoint(tracker, renderer, offerSupplier);

        SeeUClientAddons.getInstance().register(
                SeeUExtra.DESCRIPTOR,
                () -> ExtraPacketCodec.encodeClientOffer(offerSupplier.get()),
                endpoint
        );
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> endpoint.resetWorld());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> endpoint.clear());
        LevelRenderEvents.COLLECT_SUBMITS.register(context -> {
            ClientOffer offer = endpoint.synchronizeOffer();
            renderer.render(
                    context.poseStack(),
                    context.levelState(),
                    context.submitNodeCollector(),
                    offer
            );
        });
    }
}
