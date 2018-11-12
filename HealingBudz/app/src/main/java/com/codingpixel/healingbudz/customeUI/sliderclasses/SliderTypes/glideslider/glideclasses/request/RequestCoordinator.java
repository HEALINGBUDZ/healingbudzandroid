package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request;

/**
 * An interface for coordinating multiple requests with the same {@link
 * com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.target.Target}.
 */
public interface RequestCoordinator {

  /**
   * Returns true if the {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} can display a loaded bitmap.
   *
   * @param request The {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} requesting permission to display a bitmap.
   */
  boolean canSetImage(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request);

  /**
   * Returns true if the {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} can display a placeholder.
   *
   * @param request The {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} requesting permission to display a placeholder.
   */
  boolean canNotifyStatusChanged(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request);

  /**
   * Returns {@code true} if the {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} can clear the
   * {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.target.Target}.
   */
  boolean canNotifyCleared(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request);

  /**
   * Returns true if any coordinated {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} has successfully completed.
   *
   * @see com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request#isComplete()
   */
  boolean isAnyResourceSet();

  /**
   * Must be called when a {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} coordinated by this object completes successfully.
   */
  void onRequestSuccess(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request);

  /** Must be called when a {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request} coordinated by this object fails. */
  void onRequestFailed(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.Request request);
}
