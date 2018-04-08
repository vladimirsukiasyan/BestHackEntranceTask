package dsow.besthackentrancetask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import dsow.besthackentrancetask.Database.DbBitmapUtility;

/**
 * Created by Vladimir Sukiasyan on 03.04.2018.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    Context context;
    private List<Member> members;
    OnItemClickListener mItemClickListener;

    public CardAdapter(Context context, List<Member> members) {
        this.context = context;
        this.members= members;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Member member= members.get(position);
        Bitmap image=DbBitmapUtility.getImage(member.image);
        //декодирование байтого массив image в изображение
        holder.photoView.setImageBitmap(image);
        holder.nameView.setText(member.name+" "+member.surname);
        holder.roleView.setText(member.role);
    }

    public void setItems(List<Member> members) {
        this.members = members;
    }
    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView photoView;
        private TextView nameView;
        private TextView roleView;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v,getAdapterPosition());
                }
            });
            photoView = (ImageView) itemView.findViewById(R.id.photo_view);
            nameView = (TextView) itemView.findViewById(R.id.name);
            roleView= (TextView) itemView.findViewById(R.id.role);
        }
    }
}
