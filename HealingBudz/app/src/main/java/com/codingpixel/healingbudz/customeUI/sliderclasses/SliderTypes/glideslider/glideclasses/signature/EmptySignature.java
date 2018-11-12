package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.signature;

import android.support.annotation.NonNull;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Key;

import java.security.MessageDigest;

/**
 * An empty key that is always equal to all other empty keys.
 */
public final class EmptySignature implements Key {
  private static final EmptySignature EMPTY_KEY = new EmptySignature();

  @NonNull
  public static EmptySignature obtain() {
    return EMPTY_KEY;
  }

  private EmptySignature() {
    // Empty.
  }

  @Override
  public String toString() {
    return "EmptySignature";
  }

  @Override
  public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    // Do nothing.
  }
}
