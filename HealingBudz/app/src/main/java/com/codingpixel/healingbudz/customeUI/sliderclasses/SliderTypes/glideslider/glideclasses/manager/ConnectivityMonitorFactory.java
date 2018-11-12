package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.manager;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * A factory class that produces a functional
 * {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.manager.ConnectivityMonitor}.
 */
public interface ConnectivityMonitorFactory {

  @NonNull
  com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.manager.ConnectivityMonitor build(
          @NonNull Context context,
          @NonNull com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.manager.ConnectivityMonitor.ConnectivityListener listener);
}
