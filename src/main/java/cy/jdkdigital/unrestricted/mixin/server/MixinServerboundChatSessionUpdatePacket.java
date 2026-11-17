package cy.jdkdigital.unrestricted.mixin.server;

import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundChatSessionUpdatePacket.class)
public class MixinServerboundChatSessionUpdatePacket {
    /**
     * @reason Ignore chat sessions from client, since we throw away signatures anyways.
     * @author Aizistral
     */
    @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onHandle(ServerGamePacketListener listener, CallbackInfo ci) {
        ci.cancel();
    }
}
