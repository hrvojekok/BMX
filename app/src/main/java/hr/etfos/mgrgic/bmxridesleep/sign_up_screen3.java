package hr.etfos.mgrgic.bmxridesleep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sign_up_screen3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen3);
        Intent intent = getIntent();

        Button button = findViewById(R.id.doneButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up_screen3.this, frontPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
