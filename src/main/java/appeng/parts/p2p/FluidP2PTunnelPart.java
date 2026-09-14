package appeng.parts.p2p;

import appeng.api.parts.IPartItem;
import appeng.api.stacks.AEKeyType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class FluidP2PTunnelPart extends ResourceHandlerP2PTunnelPart<FluidP2PTunnelPart, FluidResource> {
    public FluidP2PTunnelPart(IPartItem<?> partItem) {
        super(partItem, Capabilities.Fluid.BLOCK, FluidResource.EMPTY, AEKeyType.fluids());
    }
}
