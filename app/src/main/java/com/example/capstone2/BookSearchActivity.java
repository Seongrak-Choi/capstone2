package com.example.capstone2;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.capstone2.LibraryContentsActivity.libraryListPosition;
import static com.example.capstone2.MainActivity.libraryList;

public class BookSearchActivity extends AppCompatActivity {
    EditText isbnSearchedit;
    Button btnSearch;
    String libraryCode;
    ArrayList<BookInfo> bookList;
    RecyclerView recyclerView;
    ProgressDialog customProgressDialog;

    static String naruKey = "4a67398b6b7486bf80f9af4997d90ea048ea346f41952a595ff2581ca7368c20";

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
                    if (isbnSearchedit.getText().toString().length() > 0) {
                        customProgressDialog.show();  //버튼을 누름과 동시에 로딩중이라는 화면을 표시
                        new Thread(new Runnable() {

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void run() {
                                bookList = isbnParser(); //책의 정보를 담는 ArrayList<BookInfo> bookList 전역변수로 선언 되어져 있음.
                                // json-simple 라이브러리를 이용해  네이버 devlopment api json을 파싱 한 후. 검색결과로 나온 책 정보중 책의 isbn을
                                // BookInfo book객체에 삽입하고, isbn만 들어있는 book객체를 ArrayList<>로 만들어 return 하여 bookList에 삽입

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        if (bookList.size() != 0) {
                                            BookAdapter adapter = new BookAdapter(bookList); //파싱을 통해 만들어진 AarrayList
                                            adapter.setBookAdapter(bookList,BookSearchActivity.this);  // adapter에 삽입한다.
                                            recyclerView.setAdapter(adapter); //리사이클 뷰에 삽입
                                            customProgressDialog.dismiss(); //리사이클뷰에 출력이 되면 로딩중 화면 제거
                                        } else {
                                            customProgressDialog.dismiss(); //만약 bookList의 사이즈가0이면 검색결과가 없음을 출력한다.
                                            Toast.makeText(BookSearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).start();
                    }else{
                        Toast.makeText(BookSearchActivity.this,"검색어를 입력하세요.",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public static BookInfo xmlParser(String isbn) {  /// 책의 isbn과 책제목 지은이 정보를 가져오는 파싱
        BookInfo book = new BookInfo();
        String queryUrl3 = "http://data4library.kr/api/bookExist?authKey=" + naruKey ///////////////받아온 is3bn으로 정보나루로 파싱 시장
                + "&libCode=" + libraryList.get(libraryListPosition).getLibraryCode()
                + "&isbn13=" + isbn;  //검색한 책 is3bn입력
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
                            System.out.println("getHasbook의 값은? :" + xpp3.getText());
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
        return book;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<BookInfo> isbnParser() {  /// 책의 isbn과 책제목 지은이 정보를 가져오는 파싱
        String clientId = "mKx0B8c3DV7K3kGfZigS"; //애플리케이션 클라이언트 아이디값"
        String clientSecret = "G3AzLo03q2"; //애플리케이션 클라이언트 시크릿값"
        String str = isbnSearchedit.getText().toString();//EditText에 작성된 Text얻어오기
        String text = null;
        try {
            text = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/book?&display=10&query=" + text;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        ArrayList<BookInfo> responseIsbn = get(apiURL, requestHeaders);
        return responseIsbn;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static ArrayList<BookInfo> get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    public static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static ArrayList<BookInfo> readBody(InputStream body) {
        ArrayList<BookInfo> bookList2 = new ArrayList<>();
        InputStreamReader streamReader = new InputStreamReader(body);
        JSONParser parser = new JSONParser();
        String isbn = null;
        String title = null;
        String image = null;
        String author = null;
        String publisher = null;
        String[] isbn13 = null;
        String link = null;
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            JSONObject obj = (JSONObject) parser.parse(responseBody.toString());
            JSONArray items = (JSONArray) obj.get("items");
            for (int i = 0; i < items.size(); i++) {
                BookInfo book = new BookInfo();
                JSONObject row = (JSONObject) items.get(i);
                title = (String) row.get("title");
                title=title.replace("<b>","");
                title=title.replace("</b>","");
                image = (String) row.get("image");
                author = (String) row.get("author");
                author =author.replace("<b>","");
                author =author.replace("</b>","");
                publisher = (String) row.get("publisher");
                link = (String) row.get("link");
                isbn = (String) row.get("isbn");
                isbn13 = isbn.split(" ");
                book.setIsbn13(isbn13[1]);
                book.setImgLink(image);
                book.setPublisher(publisher);
                book.setName(title);
                book.setAuthor(author);
                book.setLink(link);
                book.setHasBook(xmlParser(isbn13[1]).getHasBook());
                book.setLoanAvailable(xmlParser(isbn13[1]).getLoanAvailable());
                if(book.getHasBook().equals("N")||book.getHasBook()==null) {
                    continue;  //받아온 isbn13을 이용해 다시 정보나루 도서관에 api요청해서 BookInfo 객체 book에 BookInfo형을 리턴받아 book에 대입한다.
                } else{
                    bookList2.add(book); // 정보나루에서 받아온 BookInfo형을 ArrayList<BookInfo>에 add해준다.
                }
            }
            return bookList2; ///모든 파싱이 끝나고 만들어진 ArrayList<BookInfo>를 리턴해 준다.
        } catch (IOException | ParseException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

}
