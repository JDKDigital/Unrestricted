package cy.jdkdigital.unrestricted.api;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.time.Instant;
import java.util.Optional;

public record MultiplayerBan(Optional<Instant> expires, Optional<String> reason, Optional<String> reasonMessage) {
    private static final StreamCodec<ByteBuf, Instant> INSTANT = ByteBufCodecs.VAR_LONG.map(Instant::ofEpochMilli, Instant::toEpochMilli);

    public static final StreamCodec<ByteBuf, MultiplayerBan> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(INSTANT), MultiplayerBan::expires,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), MultiplayerBan::reason,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), MultiplayerBan::reasonMessage,
            MultiplayerBan::new
    );

    public boolean isPermanent() {
        return this.expires.isEmpty();
    }
}
