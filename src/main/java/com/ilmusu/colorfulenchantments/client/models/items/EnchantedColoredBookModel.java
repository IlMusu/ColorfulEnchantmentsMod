package com.ilmusu.colorfulenchantments.client.models.items;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.client.models.BakedModelHelper;
import com.ilmusu.colorfulenchantments.client.models.ItemBakedModel;
import com.ilmusu.colorfulenchantments.items.EnchantedColoredBookHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnchantedColoredBookModel implements UnbakedModel, ItemBakedModel
{
    private static final Identifier PARENT = new Identifier("item/generated");

    private static final SpriteIdentifier EMPTY_SPRITE =
            new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Resources.identifier("item/empty_overlay"));
    private static final SpriteIdentifier ENCHANTED_BOOK_SPRITE =
            new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Resources.identifier("item/enchanted_colored_book"));
    private static final SpriteIdentifier CURSED_BOOK_SPRITE =
            new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Resources.identifier("item/cursed_colored_book"));
    private static final Function<Integer, SpriteIdentifier> LACE_SPRITE = (index) ->
            new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Resources.identifier("item/book_lace_overlay_"+index));
    private static final Function<Integer, SpriteIdentifier> PIN_SPRITE = (index) ->
            new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Resources.identifier("item/book_pin_overlay_"+index));

    private Sprite particleSprite;
    private ModelTransformation transformation;

    // The main enchanted book mesh and the related missing pixels from the border
    private Mesh enchantedBookMesh;
    private final Mesh[] inverseEnchantedBookMesh = new Mesh[4];
    // The cursed book mesh the related missing pixels from the border
    private Mesh cursedBookMesh;
    private final Mesh[] inverseCursedBookMesh = new Mesh[4];
    // The layer meshes
    private final Mesh[] laceMeshes = new Mesh[4];
    private final Mesh[] pinMeshes = new Mesh[4];

    @Override
    public Collection<Identifier> getModelDependencies()
    {
        return List.of();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader)
    {

    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId)
    {
        Sprite emptySprite = textureGetter.apply(EMPTY_SPRITE);
        Sprite enchantedSprite = textureGetter.apply(ENCHANTED_BOOK_SPRITE);
        Sprite cursedSprite = textureGetter.apply(CURSED_BOOK_SPRITE);

        Sprite[] laceSprites = new Sprite[]{
                textureGetter.apply(LACE_SPRITE.apply(1)),
                textureGetter.apply(LACE_SPRITE.apply(2)),
                textureGetter.apply(LACE_SPRITE.apply(3)),
                textureGetter.apply(LACE_SPRITE.apply(4))
        };

        Sprite[] pinSprites = new Sprite[]{
                textureGetter.apply(PIN_SPRITE.apply(1)),
                textureGetter.apply(PIN_SPRITE.apply(2)),
                textureGetter.apply(PIN_SPRITE.apply(3)),
                textureGetter.apply(PIN_SPRITE.apply(4))
        };

        Sprite[] layers = ArrayUtils.addAll(laceSprites, pinSprites);

        this.particleSprite = enchantedSprite;
        this.transformation =  ((JsonUnbakedModel)baker.getOrLoadModel(PARENT)).getTransformations();

        // Creating the main book mesh and removing all the borders occupied by the laces
        this.enchantedBookMesh = BakedModelHelper.createItemMesh(enchantedSprite, layers);
        this.cursedBookMesh = BakedModelHelper.createItemMesh(cursedSprite, layers);

        for (int i = 0; i < 4; i++)
        {
            // Creating the missing borders from the main textures
            this.inverseEnchantedBookMesh[i] = BakedModelHelper.createItemInverseMesh(enchantedSprite, emptySprite, laceSprites[i], pinSprites[i]);
            this.inverseCursedBookMesh[i] = BakedModelHelper.createItemInverseMesh(cursedSprite, emptySprite, laceSprites[i], pinSprites[i]);
            // Creating the layers by removing all the non-necessary borders
            this.laceMeshes[i] = BakedModelHelper.createItemLayer(enchantedSprite, laceSprites[i], i+1);
            this.pinMeshes[i] = BakedModelHelper.createItemLayer(enchantedSprite, pinSprites[i], i+5);
        }

        // This is both the baked and unbaked model
        return this;
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context)
    {
        if(!EnchantedColoredBookHelper.isCursed(stack))
            context.meshConsumer().accept(this.enchantedBookMesh);
        else
            context.meshConsumer().accept(this.cursedBookMesh);

        boolean[] laces = EnchantedColoredBookHelper.getLacesMask(stack);
        for(int i=0; i<4; ++i)
        {
            if(laces[i])
            {
                context.meshConsumer().accept(this.laceMeshes[i]);
                context.meshConsumer().accept(this.pinMeshes[i]);
                continue;
            }

            if(!EnchantedColoredBookHelper.isCursed(stack))
                context.meshConsumer().accept(this.inverseEnchantedBookMesh[i]);
            else
                context.meshConsumer().accept(this.inverseCursedBookMesh[i]);
        }
    }

    @Override
    public Sprite getParticleSprite()
    {
        return this.particleSprite;
    }

    @Override
    public ModelTransformation getTransformation()
    {
        return this.transformation;
    }

    @Override
    public ModelOverrideList getOverrides()
    {
        return ModelOverrideList.EMPTY;
    }
}
