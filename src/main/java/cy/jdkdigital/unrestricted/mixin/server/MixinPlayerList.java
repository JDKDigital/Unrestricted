package cy.jdkdigital.unrestricted.mixin.server;

import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    /**
     * @reason Remove "Not Secure" mark in server chat logs.
     * @author Aizistral
     */
    @Inject(method = "verifyChatTrusted", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onVerifyChatTrusted(PlayerChatMessage message, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
