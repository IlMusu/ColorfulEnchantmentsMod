package com.ilmusu.colorfulenchantments.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.RenderLayer;

import java.util.ArrayList;
import java.util.List;

public interface BufferBuilderCreateCallback
{
    // Called AFTER the registration of the vanilla BufferBuilders
    Event<BufferBuilderCreateCallback> AFTER = EventFactory.createArrayBacked(BufferBuilderCreateCallback.class,
            (listeners) -> () ->
            {
                List<RenderLayer> custom = new ArrayList<>();
                for (BufferBuilderCreateCallback listener : listeners)
                    custom.addAll(listener.handler());
                return custom;
            });

    List<RenderLayer> handler();
}
