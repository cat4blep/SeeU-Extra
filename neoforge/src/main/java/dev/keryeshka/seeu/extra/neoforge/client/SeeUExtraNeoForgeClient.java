package dev.keryeshka.seeu.extra.neoforge.client;

import dev.keryeshka.seeu.extra.SeeUExtra;
import dev.keryeshka.seeu.extra.client.ExtraEntityRenderer;
import dev.keryeshka.seeu.extra.client.ExtraEntityTracker;
import dev.keryeshka.seeu.extra.client.SeeUExtraClientConfig;
import dev.keryeshka.seeu.extra.client.SeeUExtraClientEndpoint;
import dev.keryeshka.seeu.extra.protocol.ClientOffer;
import dev.keryeshka.seeu.extra.protocol.ExtraPacketCodec;
import dev.keryeshka.voxyseeu.api.addon.SeeUClientAddons;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

import java.util.function.Supplier;

@Mod(value = SeeUExtra.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = SeeUExtra.MOD_ID, value = Dist.CLIENT)
public final class SeeUExtraNeoForgeClient {
    private static SeeUExtraClientEndpoint endpoint;
    private static ExtraEntityRenderer renderer;

    public SeeUExtraNeoForgeClient() {
        SeeUExtraClientConfig config = SeeUExtraClientConfig.load(FMLPaths.CONFIGDIR.get());
        ExtraEntityTracker tracker = new ExtraEntityTracker();
        renderer = new ExtraEntityRenderer(tracker);
        Supplier<ClientOffer> offerSupplier = config::offer;
        endpoint = new SeeUExtraClientEndpoint(tracker, renderer, offerSupplier);
        SeeUClientAddons.getInstance().register(
                SeeUExtra.DESCRIPTOR,
                () -> ExtraPacketCodec.encodeClientOffer(offerSupplier.get()),
                endpoint
        );
    }

    @SubscribeEvent
    public static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        if (endpoint != null) {
            endpoint.resetWorld();
        }
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        clear();
    }

    @SubscribeEvent
    public static void onRenderLevel(SubmitCustomGeometryEvent event) {
        if (renderer == null) {
            return;
        }
        ClientOffer offer = endpoint.synchronizeOffer();
        renderer.render(
                event.getPoseStack(),
                event.getLevelRenderState(),
                event.getSubmitNodeCollector(),
                offer
        );
    }

    private static void clear() {
        if (endpoint != null) {
            endpoint.clear();
        }
    }
}
