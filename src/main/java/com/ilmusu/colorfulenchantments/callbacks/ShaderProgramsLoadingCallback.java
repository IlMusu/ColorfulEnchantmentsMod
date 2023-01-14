package com.ilmusu.colorfulenchantments.callbacks;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.resource.ResourceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface ShaderProgramsLoadingCallback
{
    // Called AFTER the registration of the vanilla ShaderProgram
    Event<ShaderProgramsLoadingCallback> AFTER = EventFactory.createArrayBacked(ShaderProgramsLoadingCallback.class,
            (listeners) -> (manager) ->
            {
                List<Pair<ShaderProgram, Consumer<ShaderProgram>>> custom = new ArrayList<>();
                for (ShaderProgramsLoadingCallback listener : listeners)
                    custom.addAll(listener.handler(manager));
                return custom;
            });

    List<Pair<ShaderProgram, Consumer<ShaderProgram>>> handler(ResourceFactory manager) throws IOException;
}
