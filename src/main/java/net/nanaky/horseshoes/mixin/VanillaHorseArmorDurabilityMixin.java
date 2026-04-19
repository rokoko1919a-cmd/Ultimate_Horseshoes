package net.nanaky.horseshoes.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.nanaky.horseshoes.config.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.core.Holder$Reference")
public class VanillaHorseArmorDurabilityMixin {

    @Shadow
    private DataComponentMap components;

    @Inject(method = "bindComponents", at = @At("TAIL"))
    private void horseshoes$patchHorseArmorDurability(DataComponentMap incomingComponents, CallbackInfo ci) {
        if (!ModConfigs.VANILLA_ARMOR_DURABILITY) return;

        Holder.Reference<?> self = (Holder.Reference<?>) (Object) this;
        if (!(self.value() instanceof Item item)) return;

        int maxDamage = 0;
        if      (item == Items.IRON_HORSE_ARMOR)      maxDamage = 195;
        else if (item == Items.GOLDEN_HORSE_ARMOR)    maxDamage = 91;
        else if (item == Items.DIAMOND_HORSE_ARMOR)   maxDamage = 429;
        else if (item == Items.NETHERITE_HORSE_ARMOR) maxDamage = 481;
        else if (item == Items.LEATHER_HORSE_ARMOR)   maxDamage = 110;
        else if (item == Items.COPPER_HORSE_ARMOR)    maxDamage = 143;
        else return;

        this.components = DataComponentMap.builder()
            .addAll(this.components)
            .set(DataComponents.MAX_DAMAGE, maxDamage)
            .set(DataComponents.DAMAGE, 0)
            .build();
    }
}