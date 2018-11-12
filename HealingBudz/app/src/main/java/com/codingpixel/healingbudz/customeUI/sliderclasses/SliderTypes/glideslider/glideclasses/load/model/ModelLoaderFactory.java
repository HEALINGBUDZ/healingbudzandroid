package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * An interface for creating a {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader} for a given model type. Will be retained
 * statically so should not retain {@link Context} or any other objects that cannot be retained for
 * the life of the application. ModelLoaders will not be retained statically so it is safe for any
 * ModelLoader built by this factory to retain a reference to a {@link Context}.
 *
 * @param <T> The type of the model the {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader}s built by
 *            this factory can handle
 * @param <Y> The type of data the {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader}s built by this
 *            factory can load.
 */
public interface ModelLoaderFactory<T, Y> {

  /**
   * Build a concrete ModelLoader for this model type.
   *
   * @param multiFactory A map of classes to factories that can be used to construct additional
   *                     {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader}s that this factory's {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader} may depend on
   * @return A new {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader}
   */
  @NonNull
  com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader<T, Y> build(@NonNull MultiModelLoaderFactory multiFactory);

  /**
   * A lifecycle method that will be called when this factory is about to replaced.
   */
  void teardown();
}
