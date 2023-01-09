package com.ilmusu.colorfulenchantments.client.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public interface RegisterBookModelsCallback
{
    // Called only on CLIENT on the initialization of the ModelLoader
    Event<RegisterBookModelsCallback> EVENT = EventFactory.createArrayBacked(RegisterBookModelsCallback.class,
            (listeners) -> () ->
            {
                List<Identifier> models = new ArrayList<>();
                for (RegisterBookModelsCallback listener : listeners)
                    models.addAll(listener.handler());
                return models;
            });

    List<Identifier> handler();
}
