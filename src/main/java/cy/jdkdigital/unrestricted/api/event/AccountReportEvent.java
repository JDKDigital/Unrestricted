package cy.jdkdigital.unrestricted.api.event;

import com.mojang.authlib.GameProfile;
import cy.jdkdigital.unrestricted.api.AccountFlags;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AccountReportEvent extends Event {
    private final GameProfile profile;
    private final AccountFlags flags;
    private final boolean localOwner;
    @Nullable
    private Component refusal;

    public AccountReportEvent(GameProfile profile, AccountFlags flags, boolean localOwner, @Nullable Component refusal) {
        this.profile = profile;
        this.flags = flags;
        this.localOwner = localOwner;
        this.refusal = refusal;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public AccountFlags getFlags() {
        return this.flags;
    }

    public boolean isLocalOwner() {
        return this.localOwner;
    }

    public Optional<Component> getRefusal() {
        return Optional.ofNullable(this.refusal);
    }

    public void refuse(Component reason) {
        this.refusal = reason;
    }

    public void allow() {
        this.refusal = null;
    }
}
