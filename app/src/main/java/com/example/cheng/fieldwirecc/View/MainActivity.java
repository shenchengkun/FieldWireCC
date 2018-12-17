package com.example.cheng.fieldwirecc.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;

import com.example.cheng.fieldwirecc.Model.Beans.SearchResponseData;
import com.example.cheng.fieldwirecc.Presenter.Presenter;
import com.example.cheng.fieldwirecc.R;
import com.example.cheng.fieldwirecc.View.Adapters.HistoryAdapter;
import com.example.cheng.fieldwirecc.View.Adapters.OnRcvScrollListener;
import com.example.cheng.fieldwirecc.View.Adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewCallback{
    private Presenter presenter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView historyRecyclerView;
    private ArrayList<String> historyList;
    private HistoryAdapter histortyAdapter;
    private int curPage = 1;
    private String curKeyword = "";
    protected String mState = "Normal";
    public static ArrayList<SearchResponseData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new Presenter(this);
        searchView = findViewById(R.id.search_view);

        recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(this,list);
        recyclerView.setAdapter(recyclerViewAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isFooter = position == recyclerViewAdapter.getItemCount() - 1;
                return isFooter ? 3 : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        historyRecyclerView = findViewById(R.id.history_list);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyList = (ArrayList<String>) presenter.readHistory(getApplicationContext());
        histortyAdapter = new HistoryAdapter(this,historyList);
        historyRecyclerView.setAdapter(histortyAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                curKeyword = s;
                curPage = 1;
                list.clear();
                setState("Loading");
                presenter.getSearch(curKeyword,curPage);
                searchView.clearFocus();
                historyRecyclerView.setVisibility(View.INVISIBLE);
                historyList.add(0,s);
                histortyAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                historyRecyclerView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                historyRecyclerView.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        recyclerViewAdapter.setOnViewItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(),ImageZoomActivity.class);
                //intent.putParcelableArrayListExtra("imageList",list);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {

                if (mState.equals("Loading") ) {
                    //being loading
                    return;
                }

                if (list.size()>=1) {
                    // loading more
                    setState("Loading");
                    presenter.getSearch(curKeyword,curPage);
                } else {
                    //the end
                    setState("TheEnd");
                }
            }
        });

        histortyAdapter.setOnViewItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String temp = historyList.get(position);
                historyList.remove(position);
                histortyAdapter.notifyDataSetChanged();
                searchView.setQuery(temp,true);
            }
        });
    }
    protected void setState(String mState) {
        this.mState = mState;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeAdaperState();
            }
        });
    }

    protected void changeAdaperState() {
        if (recyclerViewAdapter != null && recyclerViewAdapter.mFooterHolder != null) {
            recyclerViewAdapter.mFooterHolder.setData(mState);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.writeHistory(historyList,getApplicationContext());
        presenter.detachCallback();
        super.onDestroy();
    }

    @Override
    public void onSuccess(List<SearchResponseData> data) {
        curPage++;
        setState("Normal");
        list.addAll(data);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(){
        setState("NetWorkError");
    }

}
