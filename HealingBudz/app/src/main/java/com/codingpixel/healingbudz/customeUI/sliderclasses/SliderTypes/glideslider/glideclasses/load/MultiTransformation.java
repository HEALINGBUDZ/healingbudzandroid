package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load;

import android.content.Context;
import android.support.annotation.NonNull;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;

/**
 * A transformation that applies one or more transformations in iteration order to a resource.
 *
 * @param <T> The type of {@link Resource} that will be transformed.
 */
public class MultiTransformation<T> implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Transformation<T> {
  private final Collection<? extends com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Transformation<T>> transformations;

  @SafeVarargs
  @SuppressWarnings("varargs")
  public MultiTransformation(@NonNull com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Transformation<T>... transformations) {
    if (transformations.length == 0) {
      throw new IllegalArgumentException(
          "MultiTransformation must contain at least one Transformation");
    }
    this.transformations = Arrays.asList(transformations);
  }

  public MultiTransformation(@NonNull Collection<? extends com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Transformation<T>> transformationList) {
    if (transformationList.isEmpty()) {
      throw new IllegalArgumentException(
          "MultiTransformation must contain at least one Transformation");
    }
    this.transformations = transformationList;
  }

  @NonNull
  @Override
  public Resource<T> transform(
      @NonNull Context context, @NonNull Resource<T> resource, int outWidth, int outHeight) {
    Resource<T> previous = resource;

    for (com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Transformation<T> transformation : transformations) {
      Resource<T> transformed = transformation.transform(context, previous, outWidth, outHeight);
      if (previous != null && !previous.equals(resource) && !previous.equals(transformed)) {
        previous.recycle();
      }
      previous = transformed;
    }
    return previous;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MultiTransformation) {
      MultiTransformation<?> other = (MultiTransformation<?>) o;
      return transformations.equals(other.transformations);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return transformations.hashCode();
  }

  @Override
  public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    for (com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Transformation<T> transformation : transformations) {
      transformation.updateDiskCacheKey(messageDigest);
    }
  }
}
