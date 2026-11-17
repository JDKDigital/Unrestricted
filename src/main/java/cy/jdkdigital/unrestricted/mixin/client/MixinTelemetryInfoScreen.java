package cy.jdkdigital.unrestricted.mixin.client;

import cy.jdkdigital.unrestricted.client.TelemetryLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TelemetryInfoScreen.class)
public class MixinTelemetryInfoScreen {
    @Shadow
    @Final
    private Options options;

    @Shadow
    private void onOptInChanged(AbstractWidget widget, boolean optIn) {
        throw new AssertionError("Replaced by the @Shadow target");
    }

    @Inject(method = "createTelemetryCheckbox", at = @At("HEAD"), cancellable = true)
    private void unrestricted$createDataCollectionButton(CallbackInfoReturnable<AbstractWidget> cir) {
        List<TelemetryLevel> levels = Minecraft.getInstance().extraTelemetryAvailable()
                ? List.of(TelemetryLevel.NONE, TelemetryLevel.MINIMAL, TelemetryLevel.ALL)
                : List.of(TelemetryLevel.NONE, TelemetryLevel.MINIMAL);
        TelemetryLevel current = TelemetryLevel.current(this.options);
        CycleButton<TelemetryLevel> button = CycleButton.builder(TelemetryLevel::caption)
                .withValues(levels)
                .withInitialValue(levels.contains(current) ? current : TelemetryLevel.MINIMAL)
                .withTooltip(level -> Tooltip.create(Component.translatable("options.telemetry.button.tooltip", TelemetryLevel.MINIMAL.caption(), TelemetryLevel.ALL.caption())))
                .create(0, 0, 200, 20, Component.translatable("options.telemetry.button"), (cycleButton, level) -> {
                    level.apply(this.options);
                    this.onOptInChanged(cycleButton, level == TelemetryLevel.ALL);
                });
        cir.setReturnValue(button);
    }
}
