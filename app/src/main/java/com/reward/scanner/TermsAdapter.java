package com.reward.scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.scanner.model.TermData;

import java.util.List;


public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.ViewHolder> {
    private List<TermData> allCommunityModels;
    private Context context;
    private String user_id;

    public TermsAdapter(List<TermData> allCommunityModels1, Context context) {
        this.allCommunityModels = allCommunityModels1;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.terms_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (allCommunityModels.size() > 0) {
            final TermData allCommunityModel=allCommunityModels.get(position);
            holder.tv_terms.setText("* "+allCommunityModel.getTerm());
          //  holder.tv_date.setText(allCommunityModel.date);

        }
    }


    @Override
    public int getItemCount() {
        return allCommunityModels.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_terms;

        public ViewHolder(View parent) {
            super(parent);
            tv_terms = parent.findViewById(R.id.tv_terms);

        }

    }
}
