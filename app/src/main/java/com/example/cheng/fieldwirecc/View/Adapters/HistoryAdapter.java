package com.example.cheng.fieldwirecc.View.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.cheng.fieldwirecc.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {
    private OnItemClickListener mClickListener;
    private Context context;
    private List<String> list;

    public HistoryAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history,viewGroup,
                false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {

        myHolder.textView.setText(list.get(i));
        myHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(i);
                notifyDataSetChanged();
            }
        });
        myHolder.curItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button;
        View curItem;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.history_textview);
            button = itemView.findViewById(R.id.delete);
            curItem = itemView.findViewById(R.id.cur_item);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnViewItemClickListener(OnItemClickListener onViewItemClickListener){
        mClickListener = onViewItemClickListener;
    }
}
