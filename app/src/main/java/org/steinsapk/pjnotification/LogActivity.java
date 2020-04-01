package org.steinsapk.pjnotification;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        TextView logTextView = findViewById(R.id.logTextView);

        try {
            String log = readLog(getApplicationContext());
            logTextView.setText(log);
        } catch (FileNotFoundException e) {
            logTextView.setText("로그 파일이 없습니다.");
        }
    }

    private static String readLog(Context context) throws FileNotFoundException {
        FileInputStream fileInputStream = context.openFileInput("log_file_crawling.txt");
        Scanner scanner = new Scanner(fileInputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
