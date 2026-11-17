package cy.jdkdigital.unrestricted.mixin.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntry.class)
public class MixinPlayerEntry {
    @Shadow
    private Button reportButton;

    /**
     * @reason Disable (or hide if configured respectively) chat report button on client.
     * @author Aizistral
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void unrestricted$onConstructed(CallbackInfo ci) {
        if (this.reportButton != null) {
            this.reportButton.visible = false;
            this.reportButton.active = false;
        }
    }
}
