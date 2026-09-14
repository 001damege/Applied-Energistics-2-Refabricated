/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.core.areaoverlay;

import appeng.api.util.DimensionalBlockPos;

import java.util.*;

/**
 * This is based on the area render of https://github.com/TeamPneumatic/pnc-repressurized/
 */
public class AreaOverlayManager {
    private static final AreaOverlayManager INSTANCE = new AreaOverlayManager();

    private final Map<DimensionalBlockPos, IAreaOverlayDataSource> visibleAreaOverlays = new HashMap<>();

    public static AreaOverlayManager getInstance() {
        return INSTANCE;
    }

    public void showArea(IAreaOverlayDataSource source) {
        Objects.requireNonNull(source);

        visibleAreaOverlays.put(source.getOverlaySourceLocation(), source);
    }

    public boolean isVisible(IAreaOverlayDataSource source) {
        return visibleAreaOverlays.containsKey(source.getOverlaySourceLocation());
    }

    public void removeArea(IAreaOverlayDataSource source) {
        visibleAreaOverlays.remove(source.getOverlaySourceLocation());
    }

    public Collection<IAreaOverlayDataSource> getVisible() {
        return Collections.unmodifiableCollection(visibleAreaOverlays.values());
    }
}
