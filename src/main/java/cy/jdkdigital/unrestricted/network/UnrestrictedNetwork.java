package cy.jdkdigital.unrestricted.network;

import cy.jdkdigital.unrestricted.Unrestricted;
import cy.jdkdigital.unrestricted.client.ClientPayloadHandler;
import cy.jdkdigital.unrestricted.config.ServerConfig;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.Nullable;

public class UnrestrictedNetwork {
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1").optional();
        registrar.configurationToClient(ServerSettingsPayload.TYPE, ServerSettingsPayload.STREAM_CODEC,
                (payload, context) -> ClientPayloadHandler.handleServerSettings(payload, context));
        registrar.configurationToServer(AccountReportPayload.TYPE, AccountReportPayload.STREAM_CODEC, UnrestrictedNetwork::handleAccountReport);
    }

    public static void registerConfigurationTasks(RegisterConfigurationTasksEvent event) {
        event.register(new AccountCheckTask(event.getListener()));
    }

    private static void handleAccountReport(AccountReportPayload report, IPayloadContext context) {
        Component refusal = refusalFor(report);
        if (refusal != null && !context.connection().isMemoryConnection()) {
            Unrestricted.LOGGER.info("Refused {}: {}", context.connection().getLoggableAddress(true), refusal.getString());
            context.disconnect(refusal);
        } else {
            context.finishCurrentTask(AccountCheckTask.TYPE);
        }
    }

    private static @Nullable Component refusalFor(AccountReportPayload report) {
        if (!report.hasAccount()) {
            return null;
        }
        if ((report.multiplayerBanned() || report.nameBanned()) && !ServerConfig.ALLOW_BANNED_ACCOUNTS.get()) {
            return Component.translatable("disconnect.unrestricted.banned");
        }
        if (!report.multiplayerAllowed() && !ServerConfig.ALLOW_MULTIPLAYER_DISABLED_ACCOUNTS.get()) {
            return Component.translatable("disconnect.unrestricted.multiplayer_disabled");
        }
        return null;
    }
}
