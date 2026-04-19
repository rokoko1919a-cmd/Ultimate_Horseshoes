package net.nanaky.horseshoes.mixin;

import net.nanaky.horseshoes.config.ModConfigs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseArmorDurabilityMixin extends Animal {

    protected AbstractHorseArmorDurabilityMixin(EntityType<? extends Animal> type, net.minecraft.world.level.Level world) {
        super(type, world);
    }

    @Inject(method = "hurtServer", at = @At("TAIL"))
    private void horseshoes$damageBodyArmor(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfigs.VANILLA_ARMOR_DURABILITY) return;
        if (!cir.getReturnValue()) return; // horse didn't actually take damage
        if (damage <= 0.0F) return;

        ItemStack bodyArmor = this.getItemBySlot(EquipmentSlot.BODY);
        if (bodyArmor.isEmpty() || !bodyArmor.isDamageableItem()) return;
        if (!bodyArmor.canBeHurtBy(source)) return;

        int durabilityDamage = (int) Math.max(1.0F, damage / 4.0F);
        bodyArmor.hurtAndBreak(durabilityDamage, this, EquipmentSlot.BODY);
    }
}