package cy.jdkdigital.unrestricted.mixin.client;

import cy.jdkdigital.unrestricted.client.ClientState;
import cy.jdkdigital.unrestricted.config.ClientConfig;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastComponent.class)
public class MixinToastComponent {
    @Inject(method = "addToast", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onAddToast(Toast toast, CallbackInfo ci) {
        if (ClientState.isOnUnrestrictedServer() && ClientConfig.HIDE_WARNING_TOAST.get()
                && toast instanceof SystemToast systemToast
                && systemToast.getToken() == SystemToast.SystemToastId.UNSECURE_SERVER_WARNING) {
            ci.cancel();
        }
    }
}
