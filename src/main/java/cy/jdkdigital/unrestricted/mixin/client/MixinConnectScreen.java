package cy.jdkdigital.unrestricted.mixin.client;

import cy.jdkdigital.unrestricted.client.ClientState;
import net.minecraft.client.gui.screens.ConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class MixinConnectScreen {
    @Inject(method = "startConnecting", at = @At("HEAD"))
    private static void unrestricted$onStartConnecting(CallbackInfo ci) {
        ClientState.reset();
    }
}
