package su.ha.airstatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
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


public class MainActivity extends AppCompatActivity {


    private TextView txtResult;
    private EditText edtAddr;
    private Button button1, button2;
    private  Document doc;
    private String tmX, tmY;
    public static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (TextView)findViewById(R.id.txtResult);
        button1 = (Button)findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        edtAddr = findViewById(R.id.addr);

//        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{

                    if(edtAddr.length() == 0){
                        Toast.makeText(getApplicationContext(),"?????? ???????????????",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new GetXMLTask().execute();
//                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    String provider = location.getProvider();
//                    double longitude = location.getLongitude();
//                    double latitude = location.getLatitude();

//                    NetworkTask networkTask = new NetworkTask("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getTMStdrCrdnt?umdName=?????????&pageNo=1&numOfRows=10&ServiceKey=krTlRKoPhIdA17M6DgSlhLXNIcostdjyiKgqO9cyCaokr6b6CBUuEYqbtZuy%2FsUCqNPlYaWZIvw%2FY1lNOs7ovA%3D%3D",null);
//                    networkTask.execute();

                    /*txtResult.setText("???????????? : " + provider + "\n" +
                            "?????? : " + longitude + "\n" +
                            "?????? : " + latitude
                    );*/

                   /* lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);*/
                }
            }
        });



    }

    /*final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            txtResult.setText("???????????? : " + provider + "\n" +
                    "?????? : " + longitude + "\n" +
                    "?????? : " + latitude + "\n");

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };*/

    /*public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        NetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar??? ???????????? ????????? ??????
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, null);
            return result; // ????????? ????????? ????????????. ?????? onPostExecute()??? ??????????????? ???????????????.
        }

        @Override
        protected void onPostExecute(String result) {
            // ????????? ???????????? ???????????????.
            // ????????? ?????? UI ?????? ?????? ????????? ?????????.
            Log.e("data", result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }*/


    private class GetXMLTask extends AsyncTask<String, Void, Document>{
        @Override
        protected Document doInBackground(String... urls) {
            URL url;

            try {
                url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getTMStdrCrdnt?umdName=" + edtAddr.getText().toString() + "&pageNo=1&numOfRows=10&ServiceKey=krTlRKoPhIdA17M6DgSlhLXNIcostdjyiKgqO9cyCaokr6b6CBUuEYqbtZuy%2FsUCqNPlYaWZIvw%2FY1lNOs7ovA%3D%3D");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

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
                txtResult.setText("??? ????????? ????????? ???????????????.");
                return;
            }

            for(int i = 0; i< nodeList.getLength(); i++){

                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList sidoName = fstElmnt.getElementsByTagName("sidoName");
                s += sidoName.item(0).getChildNodes().item(0).getNodeValue() +" ";

                NodeList sggName = fstElmnt.getElementsByTagName("sggName");
                s += sggName.item(0).getChildNodes().item(0).getNodeValue() +" ";

                NodeList umdName  = fstElmnt.getElementsByTagName("umdName");
                s += umdName.item(0).getChildNodes().item(0).getNodeValue() +" ";

                NodeList tmXList = fstElmnt.getElementsByTagName("tmX");
                tmX = tmXList.item(0).getChildNodes().item(0).getNodeValue();


                NodeList tmYList = fstElmnt.getElementsByTagName("tmY");
                tmY = tmYList.item(0).getChildNodes().item(0).getNodeValue();

            }

            txtResult.setText(s);
            address = s;
            button2.setVisibility(View.VISIBLE);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, MeasureActivity.class);
                    intent.putExtra("tmX",tmX);
                    intent.putExtra("tmY",tmY);
                    startActivity(intent);


                }
            });
            super.onPostExecute(doc);
        }
    }




}
