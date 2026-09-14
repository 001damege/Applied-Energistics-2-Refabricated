package appeng.client.renderer.blockentity;

import appeng.api.orientation.BlockOrientation;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class CrankRenderState extends BlockEntityRenderState {
    BlockModelRenderState modelRenderState = new BlockModelRenderState();
    BlockOrientation orientation;
    float visibleRotation;
}
