package com.shivichu.recyclerviewwithviewtype.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.shivichu.recyclerviewwithviewtype.R;
import com.shivichu.recyclerviewwithviewtype.model.Model;
import com.shivichu.recyclerviewwithviewtype.utils.Constants;
import com.shivichu.recyclerviewwithviewtype.utils.PaginationRecyclerViewAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Model> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationRecyclerViewAdapterCallback mCallback;

    private String errorMsg;

    public PaginationRecyclerViewAdapter(Context context) {
        this.context = context;
        this.mCallback = (PaginationRecyclerViewAdapterCallback) context;
        movieResults = new ArrayList<>();
    }

    public List<Model> getMovies() {
        return movieResults;
    }

    public void setMovies(List<Model> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case Model.TYPE_LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
            case Model.TYPE_1:
                View viewItem = inflater.inflate(R.layout.text_type, parent, false);
                viewHolder = new Type1VH(viewItem);
                break;
            case Model.TYPE_2:
                View viewHero = inflater.inflate(R.layout.item_hero, parent, false);
                viewHolder = new Type2VH(viewHero);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Model result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {

            case Model.TYPE_LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    "Something went wrong");

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;

            case Model.TYPE_1:

                break;

            case Model.TYPE_2:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constants.TYPE;
        } else {
            return (position == movieResults.size() - 1 && isLoadingAdded) ? Model.TYPE_LOADING : Constants.TYPE;
        }
    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */


    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Model r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Model> moveResults) {
        for (Model result : moveResults) {
            add(result);
        }
    }

    public void remove(Model r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Model());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        Model result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Model getItem(int position) {
        return movieResults.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Header ViewHolder
     */
    protected class Type2VH extends RecyclerView.ViewHolder {


        public Type2VH(View itemView) {
            super(itemView);


        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected class Type1VH extends RecyclerView.ViewHolder {


        public Type1VH(View itemView) {
            super(itemView);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

}
