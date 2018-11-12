package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.stream;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Options;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.GlideUrl;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoaderFactory;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.MultiModelLoaderFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Loads {@link InputStream}s from http or https {@link Uri}s.
 */
public class HttpUriLoader implements ModelLoader<Uri, InputStream> {
  private static final Set<String> SCHEMES =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList("http", "https")));

  private final ModelLoader<GlideUrl, InputStream> urlLoader;

  // Public API.
  @SuppressWarnings("WeakerAccess")
  public HttpUriLoader(ModelLoader<GlideUrl, InputStream> urlLoader) {
    this.urlLoader = urlLoader;
  }

  @Override
  public LoadData<InputStream> buildLoadData(@NonNull Uri model, int width, int height,
      @NonNull Options options) {
    return urlLoader.buildLoadData(new GlideUrl(model.toString()), width, height, options);
  }

  @Override
  public boolean handles(@NonNull Uri model) {
    return SCHEMES.contains(model.getScheme());
  }

  /**
   * Factory for loading {@link InputStream}s from http/https {@link Uri}s.
   */
  public static class Factory implements ModelLoaderFactory<Uri, InputStream> {

    @NonNull
    @Override
    public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiFactory) {
      return new HttpUriLoader(multiFactory.build(GlideUrl.class, InputStream.class));
    }

    @Override
    public void teardown() {
      // Do nothing.
    }
  }
}