package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.cache;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Key;

import java.io.File;

/**
 * A simple class that returns null for all gets and ignores all writes.
 */
public class DiskCacheAdapter implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.cache.DiskCache {
  @Override
  public File get(Key key) {
    // no op, default for overriders
    return null;
  }

  @Override
  public void put(Key key, Writer writer) {
    // no op, default for overriders
  }

  @Override
  public void delete(Key key) {
    // no op, default for overriders
  }

  @Override
  public void clear() {
      // no op, default for overriders
  }

  /**
   * Default factory for {@link DiskCacheAdapter}.
   */
  public static final class Factory implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.cache.DiskCache.Factory {
    @Override
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.cache.DiskCache build() {
      return new DiskCacheAdapter();
    }
  }
}
