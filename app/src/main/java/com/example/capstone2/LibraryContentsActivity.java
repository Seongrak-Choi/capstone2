package com.example.capstone2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static com.example.capstone2.MainActivity.libraryList;

public class LibraryContentsActivity extends AppCompatActivity {

    Button goBookSearch;
    Button goBoard;
    Button btn_Seat;
    Button btn_useInformation;
    TextView libraryTitle;
    static int libraryListPosition;
    ArrayList<BookInfo> bookList;
    RecyclerView recyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String naruKey = "4a67398b6b7486bf80f9af4997d90ea048ea346f41952a595ff2581ca7368c20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarycontent);

        goBookSearch = findViewById(R.id.btn_gobooksearch);
        goBoard = findViewById(R.id.btn_goboard);
        libraryTitle = findViewById(R.id.librarytitle);
        btn_Seat = findViewById(R.id.btn_seat);
        btn_useInformation = findViewById(R.id.btn_useinformation);
        goBookSearch.setOnClickListener(onClickListener);
        goBoard.setOnClickListener(onClickListener);
        btn_Seat.setOnClickListener(onClickListener);
        btn_useInformation.setOnClickListener(onClickListener);
        recyclerView = findViewById(R.id.recycler3);
        recyclerView.setLayoutManager(new LinearLayoutManager(LibraryContentsActivity.this));

        Intent intent = getIntent();  //리스트뷰에서 선택된 아이템을 position을 받아와서 해당하는 librarayList의 libraryName을 가져온다.
        libraryListPosition = (int) intent.getSerializableExtra("position");
        libraryTitle.setText(libraryList.get(libraryListPosition).getLibraryName());
        System.out.println("도서관 코드: "+libraryList.get(libraryListPosition).getLibraryCode());

        new Thread(new Runnable() {  //파싱해서 도서관 대출인기 10위를 파싱하기 위한 스레드 생성

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                bookList=xmlParser();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (bookList.size() != 0) {
                            FamousBookAdapter adapter = new FamousBookAdapter(bookList); //파싱을 통해 만들어진 AarrayList
                            adapter.setFamousBookAdapter(bookList);  // adapter에 삽입한다.
                            recyclerView.setAdapter(adapter); //리사이클 뷰에 삽입
                        } else {
                        }
                    }
                });
            }
        }).start();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_gobooksearch:
                    movePage(BookSearchActivity.class, libraryListPosition);
                    break;
                case R.id.btn_goboard:
                    if (user != null) {
                        movePage(BorderActivity.class, libraryListPosition);
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(LibraryContentsActivity.this); //알림창으로 메일이 전송되었음을 알려줌
                        dlg.setMessage("게시판을 이용하시려면 로그인을 해야 합니다.");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    break;
                case R.id.btn_seat:
                    movePage(SeatActivity.class, libraryListPosition);
                    break;
                case R.id.btn_useinformation:
                    movePage(LibraryInformationActivity.class, libraryListPosition);
                    break;
            }
        }
    };

    public void onBackPressed() { //뒤로가기 버튼이 눌릴 때 전 엑티비로 이동하는 코드
        Intent intent = new Intent(this, LibrarySearchActivity.class);
        startActivity(intent);
    }

    private void movePage(Class c, int libraryListPositionPosition) {  // 액티비티를 이동시켜주는 메소드
        Intent intent = new Intent(this, c);
        intent.putExtra("position", libraryListPositionPosition);
        startActivity(intent);
    }

    private ArrayList<BookInfo> xmlParser() {  /// 책의 isbn과 책제목 지은이 정보를 가져오는 파싱
        ArrayList<BookInfo> arrayList = new ArrayList<BookInfo>();
        BookInfo book = null;
        String queryUrl = "http://data4library.kr/api/loanItemSrchByLib?authKey="
                +naruKey
                +"&libCode="
                +libraryList.get(libraryListPosition).getLibraryCode()
                +"&pageSize=10";
        System.out.println("url은? "+queryUrl);
        int check = 0; // doc하나만 컨트롤 하기 위한 변수, 하나의 isbn으로 여러개의 책의 결과가 나오는 것을 방지하기 위함.
        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = xpp.getName();//태그 이름 얻어오기
                        if (tag.equals("doc")) {// 첫번째 검색결과
                            book = new BookInfo();
                        }else if(tag.equals("no")){
                            xpp.next();
                            book.setHasBook(xpp.getText());
                        }
                        else if (tag.equals("bookname")) {
                            xpp.next();
                            book.setName(xpp.getText());
                        } else if (tag.equals("authors")) {
                            xpp.next();
                            book.setAuthor(xpp.getText());
                        } else if (tag.equals("publisher")) {
                            xpp.next();
                            book.setPublisher(xpp.getText());
                        } else if (tag.equals("bookImageURL")){
                            xpp.next();
                            book.setImgLink(xpp.getText());
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = xpp.getName(); //태그 이름 얻어오기
                        if (endTag.equals("doc")) {
                            arrayList.add(book);
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return arrayList;
    }

}
