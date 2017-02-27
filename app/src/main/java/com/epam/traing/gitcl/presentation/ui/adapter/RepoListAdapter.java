package com.epam.traing.gitcl.presentation.ui.adapter;

import android.content.Context;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epam.traing.gitcl.R;
import com.epam.traing.gitcl.db.model.AccountModel;
import com.epam.traing.gitcl.db.model.RepoModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yahor_Fralou on 2/22/2017 5:10 PM.
 */

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.ViewHolder> {
    private Context ctx;
    private List<RepoModel> repositories;
    private AccountModel accountModel;

    public RepoListAdapter(Context context, List<RepoModel> repositories, AccountModel accountModel) {
        this.ctx = context;
        this.repositories = repositories;
        this.accountModel = accountModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_repo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        RepoModel model = repositories.get(position);

        if (model.isFork()) {
            vh.imgForkLogo.setVisibility(View.VISIBLE);
            vh.imgRepoLogo.setVisibility(View.GONE);
        } else {
            vh.imgForkLogo.setVisibility(View.GONE);
            vh.imgRepoLogo.setVisibility(View.VISIBLE);
        }

        vh.txtRepoName.setText(model.getName());
        vh.txtOwnerName.setText(model.getOwnerName());
        if (model.getOwnerName().equalsIgnoreCase(accountModel.getAccountName())) {
            vh.txtOwnerName.setTextColor(ctx.getResources().getColor(R.color.repo_own) | 0x88000000);
            DrawableCompat.wrap(vh.imgLogoCircle.getDrawable()).setTint(ctx.getResources().getColor(R.color.repo_own));
        } else {
            DrawableCompat.wrap(vh.imgLogoCircle.getDrawable()).setTint(ctx.getResources().getColor(R.color.repo_not_own));
        }
        vh.txtLanguage.setText(model.getLanguage());
        vh.txtForksCount.setText(transformNumbers(model.getForksCount()));
        vh.txtStarsCount.setText(transformNumbers(model.getStargazersCount()));
        vh.txtWatchCount.setText(transformNumbers(model.getWatchersCount()));
        vh.itemRoot.setOnClickListener(view -> {
            Toast.makeText(ctx, "Clicked", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.itemRoot)
        ViewGroup itemRoot;
        @Bind(R.id.imgLogoCircle)
        ImageView imgLogoCircle;
        @Bind(R.id.imgForkLogo)
        ImageView imgForkLogo;
        @Bind(R.id.imgRepoLogo)
        ImageView imgRepoLogo;
        @Bind(R.id.txtRepoName)
        TextView txtRepoName;
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

        ViewHolder(View v) {
            super(v);

            ButterKnife.bind(ViewHolder.this, v);
        }
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
            case 1: gradeSign = "k"; break;
            case 2: gradeSign = "m"; break;
            case 3: gradeSign = "b"; break;
            case 4: gradeSign = "t"; break;
            default: gradeSign = "";
        }
        result += gradeSign;

        return result;
    }
}
