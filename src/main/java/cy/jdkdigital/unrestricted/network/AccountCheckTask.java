package cy.jdkdigital.unrestricted.network;

import cy.jdkdigital.unrestricted.Unrestricted;
import cy.jdkdigital.unrestricted.config.ServerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ConfigurationTask;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;

import java.util.function.Consumer;

public record AccountCheckTask(ServerConfigurationPacketListener listener) implements ICustomConfigurationTask {
    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type(ResourceLocation.fromNamespaceAndPath(Unrestricted.MODID, "account_check"));

    @Override
    public void run(Consumer<CustomPacketPayload> sender) {
        if (this.listener.hasChannel(AccountReportPayload.TYPE)) {
            sender.accept(new ServerSettingsPayload(ServerConfig.ALLOW_CHAT_DISABLED_ACCOUNTS.get()));
        } else if (ServerConfig.REQUIRE_UNRESTRICTED_CLIENT.get() && !this.listener.getConnection().isMemoryConnection()) {
            this.listener.disconnect(Component.literal(ServerConfig.REQUIRE_UNRESTRICTED_CLIENT_MESSAGE.get()));
        } else {
            this.listener.finishCurrentTask(TYPE);
        }
    }

    @Override
    public ConfigurationTask.Type type() {
        return TYPE;
    }
}
