package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine;

import android.support.annotation.NonNull;
import android.support.v4.util.Pools.Pool;
import android.util.Log;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Options;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.ResourceDecoder;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.data.DataRewinder;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.resource.transcode.ResourceTranscoder;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.util.Preconditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Attempts to decode and transcode  resource type from a given data type.
 *
 * @param <DataType>     The type of data ResourceType that will be decoded from.
 * @param <ResourceType> The type of intermediate resource that will be decoded.
 * @param <Transcode>    The final type of resource that will be transcoded from ResourceType and
 *                       returned to the caller.
 */
public class DecodePath<DataType, ResourceType, Transcode> {
  private static final String TAG = "DecodePath";
  private final Class<DataType> dataClass;
  private final List<? extends ResourceDecoder<DataType, ResourceType>> decoders;
  private final ResourceTranscoder<ResourceType, Transcode> transcoder;
  private final Pool<List<Throwable>> listPool;
  private final String failureMessage;

  public DecodePath(Class<DataType> dataClass, Class<ResourceType> resourceClass,
      Class<Transcode> transcodeClass,
      List<? extends ResourceDecoder<DataType, ResourceType>> decoders,
      ResourceTranscoder<ResourceType, Transcode> transcoder, Pool<List<Throwable>> listPool) {
    this.dataClass = dataClass;
    this.decoders = decoders;
    this.transcoder = transcoder;
    this.listPool = listPool;
    failureMessage = "Failed DecodePath{" + dataClass.getSimpleName() + "->"
        + resourceClass.getSimpleName() + "->" + transcodeClass.getSimpleName() + "}";
  }

  public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource<Transcode> decode(DataRewinder<DataType> rewinder, int width, int height,
                                                                   @NonNull Options options, DecodeCallback<ResourceType> callback) throws GlideException {
    com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource<ResourceType> decoded = decodeResource(rewinder, width, height, options);
    com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource<ResourceType> transformed = callback.onResourceDecoded(decoded);
    return transcoder.transcode(transformed, options);
  }

  @NonNull
  private com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource<ResourceType> decodeResource(DataRewinder<DataType> rewinder, int width,
                                                                               int height, @NonNull Options options) throws GlideException {
    List<Throwable> exceptions = Preconditions.checkNotNull(listPool.acquire());
    try {
      return decodeResourceWithList(rewinder, width, height, options, exceptions);
    } finally {
      listPool.release(exceptions);
    }
  }

  @NonNull
  private com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource<ResourceType> decodeResourceWithList(DataRewinder<DataType> rewinder, int width,
                                                                                       int height, @NonNull Options options, List<Throwable> exceptions) throws GlideException {
    com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource<ResourceType> result = null;
    //noinspection ForLoopReplaceableByForEach to improve perf
    for (int i = 0, size = decoders.size(); i < size; i++) {
      ResourceDecoder<DataType, ResourceType> decoder = decoders.get(i);
      try {
        DataType data = rewinder.rewindAndGet();
        if (decoder.handles(data, options)) {
          data = rewinder.rewindAndGet();
          result = decoder.decode(data, width, height, options);
        }
        // Some decoders throw unexpectedly. If they do, we shouldn't fail the entire load path, but
        // instead log and continue. See #2406 for an example.
      } catch (IOException | RuntimeException | OutOfMemoryError e) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
          Log.v(TAG, "Failed to decode data for " + decoder, e);
        }
        exceptions.add(e);
      }

      if (result != null) {
        break;
      }
    }

    if (result == null) {
      throw new GlideException(failureMessage, new ArrayList<>(exceptions));
    }
    return result;
  }

  @Override
  public String toString() {
    return "DecodePath{" + " dataClass=" + dataClass + ", decoders=" + decoders + ", transcoder="
        + transcoder + '}';
  }

  interface DecodeCallback<ResourceType> {
    @NonNull
    com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource<ResourceType> onResourceDecoded(@NonNull com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.Resource<ResourceType> resource);
  }
}
