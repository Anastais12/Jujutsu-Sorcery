package com.anastas1s12.jjs.mixin;

import com.anastas1s12.jjs.utils.IPlayerDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityMixin implements IPlayerDataAccessor {

    @Unique
    private CompoundTag jjs$persistentData;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void jjs$initData(CallbackInfo ci) {
        this.jjs$persistentData = new CompoundTag();
    }

    @Override
    public CompoundTag jjs$getPersistentData() {
        return this.jjs$persistentData;
    }
}
