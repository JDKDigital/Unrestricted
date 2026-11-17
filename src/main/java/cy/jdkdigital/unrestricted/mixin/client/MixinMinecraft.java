package cy.jdkdigital.unrestricted.mixin.client;

import com.mojang.authlib.minecraft.UserApiService;
import cy.jdkdigital.unrestricted.client.AccountStatus;
import cy.jdkdigital.unrestricted.client.ClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.client.multiplayer.chat.ChatAbilities;
import net.minecraft.client.multiplayer.chat.ChatRestriction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    @Final
    private boolean allowsMultiplayer;

    @Inject(method = "allowsMultiplayer", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onAllowsMultiplayer(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Minecraft minecraft && AccountStatus.hasAccount(minecraft)) {
            cir.setReturnValue(this.allowsMultiplayer && minecraft.userProperties().flag(UserApiService.UserFlag.SERVERS_ALLOWED));
        }
    }

    @Inject(method = "computeChatAbilities", at = @At("RETURN"), cancellable = true)
    private void unrestricted$onComputeChatAbilities(CallbackInfoReturnable<ChatAbilities> cir) {
        ChatAbilities abilities = cir.getReturnValue();
        if (ClientState.allowsChatDisabledAccounts() && abilities.restrictions().anyMatch(restriction -> restriction == ChatRestriction.DISABLED_BY_PROFILE)) {
            ChatAbilities.Builder builder = new ChatAbilities.Builder();
            abilities.restrictions()
                    .filter(restriction -> restriction != ChatRestriction.DISABLED_BY_PROFILE)
                    .forEach(builder::addRestriction);
            cir.setReturnValue(builder.build());
        }
    }

    @Inject(method = "getProfileKeyPairManager", at = @At("HEAD"), cancellable = true)
    private void unrestricted$onGetProfileKeyPairManager(CallbackInfoReturnable<ProfileKeyPairManager> cir) {
        if (ClientState.isOnUnrestrictedServer()) {
            cir.setReturnValue(ProfileKeyPairManager.EMPTY_KEY_MANAGER);
        }
    }
}
