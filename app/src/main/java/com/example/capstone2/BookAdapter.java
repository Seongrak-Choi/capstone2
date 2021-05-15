package com.example.capstone2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {


    private ArrayList<BookInfo> mData = null;


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView2;
        TextView author;
        TextView publisher;
        TextView bookCount;

        ViewHolder(View itemView) {
            super(itemView) ;
            textView2 = itemView.findViewById(R.id.textView2) ;
            author = itemView.findViewById(R.id.authorView) ;
            publisher = itemView.findViewById(R.id.publisherView) ;
            bookCount = itemView.findViewById(R.id.bookCountView) ;

        }
    }


    // 생성자에서 데이터 리스트 객체를 전달받음.
    BookAdapter(ArrayList<BookInfo> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_book, parent, false);
        BookAdapter.ViewHolder vh = new BookAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder holder, int position) {   //이 부분이 item_post.xml이랑 관련이 있으니 이쪽에서 setText해주면 될 듯
        holder.textView2.setText(mData.get(position).getName());
        holder.author.setText(mData.get(position).getAuthor());
        holder.publisher.setText(" ");
        holder.bookCount.setText(" ");

    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setBookAdapter(ArrayList<BookInfo> list){ //새로고침을 위해 mData를 재설정 하는 메소드
        this.mData=list;
    }
}