package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> tweetList){
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvName;
        TextView tvCreateAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvCreateAt = itemView.findViewById(R.id.tvCreatedAt);
        }

        public void bind(Tweet tweet){
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvScreenName.setText("@"+tweet.user.screenName);
            tvCreateAt.setText("∙"+calculateTweetAge(tweet.createdAt));
            Glide.with(context).load(tweet.user.profileImageUrl).apply(RequestOptions.circleCropTransform()).into(ivProfileImage);
        }
    }

    private String calculateTweetAge(String date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        String ourTime = dateFormat.format(cal.getTime());
        Date ourDate = new Date();
        Date tweetDate = new Date();

        try {
            tweetDate = dateFormat.parse(date);
            ourDate = dateFormat.parse(ourTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = ourDate.getTime() - tweetDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60000) % 60;
        long diffHours = diff / (3600000) % 24;
        long diffDays = diff / (86400000);
        if (diffDays == 0){
            if (diffHours == 0){
                if (diffMinutes == 0){
                    return Long.toString(diffSeconds)+"s";
                }
                else return Long.toString(diffMinutes)+"m";
            }
            else return Long.toString(diffHours)+"h";
        }
        else return Long.toString(diffDays)+"d";

    }
}
