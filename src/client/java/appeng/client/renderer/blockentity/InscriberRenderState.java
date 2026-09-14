package appeng.client.renderer.blockentity;

import appeng.api.orientation.BlockOrientation;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class InscriberRenderState extends BlockEntityRenderState {
    public BlockOrientation orientation;
    public float progress;
    public int frontLightCoords;

    public ItemStackRenderState topItem = new ItemStackRenderState();
    public ItemStackRenderState middleItem = new ItemStackRenderState();
    public ItemStackRenderState bottomItem = new ItemStackRenderState();
}
