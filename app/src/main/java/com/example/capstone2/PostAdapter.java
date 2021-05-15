package com.example.capstone2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;
    private ArrayList<PostInfo> mData = null;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView nickView;
        TextView time;
        ViewHolder(View itemView) {
            super(itemView) ;
            textView1 = itemView.findViewById(R.id.textView) ;
            nickView = itemView.findViewById(R.id.nick);
            time = itemView.findViewById(R.id.timeView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });
        }
    }


    // 생성자에서 데이터 리스트 객체를 전달받음.
    PostAdapter(ArrayList<PostInfo> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_post, parent, false);
        PostAdapter.ViewHolder vh = new PostAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {   //이 부분이 item_post.xml이랑 관련이 있으니 이쪽에서 setText해주면 될 듯
        String text = (mData.get(position).getTitle());
        String nick = (mData.get(position).getNickName());
        holder.textView1.setText(text);
        holder.nickView.setText(nick);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date=sdf.format(mData.get(position).getCreatedAt());
        holder.time.setText(date);
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setPostAdapter(ArrayList<PostInfo> list){ //새로고침을 위해 mData를 재설정 하는 메소드
        this.mData=list;
    }
}