package com.example.capstone2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentInfo> mData = null;


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView nickView;
        TextView time;
        ViewHolder(View itemView) {
            super(itemView) ;
            content = itemView.findViewById(R.id.txt_content) ;
            nickView = itemView.findViewById(R.id.txt_nickname);
            time = itemView.findViewById(R.id.timeView4);
        }
    }


    // 생성자에서 데이터 리스트 객체를 전달받음.
    CommentAdapter(ArrayList<CommentInfo> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_comment, parent, false);
        CommentAdapter.ViewHolder vh = new CommentAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        String text = (mData.get(position).getContents());
        String nick = (mData.get(position).getNickName());
        holder.content.setText(text);
        holder.nickView.setText(nick);
        SimpleDateFormat sdf = new SimpleDateFormat(" MM-dd hh:mm");
        String date=sdf.format(mData.get(position).getCreatedAt());
        holder.time.setText(date);
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setCommentAdapter(ArrayList<CommentInfo> list){ //새로고침을 위해 mData를 재설정 하는 메소드
        this.mData=list;
    }
}