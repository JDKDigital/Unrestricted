package cy.jdkdigital.unrestricted.client;

import cy.jdkdigital.unrestricted.network.ServerSettingsPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleServerSettings(ServerSettingsPayload payload, IPayloadContext context) {
        ClientState.joinedUnrestrictedServer(payload.allowChatDisabledAccounts());
        context.reply(AccountStatus.report(Minecraft.getInstance()));
    }
}
