package com.example.admin.week6porject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> randomStringList;
    RecyclerView rvRandomStrings;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;
    ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Client App");
        rvRandomStrings = (RecyclerView) findViewById(R.id.rvRandomStrings);
        layoutManager = new LinearLayoutManager(this);
        itemAnimator = new DefaultItemAnimator();
        rvRandomStrings.setLayoutManager(layoutManager);
        rvRandomStrings.setItemAnimator(itemAnimator);
        Intent intent = new Intent("com.example.admin.service.AIDL");
        bindService(convertImplicitToExplicit(intent),serviceConnection, Context.BIND_AUTO_CREATE);

    }
    ServiceConnection serviceConnection =  new ServiceConnection() {
        private IMyAidlInterface iMyAidlInterface;
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                randomStringList = iMyAidlInterface.getRandomData();
                listAdapter = new ListAdapter(randomStringList);
                rvRandomStrings.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            unbindService(serviceConnection);
            iMyAidlInterface = null;
        }
    };
    public Intent convertImplicitToExplicit(Intent implicit){
        PackageManager manager = getPackageManager();
        List<ResolveInfo> resolveInfoList =  manager.queryIntentServices(implicit,0);
        if(resolveInfoList == null || resolveInfoList.size() !=1){
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component =  new ComponentName(serviceInfo.serviceInfo.packageName,serviceInfo.serviceInfo.name);
        Intent explicit = new Intent(implicit);
        explicit.setComponent(component);
        return explicit;
    }

}
