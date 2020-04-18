/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model.event;

import controller.MainController;
import model.Game;
import model.Map;

/**
 * RendererProcess
 *
 * @author Philipp Winter
 */
public class RendererProcess implements Process {

    private static final long BASE_TIME = 1000;

    private static final int STARTUP_DELAY = 15;

    @Override
    public long getTiming() {
        long refreshRate = (long) Game.getInstance().getRefreshRate();
        return BASE_TIME/refreshRate;
    }

    @Override
    public long getStartupDelay() {
        return STARTUP_DELAY;
    }

    @Override
    public void onLoad() {
        Map.getInstance().markAllForRendering();
    }

    @Override
    public void run() {
        MainController.getInstance().getGui().getRenderer().markReady();
    }
}
