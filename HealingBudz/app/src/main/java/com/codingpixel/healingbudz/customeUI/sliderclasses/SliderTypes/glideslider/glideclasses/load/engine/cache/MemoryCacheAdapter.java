package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Key;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource;

/**
 * A simple class that ignores all puts and returns null for all gets.
 */
public class MemoryCacheAdapter implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.cache.MemoryCache {

  private ResourceRemovedListener listener;

  @Override
  public long getCurrentSize() {
    return 0;
  }

  @Override
  public long getMaxSize() {
    return 0;
  }

  @Override
  public void setSizeMultiplier(float multiplier) {
    // Do nothing.
  }

  @Nullable
  @Override
  public Resource<?> remove(@NonNull Key key) {
    return null;
  }

  @Nullable
  @Override
  public Resource<?> put(@NonNull Key key, @Nullable Resource<?> resource) {
    if (resource != null) {
      listener.onResourceRemoved(resource);
    }
    return null;
  }

  @Override
  public void setResourceRemovedListener(@NonNull ResourceRemovedListener listener) {
    this.listener = listener;
  }

  @Override
  public void clearMemory() {
    // Do nothing.
  }

  @Override
  public void trimMemory(int level) {
    // Do nothing.
  }
}
