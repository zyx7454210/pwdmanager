package com.zyx.pwdmanager;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zyx.pwdmanager.greendao.Group;
import com.zyx.pwdmanager.greendao.Item;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private Context mContext;

    private List<Item> mDataList;

    private ItemAdapter.OnItemClickListener mItemClickListener;

    public ItemAdapter(Context context) {
        this.mContext = context;
    }


    public void setDataList(List<Item> dataList){
        this.mDataList = dataList;
    }

    public void setItemClickListener(ItemAdapter.OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_item,parent,false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Item item = mDataList.get(position);
        holder.title.setText(item.getTitle());
        holder.account.setText(item.getAccount());
        holder.password.setText(item.getPassword());
    }



    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RelativeLayout relativeLayout;

        private TextView title;

        private ImageView icon;

        private ExpandableLayout expandableLayout;

        private TextView account;

        private TextView password;

        private ImageView accountCopy;

        private ImageView passwordCopy;

        private ImageView show;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.titlelayout);
            relativeLayout.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setOnClickListener(this);
            account = itemView.findViewById(R.id.account);
            password = itemView.findViewById(R.id.password);
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            accountCopy = itemView.findViewById(R.id.accountcopy);
            accountCopy.setOnClickListener(this);
            passwordCopy = itemView.findViewById(R.id.passwordcopy);
            passwordCopy.setOnClickListener(this);
            show = itemView.findViewById(R.id.show);
            show.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != mItemClickListener) {
                Item item = mDataList.get(getAdapterPosition());
                mItemClickListener.onItemClick(getAdapterPosition(),v,item);
            }
        }
    }


    interface OnItemClickListener {
        void onItemClick(int position,View view,Item item);
    }
}
