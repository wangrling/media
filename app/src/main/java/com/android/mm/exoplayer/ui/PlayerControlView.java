package com.android.mm.exoplayer.ui;

// 进行播放控制

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mm.R;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.util.RepeatModeUtil;

import java.util.Formatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A view for controlling {@link Player} instances.
 *
 * <p>
 *     A PlayerControlView can be customized by setting attributes (or calling corresponding
 *     methods), overriding the view's layout file or by specifying a custom view layout file,
 *     as outline below.
 * </p>
 *
 * <h3>Attributes</h3>
 *
 * The following attributes can be set on a PlayerControlView when used in a layout XML file:
 *
 * <ul>
 *     <li>
 *         // 不操作UI后UI的显示时间。
 *         <b>{@code show_timeout}</b> - The time between the last user interaction and the controls
 *         being automatically hidden, in milliseconds. Use zero if the controls should not
 *         automatically timeout.
 *         <ul>
 *             <li>Corresponding method: {@link #setShowTimeoutMus(int)}</li>
 *             <li>Default: {@link #DEFAULT_SHOW_TIME_MS}</li>
 *         </ul>
 *     </li>
 *
 * </ul>
 *
 * <h3>Overriding the layout file</h3>
 *
 * // 自定义exo_player_control_view.xml方法。
 * To customize the layout of the PlayerControlView throughout your app, or just for certain
 * configurations, you can define {@code exo_player_control_view.xml} layout file in your
 * application {@code res/layout*} directories. These layouts will override the one provided by
 * the ExoPlayer library, and will be inflated for use by PlayerControlView. The view
 * identifies and binds its children by looking for the following ids.
 *
 * // id名字不能改变。
 *
 *
 */

// 通过gravity属性来控制子View的位置。
public class PlayerControlView extends FrameLayout {
    /* 应该没什么作用，毕竟不是通过gradle引用的UI模块。
    static {
        ExoPlayerLibraryInfo.registerModule("google.exo.ui");
    }
    */

    /** Listener to be notified about changes of the visibility of the UI control. */
    public interface VisibilityListener {

        /**
         * Called when the visibility changes.
         *
         * @param visibility The new visibility. Either {@link View#VISIBLE} or {@link View#GONE}.
         */
        void onVisibilityChange(int visibility);
    }

    /** The default fast forward increment, in milliseconds. */
    public static final int DEFAULT_FAST_FORWARD_MS = 15000;
    /** The default rewind increment, in milliseconds. */
    public static final int DEFAULT_REWIND_MS = 5000;
    /** The default show timeout, in milliseconds. */
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    /** The default repeat toggle modes. */
    public static final @RepeatModeUtil.RepeatToggleModes int DEFAULT_REPEAT_TOGGLE_MODES =
            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE;

    /** The maximum number of windows that can be shown in a multi-window time bar. */
    public static final int MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR = 100;

    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;

    private final ComponentListener componentListener;
    private final View previousButton;
    private final View nextButton;
    private final View playButton;
    private final View pauseButton;
    private final View fastForwardButton;
    private final View rewindButton;
    private final ImageView repeatToggleButton;
    private final View shuffleButton;
    private final TextView durationView;
    private final TextView positionView;
    private final TimeBar timeBar;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private final Timeline.Period period;
    private final Timeline.Window window;
    private final Runnable updateProgressAction;
    private final Runnable hideAction;

    private final Drawable repeatOffButtonDrawable;
    private final Drawable repeatOneButtonDrawable;
    private final Drawable repeatAllButtonDrawable;
    private final String repeatOffButtonContentDescription;
    private final String repeatOneButtonContentDescription;
    private final String repeatAllButtonContentDescription;

    private Player player;
    private com.google.android.exoplayer2.ControlDispatcher controlDispatcher;
    private VisibilityListener visibilityListener;
    private @Nullable PlaybackPreparer playbackPreparer;

    private boolean isAttachedToWindow;
    private boolean showMultiWindowTimeBar;
    private boolean multiWindowTimeBar;
    private boolean scrubbing;
    private int rewindMs;
    private int fastForwardMs;
    private int showTimeoutMs;
    private @RepeatModeUtil.RepeatToggleModes int repeatToggleModes;
    private boolean showShuffleButton;
    private long hideAtMs;
    private long[] adGroupTimesMs;
    private boolean[] playedAdGroups;
    private long[] extraAdGroupTimesMs;
    private boolean[] extraPlayedAdGroups;



    public PlayerControlView(@NonNull Context context) {
        super(context, null);
    }

    public PlayerControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PlayerControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int controllerLayoutId = R.layout.exo_player_control_view;

        rewindMs = DEFAULT_REWIND_MS;
    }


}
