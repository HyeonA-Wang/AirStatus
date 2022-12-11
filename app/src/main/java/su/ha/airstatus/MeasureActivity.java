package su.ha.airstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MeasureActivity extends AppCompatActivity {

    private TextView txtResult;
    private Document doc;
    private Button btnAirStatus;
    private EditText edtStation;
    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        btnAirStatus = findViewById(R.id.btn_air_status);
        txtResult = findViewById(R.id.txt_result);
        edtStation = findViewById(R.id.edt_station);
        new GetXMLTask().execute();

        btnAirStatus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MeasureActivity.this, AirStatusActivity.class);
                intent.putExtra("station", edtStation.getText().toString());
                startActivity(intent);
            }
        });
    }


    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            URL url;

            try {
                url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX="+getIntent().getStringExtra("tmX") + "&tmY=" + getIntent().getStringExtra("tmY") + "&pageNo=1&numOfRows=10&ServiceKey=krTlRKoPhIdA17M6DgSlhLXNIcostdjyiKgqO9cyCaokr6b6CBUuEYqbtZuy%2FsUCqNPlYaWZIvw%2FY1lNOs7ovA%3D%3D&ver=1.0");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                Log.e("url",url.toString());
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {

            String s = "";

            NodeList nodeList = doc.getElementsByTagName("item");

            if(nodeList.getLength() == 0){
                txtResult.setText("측정소 이름을 정확히 넣어주세요.");
                return;
            }

            for(int i = 0; i< nodeList.getLength(); i++){

                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList stationName = fstElmnt.getElementsByTagName("stationName");
                s += "측정소 이름 : "+  stationName.item(0).getChildNodes().item(0).getNodeValue() +"\n";

                NodeList addr = fstElmnt.getElementsByTagName("addr");
                s += "주소 : "+  addr.item(0).getChildNodes().item(0).getNodeValue() +"\n\n";

            }

            txtResult.setText(s);
            super.onPostExecute(doc);
        }
    }
}