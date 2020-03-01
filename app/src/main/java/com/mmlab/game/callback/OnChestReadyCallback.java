package com.mmlab.game.callback;


import com.mmlab.game.module.Chest;

import java.util.List;

public interface OnChestReadyCallback {
    void onChestDataComplete(List<Chest> dataChestList);
    void onNoChestData();
}
