package cy.jdkdigital.unrestricted.mixin.server;

import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerboundChatCommandSignedPacket.class)
public class MixinServerboundChatCommandSignedPacket {
    @Inject(method = "argumentSignatures", at = @At("RETURN"), cancellable = true)
    private void unrestricted$onGetSignatures(CallbackInfoReturnable<ArgumentSignatures> cir) {
        cir.setReturnValue(ArgumentSignatures.EMPTY);
    }
}
