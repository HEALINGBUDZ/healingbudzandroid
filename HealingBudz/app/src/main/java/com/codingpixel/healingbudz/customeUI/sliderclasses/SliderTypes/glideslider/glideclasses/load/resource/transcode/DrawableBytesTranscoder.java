package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.transcode;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Options;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.bitmap_recycle.BitmapPool;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.bitmap.BitmapResource;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.gif.GifDrawable;

/**
 * Obtains {@code byte[]} from {@link BitmapDrawable}s by delegating to a
 * {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.transcode.ResourceTranscoder} for {@link Bitmap}s to {@code byte[]}s.
 */
public final class DrawableBytesTranscoder implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.transcode.ResourceTranscoder<Drawable, byte[]> {
  private final BitmapPool bitmapPool;
  private final com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.transcode.ResourceTranscoder<Bitmap, byte[]> bitmapBytesTranscoder;
  private final com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.transcode.ResourceTranscoder<GifDrawable, byte[]> gifDrawableBytesTranscoder;

  public DrawableBytesTranscoder(
      @NonNull BitmapPool bitmapPool,
      @NonNull com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.transcode.ResourceTranscoder<Bitmap, byte[]> bitmapBytesTranscoder,
      @NonNull com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.transcode.ResourceTranscoder<GifDrawable, byte[]> gifDrawableBytesTranscoder) {
    this.bitmapPool = bitmapPool;
    this.bitmapBytesTranscoder = bitmapBytesTranscoder;
    this.gifDrawableBytesTranscoder = gifDrawableBytesTranscoder;
  }

  @Nullable
  @Override
  public Resource<byte[]> transcode(@NonNull Resource<Drawable> toTranscode,
      @NonNull Options options) {
    Drawable drawable = toTranscode.get();
    if (drawable instanceof BitmapDrawable) {
      return bitmapBytesTranscoder.transcode(
          BitmapResource.obtain(((BitmapDrawable) drawable).getBitmap(), bitmapPool), options);
    } else if (drawable instanceof GifDrawable) {
      return gifDrawableBytesTranscoder.transcode(toGifDrawableResource(toTranscode), options);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  private static Resource<GifDrawable> toGifDrawableResource(@NonNull Resource<Drawable> resource) {
    return (Resource<GifDrawable>) (Resource<?>) resource;
  }
}
