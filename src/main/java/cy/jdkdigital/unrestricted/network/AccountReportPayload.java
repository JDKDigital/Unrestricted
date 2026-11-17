package cy.jdkdigital.unrestricted.network;

import cy.jdkdigital.unrestricted.Unrestricted;
import cy.jdkdigital.unrestricted.api.AccountFlags;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AccountReportPayload(AccountFlags flags) implements CustomPacketPayload {
    public static final Type<AccountReportPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Unrestricted.MODID, "account_report"));

    public static final StreamCodec<ByteBuf, AccountReportPayload> STREAM_CODEC = AccountFlags.STREAM_CODEC.map(AccountReportPayload::new, AccountReportPayload::flags);

    @Override
    public Type<AccountReportPayload> type() {
        return TYPE;
    }
}
