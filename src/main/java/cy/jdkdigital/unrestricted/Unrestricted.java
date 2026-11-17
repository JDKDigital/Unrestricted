package cy.jdkdigital.unrestricted;

import com.mojang.logging.LogUtils;
import cy.jdkdigital.unrestricted.config.ServerConfig;
import cy.jdkdigital.unrestricted.network.UnrestrictedNetwork;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Unrestricted.MODID)
public class Unrestricted {
    public static final String MODID = "unrestricted";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Unrestricted(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        modBus.addListener(UnrestrictedNetwork::registerPayloads);
        modBus.addListener(UnrestrictedNetwork::registerConfigurationTasks);
    }
}
