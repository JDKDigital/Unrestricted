package cy.jdkdigital.unrestricted.client;

import com.mojang.authlib.minecraft.BanDetails;
import com.mojang.authlib.minecraft.UserApiService;
import cy.jdkdigital.unrestricted.api.AccountFlags;
import cy.jdkdigital.unrestricted.api.MultiplayerBan;
import cy.jdkdigital.unrestricted.network.AccountReportPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

import java.util.Optional;

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
            return new AccountReportPayload(AccountFlags.NO_ACCOUNT);
        }
        UserApiService.UserProperties properties = minecraft.userProperties();
        return new AccountReportPayload(new AccountFlags(
                true,
                Optional.ofNullable(minecraft.multiplayerBan()).map(AccountStatus::toMultiplayerBan),
                minecraft.isNameBanned(),
                properties.flag(UserApiService.UserFlag.SERVERS_ALLOWED),
                properties.flag(UserApiService.UserFlag.CHAT_ALLOWED)
        ));
    }

    private static MultiplayerBan toMultiplayerBan(BanDetails ban) {
        return new MultiplayerBan(Optional.ofNullable(ban.expires()), Optional.ofNullable(ban.reason()), Optional.ofNullable(ban.reasonMessage()));
    }
}
