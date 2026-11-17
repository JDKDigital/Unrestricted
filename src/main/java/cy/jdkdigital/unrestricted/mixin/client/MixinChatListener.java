package cy.jdkdigital.unrestricted.mixin.client;

import cy.jdkdigital.unrestricted.client.ClientState;
import cy.jdkdigital.unrestricted.config.ClientConfig;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;
import java.util.UUID;

@Mixin(ChatListener.class)
public class MixinChatListener {
    @Shadow
    private boolean isSenderLocalPlayer(UUID sender) {
        throw new AssertionError("Replaced by the @Shadow target");
    }

    /**
     * @reason Removes "Not Secure" and "Modified" statuses of chat messages. They ultimately
     * serve no purpose but to annoy the user and scare them away from servers that actually
     * try to protect them by stripping message signatures.
     * @author Aizistral
     */
    @Inject(method = "evaluateTrustLevel", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onEvaluateTrustLevel(PlayerChatMessage message, Component decorated, Instant received, CallbackInfoReturnable<ChatTrustLevel> cir) {
        if (!ClientState.isOnUnrestrictedServer() || !ClientConfig.HIDE_INSECURE_MESSAGE_INDICATORS.get() || this.isSenderLocalPlayer(message.sender())) {
            return;
        }
        if (ChatTrustLevel.evaluate(message, decorated, received) == ChatTrustLevel.NOT_SECURE) {
            cir.setReturnValue(ChatTrustLevel.SECURE);
        }
    }
}
