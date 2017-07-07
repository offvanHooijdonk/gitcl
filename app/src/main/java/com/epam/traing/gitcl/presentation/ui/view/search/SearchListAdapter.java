package com.epam.traing.gitcl.presentation.ui.view.search;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.HistoryModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.db.model.search.SearchResultItem;
import com.epam.traing.gitcl.presentation.ui.helper.DateHelper;
import com.epam.traing.gitcl.presentation.ui.view.BadgeNumbersView;
import com.epam.traing.gitcl.presentation.ui.view.RepoIconView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.epam.traing.gitcl.presentation.ui.view.RepoIconView.TYPE_FORK;
import static com.epam.traing.gitcl.presentation.ui.view.RepoIconView.TYPE_REPO;

/**
 * Created by Yahor_Fralou on 3/20/2017 3:45 PM.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private Context ctx;
    private HistoryPickListener historyPickListener;
    private ItemClickListener itemClickListener;

    private List<SearchResultItem> items;
    private String searchText;
    private AccountModel accountModel;
    private DateHelper dateHelper = new DateHelper();
    private int orientation = Configuration.ORIENTATION_UNDEFINED;

    public SearchListAdapter(Context ctx, List<SearchResultItem> items, AccountModel accountModel) {
        this.ctx = ctx;
        this.items = items;
        this.accountModel = accountModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_search, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResultItem resultItem = items.get(position);
        if (resultItem.getType() == SearchResultItem.HISTORY) {
            showHistory((HistoryModel) resultItem.getItem(), holder);
        } else if (resultItem.getType() == SearchResultItem.REPOSITORY) {
            showRepository((RepoModel) resultItem.getItem(), holder);
        } else if (resultItem.getType() == SearchResultItem.ACCOUNT) {
            showAccount((AccountModel) resultItem.getItem(), holder);
        }

        holder.itemRoot.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onSearchItemClick(resultItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType() + orientation * 10;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setHistoryPickListener(HistoryPickListener historyPickListener) {
        this.historyPickListener = historyPickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void showHistory(HistoryModel model, ViewHolder vh) {
        vh.itemHistoryView.setVisibility(View.VISIBLE);
        vh.itemAccountView.setVisibility(View.GONE);
        vh.itemRepoView.setVisibility(View.GONE);

        vh.txtHistory.setText(styleStringWithSearch(model.getText(), searchText));
        vh.imgToSearchBar.setOnClickListener(v -> {
            if (historyPickListener != null) {
                historyPickListener.onHistoryPicked(model.getText());
            }
        });
    }

    private void showAccount(AccountModel model, ViewHolder vh) {
        vh.itemHistoryView.setVisibility(View.GONE);
        vh.itemAccountView.setVisibility(View.VISIBLE);
        vh.itemRepoView.setVisibility(View.GONE);

        if (model.getAvatar() != null) {
            Glide.with(ctx)
                    .load(model.getAvatar())
                    .placeholder(R.drawable.ic_account_default_72)
                    .bitmapTransform(new CropCircleTransformation(ctx))
                    .into(vh.imgAccount);
        } else {
            vh.imgAccount.setImageResource(R.drawable.ic_account_default_72);
        }

        vh.txtAccountName.setText(styleStringWithSearch(model.getAccountName(), searchText));
        if (model.getPersonName() != null) {
            vh.txtFullName.setText(styleStringWithSearch(model.getPersonName(), searchText));
        } else {
            vh.txtFullName.setVisibility(View.GONE);
        }

    }

    private void showRepository(RepoModel model, ViewHolder vh) {
        vh.itemHistoryView.setVisibility(View.GONE);
        vh.itemAccountView.setVisibility(View.GONE);
        vh.itemRepoView.setVisibility(View.VISIBLE);

        vh.txtRepoName.setText(styleStringWithSearch(model.getName(), searchText));
        //vh.txtRepoName.setTransitionName(ctx.getString(R.string.transit_repo_name) + position);

        vh.repoIcon.setRepoType(model.isFork() ? TYPE_FORK : TYPE_REPO);
        vh.repoIcon.setPrivateRepo(model.isPrivateRepo());
        //vh.repoIcon.setTransitionName(ctx.getString(R.string.transit_repo_icon) + position);

        vh.txtOwnerName.setText(model.getOwnerName());
        if (model.getOwnerName().equalsIgnoreCase(accountModel.getAccountName())) {
            if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
                vh.txtOwnerName.setTextColor(ctx.getResources().getColor(R.color.repo_own) | 0x66000000);
            }
            vh.repoIcon.setIsOwn(true);
        } else {
            vh.repoIcon.setIsOwn(false);
        }
        if (model.getPushDate() > 0) {
            vh.txtLastPushed.setText(dateHelper.formatDateTimeShort(model.getPushDate()));
        } else {
            vh.txtLastPushed.setText(R.string.repo_list_push_date_empty);
        }
        vh.txtForksCount.setText(new BadgeNumbersView.NumberFormatter(ctx).formatNumber(model.getForksCount()));
        vh.badgeStar.setNumberValue(model.getStargazersCount());
        vh.badgeStar.setNumberValue(model.getWatchersCount());

        /*vh.itemRoot.setOnClickListener(view -> {
            if (listener != null) {
                listener.onRepoClick(vh, position);
            }
        });*/
    }

    private CharSequence styleStringWithSearch(String text, String search) {
        if (search == null || search.isEmpty()) return text;

        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        int lastFound = 0;
        while((lastFound = text.toLowerCase().indexOf(search.toLowerCase(), lastFound)) != -1) {
            ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), lastFound, lastFound + search.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            lastFound += search.length();
        }

        return ssb;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.itemRoot)
        View itemRoot;

        @Bind(R.id.itemHistory)
        View itemHistoryView;
        @Bind(R.id.itemAccount)
        View itemAccountView;
        @Bind(R.id.itemRepository)
        View itemRepoView;

        @Bind(R.id.txtHistoryEntry)
        TextView txtHistory;
        @Bind(R.id.imgToSearchBar)
        ImageView imgToSearchBar;

        @Bind(R.id.imgAccount)
        ImageView imgAccount;
        @Bind(R.id.txtAccountName)
        TextView txtAccountName;
        @Bind(R.id.txtFullName)
        TextView txtFullName;

        @Bind(R.id.repoIcon)
        public RepoIconView repoIcon;
        @Bind(R.id.txtRepoName)
        public TextView txtRepoName;
        @Bind(R.id.txtOwnerName)
        TextView txtOwnerName;
        @Bind(R.id.txtLastPushed)
        TextView txtLastPushed;
        @Bind(R.id.txtForksCount)
        TextView txtForksCount;
        @Bind(R.id.badgeWatch)
        BadgeNumbersView badgeWatch;
        @Bind(R.id.badgeStar)
        BadgeNumbersView badgeStar;

        ViewHolder(View v) {
            super(v);

            ButterKnife.bind(ViewHolder.this, v);
        }
    }

    interface HistoryPickListener {
        void onHistoryPicked(String text);
    }

    interface ItemClickListener {
        void onSearchItemClick(SearchResultItem searchResultItem);
    }
}
