package com.android.mm.exoplayer.testutil;

import android.content.Context;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;

public class TestUtil {

    public static final String TAG = "ExoPlayer";

    public static byte[] getByteArray(Context context, String fileName) throws IOException {
        return Util.toByteArray(getInputStream(context, fileName));
    }

    public static InputStream getInputStream(Context context, String fileName) throws IOException {
        return context.getResources().getAssets().open(fileName);
    }

    /**
     * Asserts that data read from a {@link DataSource} matches {@code expected}.
     *
     * @param dataSource The {@link DataSource} through which to read.
     * @param dataSpec The {@link DataSpec} to use when opening the {@link DataSource}.
     * @param expectedData The expected data.
     * @param expectKnownLength Whether to assert that {@link DataSource#open} returns the expected
     *     data length. If false then it's asserted that {@link C#LENGTH_UNSET} is returned.
     * @throws IOException If an error occurs reading fom the {@link DataSource}.
     */
    public static void assertDataSourceContent(
            DataSource dataSource, DataSpec dataSpec, byte[] expectedData, boolean expectKnownLength
    ) throws IOException {
        try {
            Log.d(TAG, "assertDataSourceContent");
            // 抛出异常没有去捕获导致测试通过。
            long length = dataSource.open(dataSpec);
            assertThat(length).isEqualTo(expectKnownLength ? expectedData.length : C.LENGTH_UNSET);
            byte[] readData = readToEnd(dataSource);
            assertThat(readData).isEqualTo(expectedData);
        } finally {
            dataSource.close();
        }
    }

    private static byte[] readToEnd(DataSource dataSource) throws IOException {
        byte[] data = new byte[1024];
        int position = 0;
        int bytesRead = 0;
        while (bytesRead != C.RESULT_END_OF_INPUT) {
            if (position == data.length) {
                data = Arrays.copyOf(data, data.length * 2);
            }
            bytesRead = dataSource.read(data, position, data.length - position);
            if (bytesRead != C.RESULT_END_OF_INPUT) {
                position += bytesRead;
            }
        }
        return Arrays.copyOf(data, position);
    }


}
