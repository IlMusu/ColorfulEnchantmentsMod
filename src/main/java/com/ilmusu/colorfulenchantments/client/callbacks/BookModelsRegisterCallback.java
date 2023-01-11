package com.ilmusu.colorfulenchantments.client.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public interface BookModelsRegisterCallback
{
    // Called only on CLIENT on the initialization of the ModelLoader
    Event<BookModelsRegisterCallback> EVENT = EventFactory.createArrayBacked(BookModelsRegisterCallback.class,
            (listeners) -> () ->
            {
                List<Identifier> models = new ArrayList<>();
                for (BookModelsRegisterCallback listener : listeners)
                    models.addAll(listener.handler());
                return models;
            });

    List<Identifier> handler();
}
