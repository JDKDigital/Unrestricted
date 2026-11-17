package cy.jdkdigital.unrestricted.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import cy.jdkdigital.unrestricted.client.TelemetryLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TelemetryInfoScreen.class)
public class MixinTelemetryInfoScreen {
    @Shadow
    @Final
    private Options options;

    @Shadow
    private void onOptInChanged(AbstractWidget widget, boolean value) {
        throw new AssertionError("Replaced by the @Shadow target");
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;<init>(Lnet/minecraft/client/gui/screens/Screen;II)V"), index = 2)
    private int unrestricted$footerHeight(int footerHeight) {
        return HeaderAndFooterLayout.DEFAULT_HEADER_AND_FOOTER_HEIGHT + 24;
    }

    @ModifyExpressionValue(method = "init", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/telemetry/TelemetryInfoScreen;EXTRA_TELEMETRY_AVAILABLE:Z"))
    private boolean unrestricted$skipOptInCheckbox(boolean extraTelemetryAvailable) {
        return false;
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;horizontal()Lnet/minecraft/client/gui/layouts/LinearLayout;", ordinal = 1))
    private void unrestricted$addDataCollectionButton(CallbackInfo ci, @Local(ordinal = 2) LinearLayout footer) {
        List<TelemetryLevel> levels = Minecraft.getInstance().extraTelemetryAvailable()
                ? List.of(TelemetryLevel.NONE, TelemetryLevel.MINIMAL, TelemetryLevel.ALL)
                : List.of(TelemetryLevel.NONE, TelemetryLevel.MINIMAL);
        TelemetryLevel current = TelemetryLevel.current(this.options);
        CycleButton<TelemetryLevel> button = CycleButton.builder(TelemetryLevel::caption, levels.contains(current) ? current : TelemetryLevel.MINIMAL)
                .withValues(levels)
                .withTooltip(level -> Tooltip.create(Component.translatable("options.telemetry.button.tooltip", TelemetryLevel.MINIMAL.caption(), TelemetryLevel.ALL.caption())))
                .create(0, 0, 200, 20, Component.translatable("options.telemetry.button"), (cycleButton, level) -> {
                    level.apply(this.options);
                    this.onOptInChanged(cycleButton, level == TelemetryLevel.ALL);
                });
        footer.addChild(button);
    }
}
