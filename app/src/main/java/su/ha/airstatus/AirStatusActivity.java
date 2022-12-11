package su.ha.airstatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AirStatusActivity extends AppCompatActivity {

    private Document doc;
    private TextView txtResult;
    private TextView txt_time,txt_Addr, value10, value25;
    private ImageView image10, image25;
    private LinearLayout layout10, layout25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_sratus);
        txtResult = findViewById(R.id.txt_result);
        new GetXMLTask().execute();
        txt_time = findViewById(R.id.txt_time);
        txt_Addr = findViewById(R.id.txt_addr);
        value10 = findViewById(R.id.value_10);
        image10 = findViewById(R.id.image10);
        layout10 = findViewById(R.id.layout_10);
        image25 = findViewById(R.id.image25);
        layout25 = findViewById(R.id.layout_25);
        value25 = findViewById(R.id.value_25);
    }

    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String...urls){
            URL url;

            try {
                url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?serviceKey=UHCaKEPeo%2FpSa3vfmcQWxuc45jUONWMP6FYL4aOrdCXO8anuuDJeyIm7lqskPklPgQWUIfEuHg2aTSstBPh8AQ%3D%3D&numOfRows=10&pageNo=1&stationName=" + getIntent().getStringExtra("station") + "&dataTerm=DAILY&ver=1.3&");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                Log.e("url", url.toString());
            } catch (Exception e){
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc){

            String s = "";

            NodeList nodeList = doc.getElementsByTagName("item");
            if (nodeList.getLength() == 0){
                Log.e("nodelist", nodeList + "");
//                txtResult.setText("측정소 이름이 정확하지 않습니다.");
                return;
            }

            Node node = nodeList.item(0);
            Element fstElmnt = (Element) node;

            NodeList dataTime = fstElmnt.getElementsByTagName("dataTime");
            txt_time.setText(dataTime.item(0).getChildNodes().item(0).getNodeValue());

            NodeList mangName = fstElmnt.getElementsByTagName("mangName");
            txt_Addr.setText(MainActivity.address);

            NodeList pm10Value = fstElmnt.getElementsByTagName("pm10Value");
            value10.setText(pm10Value.item(0).getChildNodes().item(0).getNodeValue());

            NodeList pm25Value = fstElmnt.getElementsByTagName("pm25Value");
            value25.setText(pm25Value.item(0).getChildNodes().item(0).getNodeValue());

            NodeList pm10Grade = fstElmnt.getElementsByTagName("pm10Grade");

            NodeList pm25Grade = fstElmnt.getElementsByTagName("pm25Grade");

            switch(pm10Grade.item(0).getChildNodes().item(0).getNodeValue()){
                case "1":
                    Glide.with(AirStatusActivity.this).load(R.drawable.laugh).into(image10);
                    layout10.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
                    break;
                case "2":
                    Glide.with(AirStatusActivity.this).load(R.drawable.smile).into(image10);
                    layout10.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                    break;
                case "3":
                    Glide.with(AirStatusActivity.this).load(R.drawable.meh).into(image10);
                    layout10.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow));
                    break;
                case "4":
                    Glide.with(AirStatusActivity.this).load(R.drawable.pain).into(image10);
                    layout10.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                    break;
            }

            switch(pm25Grade.item(0).getChildNodes().item(0).getNodeValue()){
                case "1":
                    Glide.with(AirStatusActivity.this).load(R.drawable.laugh).into(image25);
                    layout25.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
                    break;
                case "2":
                    Glide.with(AirStatusActivity.this).load(R.drawable.smile).into(image25);
                    layout25.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                    break;
                case "3":
                    Glide.with(AirStatusActivity.this).load(R.drawable.meh).into(image25);
                    layout25.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow));
                    break;
                case "4":
                    Glide.with(AirStatusActivity.this).load(R.drawable.pain).into(image25);
                    layout25.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                    break;
            }

//            txtResult.setText(s);
            super.onPostExecute(doc);
        }
    }
}