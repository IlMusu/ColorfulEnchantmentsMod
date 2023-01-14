package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.networking.messages.EnchantmentColorMessage;
import com.ilmusu.colorfulenchantments.networking.messages._Message;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class ModMessages
{
    public static class ServerHandlers
    {
        public static void register()
        {

        }

        protected static void registerMessage(_Message message)
        {
            ServerPlayNetworking.registerGlobalReceiver(message.IDENTIFIER, (server, player, handler, buf, responseSender) ->
                    message.tryToDecodeAndHandle(server, () -> player, buf)
            );
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ClientHandlers
    {
        public static void register()
        {
            registerMessage(new EnchantmentColorMessage());
        }

        protected static void registerMessage(_Message message)
        {
            ClientPlayNetworking.registerGlobalReceiver(message.IDENTIFIER, (client, handler, buf, responseSender) ->
                    message.tryToDecodeAndHandle(client, () -> MinecraftClient.getInstance().player, buf)
            );
        }
    }
}
