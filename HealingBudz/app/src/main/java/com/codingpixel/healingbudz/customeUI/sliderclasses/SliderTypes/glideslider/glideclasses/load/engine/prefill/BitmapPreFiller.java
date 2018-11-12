package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.DecodeFormat;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.bitmap_recycle.BitmapPool;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.cache.MemoryCache;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for pre-filling {@link Bitmap Bitmaps} in a
 * {@link BitmapPool}.
 */
public final class BitmapPreFiller {

  private final MemoryCache memoryCache;
  private final BitmapPool bitmapPool;
  private final DecodeFormat defaultFormat;
  private final Handler handler = new Handler(Looper.getMainLooper());

  private BitmapPreFillRunner current;

  public BitmapPreFiller(MemoryCache memoryCache, BitmapPool bitmapPool,
      DecodeFormat defaultFormat) {
    this.memoryCache = memoryCache;
    this.bitmapPool = bitmapPool;
    this.defaultFormat = defaultFormat;
  }

  @SuppressWarnings("deprecation")
  public void preFill(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillType.Builder... bitmapAttributeBuilders) {
    if (current != null) {
      current.cancel();
    }

    com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillType[] bitmapAttributes = new com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillType[bitmapAttributeBuilders.length];
    for (int i = 0; i < bitmapAttributeBuilders.length; i++) {
      com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillType.Builder builder = bitmapAttributeBuilders[i];
      if (builder.getConfig() == null) {
        builder.setConfig(
            defaultFormat == DecodeFormat.PREFER_ARGB_8888
                || defaultFormat == DecodeFormat.PREFER_ARGB_8888_DISALLOW_HARDWARE
            ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
      }
      bitmapAttributes[i] = builder.build();
    }

    com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillQueue allocationOrder = generateAllocationOrder(bitmapAttributes);
    current = new BitmapPreFillRunner(bitmapPool, memoryCache, allocationOrder);
    handler.post(current);
  }

  @VisibleForTesting
  com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillQueue generateAllocationOrder(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillType... preFillSizes) {
    final long maxSize =
        memoryCache.getMaxSize() - memoryCache.getCurrentSize() + bitmapPool.getMaxSize();

    int totalWeight = 0;
    for (com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillType size : preFillSizes) {
      totalWeight += size.getWeight();
    }

    final float bytesPerWeight = maxSize / (float) totalWeight;

    Map<com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillType, Integer> attributeToCount = new HashMap<>();
    for (com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillType size : preFillSizes) {
      int bytesForSize = Math.round(bytesPerWeight * size.getWeight());
      int bytesPerBitmap = getSizeInBytes(size);
      int bitmapsForSize = bytesForSize / bytesPerBitmap;
      attributeToCount.put(size, bitmapsForSize);
    }

    return new com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.prefill.PreFillQueue(attributeToCount);
  }

  private static int getSizeInBytes(PreFillType size) {
    return Util.getBitmapByteSize(size.getWidth(), size.getHeight(), size.getConfig());
  }
}

