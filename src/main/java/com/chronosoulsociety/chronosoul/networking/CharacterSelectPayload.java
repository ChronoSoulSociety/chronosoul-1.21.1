package com.chronosoulsociety.chronosoul.networking;

import net.minecraft.network.PacketByteBuf;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * 角色选择请求Payload
 */
public record CharacterSelectPayload(String characterId) implements CustomPayload {
    public static final Id<CharacterSelectPayload> ID = new Id<>(ModPackets.SELECT_CHARACTER);
    public static final PacketCodec<PacketByteBuf, CharacterSelectPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, CharacterSelectPayload::characterId,
            CharacterSelectPayload::new
    );
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    
    /**
     * 注册Payload类型
     */
    public static void register() {
        // 角色选择是客户端发给服务器的请求
        PayloadTypeRegistry.playC2S().register(ID, CODEC);
    }
}