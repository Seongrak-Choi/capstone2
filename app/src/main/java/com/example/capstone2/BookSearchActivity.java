package com.example.capstone2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
    Button btnSearch;
    int libraryListPosition;
    int libraryCode;
    ArrayList<BookInfo> bookList;
    RecyclerView recyclerView;
    ProgressDialog customProgressDialog;

    String key = "5f6c04a67fd10d16003dabc8f8419c2065f6695db0e69fd2e550c2527d44c487";
    String naruKey = "2c5f97e13c77c0c0b69a5d8d8b2777c61edafe30ce2c477f2b36956897d5e798";

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
        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    //Button을 클릭했을 때 자동으로 호출되는 callback method
    View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.isbnSearchButton:
                    customProgressDialog.show();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            bookList = xmlParser();

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    if (bookList.size() != 0) {
                                        BookAdapter adapter = new BookAdapter(bookList);
                                        adapter.setBookAdapter(bookList);
                                        recyclerView.setAdapter(adapter);
                                        customProgressDialog.dismiss();
                                    } else {
                                        customProgressDialog.dismiss();
                                        Toast.makeText(BookSearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
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
                + "&pageSize=50&pageNum=1&kwd=" + location;

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
                        } else if (tag.equals("author_info")) {
                            xpp.next();
                            book.setAuthor(xpp.getText());
                        } else if (tag.equals("isbn")) {
                            xpp.next();
                            if (xpp.getText().length()==13)  {//만약 isbn이 널이면 건너 뛴다.
                                String[] bookIsbn13 = xpp.getText().split(" ");                                   //받아온 isbn이 여러개 있을 경우 나눠서 배열로 받은 다음 첫번째 isbn만 사용하기 위함
                                book.setIsbn13(bookIsbn13[0]); // isbn값을 book객체에 넣어줌
                                System.out.println(book.getIsbn13());
                                //////////////////////////////////////////////////
                                String queryUrl2 = "http://data4library.kr/api/itemSrch?type=ALL&libCode=" ///////////////받아온 isbn으로 정보나루로 파싱 시장
                                        + libraryList.get(libraryListPosition).getLibraryCode()
                                        + "&authKey=" + naruKey //정보나루 인증키 값
                                        + "&isbn13=" + book.getIsbn13()  //검색한 책 isbn입력
                                        + "&pageNo=1&page\n";
                                int check = 0; // doc하나만 컨트롤 하기 위ㅎ한 변수
                                try {
                                    URL url2 = new URL(queryUrl2);//문자열로 된 요청 url2을 URL 객체로 생성.
                                    InputStream is2 = url2.openStream(); //url2위치로 입력스트림 연결

                                    XmlPullParserFactory factory2 = XmlPullParserFactory.newInstance();
                                    XmlPullParser xpp2 = factory2.newPullParser();
                                    xpp2.setInput(new InputStreamReader(is2, "UTF-8")); //inputstream 으로부터 xml 입력받기

                                    xpp2.next();
                                    int eventType2 = xpp2.getEventType();
                                    while (eventType2 != XmlPullParser.END_DOCUMENT) {
                                        switch (eventType2) {
                                            case XmlPullParser.START_TAG:
                                                String tag2 = xpp2.getName();//태그 이름 얻어오기
                                                if (tag2.equals("doc")) {// 첫번째 검색결과
                                                } else if (tag2.equals("bookImageURL")) {
                                                    xpp2.next();
                                                    book.setImgLink(xpp2.getText());
                                                } else if (tag2.equals("publisher")) {
                                                    xpp2.next();
                                                    book.setPublisher(xpp2.getText());
                                                }
                                                break;
                                            case XmlPullParser.END_TAG:
                                                String endTag = xpp2.getName(); //태그 이름 얻어오기

                                                if (endTag.equals("doc")) {
                                                    check = 1;
                                                }
                                                break;
                                        }
                                        if (check != 1) { //endTag가 doc여서 하나의 정보를 가져오면  while문을 빠져 나가기 위한 방법
                                            eventType2 = xpp2.next();
                                        } else {
                                            eventType2 = XmlPullParser.END_DOCUMENT;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                ///////////////////////////////////
                                
                                String queryUrl3 = "http://data4library.kr/api/bookExist?authKey="+ naruKey ///////////////받아온 is3bn으로 정보나루로 파싱 시장
                                        + "&libCode=" + libraryList.get(libraryListPosition).getLibraryCode()
                                        + "&isbn13=" + book.getIsbn13();  //검색한 책 is3bn입력

                                try {
                                    URL url3 = new URL(queryUrl3);//문자열로 된 요청 url3을 URL 객체로 생성.
                                    InputStream is3 = url3.openStream(); //url3위치로 입력스트림 연결

                                    XmlPullParserFactory factory3 = XmlPullParserFactory.newInstance();
                                    XmlPullParser xpp3 = factory3.newPullParser();
                                    xpp3.setInput(new InputStreamReader(is3, "UTF-8")); //inputstream 으로부터 xml 입력받기

                                    xpp3.next();
                                    int eventType3 = xpp3.getEventType();
                                    int check2 = 0;
                                    while (eventType3 != XmlPullParser.END_DOCUMENT) {
                                        switch (eventType3) {
                                            case XmlPullParser.START_TAG:
                                                String tag3 = xpp3.getName();//태그 이름 얻어오기
                                                if (tag3.equals("result")) {// 첫번째 검색결과
                                                } else if (tag3.equals("hasBook")) {
                                                    xpp3.next();
                                                    System.out.println(xpp3.getText());
                                                    book.setHasBook(xpp3.getText());
                                                } else if (tag3.equals("loanAvailable")) {
                                                    xpp3.next();
                                                    book.setLoanAvailable(xpp3.getText());
                                                }
                                                break;
                                            case XmlPullParser.END_TAG:
                                                String endTag = xpp3.getName(); //태그 이름 얻어오기

                                                if (endTag.equals("result")) {
                                                    check2 = 1;
                                                }
                                                break;
                                        }
                                        if (check2 != 1) { //endTag가 doc여서 하나의 정보를 가져오면  while문을 빠져 나가기 위한 방법
                                            eventType3 = xpp3.next();
                                        } else {
                                            eventType3 = XmlPullParser.END_DOCUMENT;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                /////////////////////////////
                            } else {  //만약 isbn이 널이면 건너 뛴다.
                                eventType = xpp.next();
                                continue;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = xpp.getName(); //태그 이름 얻어오기
                        if (endTag.equals("item")) {
                            if(book.getHasBook().equals("Y")) {
                                arrayList.add(book);
                            }
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
