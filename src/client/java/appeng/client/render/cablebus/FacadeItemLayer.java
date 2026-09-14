package appeng.client.render.cablebus;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;

import java.util.List;

public record FacadeItemLayer(RenderType renderType, List<BakedQuad> quads) {
}
