package com.dwakenya.techsavanna.newdwa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;


public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.VideoInfoHolder>{
        
        //these ids are the unique id for each video
        String[] VideoID = {"P3mAtvs5Elc", "nCgQDjiotG0", "P3mAtvs5Elc"};
        Context ctx;
        
        public YoutubeAdapter(Context context) {
            this.ctx = context;
           
        }
        
        @Override
        public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtuberaw, parent, false);
            return new VideoInfoHolder(itemView);
        }
    
    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {
        final ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Loading Content");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    
        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                progressDialog.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("No internet connection...");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        ((Activity)ctx).finish();
                    }
                });
    
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                if (!s.isEmpty()){
                    holder.CardViewYou.setVisibility(View.VISIBLE);
                    youTubeThumbnailView.setVisibility(View.VISIBLE);
                    holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }
        };
    
        holder.youTubeThumbnailView.initialize(YoutubeConfig.getApiKey(), new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(VideoID[position]);
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }
        
            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                progressDialog.dismiss();
                //write something for failure
            }
        });
        holder.relativeLayoutOverYouTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx,
                    YoutubeConfig.getApiKey(),
                    VideoID[position],//video id
                    10,     //after this time, video will start automatically
                    true,               //autoplay or not
                    false);             //lightbox mode or not; show the video in a small box
            ctx.startActivity(intent);
        
            }
        });
   
    }
    
    @Override
    public int getItemCount() {
        return VideoID.length;
    }
    
    public class VideoInfoHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        CardView CardViewYou;
        
        public VideoInfoHolder(View itemView) {
            super(itemView);
            relativeLayoutOverYouTubeThumbnailView = itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);
            CardViewYou = itemView.findViewById(R.id.CardViewYou);
        }
        
//        @Override
//        public void onClick(View v) {
//            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx,
//                    YoutubeConfig.getApiKey(),
//                    VideoID[getLayoutPosition()],//video id
//                    100,     //after this time, video will start automatically
//                    true,               //autoplay or not
//                    false);             //lightbox mode or not; show the video in a small box
//            ctx.startActivity(intent);
//        }
    }
}
