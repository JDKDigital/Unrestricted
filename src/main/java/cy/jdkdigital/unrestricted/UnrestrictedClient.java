package cy.jdkdigital.unrestricted;

import cy.jdkdigital.unrestricted.client.ClientState;
import cy.jdkdigital.unrestricted.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Unrestricted.MODID, dist = Dist.CLIENT)
public class UnrestrictedClient {
    public UnrestrictedClient(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.addListener(UnrestrictedClient::onLoggingIn);
        NeoForge.EVENT_BUS.addListener(UnrestrictedClient::onLoggingOut);
    }

    private static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!ClientState.isOnUnrestrictedServer() && !minecraft.isLocalServer() && ClientConfig.WARN_ON_REPORTABLE_SERVER.get()) {
            minecraft.gui.getChat().addMessage(Component.translatable("chat.unrestricted.reportable_server").withStyle(ChatFormatting.YELLOW));
        }
    }

    private static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientState.reset();
    }
}
