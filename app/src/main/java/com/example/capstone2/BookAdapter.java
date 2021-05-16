package com.example.capstone2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {


    private ArrayList<BookInfo> mData = null;
    Bitmap bitmap;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView2;
        TextView author;
        TextView publisher;
        TextView bookCount;
        ImageView bookImage;

        ViewHolder(View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.textView2);
            author = itemView.findViewById(R.id.authorView);
            publisher = itemView.findViewById(R.id.publisherView);
            bookCount = itemView.findViewById(R.id.bookCountView);
            bookImage = itemView.findViewById(R.id.bookimage);
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
    public void onBindViewHolder(BookAdapter.ViewHolder holder, final int position) {   //이 부분이 item_post.xml이랑 관련이 있으니 이쪽에서 setText해주면 될 듯
        holder.textView2.setText(mData.get(position).getName());
        holder.author.setText("저자 :" + mData.get(position).getAuthor());
        holder.publisher.setText("출판사 :" + mData.get(position).getPublisher());
        holder.bookCount.setText("대출 여부 :" + mData.get(position).getLoanAvailable());
        Thread mThread = new Thread() {  //ImageView에 사진을 넣기 위한 스레드 코드
            public void run() {
                try {
                    URL url = new URL(mData.get(position).getImgLink());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput((true));
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();
        try {
            mThread.join();
            holder.bookImage.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setBookAdapter(ArrayList<BookInfo> list) { //새로고침을 위해 mData를 재설정 하는 메소드
        this.mData = list;
    }
}