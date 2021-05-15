package com.example.capstone2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Vector;

import static com.example.capstone2.MainActivity.libraryList;

public class BookSearchActivity extends AppCompatActivity {
    EditText isbnSearchedit;
    TextView isbnSearchtext;
    Button btnSearch;
    int libraryListPosition;
    int libraryCode;
    int count;
    ArrayList<BookInfo> bookList;
    RecyclerView recyclerView;
    BookAdapter adapter=new BookAdapter(bookList);


    String key = "5f6c04a67fd10d16003dabc8f8419c2065f6695db0e69fd2e550c2527d44c487";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksearch);
        Intent intent = getIntent();
        libraryListPosition = (int) intent.getSerializableExtra("position");

        libraryCode = libraryList.get(libraryListPosition).getLibraryCode();
        isbnSearchedit = (EditText) findViewById(R.id.isbnSearchedit);
        btnSearch = findViewById(R.id.isbnSearchButton);
        btnSearch.setOnClickListener(mOnClick);

        recyclerView = findViewById(R.id.recycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookSearchActivity.this));
    }

    //Button을 클릭했을 때 자동으로 호출되는 callback method
    View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.isbnSearchButton:
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                             bookList = xmlParser();
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    adapter.setBookAdapter(bookList);
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        }
                    }).start();

                    break;
            }

        }
    };


    private ArrayList<BookInfo> xmlParser() {  /// 책의 isbn과 책제목 지은이 정보를 가져오는 파싱
        ArrayList<BookInfo> arrayList = new ArrayList<BookInfo>();
        String str = isbnSearchedit.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수
        String queryUrl = "https://www.nl.go.kr/NL/search/openApi/search.do?key="
                + key
                + "&pageSize=3&pageNum=1&kwd=" + location;

        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            BookInfo book = null;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = xpp.getName();//태그 이름 얻어오기
                        if (tag.equals("item")) {// 첫번째 검색결과
                            book = new BookInfo();
                        } else if (tag.equals("title_info")) {
                            xpp.next();
                            book.setName(xpp.getText());
                            System.out.println(book.getName());
                        } else if (tag.equals("author_info")) {
                            xpp.next();
                            book.setAuthor(xpp.getText());
                            System.out.println(book.getAuthor());
                        } else if (tag.equals("isbn")) {
                            xpp.next();
                            book.setIsbn13(xpp.getText());
                            System.out.println(book.getIsbn13());
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = xpp.getName(); //태그 이름 얻어오기

                        if (endTag.equals("item")) {
                            arrayList.add(book);
                        }
                        break;
                }
                eventType = xpp.next();
                System.out.println("next들 훓어보기 :"+eventType);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return arrayList;
    }

}
