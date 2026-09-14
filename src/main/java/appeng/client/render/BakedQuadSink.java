package appeng.client.render;

import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BakedQuadSink {
    void add(@Nullable Direction cullFace, BakedQuad quad);
}
