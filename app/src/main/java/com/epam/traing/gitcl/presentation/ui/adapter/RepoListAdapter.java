package com.epam.traing.gitcl.presentation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.RepoModel;
import com.epam.traing.gitcl.presentation.ui.view.RepoIconView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.epam.traing.gitcl.presentation.ui.view.RepoIconView.TYPE_FORK;
import static com.epam.traing.gitcl.presentation.ui.view.RepoIconView.TYPE_REPO;

/**
 * Created by Yahor_Fralou on 2/22/2017 5:10 PM.
 */

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {
    private Context ctx;
    private List<RepoModel> repositories;
    private AccountModel accountModel;
    private RepoClickListener listener;


    public RepoListAdapter(Context context, List<RepoModel> repositories, AccountModel accountModel) {
        this.ctx = context;
        this.repositories = repositories;
        this.accountModel = accountModel;
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_repo, parent, false);
        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder vh, int position) {
        RepoModel model = repositories.get(position);
        vh.txtRepoName.setText(model.getName());
        vh.txtRepoName.setTransitionName(ctx.getString(R.string.transit_repo_name) + position);

        vh.repoIcon.setRepoType(model.isFork() ? TYPE_FORK : TYPE_REPO);
        vh.repoIcon.setPrivateRepo(model.isPrivateRepo());
        vh.repoIcon.setTransitionName(ctx.getString(R.string.transit_repo_icon) + position);

        vh.txtOwnerName.setText(model.getOwnerName());
        if (model.getOwnerName().equalsIgnoreCase(accountModel.getAccountName())) {
            vh.txtOwnerName.setTextColor(ctx.getResources().getColor(R.color.repo_own) | 0x88000000);
            vh.repoIcon.setIsOwn(true);
        } else {
            vh.repoIcon.setIsOwn(false);
        }
        vh.txtLanguage.setText(model.getLanguage());
        vh.txtForksCount.setText(transformNumbers(model.getForksCount()));
        vh.txtStarsCount.setText(transformNumbers(model.getStargazersCount()));
        vh.txtWatchCount.setText(transformNumbers(model.getWatchersCount()));
        vh.itemRoot.setOnClickListener(view -> {
            if (listener != null) {
                listener.onRepoClick(vh, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.itemRoot)
        ViewGroup itemRoot;
        @Bind(R.id.repoIcon)
        public RepoIconView repoIcon;
        @Bind(R.id.txtRepoName)
        public TextView txtRepoName;
        @Bind(R.id.txtOwnerName)
        TextView txtOwnerName;
        @Bind(R.id.txtLanguage)
        TextView txtLanguage;
        @Bind(R.id.txtForksCount)
        TextView txtForksCount;
        @Bind(R.id.txtStarsCount)
        TextView txtStarsCount;
        @Bind(R.id.txtWatchCount)
        TextView txtWatchCount;

        RepoViewHolder(View v) {
            super(v);

            ButterKnife.bind(RepoViewHolder.this, v);
        }
    }

    public void setClickListener(RepoClickListener l) {
        this.listener = l;
    }

    private static String transformNumbers(long number) {
        int grade = (String.valueOf(number).length() - 1) / 3;
        int topGradeNumber = (int) (number / Math.pow(10, 3 * grade));

        String result = String.valueOf(topGradeNumber);
        if (result.length() == 1 && grade > 0) {
            int subTopNumber = (int) (number % Math.pow(10, 3 * grade) / Math.pow(10, 3 * grade - 1));
            if (subTopNumber > 0) {
                result += "." + String.valueOf(subTopNumber);
            }
        }

        String gradeSign;
        switch (grade) {
            case 1:
                gradeSign = "k";
                break;
            case 2:
                gradeSign = "m";
                break;
            case 3:
                gradeSign = "b";
                break;
            case 4:
                gradeSign = "t";
                break;
            default:
                gradeSign = "";
        }
        result += gradeSign;

        return result;
    }

    public interface RepoClickListener {
        void onRepoClick(RepoViewHolder vh, int position);
    }
}
