package cy.jdkdigital.unrestricted.network;

import cy.jdkdigital.unrestricted.Unrestricted;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ServerSettingsPayload(boolean allowChatDisabledAccounts) implements CustomPacketPayload {
    public static final Type<ServerSettingsPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unrestricted.MODID, "server_settings"));

    public static final StreamCodec<ByteBuf, ServerSettingsPayload> STREAM_CODEC = ByteBufCodecs.BOOL.map(
            ServerSettingsPayload::new,
            ServerSettingsPayload::allowChatDisabledAccounts
    );

    @Override
    public Type<ServerSettingsPayload> type() {
        return TYPE;
    }
}
