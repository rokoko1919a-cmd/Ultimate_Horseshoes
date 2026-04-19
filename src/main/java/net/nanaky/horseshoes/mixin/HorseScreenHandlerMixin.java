package net.nanaky.horseshoes.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.nanaky.horseshoes.Horseshoes;
import net.nanaky.horseshoes.IHorseshoesContainer;
import net.nanaky.horseshoes.config.ModConfigs;
import net.nanaky.horseshoes.items.HorseshoesItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseInventoryMenu.class)
public abstract class HorseScreenHandlerMixin extends AbstractContainerMenu {

    protected HorseScreenHandlerMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void horseshoes$addHorseshoeSlot(int syncId, Inventory playerInventory,
                                              Container inventory, AbstractHorse horse,
                                              int inventoryColumns, CallbackInfo ci) {
        if (!horse.getType().builtInRegistryHolder().is(Horseshoes.ALLOWED)) return;

        // Cast to our interface to get the dedicated 1-slot horseshoe container
        // This is completely separate from the horse's main inventory, no index conflicts
        Container horseshoeContainer = ((IHorseshoesContainer) horse).horseshoes$getContainer();

        this.addSlot(new Slot(horseshoeContainer, 0, 8, 54) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof HorseshoesItem;
            }
            @Override
            public boolean mayPickup(Player player) {
                return !ModConfigs.SLOT_LOCK;
            }
            @Override
            public int getMaxStackSize() { return 1; }
            @Override
            public boolean isActive() { return horse.isAlive(); }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int horseshoeIndex = this.slots.size() - 1;
        Slot horseshoeSlot = this.slots.get(horseshoeIndex);

        // Find where player inventory starts — after all horse slots
        // Saddle(1) + Armor(1) + chest slots + horseshoe(1) = horseshoeIndex slots before player inv
        // Player inv starts right after horse chest slots, before horseshoe
        int playerInvStart = horseshoeIndex - 36; // 27 main + 9 hotbar
        int playerInvEnd = horseshoeIndex;        // exclusive, so up to but not including horseshoe

        if (index == horseshoeIndex) {
            if (ModConfigs.SLOT_LOCK) return ItemStack.EMPTY;
            // slot lock off — allow shift-click out
            ItemStack stack = horseshoeSlot.getItem();
            if (stack.isEmpty()) return ItemStack.EMPTY;
            if (net.minecraft.world.item.enchantment.EnchantmentHelper.has(
                stack, net.minecraft.world.item.enchantment.EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
                return ItemStack.EMPTY;
            }
            if (!this.moveItemStackTo(stack, playerInvStart, playerInvEnd, true)) {
                return ItemStack.EMPTY;
            }
            horseshoeSlot.setChanged();
            return ItemStack.EMPTY;
        }

        Slot clickedSlot = this.slots.get(index);
        ItemStack stack = clickedSlot.getItem();
        if (stack.isEmpty()) return ItemStack.EMPTY;

        if (index < playerInvStart) {
            // Clicked a horse slot — move to player inventory
            if (!this.moveItemStackTo(stack, playerInvStart, playerInvEnd, true)) {
                return ItemStack.EMPTY;
            }
            clickedSlot.setChanged();
        } else {
            // Clicked player inventory — try horse slots in priority order
            if (stack.getItem() instanceof HorseshoesItem) {
                if (horseshoeSlot.isActive() && !horseshoeSlot.hasItem()) {
                    if (!this.moveItemStackTo(stack, horseshoeIndex, horseshoeIndex + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    clickedSlot.setChanged();
                }
            } else if (this.slots.get(0).isActive() && this.slots.get(0).mayPlace(stack)) {
                // Try saddle slot
                if (!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
                clickedSlot.setChanged();
            } else if (this.slots.get(1).isActive() && this.slots.get(1).mayPlace(stack)) {
                // Try armor slot
                if (!this.moveItemStackTo(stack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
                clickedSlot.setChanged();
            } else if (playerInvStart > 2) {
                // Try horse chest inventory
                if (!this.moveItemStackTo(stack, 2, playerInvStart, false)) {
                    return ItemStack.EMPTY;
                }
                clickedSlot.setChanged();
            }
        }

        return ItemStack.EMPTY;
    }
}