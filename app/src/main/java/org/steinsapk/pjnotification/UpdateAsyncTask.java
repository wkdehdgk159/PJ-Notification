package org.steinsapk.pjnotification;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

public class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {
    private Crawling crawling;
    private ProgressDialog progressDialog;
    private String error = "";
    private Context context;
    private String id;
    private String password;

    public UpdateAsyncTask(Context context, boolean setDialog) {
        if (setDialog) {
            // 로딩 화면 객체 설정
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("강의 공지사항 업데이트 중..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        crawling = new Crawling(context, context.openOrCreateDatabase("database.db", MODE_ENABLE_WRITE_AHEAD_LOGGING,null));
        this.context = context;

        UserInfo userInfo = new UserInfo(context);
        id = userInfo.getSavedInfo("ID");
        password = userInfo.getSavedInfo("PW");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... values) {
        try {
            // 로그인
            crawling.login(id, password);

            // 새로운 강의가 있으면 추가한다. (없어진 강의를 없애지는 못함)
            crawling.saveCourseList();

            // 강의 아이템 업데이트
            crawling.saveCourseItem();

            // 강의 공지 업데이트
            crawling.saveCourseNotice();
        } catch (Exception e) {
            debugLog(e.toString());
            error = e.getMessage();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void value) {
        super.onPostExecute(value);

        try {
            if (!error.equals(""))
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();

            progressDialog.dismiss();
        } catch (Exception e) {
            // MyService에서 실행될 때, 화면이 없기 때문에 Exception 발생
            debugLog(e.toString());
        }

        // DB 닫기
        crawling.closeDB();
    }

    private static void debugLog(String log) {
        Log.e("UpdateAsyncTask", log);
    }
}
