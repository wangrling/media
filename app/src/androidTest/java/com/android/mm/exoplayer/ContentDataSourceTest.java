package com.android.mm.exoplayer;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;

import com.google.android.exoplayer2.C;

import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContentDataSourceTest {

    private static final String AUTHORITY = "com.google.android.exoplayer2.core.test";
    private static final String DATA_PATH = "binary/1024_incrementing_bytes.mp3";

    @Test
    public void testRead() {
        assertData(0, C.LENGTH_UNSET, false);
    }

    private static void assertData(int offset, int length, boolean pipeMode) {

    }

    /**
     * A {@link ContentProvider} for the test.
     */
    public static final class TestContentProvider extends ContentProvider implements
            ContentProvider.PipeDataWriter<Object> {

        private static final String PARAM_PIPE_MODE = "pipe-mode";

        public static Uri buildUri(String filePath, boolean pipeMode) {
            Uri.Builder builder = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_CONTENT)
                    .authority(AUTHORITY)
                    .path(filePath);

            if (pipeMode) {
                builder.appendQueryParameter(TestContentProvider.PARAM_PIPE_MODE, "1");
            }
            return builder.build();
        }

        @Override
        public boolean onCreate() {
            return false;
        }

        @Nullable
        @Override
        public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
            return null;
        }

        @Nullable
        @Override
        public String getType(@NonNull Uri uri) {
            return null;
        }

        @Nullable
        @Override
        public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
            return null;
        }

        @Override
        public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
            return 0;
        }

        @Override
        public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
            return 0;
        }

        // write a stream of data to a pipe.
        @Override
        public void writeDataToPipe(@NonNull ParcelFileDescriptor output, @NonNull Uri uri, @NonNull String mimeType, @Nullable Bundle opts, @Nullable Object args) {

        }
    }
}
