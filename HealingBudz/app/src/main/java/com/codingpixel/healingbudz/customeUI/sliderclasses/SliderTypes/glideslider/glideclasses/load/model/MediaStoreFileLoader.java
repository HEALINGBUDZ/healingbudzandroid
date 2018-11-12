package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.Priority;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.DataSource;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.Options;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.data.DataFetcher;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.data.mediastore.MediaStoreUtil;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.signature.ObjectKey;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Loads the file path for {@link MediaStore} owned {@link Uri uris}.
 */
public final class MediaStoreFileLoader implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoader<Uri, File> {

  private final Context context;

  // Public API.
  @SuppressWarnings("WeakerAccess")
  public MediaStoreFileLoader(Context context) {
    this.context = context;
  }

  @Override
  public LoadData<File> buildLoadData(@NonNull Uri uri, int width, int height,
      @NonNull Options options) {
    return new LoadData<>(new ObjectKey(uri), new FilePathFetcher(context, uri));
  }

  @Override
  public boolean handles(@NonNull Uri uri) {
    return MediaStoreUtil.isMediaStoreUri(uri);
  }

  private static class FilePathFetcher implements DataFetcher<File> {
    private static final String[] PROJECTION = new String[] {
        MediaStore.MediaColumns.DATA,
    };

    private final Context context;
    private final Uri uri;

    FilePathFetcher(Context context, Uri uri) {
      this.context = context;
      this.uri = uri;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super File> callback) {
      Cursor cursor = context.getContentResolver().query(uri, PROJECTION, null /*selection*/,
          null /*selectionArgs*/, null /*sortOrder*/);

      String filePath = null;
      if (cursor != null) {
        try {
          if (cursor.moveToFirst()) {
            filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
          }
        } finally {
          cursor.close();
        }
      }

      if (TextUtils.isEmpty(filePath)) {
        callback.onLoadFailed(new FileNotFoundException("Failed to find file path for: " + uri));
      } else {
        callback.onDataReady(new File(filePath));
      }
    }

    @Override
    public void cleanup() {
      // Do nothing.
    }

    @Override
    public void cancel() {
      // Do nothing.
    }

    @NonNull
    @Override
    public Class<File> getDataClass() {
      return File.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
      return DataSource.LOCAL;
    }
  }

  /**
   * {@link com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoaderFactory} for {@link MediaStoreFileLoader}s.
   */
  public static final class Factory implements com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.model.ModelLoaderFactory<Uri, File> {

    private final Context context;

    public Factory(Context context) {
      this.context = context;
    }

    @NonNull
    @Override
    public ModelLoader<Uri, File> build(MultiModelLoaderFactory multiFactory) {
      return new MediaStoreFileLoader(context);
    }

    @Override
    public void teardown() {
      // Do nothing.
    }
  }
}
