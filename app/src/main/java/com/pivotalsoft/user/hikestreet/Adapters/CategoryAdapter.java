package com.pivotalsoft.user.hikestreet.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.pivotalsoft.user.hikestreet.Items.CategoryItem;
import com.pivotalsoft.user.hikestreet.R;

import java.util.List;

/**
 * Created by Gangadhar on 11/10/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoryItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCategoryName;

        // public RelativeLayout parentLayout;


        public MyViewHolder(View view) {
            super(view);
            txtCategoryName = (TextView) view.findViewById(R.id.txtCategoryName);

            // parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
        }
    }


    public CategoryAdapter(Context mContext, List<CategoryItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryItem album = coursesItemList.get(position);
        holder.txtCategoryName.setText(album.getName());


}


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }



}
