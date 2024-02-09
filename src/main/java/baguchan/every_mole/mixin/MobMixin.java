package baguchan.every_mole.mixin;

import baguchan.every_mole.utils.ConvertUtils;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements UnderzealotSacrifice {
    @Shadow public abstract boolean canPickUpLoot();

    @Shadow protected abstract float getEquipmentDropChance(EquipmentSlot p_21520_);

    @Shadow public abstract boolean isNoAi();

    @Shadow public abstract void setPersistenceRequired();

    private boolean isBeingSacrificed = false;
    private int sacrificeTime;

    protected MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }


    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (this.isBeingSacrificed && !this.level().isClientSide) {
            --this.sacrificeTime;

            if (this.sacrificeTime < 0) {
                if (this.isPassenger()) {
                    Entity var4 = this.getVehicle();
                    if (var4 instanceof UnderzealotEntity) {
                        UnderzealotEntity underzealot = (UnderzealotEntity)var4;
                        underzealot.postSacrifice(this);
                        underzealot.triggerIdleDigging();
                    }
                }

                this.stopRiding();
                Entity convert = this.convertToMonster(ConvertUtils.getSacrifice(this.getType()).getConvertEntityType(), true);
                this.playSound((SoundEvent) ACSoundRegistry.DARK_CLOUD_DISAPPEAR.get(), 8.0F, 1.0F);
                if (convert instanceof Mob convertLiving) {
                    ForgeEventFactory.onLivingConvert(this, convertLiving);
                    convert.stopRiding();
                }
            }
        }

    }

    @Nullable
    private Entity convertToMonster(EntityType<?> p_21407_, boolean p_21408_) {
        if (this.isRemoved()) {
            return null;
        } else {
            Entity t = p_21407_.create(this.level());
            if (t == null) {
                return null;
            } else {
                t.copyPosition(this);
                if(t instanceof  Mob mob) {
                    mob.setBaby(this.isBaby());
                    mob.setNoAi(this.isNoAi());
                }
                if (this.hasCustomName()) {
                    t.setCustomName(this.getCustomName());
                    t.setCustomNameVisible(this.isCustomNameVisible());
                }
                if(t instanceof Mob mob) {
                    if (mob.isPersistenceRequired()) {
                        this.setPersistenceRequired();
                    }
                }

                t.setInvulnerable(this.isInvulnerable());
                if(t instanceof  Mob mob) {
                    if (p_21408_) {
                        mob.setCanPickUpLoot(this.canPickUpLoot());

                        for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                            ItemStack itemstack = this.getItemBySlot(equipmentslot);
                            if (!itemstack.isEmpty()) {
                                t.setItemSlot(equipmentslot, itemstack.copyAndClear());
                                mob.setDropChance(equipmentslot, this.getEquipmentDropChance(equipmentslot));
                            }
                        }
                    }
                }

                this.level().addFreshEntity(t);
                if (this.isPassenger()) {
                    Entity entity = this.getVehicle();
                    this.stopRiding();
                    t.startRiding(entity, true);
                }

                CompoundTag compoundTag1 = NbtPredicate.getEntityTagToCompare(this).copy();
                t.load(compoundTag1);

                this.discard();
                return t;
            }
        }
    }

    @Override
    public void triggerSacrificeIn(int time) {
        this.isBeingSacrificed = true;
        this.sacrificeTime = time;
    }

    @Override
    public boolean isValidSacrifice(int i) {
        return this.getType() != EntityType.PLAYER && ConvertUtils.getSacrifice(this.getType()) != null;
    }
}
