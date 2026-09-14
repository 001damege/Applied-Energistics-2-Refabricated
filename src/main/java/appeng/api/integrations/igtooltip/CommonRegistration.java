package appeng.api.integrations.igtooltip;

import appeng.api.integrations.igtooltip.providers.ServerDataProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface CommonRegistration {
    <T extends BlockEntity> void addBlockEntityData(Identifier id,
            Class<T> blockEntityClass,
            ServerDataProvider<? super T> provider);

}
