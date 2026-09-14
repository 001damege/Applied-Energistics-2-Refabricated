package appeng.api.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum InscriberInputCapacity {
    ONE(1),
    FOUR(4),
    SIXTY_FOUR(64);

    public final int capacity;
}
