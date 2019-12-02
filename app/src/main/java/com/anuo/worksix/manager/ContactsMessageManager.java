package com.anuo.worksix.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;

import com.anuo.worksix.data.CallLogInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactsMessageManager {
    private static ContactsMessageManager instance;
    private List<CallLogInfo> inList;
    private List<CallLogInfo> outList;
    private List<CallLogInfo> missList;

    private ContactsMessageManager() {
    }

    public static ContactsMessageManager getInstance() {
        if (instance == null) {
            instance = new ContactsMessageManager();
        }
        return instance;
    }

    public void refresh(Context context, Callback callback) {
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            try {
                inList = new ArrayList<>();
                outList = new ArrayList<>();
                missList = new ArrayList<>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = CallLog.Calls.CONTENT_URI;
                String[] projects = new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE};
                Cursor cursor = resolver.query(uri, projects, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    if (name == null) name = "未备注";
                    String number = cursor.getString(1);
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(cursor.getLong(2)));
                    int type = cursor.getInt(3);
                    switch (type) {
                        case 1:
                            inList.add(new CallLogInfo(name, number, date));
                            break;
                        case 2:
                            outList.add(new CallLogInfo(name, number, date));
                            break;
                        case 3:
                            missList.add(new CallLogInfo(name, number, date));
                            break;
                    }
                }
                cursor.close();
            } catch (Exception e) {
                handler.post(() -> {
                    callback.onFail(e);
                });
                return;
            }
            handler.post(callback::onSuccess);
        }).start();
    }

    public List<CallLogInfo> getInList() {
        return new ArrayList<>(inList);
    }

    public List<CallLogInfo> getOutList() {
        return new ArrayList<>(outList);
    }

    public List<CallLogInfo> getMissList() {
        return new ArrayList<>(missList);
    }

    public interface Callback {
        void onSuccess();

        void onFail(Exception e);
    }
}
