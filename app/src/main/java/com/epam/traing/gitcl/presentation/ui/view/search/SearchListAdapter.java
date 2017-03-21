package com.epam.traing.gitcl.presentation.ui.view.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.db.model.HistoryModel;

import java.util.List;

/**
 * Created by Yahor_Fralou on 3/20/2017 3:45 PM.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private Context ctx;
    private List<ItemWrapper> items;

    public SearchListAdapter(Context ctx, List<ItemWrapper> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_search, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemWrapper wrapper = items.get(position);
        if (wrapper.getType() == ItemWrapper.HISTORY) {
            showHistory((HistoryModel) wrapper.getItem(), holder);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    private void showHistory(HistoryModel model, ViewHolder vh) {
        vh.itemHistoryView.setVisibility(View.VISIBLE);
        vh.itemAccountView.setVisibility(View.GONE);
        vh.itemRepoView.setVisibility(View.GONE);

        vh.txtHistory.setText(model.getText());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View itemHistoryView;
        View itemAccountView;
        View itemRepoView;
        TextView txtHistory;

        public ViewHolder(View v) {
            super(v);

            itemHistoryView = v.findViewById(R.id.itemHistory);
            itemAccountView = v.findViewById(R.id.itemAccount);
            itemRepoView = v.findViewById(R.id.itemRepository);
            txtHistory = (TextView) v.findViewById(R.id.txtHistoryEntry);
        }
    }

    public static class ItemWrapper {
        public static final int HISTORY = 0;
        public static final int ACCOUNT = 1;
        public static final int REPOSITORY = 2;

        private int type;
        private Object item;

        public ItemWrapper(int type, Object item) {
            this.type = type;
            this.item = item;
        }

        public Object getItem() {
            return item;
        }

        public void setItem(Object item) {
            this.item = item;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
