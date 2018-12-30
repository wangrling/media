# MultiMedia

ExoPlayer
Exoplayer is an open source, application leve media player build on top of Android's low level media APIs.
1. Creating the player
SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);
2. Attaching the player to a view
// Bind the player to the view.
playerView.setPlayer(player);
3. Preparing the player
// Produces DataSource instances through which media data is loaded.
DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "yourApplicationName"));
// This is the MediaSource representing the media to be played.
MediaSource videoSource = new ExtratorMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri);
// Prepare the player with the source.
player.prepare(videoSource);
4. Controlling the player
player.setPlayWhenReady(playWhenReady);
player.setRepeatMode(repeatMode);
player.setShuffleModeEnabled(shuffleModeEnabled);
player.setPlaybackParameters(playbackParameters);
5. Listening to player events
// Add a listener to receive events from the player.
player.addListener(eventListener);
6. Releasing the player
exoPlayer.release();

MediaSource
The ExoPlayer library provides MediaSource implementations for DASH(DashMediaSource), SmoothStreaming(SsMediaSource), HLS(HlsMediaSource)
and regular media files(ExtractorMediaSource).
The ExoPlayer library aso provides ConcatenatingMediaSource, ClippingMediaSource, LoopingMediaSource and MergingMediaSource. These
MediaSource implementations enable more complex playback functionality through composition.
Playlists
MediaSource firstSource = new ExtractorMediaSource.Factory(...).CreateMediaSource(firstVideoUri);
MediaSource secondSource = new ExtractorMediaSource.Factory(...).createMediaSource(secondVideoUri);
// Plays the first video, then the second video.
ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource(firstSource, secondSource);
Clipping a video
MediaSource videoSource = new ExtractorMediaSource.Factory(...).createMediaSource(videoUri);
// Clip to start at 5 seconds and end at 10 seconds.
ClippingMediaSource clippingSource = new ClippingMediaSource(videoSource, /* startPositionUs= */ 5_000_000, /* endPositionUs= */ 10_000_000);
Looping a video
MediaSource source = new ExtractorMediaSource.Factory(...).createMediaSource(videoUri);
// Plays the video twice.
LoopingMediaSource loopingSource = new LoopingMediaSource(source, 2);
Side-loading a subtitle file
// Build the video MediaSource.
MediaSource videoSource = new ExtractorMediaSource.Factory(...).createMediaSource(videoUri);
// Build the subtitle MediaSource.
Format subtitleFormat = Format.createTextSampleFormat(
	id,		// An identifier for the track. May be null.
	MimeTypes.APPLICTION_SUBRIP,	// The mime type. Must by set correctly.
	selectionFlags,	// Selection flags for the track.
	language);	// The subtitle language. May be null.
MediaSource subtitleSource = new SingleSampleMediaSource.Factory(...).createMediaSource(subtitleUri, subtitleFormat, C.TIME_UNSET);
// Plays the video with the sideloaded subtitle.
MergingMediaSource mergeSource = new MergingMediaSource(videoSource, subtitleSource);

Track selection determines which of the available media tracks are played by the player's Renderer S.
DefaultTrackSelector trackSelector = new DefaultTrackSelector();
// Tells the selector to restrict video track selections to SD, and to select a German audio track if there is one.
trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd().setPreferredAudioLanguage("deu"));
SimpleExoPlayer player = ExoPlayerFactor.newSimpleInstance(context, trackSelector);

Customization
1. Renderer - You may want to implement a custom Renderer to handle a media type not supported by the default implementations provided by the library.
2. TrackSelector - Implementing a cusotm TrackSelector allows an app developer to change the way in which tracks exposed by a
	MediaSource are selected for consumption by each of the available Renderer S.
3. LoadControl - Implementing a custom LoadControl allows an app developer to change the player's buffering policy.
4. Extractor - If you need to support a container format not currently supported by the library, consider implementing a custom Extractor
	class, which can then be used to together with ExtractorMediaSource to play media of that type.
5. MediaSource - Implementing a custom MediaSource class may be appropriate if you wish to obtain media samples to feed to renderers in a
	custom way, or if you wish to implement custom MediaSource compositing bahavior.
6. DataSource - ExoPlayer's upstream package already contains a number of DataSource implementations for different use cases. You may want
	to implement you own DataSource class to load data in another way, such as over a custom protocal, using a custom HTTP stack, or from
	a custom persistent cache.

