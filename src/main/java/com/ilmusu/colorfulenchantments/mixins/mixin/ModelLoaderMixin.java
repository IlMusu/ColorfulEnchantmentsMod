package com.ilmusu.colorfulenchantments.mixins.mixin;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.client.callbacks.RegisterBookModelsCallback;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin
{
    @Shadow protected abstract void addModel(ModelIdentifier modelId);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 3))
    public void overrideModel(BlockColors blockColors, Profiler profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci)
    {
        // Obtaining the models as the item model resources and mapping them to ModelIdentifier
        List<ModelIdentifier> models =  RegisterBookModelsCallback.EVENT.invoker().handler().stream().map(
                (model) -> new ModelIdentifier(model, "inventory")
        ).toList();
        // Loading all the custom enchanted book models
        for(ModelIdentifier model : models)
            this.addModel(model);
    }
}
