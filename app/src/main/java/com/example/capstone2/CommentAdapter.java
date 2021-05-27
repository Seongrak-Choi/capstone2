package com.example.capstone2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.capstone2.LibraryContentsActivity.libraryListPosition;
import static com.example.capstone2.MainActivity.libraryList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentInfo> mData = null;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView nickView;
        TextView time;
        ImageButton btn_commentdel;
        
        ViewHolder(View itemView) {
            super(itemView) ;
            content = itemView.findViewById(R.id.txt_content) ;
            nickView = itemView.findViewById(R.id.txt_nickname);
            time = itemView.findViewById(R.id.timeView4);
            btn_commentdel=itemView.findViewById(R.id.commentdelete);
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
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, final int position) {
        String text = (mData.get(position).getContents());
        String nick = (mData.get(position).getNickName());
        holder.content.setText(text);
        holder.nickView.setText(nick);
        SimpleDateFormat sdf = new SimpleDateFormat(" MM-dd hh:mm");
        String date=sdf.format(mData.get(position).getCreatedAt());
        holder.time.setText(date);
        if(user.getUid().equals(mData.get(position).getuID())) {
            holder.btn_commentdel.setVisibility(View.VISIBLE);
        }
        holder.btn_commentdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDialog(position);
            }
        });
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setCommentAdapter(ArrayList<CommentInfo> list,Context context){ //새로고침을 위해 mData를 재설정 하는 메소드
        this.mData=list;
        this.context=context;
    }

    void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("게시글을 삭제 하시겠습니까?");
        builder.setNegativeButton("예", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                db.collection(libraryList.get(libraryListPosition).getLibraryName())
                        .document(mData.get(position).getPostDocumentValue())
                        .collection("comment")
                        .document(mData.get(position).getDocumentValue())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mData.remove(position);
                                notifyItemChanged(position);
                                notifyItemRangeChanged(position, mData.size());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        });
        builder.setPositiveButton("아니오",null);
        builder.show();
    }

}