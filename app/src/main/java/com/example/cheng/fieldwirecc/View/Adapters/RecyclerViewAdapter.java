package com.example.cheng.fieldwirecc.View.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cheng.fieldwirecc.Model.Beans.SearchResponseData;
import com.example.cheng.fieldwirecc.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnItemClickListener mClickListener;
    private Context context;
    private List<SearchResponseData> list;
    public FooterHolder mFooterHolder;

    public RecyclerViewAdapter(Context context, List<SearchResponseData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i==0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_image,viewGroup,
                    false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.footer_image, viewGroup,
                    false);
            mFooterHolder = new FooterHolder(view);
            return mFooterHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myHolder, final int i) {
        if(myHolder instanceof RecyclerViewAdapter.MyHolder) {
            Glide.with(context).load("https://i.imgur.com/" +
                    list.get(i).getId() + "m.jpg").into(((MyHolder)myHolder).imageView);
            ((MyHolder)myHolder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(i);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size())
            return 1;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnViewItemClickListener(OnItemClickListener onViewItemClickListener){
        mClickListener = onViewItemClickListener;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        private View mLoadingViewstubstub;
        private View mEndViewstub;
        private View mNetworkErrorViewstub;

        public FooterHolder(View itemView) {
            super(itemView);
            mLoadingViewstubstub = itemView.findViewById(R.id.loading_viewstub);
            mEndViewstub = itemView.findViewById(R.id.end_viewstub);
            mNetworkErrorViewstub = itemView.findViewById(R.id.network_error_viewstub);
        }

        public void setData(String status) {
            switch (status) {
                case "Normal":
                    setAllGone();
                    break;
                case "Loading":
                    setAllGone();
                    mLoadingViewstubstub.setVisibility(View.VISIBLE);
                    break;
                case "TheEnd":
                    setAllGone();
                    mEndViewstub.setVisibility(View.VISIBLE);
                    break;
                case "NetWorkError":
                    setAllGone();
                    mNetworkErrorViewstub.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

        }

        private void setAllGone() {
            if (mLoadingViewstubstub != null) {
                mLoadingViewstubstub.setVisibility(View.GONE);
            }
            if (mEndViewstub != null) {
                mEndViewstub.setVisibility(View.GONE);
            }
            if (mNetworkErrorViewstub != null) {
                mNetworkErrorViewstub.setVisibility(View.GONE);
            }
        }

    }
}
