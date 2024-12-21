package you.yuli.noserverpacks.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ResourcePackSendS2CPacket.class)
public abstract class ResourcePackSendS2CPacketMixin {
    @Shadow @Final private UUID id;

    @Shadow @Final private String url;

    @Inject(method = "apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void apply(ClientCommonPacketListener clientCommonPacketListener, CallbackInfo ci) {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new ResourcePackStatusC2SPacket(id, ResourcePackStatusC2SPacket.Status.ACCEPTED));
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new ResourcePackStatusC2SPacket(id, ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
        }

        String message = "§aBypassing server resource pack (" + url + ")";
        System.out.println(message);
        if (MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().player.sendMessage(Text.of(message));

        ci.cancel();
    }
}