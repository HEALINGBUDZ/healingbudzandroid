package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

/**
 * A coordinator that coordinates two individual {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request}s that load a small thumbnail
 * version of an image and the full size version of the image at the same time.
 */
public class ThumbnailRequestCoordinator implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.RequestCoordinator,
        com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request {
  @Nullable private final com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.RequestCoordinator parent;

  private com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request full;
  private com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request thumb;
  private boolean isRunning;

  @VisibleForTesting
  ThumbnailRequestCoordinator() {
    this(/*parent=*/ null);
  }

  public ThumbnailRequestCoordinator(@Nullable com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.RequestCoordinator parent) {
    this.parent = parent;
  }

  public void setRequests(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request full, com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request thumb) {
    this.full = full;
    this.thumb = thumb;
  }

  /**
   * Returns true if the request is either the request loading the full size image or if the request
   * loading the full size image has not yet completed.
   *
   * @param request {@inheritDoc}
   */
  @Override
  public boolean canSetImage(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    return parentCanSetImage() && (request.equals(full) || !full.isResourceSet());
  }

  private boolean parentCanSetImage() {
    return parent == null || parent.canSetImage(this);
  }

  /**
   * Returns true if the request is the request loading the full size image and if neither the full
   * nor the thumbnail image have completed successfully.
   *
   * @param request {@inheritDoc}.
   */
  @Override
  public boolean canNotifyStatusChanged(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    return parentCanNotifyStatusChanged() && request.equals(full) && !isAnyResourceSet();
  }

  @Override
  public boolean canNotifyCleared(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    return parentCanNotifyCleared() && request.equals(full);
  }

  private boolean parentCanNotifyCleared() {
    return parent == null || parent.canNotifyCleared(this);
  }

  private boolean parentCanNotifyStatusChanged() {
    return parent == null || parent.canNotifyStatusChanged(this);
  }

  @Override
  public boolean isAnyResourceSet() {
    return parentIsAnyResourceSet() || isResourceSet();
  }

  @Override
  public void onRequestSuccess(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    if (request.equals(thumb)) {
      return;
    }
    if (parent != null) {
      parent.onRequestSuccess(this);
    }
    // Clearing the thumb is not necessarily safe if the thumb is being displayed in the Target,
    // as a layer in a cross fade for example. The only way we know the thumb is not being
    // displayed and is therefore safe to clear is if the thumb request has not yet completed.
    if (!thumb.isComplete()) {
      thumb.clear();
    }
  }

  @Override
  public void onRequestFailed(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    if (!request.equals(full)) {
      return;
    }

    if (parent != null) {
      parent.onRequestFailed(this);
    }
  }

  private boolean parentIsAnyResourceSet() {
    return parent != null && parent.isAnyResourceSet();
  }

  /**
   * Starts first the thumb request and then the full request.
   */
  @Override
  public void begin() {
    isRunning = true;
    // If the request has completed previously, there's no need to restart both the full and the
    // thumb, we can just restart the full.
    if (!full.isComplete() && !thumb.isRunning()) {
      thumb.begin();
    }
    if (isRunning && !full.isRunning()) {
      full.begin();
    }
  }

  @Override
  public void pause() {
    isRunning = false;
    full.pause();
    thumb.pause();
  }

  @Override
  public void clear() {
    isRunning = false;
    thumb.clear();
    full.clear();
  }

  @Override
  public boolean isPaused() {
    return full.isPaused();
  }

  /**
   * Returns true if the full request is still running.
   */
  @Override
  public boolean isRunning() {
    return full.isRunning();
  }

  /**
   * Returns true if the full request is complete.
   */
  @Override
  public boolean isComplete() {
    return full.isComplete() || thumb.isComplete();
  }

  @Override
  public boolean isResourceSet() {
    return full.isResourceSet() || thumb.isResourceSet();
  }

  @Override
  public boolean isCancelled() {
    return full.isCancelled();
  }

  /**
   * Returns true if the full request has failed.
   */
  @Override
  public boolean isFailed() {
    return full.isFailed();
  }

  @Override
  public void recycle() {
    full.recycle();
    thumb.recycle();
  }

  @Override
  public boolean isEquivalentTo(Request o) {
    if (o instanceof ThumbnailRequestCoordinator) {
      ThumbnailRequestCoordinator that = (ThumbnailRequestCoordinator) o;
      return (full == null ? that.full == null : full.isEquivalentTo(that.full))
          && (thumb == null ? that.thumb == null : thumb.isEquivalentTo(that.thumb));
    }
    return false;
  }
}
