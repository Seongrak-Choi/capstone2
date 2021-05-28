package com.example.capstone2;

import android.content.Intent;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.capstone2.MainActivity.libraryList;

class Information{
    String callNumber;
    String latitude;
    String longitude;
    String homePage;
    String title;
    String closed;
    String open;
    String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public String getClosed() {
        return closed;
    }

    public String getHomePage() {
        return homePage;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOpen() {
        return open;
    }

    public String getTitle() {
        return title;
    }
}

public class LibraryInformationActivity extends AppCompatActivity {
    TextView libraryTitle;
    TextView clossedText;
    TextView openText;
    TextView addressText;
    Button btnLocation;
    Button btnCall;
    Button btnHomepage;
    ArrayList<Information> infoList;
    String naruKey = "4a67398b6b7486bf80f9af4997d90ea048ea346f41952a595ff2581ca7368c20";
    int libraryListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libraryinformation);
        libraryTitle = findViewById(R.id.librarytitle);
        clossedText = findViewById(R.id.clossed_text);
        openText = findViewById(R.id.open_text);
        btnLocation = findViewById(R.id.btn_location);
        btnCall = findViewById(R.id.btn_call);
        addressText = findViewById(R.id.address_text);
        btnHomepage = findViewById(R.id.btn_homepage);

        btnLocation.setOnClickListener(onClickListener);
        btnHomepage.setOnClickListener(onClickListener);
        btnCall.setOnClickListener(onClickListener);


        Intent intent = getIntent();
        libraryListPosition = (int) intent.getSerializableExtra("position");

        new Thread(new Runnable() {

            @Override
            public void run() {
                infoList = xmlParser();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if(infoList.size()!=0) {
                            libraryTitle.setText(infoList.get(0).getTitle());
                            clossedText.setText(infoList.get(0).getClosed());
                            openText.setText(infoList.get(0).getOpen());
                            addressText.setText(infoList.get(0).getAddress());
                        }else{
                            System.out.println("infoList 사이즈가 0이에요");
                        }
                    }
                });
            }
        }).start();

    }


    private ArrayList<Information> xmlParser() {  /// 책의 isbn과 책제목 지은이 정보를 가져오는 파싱
        ArrayList<Information> arrayList = new ArrayList<Information>();
        String queryUrl = "http://data4library.kr/api/libSrch?authKey=" +
                naruKey+"&pageNo=1&pageSize=10&libCode="+libraryList.get(libraryListPosition).getLibraryCode();
        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            Information info = null;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = xpp.getName();//태그 이름 얻어오기
                        if (tag.equals("lib")) {// 첫번째 검색결과
                            info = new Information();
                        } else if (tag.equals("libName")) {
                            xpp.next();
                            info.setTitle(xpp.getText());
                        } else if (tag.equals("address")) {
                            xpp.next();
                            info.setAddress(xpp.getText());
                        } else if (tag.equals("tel")) {
                            xpp.next();
                            info.setCallNumber(xpp.getText());
                        }else if (tag.equals("latitude")) {
                            xpp.next();
                            info.setLatitude(xpp.getText());
                        }else if (tag.equals("longitude")) {
                            xpp.next();
                            info.setLongitude(xpp.getText());
                        }else if (tag.equals("homepage")) {
                            xpp.next();
                            info.setHomePage(xpp.getText());
                        }else if (tag.equals("closed")) {
                            xpp.next();
                            info.setClosed(xpp.getText());
                        }
                        else if (tag.equals("operatingTime")) {
                            xpp.next();
                            info.setOpen(xpp.getText());
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = xpp.getName(); //태그 이름 얻어오기

                        if (endTag.equals("lib")) {
                            arrayList.add(info);
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_location:
                    Uri uri = Uri.parse("https://maps.google.com/maps?q="+infoList.get(0).getLatitude()+","+infoList.get(0).getLongitude());
                    Intent intent3 = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent3);
                    break;
                case R.id.btn_call:
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+infoList.get(0).getCallNumber()));
                    startActivity(intent);
                    break;
                case R.id.btn_homepage:
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(infoList.get(0).getHomePage()));
                    startActivity(intent2);
                    break;
            }
        }
    };

}
