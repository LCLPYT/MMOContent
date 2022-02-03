package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import work.lclpnet.mmocontent.asm.mixin.common.AbstractButtonBlockAccessor;

import javax.annotation.Nullable;

public abstract class MMOButtonBlock extends AbstractButtonBlock implements IMMOBlock {

    protected MMOButtonBlock(boolean wooden, Settings settings) {
        super(wooden, settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }

    public abstract int getActiveDuration();

    public boolean isWooden() {
        return ((AbstractButtonBlockAccessor) this).isWooden();
    }

    @Nullable
    public static MMOButtonBlock asMMOButton(Object obj) {
        return obj instanceof MMOButtonBlock ? (MMOButtonBlock) obj : null;
    }

    public static class Impl extends MMOButtonBlock {

        private final SoundEvent onSound, offSound;
        private final int pressedTicks;

        public Impl(boolean wooden, Settings settings) {
            this(wooden, settings,
                    wooden ? SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON,
                    wooden ? SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF,
                    wooden ? 30 : 20);
        }

        public Impl(boolean wooden, Settings settings, SoundEvent onSound, SoundEvent offSound, int pressedTicks) {
            super(wooden, settings);
            this.onSound = onSound;
            this.offSound = offSound;
            this.pressedTicks = pressedTicks;
        }

        @Override
        protected SoundEvent getClickSound(boolean powered) {
            return powered ? this.onSound : this.offSound;
        }

        @Override
        public int getActiveDuration() {
            return pressedTicks;
        }
    }
}
