package com.shivichu.recyclerviewwithviewtype.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.shivichu.recyclerviewwithviewtype.R;
import com.shivichu.recyclerviewwithviewtype.adapter.PaginationRecyclerViewAdapter;
import com.shivichu.recyclerviewwithviewtype.model.Model;
import com.shivichu.recyclerviewwithviewtype.utils.Constants;
import com.shivichu.recyclerviewwithviewtype.utils.PaginationRecyclerViewAdapterCallback;
import com.shivichu.recyclerviewwithviewtype.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PaginationRecyclerViewAdapterCallback {

    private PaginationRecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static final int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private List<Model> mDataList =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);

        Constants.TYPE = Model.TYPE_2;

        adapter = new PaginationRecyclerViewAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                },2000);

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();
    }

    private void loadFirstPage() {
        Log.d("TAG", "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
//        hideErrorView();
        currentPage = PAGE_START;

        for(int i=0;i<20;i++) {

            Model model = new Model();
            model.setText("Test "+i);

            mDataList.add(model);

        }

        adapter.addAll(mDataList);

       adapter.addLoadingFooter();

    }

    private void loadNextPage() {
        Log.d("TAG", "loadNextPage: " + currentPage);

        adapter.removeLoadingFooter();
        isLoading = false;

        int size = mDataList.size();

        for(int i=size;i<25;i++) {

            Model model = new Model();
            model.setText("Test "+i);

            mDataList.add(model);

        }

        adapter.addAll(mDataList);

        adapter.addLoadingFooter();
    }

    @Override
    public void retryPageLoad() {

    }
}
