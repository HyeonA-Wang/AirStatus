package su.ha.airstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtId, edtPw;
    String id,pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id = "hyeona";
        pw = "wang";
        btnLogin = findViewById(R.id.login_btn);
        edtId = findViewById(R.id.edt_id);
        edtPw = findViewById(R.id.edt_password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
            /*if(!id.equals(edtId.getText().toString())){
                Toast.makeText(getBaseContext(),"ID가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
        }
            if(!pw.equals(edtPw.getText().toString())){
                Toast.makeText(getBaseContext(),"PW가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                 return;
        }*/
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    });

    }
}