package cy.jdkdigital.unrestricted.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import cy.jdkdigital.unrestricted.client.AccountStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {
    @WrapOperation(method = "createNormalMenuOptions",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/Button;active:Z", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void unrestricted$setRealmsButtonActive(Button button, boolean active, Operation<Void> original) {
        original.call(button, active && AccountStatus.accountAllowsMultiplayer(Minecraft.getInstance()));
    }
}
