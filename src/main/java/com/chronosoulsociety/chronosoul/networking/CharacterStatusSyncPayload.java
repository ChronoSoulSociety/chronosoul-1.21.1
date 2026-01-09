package com.chronosoulsociety.chronosoul.networking;

import net.minecraft.network.PacketByteBuf;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * 角色状态同步Payload
 */
public record CharacterStatusSyncPayload(boolean hasSelected, String characterId) implements CustomPayload {
    public static final Id<CharacterStatusSyncPayload> ID = new Id<>(ModPackets.SYNC_CHARACTER_STATUS);
    public static final PacketCodec<PacketByteBuf, CharacterStatusSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, CharacterStatusSyncPayload::hasSelected,
            PacketCodecs.STRING, CharacterStatusSyncPayload::characterId,
            CharacterStatusSyncPayload::new
    );
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    
    /**
     * 注册Payload类型
     */
    public static void register() {
        // 角色状态同步是服务器发给客户端的响应
        PayloadTypeRegistry.playS2C().register(ID, CODEC);
    }
}