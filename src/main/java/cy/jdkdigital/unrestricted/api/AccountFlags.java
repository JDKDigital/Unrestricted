package cy.jdkdigital.unrestricted.api;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record AccountFlags(boolean hasAccount, Optional<MultiplayerBan> multiplayerBan, boolean nameBanned, boolean multiplayerAllowed, boolean chatAllowed) {
    public static final AccountFlags NO_ACCOUNT = new AccountFlags(false, Optional.empty(), false, true, true);

    public static final StreamCodec<ByteBuf, AccountFlags> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, AccountFlags::hasAccount,
            ByteBufCodecs.optional(MultiplayerBan.STREAM_CODEC), AccountFlags::multiplayerBan,
            ByteBufCodecs.BOOL, AccountFlags::nameBanned,
            ByteBufCodecs.BOOL, AccountFlags::multiplayerAllowed,
            ByteBufCodecs.BOOL, AccountFlags::chatAllowed,
            AccountFlags::new
    );

    public boolean isMultiplayerBanned() {
        return this.multiplayerBan.isPresent();
    }

    public boolean isBanned() {
        return this.isMultiplayerBanned() || this.nameBanned;
    }
}
