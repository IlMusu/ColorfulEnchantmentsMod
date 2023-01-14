package com.ilmusu.colorfulenchantments.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.RenderLayer;

import java.util.ArrayList;
import java.util.List;

public interface WorldRendererLayersCallback
{
    // Called AFTER the rendering of items glint
    Event<WorldRendererLayersCallback> AFTER_DIRECT_GLINT = EventFactory.createArrayBacked(WorldRendererLayersCallback.class,
            (listeners) -> () ->
            {
                List<RenderLayer> custom = new ArrayList<>();
                for (WorldRendererLayersCallback listener : listeners)
                    custom.addAll(listener.handler());
                return custom;
            });

    List<RenderLayer> handler();
}
