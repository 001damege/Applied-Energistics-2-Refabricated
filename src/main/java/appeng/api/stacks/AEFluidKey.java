package appeng.api.stacks;

import appeng.api.storage.AEKeyFilter;
import appeng.core.AELog;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class AEFluidKey extends AEKey {
    public static final MapCodec<AEFluidKey> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BuiltInRegistries.FLUID.holderByNameCodec().validate(
                            holder -> holder.is(Fluids.EMPTY.builtInRegistryHolder())
                                    ? DataResult.error(() -> "Fluid must not be minecraft:empty")
                                    : DataResult.success(holder))
                            .fieldOf("id").forGetter(key -> key.stack.getFluid().builtInRegistryHolder()),
                    DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(key -> key.stack.getComponents()))
                    .apply(instance, (fluidHolder,
                            dataComponentPatch) -> new AEFluidKey(of(fluidHolder.value()).stack)));
    public static final Codec<AEFluidKey> CODEC = MAP_CODEC.codec();

    public static final int AMOUNT_BUCKET = 1000;
    public static final int AMOUNT_BLOCK = 1000;

    private final FluidVariant stack;
    private final int hashCode;

    private AEFluidKey(FluidVariant stack) {
        Preconditions.checkArgument(!stack.isBlank(), "stack was blank");
        this.stack = stack;
        this.hashCode = FluidVariant.of(stack.getFluid()).hashCode();
    }

    public static AEFluidKey of(Fluid fluid) {
        return of(FluidVariant.of(fluid));
    }

    @Nullable
    public static AEFluidKey of(FluidVariant fluidVariant) {
        return fluidVariant.isBlank() ? null : new AEFluidKey(fluidVariant);
    }

    public static boolean matches(AEKey what, FluidVariant fluid) {
        return what instanceof AEFluidKey fluidKey && fluidKey.matches(fluid);
    }

    public static boolean is(AEKey what) {
        return what instanceof AEFluidKey;
    }

    public static AEKeyFilter filter() {
        return AEFluidKey::is;
    }

    public boolean matches(FluidVariant variant) {
        return false;
    }

    @Override
    public AEKeyType getType() {
        return AEKeyType.fluids();
    }

    @Override
    public AEKey dropSecondary() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AEFluidKey aeFluidKey = (AEFluidKey) o;
        // The hash code comparison is a fast-fail cheap check
        return hashCode == aeFluidKey.hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public static AEFluidKey fromTag(HolderLookup.Provider registries, CompoundTag tag) {
        var ops = registries.createSerializationContext(NbtOps.INSTANCE);
        try {
            return CODEC.decode(ops, tag).getOrThrow().getFirst();
        } catch (Exception e) {
            AELog.debug("Tried to load an invalid fluid key from NBT: %s", tag, e);
            return null;
        }
    }

    @Override
    public CompoundTag toTag(HolderLookup.Provider registries) {
        var ops = registries.createSerializationContext(NbtOps.INSTANCE);
        return (CompoundTag) CODEC.encodeStart(ops, this).getOrThrow();
    }

    @Override
    public Object getPrimaryKey() {
        return getFluid();
    }

    @Override
    public ResourceLocation getId() {
        return BuiltInRegistries.FLUID.getKey(getFluid());
    }

    @Override
    public void addDrops(long amount, List<ItemStack> drops, Level level, BlockPos pos) {
        // Fluids are voided
    }

    @Override
    protected Component computeDisplayName() {
        return Component.empty();
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    @Override
    public boolean isTagged(TagKey<?> tag) {
        // This will just return false for incorrectly cast tags
        return stack.getFluid().is((TagKey<Fluid>) tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T get(DataComponentType<T> type) {
        return (T) stack.getComponents().get(type);
    }

    @Override
    public boolean hasComponents() {
        return !stack.hasComponents();
    }

    public FluidVariant toResource() {
        return FluidVariant.of(stack.getFluid());
    }

    public FluidVariant toStack(int amount) {
        return FluidVariant.blank();
    }

    public Fluid getFluid() {
        return stack.getFluid();
    }

    @Override
    public void writeToPacket(RegistryFriendlyByteBuf data) {
        FluidVariant.PACKET_CODEC.encode(data, stack);
    }

    public static AEFluidKey fromPacket(RegistryFriendlyByteBuf data) {
        var stack = FluidVariant.PACKET_CODEC.decode(data);
        return new AEFluidKey(stack);
    }

    public static boolean is(@Nullable GenericStack stack) {
        return stack != null && stack.what() instanceof AEFluidKey;
    }

    @Override
    public String toString() {
        var id = BuiltInRegistries.FLUID.getKey(getFluid());
        String idString = id != BuiltInRegistries.FLUID.getDefaultKey() ? id.toString() : getFluid().getClass().getName() + "(unregistered)";
        return stack.getComponents().isEmpty() ? idString : idString + " (+components)";
    }
}
