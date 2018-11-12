package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * A factory responsible for producing the correct type of
 * {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.target.Target} for a given {@link android.view.View} subclass.
 */
public class ImageViewTargetFactory {
  @NonNull
  @SuppressWarnings("unchecked")
  public <Z> com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.target.ViewTarget<ImageView, Z> buildTarget(@NonNull ImageView view,
                                                                                    @NonNull Class<Z> clazz) {
    if (Bitmap.class.equals(clazz)) {
      return (com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.target.ViewTarget<ImageView, Z>) new com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.target.BitmapImageViewTarget(view);
    } else if (Drawable.class.isAssignableFrom(clazz)) {
      return (ViewTarget<ImageView, Z>) new DrawableImageViewTarget(view);
    } else {
      throw new IllegalArgumentException(
          "Unhandled class: " + clazz + ", try .as*(Class).transcode(ResourceTranscoder)");
    }
  }
}
