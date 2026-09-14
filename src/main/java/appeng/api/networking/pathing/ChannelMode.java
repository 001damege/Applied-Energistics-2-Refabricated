package appeng.api.networking.pathing;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Defines how AE2's channel capacities work.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum ChannelMode {
    /**
     * Cables carry infinite channels, effectively disabling pathfinding and channel requirements.
     */
    INFINITE(Integer.MAX_VALUE, 0),
    /**
     * Default channel capacity per cable.
     */
    DEFAULT(8, 1),
    /**
     * Double capacity per cable.
     */
    X2(16, 2),
    /**
     * Triple capacity per cable.
     */
    X3(24, 3),
    /**
     * Quadruple capacity per cable.
     */
    X4(32, 4);

    // The maximum number of channels supported by ad-hoc networks.
    @Getter
    private final int adHocNetworkChannels;
    // Multiplier for the default capacity of cables. Must be a power of two.
    @Getter
    private final int cableCapacityFactor;
}
