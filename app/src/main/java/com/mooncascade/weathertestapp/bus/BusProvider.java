package com.mooncascade.weathertestapp.bus;

import com.squareup.otto.Bus;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class BusProvider {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }

}
