package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request;

import android.support.annotation.Nullable;

/**
 * Runs a single primary {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} until it completes and then a fallback error request only
 * if the single primary request fails.
 */
public final class ErrorRequestCoordinator implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.RequestCoordinator,
        com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request {

  @Nullable
  private final com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.RequestCoordinator parent;
  private com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request primary;
  private com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request error;

  public ErrorRequestCoordinator(@Nullable com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.RequestCoordinator parent) {
    this.parent = parent;
  }

  public void setRequests(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request primary, com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request error) {
    this.primary = primary;
    this.error = error;
  }

  @Override
  public void begin() {
    if (!primary.isRunning()) {
      primary.begin();
    }
  }

  @Override
  public void pause() {
    if (!primary.isFailed()) {
      primary.pause();
    }
    if (error.isRunning()) {
      error.pause();
    }
  }

  @Override
  public void clear() {
    primary.clear();
    // Don't check primary.isFailed() here because it will have been reset by the clear call
    // immediately before this.
    if (error.isRunning()) {
      error.clear();
    }
  }

  @Override
  public boolean isPaused() {
    return primary.isFailed() ? error.isPaused() : primary.isPaused();
  }

  @Override
  public boolean isRunning() {
    return primary.isFailed() ? error.isRunning() : primary.isRunning();
  }

  @Override
  public boolean isComplete() {
    return primary.isFailed() ? error.isComplete() : primary.isComplete();
  }

  @Override
  public boolean isResourceSet() {
    return primary.isFailed() ? error.isResourceSet() : primary.isResourceSet();
  }

  @Override
  public boolean isCancelled() {
    return primary.isFailed() ? error.isCancelled() : primary.isCancelled();
  }

  @Override
  public boolean isFailed() {
    return primary.isFailed() && error.isFailed();
  }

  @Override
  public void recycle() {
    primary.recycle();
    error.recycle();
  }

  @Override
  public boolean isEquivalentTo(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request o) {
    if (o instanceof ErrorRequestCoordinator) {
      ErrorRequestCoordinator other = (ErrorRequestCoordinator) o;
      return primary.isEquivalentTo(other.primary) && error.isEquivalentTo(other.error);
    }
    return false;
  }

  @Override
  public boolean canSetImage(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    return parentCanSetImage() && isValidRequest(request);
  }

  private boolean parentCanSetImage() {
    return parent == null || parent.canSetImage(this);
  }

  @Override
  public boolean canNotifyStatusChanged(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    return parentCanNotifyStatusChanged() && isValidRequest(request);
  }

  @Override
  public boolean canNotifyCleared(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    return parentCanNotifyCleared() && isValidRequest(request);
  }

  private boolean parentCanNotifyCleared() {
    return parent == null || parent.canNotifyCleared(this);
  }

  private boolean parentCanNotifyStatusChanged() {
    return parent == null || parent.canNotifyStatusChanged(this);
  }

  private boolean isValidRequest(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    return request.equals(primary) || (primary.isFailed() && request.equals(error));
  }

  @Override
  public boolean isAnyResourceSet() {
    return parentIsAnyResourceSet() || isResourceSet();
  }

  private boolean parentIsAnyResourceSet() {
    return parent != null && parent.isAnyResourceSet();
  }

  @Override
  public void onRequestSuccess(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request) {
    if (parent != null) {
      parent.onRequestSuccess(this);
    }
  }

  @Override
  public void onRequestFailed(Request request) {
    if (!request.equals(error)) {
      if (!error.isRunning()) {
        error.begin();
      }
      return;
    }

    if (parent != null) {
      parent.onRequestFailed(this);
    }
  }
}
