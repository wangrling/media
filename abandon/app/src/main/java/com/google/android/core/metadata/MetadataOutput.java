package com.google.android.core.metadata;

/**
 * Receives metadata output.
 */
public interface MetadataOutput {

    /**
     * Called when there is metadata associated with current playback time.
     *
     * @param metadata The metadata.
     */
    void onMetadata(Metadata metadata);

}
