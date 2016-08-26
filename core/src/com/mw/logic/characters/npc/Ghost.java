package com.mw.logic.characters.npc;

import com.mw.logic.characters.base.Monster;
import com.mw.stage.MapStage;

/**
 * Created by BanditCat on 2016/7/25.
 */
public class Ghost extends Monster {
    @Override
    public void doClick(MapStage mapStage, int x, int y) {
        super.doClick(mapStage, x, y);
        getActor().findWays(x,y);
    }
}
