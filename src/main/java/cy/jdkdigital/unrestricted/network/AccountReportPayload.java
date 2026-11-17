package cy.jdkdigital.unrestricted.network;

import cy.jdkdigital.unrestricted.Unrestricted;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AccountReportPayload(boolean hasAccount, boolean multiplayerBanned, boolean nameBanned, boolean multiplayerAllowed, boolean chatAllowed) implements CustomPacketPayload {
    public static final Type<AccountReportPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Unrestricted.MODID, "account_report"));

    public static final StreamCodec<ByteBuf, AccountReportPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, AccountReportPayload::hasAccount,
            ByteBufCodecs.BOOL, AccountReportPayload::multiplayerBanned,
            ByteBufCodecs.BOOL, AccountReportPayload::nameBanned,
            ByteBufCodecs.BOOL, AccountReportPayload::multiplayerAllowed,
            ByteBufCodecs.BOOL, AccountReportPayload::chatAllowed,
            AccountReportPayload::new
    );

    @Override
    public Type<AccountReportPayload> type() {
        return TYPE;
    }
}
