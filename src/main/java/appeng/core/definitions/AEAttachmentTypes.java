package appeng.core.definitions;

import appeng.core.AppEng;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

@SuppressWarnings("UnstableApiUsage")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AEAttachmentTypes {
    public static final AttachmentType<Boolean> HOLDING_CTRL = AttachmentRegistry.create(AppEng.makeId("ctrl"), builder -> builder
            .initializer(() -> false));
}
