package com.android.live.player.core.upstream;

import java.io.IOException;

public interface DataSource {




    /**
     * Closes the source.
     * <p>
     * Note: This method must be called even if the corresponding call to {@link #open(DataSpec)}
     * threw an {@link IOException}. See {@link #open(DataSpec)} for more details.
     *
     * @throws IOException If an error occurs closing the source.
     */
    void close() throws IOException;
}
