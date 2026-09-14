package appeng.client.renderer.part;

import appeng.api.orientation.BlockOrientation;
import appeng.client.api.AEKeyRenderState;
import appeng.client.api.renderer.parts.PartDynamicRenderState;
import net.minecraft.util.FormattedCharSequence;

public class MonitorRenderState extends PartDynamicRenderState {
    public BlockOrientation orientation;
    public final AEKeyRenderState what = new AEKeyRenderState();
    public FormattedCharSequence text;
    public int textColor;
    public int textWidth;
}
