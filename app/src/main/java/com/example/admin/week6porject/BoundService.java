package com.example.admin.week6porject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BoundService extends Service {

    private static final String TAG = "BoundService";
    private final Random mGenerator = new Random();
    List<String> list = new ArrayList<>();

    public BoundService() {
    }

    public BoundService(List<String> list) {
        this.list = list;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return new IMyAidlInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }
            @Override
            public int getPid() throws RemoteException {
                return Process.myPid();
            }

            @Override
            public List<String> getRandomData() throws RemoteException {
                list = new ArrayList<>();
                for(int i = 0; i < mGenerator.nextInt(100) ; i++){
                    list.add(new BigInteger(130, mGenerator).toString(32));
                }
                return list;
            }
        };
    }

}
