package cy.jdkdigital.unrestricted.mixin.client;

import cy.jdkdigital.unrestricted.client.ClientState;
import cy.jdkdigital.unrestricted.config.ClientConfig;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiMessageTag.class)
public class MixinGuiMessageTag {
    /**
     * @reason Remove gray bar as indication besides system messages, if mod is configured
     * respectively. The mod can convert all player messages to system messages when
     * installed on server, so we don't need that most of the time.
     * @author Aizistral
     */
    @Inject(method = {"system", "systemSinglePlayer"}, at = @At("HEAD"), cancellable = true)
    private static void unrestricted$onSystem(CallbackInfoReturnable<GuiMessageTag> cir) {
        if (ClientState.isOnUnrestrictedServer() && ClientConfig.HIDE_SYSTEM_MESSAGE_INDICATORS.get()) {
            cir.setReturnValue(null);
        }
    }
}
