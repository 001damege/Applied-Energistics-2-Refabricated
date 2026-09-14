package appeng.client.renderer.blockentity;

import appeng.api.orientation.BlockOrientation;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class ChargerRenderState extends BlockEntityRenderState {
    public BlockOrientation blockOrientation;
    public Transformation transform;
    public ItemStackRenderState item = new ItemStackRenderState();
}
