package cy.jdkdigital.unrestricted.mixin.server;

import cy.jdkdigital.unrestricted.config.ServerConfig;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class MixinServerCommonPacketListenerImpl {
    /**
     * @reason Convert player message to system message if mod is configured respectively.
     * This allows to circumvent signature check on client, as it only checks player messages.
     * @author JFronny (original implementation)
     * @author Aizistral
     */
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onSend(Packet<?> packet, CallbackInfo ci) {
        if ((Object) this instanceof ServerGamePacketListenerImpl listener
                && packet instanceof ClientboundPlayerChatPacket chat
                && ServerConfig.CONVERT_TO_GAME_MESSAGE.get()) {
            Component content = chat.unsignedContent() != null ? chat.unsignedContent() : Component.literal(chat.body().content());
            ci.cancel();
            listener.send(new ClientboundSystemChatPacket(chat.chatType().decorate(content), false));
        }
    }

    /**
     * @reason Ensure conversion works regardless of which send method is used.
     * @author Aizistral
     */
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onSend(Packet<?> packet, @Nullable ChannelFutureListener channelFutureListener, CallbackInfo ci) {
        if ((Object) this instanceof ServerGamePacketListenerImpl listener
                && packet instanceof ClientboundPlayerChatPacket chat
                && channelFutureListener != null
                && ServerConfig.CONVERT_TO_GAME_MESSAGE.get()) {
            ci.cancel();
            listener.send(chat);
        }
    }
}
