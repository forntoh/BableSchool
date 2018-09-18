package ga.forntoh.bableschool.adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.Video;
import ga.forntoh.bableschool.utils.Utils;

public class VideosAdapter extends CategoryAdapter {

    public VideosAdapter(ArrayList list) {
        super(list);
    }

    @NonNull
    @Override
    public VideosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideosAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Video video = (Video) list.get(position);
        holder.title.setText(video.getTitle());
        holder.author.setText(video.getAuthor());
        holder.duration.setText(video.getDuration());
        if (TextUtils.isEmpty(video.getThumbnail()))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
        else
            Picasso.get().load(video.getThumbnail()).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder {

        private VideoView videoView;
        private MediaController mediaController;
        private ProgressBar videoLoadingProgress;
        private PopupWindow popupWindow;

        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
            author = itemView.findViewById(R.id.video_author);
            duration = itemView.findViewById(R.id.video_time);
            thumbnail = itemView.findViewById(R.id.video_thumbnail);
        }

        @SuppressLint("InflateParams")
        @Override
        public void onClick(View view) {
            View layout = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_video_player, null);
            popupWindow = Utils.startPopUpWindow(layout, itemView.getRootView().getRootView(), this::onTouch);

            videoView = layout.findViewById(R.id.videoView);

            videoView.setOnCompletionListener((mediaPlayer) -> popupWindow.dismiss());
            videoLoadingProgress = layout.findViewById(R.id.progressBar);

            mediaController = new MediaController(itemView.getContext());
            mediaController.setAnchorView(videoView);
            mediaController.setMediaPlayer(videoView);
            mediaController.setEnabled(false);

            videoLoadingProgress.setVisibility(View.VISIBLE);

            initPlayer();

            Uri videoUri = Uri.parse(((Video) list.get(getAdapterPosition())).getUrl());
            videoView.setVideoURI(videoUri);
            videoView.start();
        }

        private void initPlayer() {
            videoView.setOnPreparedListener(vp -> {
                videoLoadingProgress.setVisibility(View.GONE);
                mediaController.setEnabled(true);
            });
        }

        boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mediaController.isShowing()) {
                    mediaController.hide();
                } else {
                    mediaController.show();
                }
            }
            return false;
        }
    }
}
