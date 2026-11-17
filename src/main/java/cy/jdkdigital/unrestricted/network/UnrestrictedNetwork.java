package cy.jdkdigital.unrestricted.network;

import cy.jdkdigital.unrestricted.Unrestricted;
import cy.jdkdigital.unrestricted.api.AccountFlags;
import cy.jdkdigital.unrestricted.api.event.AccountReportEvent;
import cy.jdkdigital.unrestricted.client.ClientPayloadHandler;
import cy.jdkdigital.unrestricted.config.ServerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
        if (!(context.listener() instanceof ServerConfigurationPacketListenerImpl configuration)) {
            context.finishCurrentTask(AccountCheckTask.TYPE);
            return;
        }
        AccountFlags flags = report.flags();
        boolean localOwner = context.connection().isMemoryConnection();
        AccountReportEvent event = NeoForge.EVENT_BUS.post(new AccountReportEvent(configuration.getOwner(), flags, localOwner, refusalFor(flags)));
        String name = event.getProfile().name();
        Optional<Component> refusal = event.getRefusal();
        if (refusal.isPresent() && !localOwner) {
            Unrestricted.LOGGER.info("Refused {}: {}", name, refusal.get().getString());
            context.disconnect(refusal.get());
            return;
        }
        if (flags.isBanned()) {
            Unrestricted.LOGGER.info("{} joined with a banned account: {}", name, flags);
        }
        context.finishCurrentTask(AccountCheckTask.TYPE);
    }

    private static @Nullable Component refusalFor(AccountFlags flags) {
        if (!flags.hasAccount()) {
            return null;
        }
        if (flags.isBanned() && !ServerConfig.ALLOW_BANNED_ACCOUNTS.get()) {
            return Component.translatable("disconnect.unrestricted.banned");
        }
        if (!flags.multiplayerAllowed() && !ServerConfig.ALLOW_MULTIPLAYER_DISABLED_ACCOUNTS.get()) {
            return Component.translatable("disconnect.unrestricted.multiplayer_disabled");
        }
        return null;
    }
}
