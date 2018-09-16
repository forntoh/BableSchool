package ga.forntoh.bableschool.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import net.protyposis.android.mediaplayer.MediaSource;
import net.protyposis.android.mediaplayer.UriSource;
import net.protyposis.android.mediaplayer.dash.AdaptationLogic;
import net.protyposis.android.mediaplayer.dash.DashSource;
import net.protyposis.android.mediaplayer.dash.SimpleRateBasedAdaptationLogic;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ga.forntoh.bableschool.R;

public class Utils {

    public static final String TAG = "BS_LOG";

    public static String formatScore(double d) {
        if (d < 0) return "N/A";
        return new DecimalFormat("##.##").format(d);
    }

    public static void setTabWidthAsWrapContent(TabLayout tabLayout, int tabPosition) {
        LinearLayout layout = (LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tabPosition);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0f;
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        layout.setLayoutParams(layoutParams);
    }

    public static MediaSource uriToMediaSource(Context context, Uri uri) {
        MediaSource source = null;

        // A DASH source is either detected if the given URL has an .mpd extension or if the DASH
        // pseudo protocol has been prepended.
        if (uri.toString().endsWith(".mpd") || uri.toString().startsWith("dash://")) {
            AdaptationLogic adaptationLogic;

            // Strip dash:// pseudo protocol
            if (uri.toString().startsWith("dash://")) {
                uri = Uri.parse(uri.toString().substring(7));
            }

            //adaptationLogic = new ConstantPropertyBasedLogic(ConstantPropertyBasedLogic.Mode.HIGHEST_BITRATE);
            adaptationLogic = new SimpleRateBasedAdaptationLogic();

            source = new DashSource(context, uri, adaptationLogic);
        } else {
            source = new UriSource(context, uri);
        }
        return source;
    }

    public static void uriToMediaSourceAsync(final Context context, Uri uri, MediaSourceAsyncCallbackHandler callback) {
        LoadMediaSourceAsyncTask loadingTask = new LoadMediaSourceAsyncTask(context, callback);

        try {
            loadingTask.execute(uri).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PopupWindow startPopUpWindow(View layout, View root, View.OnTouchListener onTouch) {
        PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        popupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(onTouch);
        popupWindow.setOnDismissListener(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                ((Activity) layout.getContext()).getWindow().setStatusBarColor(root.getContext().getResources().getColor(R.color.bgLightGrey));
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            ((Activity) layout.getContext()).getWindow().setStatusBarColor(ContextCompat.getColor(root.getContext(), android.R.color.black));
        return popupWindow;
    }

    public static void setupListDisplay(RecyclerView.Adapter adapter, RecyclerView rv, int span, boolean dividers, RecyclerView.ItemDecoration decoration) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(rv.getContext(), span);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);

        if (decoration != null) rv.addItemDecoration(decoration);
        else if (dividers) {
            rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL));
            rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), LinearLayoutManager.HORIZONTAL));
        }
    }

    public static float dpToPixels(Context c, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, c.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static void setupVerticalDisplay(RecyclerView.Adapter adapter, RecyclerView rv, boolean dividers, RecyclerView.ItemDecoration decoration) {
        try {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rv.getContext(), LinearLayoutManager.VERTICAL, false);
            rv.setLayoutManager(layoutManager);
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
            rv.setNestedScrollingEnabled(false);
            rv.addItemDecoration(decoration);
            if (dividers)
                rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL));
        } catch (Exception ignored) {
        }
    }

    public static Thread setupHorizontalDisplay(RecyclerView.Adapter adapter, ArrayList list, RecyclerView rv, int time, boolean snap, RecyclerView.ItemDecoration decoration) {
        Thread thread = null;
        try {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rv.getContext(), LinearLayoutManager.HORIZONTAL, false);
            rv.setLayoutManager(layoutManager);
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
            rv.setNestedScrollingEnabled(false);
            rv.addItemDecoration(decoration);

            if (snap) {
                SnapHelper helper = new LinearSnapHelper();
                helper.attachToRecyclerView(rv);
            }

            if (time > 0) {
                thread = new Thread(() -> {
                    while (true) {
                        try {
                            LinearLayoutManager mL = (LinearLayoutManager) rv.getLayoutManager();
                            int pos = mL.findLastCompletelyVisibleItemPosition();
                            rv.smoothScrollToPosition(pos == list.size() - 1 ? 0 : pos + 1);
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                });
                thread.start();
                thread.setName("Horizontal Scroll:" + rv.getId());
            } else return null;

        } catch (Exception ignored) {
        }
        return thread;
    }

    public interface MediaSourceAsyncCallbackHandler {
        void onMediaSourceLoaded(MediaSource mediaSource);

        void onException(Exception e);
    }

    private static class LoadMediaSourceAsyncTask extends AsyncTask<Uri, Void, MediaSource> {

        private Context mContext;
        private MediaSourceAsyncCallbackHandler mCallbackHandler;
        private MediaSource mMediaSource;
        private Exception mException;

        LoadMediaSourceAsyncTask(Context context, MediaSourceAsyncCallbackHandler callbackHandler) {
            mContext = context;
            mCallbackHandler = callbackHandler;
        }

        @Override
        protected MediaSource doInBackground(Uri... params) {
            try {
                mMediaSource = Utils.uriToMediaSource(mContext, params[0]);
                return mMediaSource;
            } catch (Exception e) {
                mException = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(MediaSource mediaSource) {
            if (mException != null) {
                mCallbackHandler.onException(mException);
            } else {
                mCallbackHandler.onMediaSourceLoaded(mMediaSource);
            }
        }
    }
}
