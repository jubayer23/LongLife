package com.creative.longlife;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.longlife.adapter.ServiceListAdapter2;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Service;
import com.github.clans.fab.FloatingActionButton;

import java.util.List;

public class ServiceDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab_call, fab_chat, fab_share_link;

    android.support.design.widget.FloatingActionButton fab_favourite;

    List<Service> favServiceList;

    private Service current_service;

    private int favServicePosition = -1;

    TextView tv_service_title,tv_price,tv_company_name,tv_address,tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        current_service = (Service) getIntent().getSerializableExtra(ServiceListAdapter2.KEY_SERVICE);

        init();
    }


    private void init() {
        this.favServiceList = MydApplication.getInstance().getPrefManger().getFavServices();

        fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        fab_call.setOnClickListener(this);
        fab_chat = (FloatingActionButton) findViewById(R.id.fab_chat);
        fab_chat.setOnClickListener(this);
        fab_share_link = (FloatingActionButton) findViewById(R.id.fab_share_link);
        fab_share_link.setOnClickListener(this);


        fab_favourite = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab_favourite);
        fab_favourite.setOnClickListener(this);
        fab_favourite.setImageResource(R.drawable.love);
        int count = 0;
        for (Service favService : favServiceList) {
           if(current_service.getId().equalsIgnoreCase(favService.getId())){
               fab_favourite.setImageResource(R.drawable.love_fill);
               favServicePosition = count;
               break;
           }
           count++;
        }


        tv_service_title = (TextView) findViewById(R.id.tv_service_title);
        tv_service_title.setText(current_service.getTitle());
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_price.setText("$" +current_service.getPrice());
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_company_name.setText(current_service.getCompany().getName());
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_address.setText(current_service.getCompany().getAddress());
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_description.setText(current_service.getDescription());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.fab_call) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + current_service.getCompany().getPhone()));
            startActivity(intent);

        }
        if (id == R.id.fab_chat) {

            PackageManager pm=getPackageManager();
            try {

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = "Type your message";

                PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                //Check if package exists or not. If not then code
                //in catch block will be called
                waIntent.setPackage("com.whatsapp");

                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "Share with"));

            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        if (id == R.id.fab_share_link) {

        }
        if (id == R.id.fab_favourite) {

            if(favServicePosition != -1){
                fab_favourite.setImageResource(R.drawable.love);
                favServiceList.remove(favServicePosition);
                MydApplication.getInstance().getPrefManger().setFavServices(favServiceList);
                favServicePosition = -1;
            }else{
                fab_favourite.setImageResource(R.drawable.love_fill);
                favServicePosition = favServiceList.size();
                favServiceList.add(current_service);
                MydApplication.getInstance().getPrefManger().setFavServices(favServiceList);
            }

        }
    }
}
