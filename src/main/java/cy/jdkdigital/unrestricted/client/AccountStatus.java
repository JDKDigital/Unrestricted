package cy.jdkdigital.unrestricted.client;

import com.mojang.authlib.minecraft.UserApiService;
import cy.jdkdigital.unrestricted.network.AccountReportPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

public class AccountStatus {
    public static boolean hasAccount(Minecraft minecraft) {
        return minecraft.getUser().getType() == User.Type.MSA;
    }

    public static boolean accountAllowsMultiplayer(Minecraft minecraft) {
        return minecraft.userProperties().flag(UserApiService.UserFlag.SERVERS_ALLOWED)
                && minecraft.multiplayerBan() == null
                && !minecraft.isNameBanned();
    }

    public static AccountReportPayload report(Minecraft minecraft) {
        if (!hasAccount(minecraft)) {
            return new AccountReportPayload(false, false, false, true, true);
        }
        UserApiService.UserProperties properties = minecraft.userProperties();
        return new AccountReportPayload(
                true,
                minecraft.multiplayerBan() != null,
                minecraft.isNameBanned(),
                properties.flag(UserApiService.UserFlag.SERVERS_ALLOWED),
                properties.flag(UserApiService.UserFlag.CHAT_ALLOWED)
        );
    }
}
