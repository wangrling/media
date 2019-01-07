package com.android.media.player.core;

/**
 * A media player interface defining traditional high-level functionality,
 * such as the ability to play, pause, seek and query properties of the currently playing media.
 *
 * <p>Some important properties of media players that implement this interface are:</p>
 * <ul>
 *     <li>They can provide a {@link Timeline} representing the structure of the media being played,
 *     which can be obtained by calling {@link #getCurrentTimeline()}.</li>
 *     <li>They can provide a {@link TrackGroupArray} defining the currently available tracks,
 *     which can be obtained by calling {@link #getCurrentTrackGroups()}.</li>
 *     <li>They contain a number of renderers, each of which is able to render tracks of a single
 *     type (e.g. audio, video or text). The number of renderers and their respective track types
 *     can be obtained by calling {@link #getRendererCount()} and {@link #getRendererType(int)}.</li>
 *     <li>They can provide a {@link TrackSelectionArray} defining which of the currently available
 *     tracks are selected to be rendered by each renderer. This can be obtained by calling
 *     {@link #getCurrentTrackSelections()}.</li>
 * </ul>
 */

// 总体的意思是给App client程序提供各种各种函数操作player，比如常规的暂停和开始，可以选择Track，想要播放什么声音，
// 可以选择Renderer，渲染不同的源，通常都是有默认的。
public interface Player {

}
