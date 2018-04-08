package dsow.besthackentrancetask;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Vladimir Sukiasyan on 03.04.2018.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {
    Context context;
    List<String> memberInfo;
    OnItemLongClickListener mItemLongClickListener;
    CardAdapter.OnItemClickListener mItemClickListener;

    public DetailAdapter(Context context, List<String> memberInfo) {
        this.context = context;
        this.memberInfo= memberInfo;
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final CardAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.member_detailed_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.editText.setText(memberInfo.get(position));
        switch (position){
            case 0: holder.iconItem.setImageResource(R.drawable.ic_fio);break;
            case 1: holder.iconItem.setImageResource(R.drawable.ic_role);break;
            case 2: holder.iconItem.setImageResource(R.drawable.ic_group);break;
            case 3: holder.iconItem.setImageResource(R.drawable.ic_info);break;
            case 4: holder.iconItem.setImageResource(R.drawable.ic_link);break;
        }
    }

    @Override
    public int getItemCount() {
        return memberInfo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iconItem;
        EditText editText;

        public MyViewHolder(View itemView) {
            super(itemView);
            iconItem = (ImageView) itemView.findViewById(R.id.item_icon);
            editText = (EditText) itemView.findViewById(R.id.editText);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mItemLongClickListener.onItemLongClick(view,getAdapterPosition());
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }
}
