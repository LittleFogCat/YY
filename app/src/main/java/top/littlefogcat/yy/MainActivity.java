package top.littlefogcat.yy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStart).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationService.class);
            startService(intent);
        });

        findViewById(R.id.btnStart1).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationService.class);
            intent.putExtra("type", 1);
            startService(intent);
        });

        findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationService.class);
            startService(intent);
        });

    }

}
