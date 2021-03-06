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

        Intent intent = getIntent();  //?????????????????? ????????? ???????????? position??? ???????????? ???????????? librarayList??? libraryName??? ????????????.
        libraryListPosition = (int) intent.getSerializableExtra("position");
        libraryTitle.setText(libraryList.get(libraryListPosition).getLibraryName());
        System.out.println("????????? ??????: "+libraryList.get(libraryListPosition).getLibraryCode());

        new Thread(new Runnable() {  //???????????? ????????? ???????????? 10?????? ???????????? ?????? ????????? ??????

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                bookList=xmlParser();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (bookList.size() != 0) {
                            FamousBookAdapter adapter = new FamousBookAdapter(bookList); //????????? ?????? ???????????? AarrayList
                            adapter.setFamousBookAdapter(bookList);  // adapter??? ????????????.
                            recyclerView.setAdapter(adapter); //???????????? ?????? ??????
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
                        AlertDialog.Builder dlg = new AlertDialog.Builder(LibraryContentsActivity.this); //??????????????? ????????? ?????????????????? ?????????
                        dlg.setMessage("???????????? ?????????????????? ???????????? ?????? ?????????.");
                        dlg.setPositiveButton("??????", null);
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

    public void onBackPressed() { //???????????? ????????? ?????? ??? ??? ???????????? ???????????? ??????
        Intent intent = new Intent(this, LibrarySearchActivity.class);
        startActivity(intent);
    }

    private void movePage(Class c, int libraryListPositionPosition) {  // ??????????????? ?????????????????? ?????????
        Intent intent = new Intent(this, c);
        intent.putExtra("position", libraryListPositionPosition);
        startActivity(intent);
    }

    private ArrayList<BookInfo> xmlParser() {  /// ?????? isbn??? ????????? ????????? ????????? ???????????? ??????
        ArrayList<BookInfo> arrayList = new ArrayList<BookInfo>();
        BookInfo book = null;
        String queryUrl = "http://data4library.kr/api/loanItemSrchByLib?authKey="
                +naruKey
                +"&libCode="
                +libraryList.get(libraryListPosition).getLibraryCode()
                +"&pageSize=10";
        System.out.println("url???? "+queryUrl);
        int check = 0; // doc????????? ????????? ?????? ?????? ??????, ????????? isbn?????? ???????????? ?????? ????????? ????????? ?????? ???????????? ??????.
        try {
            URL url = new URL(queryUrl);//???????????? ??? ?????? url??? URL ????????? ??????.
            InputStream is = url.openStream(); //url????????? ??????????????? ??????

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream ???????????? xml ????????????

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = xpp.getName();//?????? ?????? ????????????
                        if (tag.equals("doc")) {// ????????? ????????????
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
                        String endTag = xpp.getName(); //?????? ?????? ????????????
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