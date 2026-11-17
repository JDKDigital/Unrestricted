package cy.jdkdigital.unrestricted.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue WARN_ON_REPORTABLE_SERVER = BUILDER
            .comment("When joining a multiplayer server without Unrestricted, show a chat line saying that chat on that server can still be reported.")
            .define("warnOnReportableServer", true);

    public static final ModConfigSpec.BooleanValue HIDE_WARNING_TOAST = BUILDER
            .comment("Hide the \"chat messages can't be verified\" toast shown when joining.")
            .define("hideWarningToast", true);

    public static final ModConfigSpec.BooleanValue HIDE_SYSTEM_MESSAGE_INDICATORS = BUILDER
            .comment("Hide the grey bar next to system messages. The server sends player chat as system messages, so most chat would carry it.")
            .define("hideSystemMessageIndicators", true);

    public static final ModConfigSpec.BooleanValue HIDE_INSECURE_MESSAGE_INDICATORS = BUILDER
            .comment("Hide the \"Not Secure\" indicator on other players' messages. The server strips signatures, so every player message would carry it.")
            .define("hideInsecureMessageIndicators", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
