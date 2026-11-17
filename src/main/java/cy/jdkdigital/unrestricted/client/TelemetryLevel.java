package cy.jdkdigital.unrestricted.client;

import cy.jdkdigital.unrestricted.config.ClientConfig;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;

public enum TelemetryLevel {
    NONE("options.telemetry.state.none"),
    MINIMAL("options.telemetry.state.minimal"),
    ALL("options.telemetry.state.all");

    private final String translationKey;

    TelemetryLevel(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component caption() {
        return Component.translatable(this.translationKey);
    }

    public static TelemetryLevel current(Options options) {
        if (ClientConfig.DISABLE_TELEMETRY.get()) {
            return NONE;
        }
        return options.telemetryOptInExtra().get() ? ALL : MINIMAL;
    }

    public void apply(Options options) {
        ClientConfig.DISABLE_TELEMETRY.set(this == NONE);
        ClientConfig.DISABLE_TELEMETRY.save();
        options.telemetryOptInExtra().set(this == ALL);
        options.save();
    }
}
