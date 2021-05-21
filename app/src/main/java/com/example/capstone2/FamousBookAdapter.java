package com.example.capstone2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FamousBookAdapter extends RecyclerView.Adapter<FamousBookAdapter.ViewHolder> {
    private ArrayList<BookInfo> mData = null;
    Bitmap bitmap;


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView3;
        TextView author3;
        TextView publisher3;
        ImageView bookImage3;
        TextView bookCount3;

        ViewHolder(View itemView) {
            super(itemView);
            bookCount3 = itemView.findViewById(R.id.bookCountView3);
            textView3 = itemView.findViewById(R.id.textView3);
            author3 = itemView.findViewById(R.id.authorView3);
            publisher3 = itemView.findViewById(R.id.publisherView3);
            bookImage3 = itemView.findViewById(R.id.bookimage3);
        }
    }


    // 생성자에서 데이터 리스트 객체를 전달받음.
    FamousBookAdapter(ArrayList<BookInfo> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public FamousBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_famousbook, parent, false);
        FamousBookAdapter.ViewHolder vh = new FamousBookAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FamousBookAdapter.ViewHolder holder, final int position) {   //이 부분이 item_post.xml이랑 관련이 있으니 이쪽에서 setText해주면 될 듯
        holder.textView3.setText(mData.get(position).getName());
        holder.author3.setText(mData.get(position).getAuthor());
        holder.publisher3.setText("출판사 :" + mData.get(position).getPublisher());
        holder.bookCount3.setText("인기대출 순위 : " + mData.get(position).getHasBook()+"위");
        if (mData.get(position).getImgLink() != null) {
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
                holder.bookImage3.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            holder.bookImage3.setImageBitmap(null);
        }
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setFamousBookAdapter(ArrayList<BookInfo> list){ //새로고침을 위해 mData를 재설정 하는 메소드
        this.mData=list;
    }
}