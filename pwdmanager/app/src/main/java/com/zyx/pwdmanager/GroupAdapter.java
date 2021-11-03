package com.zyx.pwdmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zyx.pwdmanager.greendao.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context mContext;

    private List<Group> mDataList;

    private OnItemClickListener mItemClickListener;

    public GroupAdapter(Context context) {
        this.mContext = context;
    }


    public void setDataList(List<Group> dataList){
        this.mDataList = dataList;
    }

    public void setItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_group,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group group = mDataList.get(position);
        holder.name.setText(group.getName()+"("+group.getItemList().size()+")");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            if (null != mItemClickListener) {
                Group group = mDataList.get(getAdapterPosition());
                mItemClickListener.onItemClick(group.getName(),group.getId());
            }
        }
    }


    interface OnItemClickListener {
        void onItemClick(String groupName,long groupId);
    }
}
