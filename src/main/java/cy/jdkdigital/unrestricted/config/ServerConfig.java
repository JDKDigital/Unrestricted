package cy.jdkdigital.unrestricted.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue CONVERT_TO_GAME_MESSAGE = BUILDER
            .comment("Send player chat to clients as system messages instead of unsigned player chat. Chat is unreportable either way, since the server strips signatures.",
                    "On: players without Unrestricted see no \"Not Secure\" markers, but system messages lose their sender, so hiding a player and sender-based chat mods stop working.",
                    "Off: chat stays player chat. Players without Unrestricted see it as \"Not Secure\", or not at all with \"Only Show Secure Chat\" on, as they chose.")
            .define("convertToGameMessage", false);

    public static final ModConfigSpec.BooleanValue ALLOW_BANNED_ACCOUNTS = BUILDER
            .comment("Let in players whose client reports a multiplayer ban or name ban on their Microsoft account.",
                    "Only takes effect on offline-mode servers: on online-mode servers Mojang refuses banned accounts at login.")
            .define("allowBannedAccounts", false);

    public static final ModConfigSpec.BooleanValue ALLOW_MULTIPLAYER_DISABLED_ACCOUNTS = BUILDER
            .comment("Let in players whose client reports that multiplayer is turned off for their Microsoft account (usually a parental setting).",
                    "Only takes effect on offline-mode servers: on online-mode servers Mojang refuses these accounts at login.")
            .define("allowMultiplayerDisabledAccounts", false);

    public static final ModConfigSpec.BooleanValue ALLOW_CHAT_DISABLED_ACCOUNTS = BUILDER
            .comment("Let players whose Microsoft account has chat turned off use chat and commands on this server; the client blocks both with the same check. Needs Unrestricted on the client.",
                    "Chat can be off because a parent turned it off in Microsoft Family Safety, or because of a regional age-verification requirement (such as the UK's) the player hasn't completed.",
                    "The account data doesn't say which, so turning this on overrides both on this server.")
            .define("allowChatDisabledAccounts", false);

    public static final ModConfigSpec.BooleanValue REQUIRE_UNRESTRICTED_CLIENT = BUILDER
            .comment("Disconnect players whose client doesn't have Unrestricted.")
            .define("requireUnrestrictedClient", false);

    public static final ModConfigSpec.ConfigValue<String> REQUIRE_UNRESTRICTED_CLIENT_MESSAGE = BUILDER
            .comment("Disconnect message for players without Unrestricted. Shown as plain text, since their client doesn't have Unrestricted's translations.")
            .define("requireUnrestrictedClientMessage", "This server requires the Unrestricted mod on your client.");

    public static final ModConfigSpec SPEC = BUILDER.build();
}
