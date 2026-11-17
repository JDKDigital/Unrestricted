package cy.jdkdigital.unrestricted.mixin.client;

import cy.jdkdigital.unrestricted.config.ClientConfig;
import net.minecraft.client.telemetry.ClientTelemetryManager;
import net.minecraft.client.telemetry.TelemetryEventSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientTelemetryManager.class)
public class MixinClientTelemetryManager {
    @Inject(method = "createEventSender", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onCreateEventSender(CallbackInfoReturnable<TelemetryEventSender> cir) {
        if (ClientConfig.DISABLE_TELEMETRY.get()) {
            cir.setReturnValue(TelemetryEventSender.DISABLED);
        }
    }
}
