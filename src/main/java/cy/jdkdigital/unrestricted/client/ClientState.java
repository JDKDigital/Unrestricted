package cy.jdkdigital.unrestricted.client;

public class ClientState {
    private static boolean onUnrestrictedServer = false;
    private static boolean allowChatDisabledAccounts = false;

    public static void joinedUnrestrictedServer(boolean serverAllowsChatDisabledAccounts) {
        onUnrestrictedServer = true;
        allowChatDisabledAccounts = serverAllowsChatDisabledAccounts;
    }

    public static void reset() {
        onUnrestrictedServer = false;
        allowChatDisabledAccounts = false;
    }

    public static boolean isOnUnrestrictedServer() {
        return onUnrestrictedServer;
    }

    public static boolean allowsChatDisabledAccounts() {
        return onUnrestrictedServer && allowChatDisabledAccounts;
    }
}
