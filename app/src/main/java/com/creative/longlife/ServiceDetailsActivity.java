package com.creative.longlife;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.longlife.adapter.ServiceListAdapter2;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Service;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.clans.fab.FloatingActionButton;

import java.util.List;

public class ServiceDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab_call, fab_chat, fab_share_link,fab_copy_number,fab_make_fav;

    android.support.design.widget.FloatingActionButton fab_favourite;

    List<Service> favServiceList;

    private Service current_service;

    private int favServicePosition = -1;

    TextView tv_service_title,tv_price,tv_company_name,tv_address,tv_description;

    private SimpleDraweeView img_cover;

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
        fab_call.setLabelText("Call +"+ current_service.getCompany().getPhone());
        fab_chat = (FloatingActionButton) findViewById(R.id.fab_chat);
        fab_chat.setOnClickListener(this);
        fab_share_link = (FloatingActionButton) findViewById(R.id.fab_share_link);
        fab_share_link.setOnClickListener(this);
        fab_copy_number = (FloatingActionButton) findViewById(R.id.fab_copy_number);
        fab_copy_number.setOnClickListener(this);
        fab_make_fav = (FloatingActionButton) findViewById(R.id.fab_make_fav);
        fab_make_fav.setOnClickListener(this);

        img_cover = (SimpleDraweeView) findViewById(R.id.img_cover);
        if(current_service.getImg_url() != null && !current_service.getImg_url().isEmpty()){
            Uri imageUri = Uri.parse(GlobalAppAccess.BASE_URL_IMAGE + current_service.getImg_url());
            img_cover.setImageURI(imageUri);
        }


        fab_favourite = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab_favourite);
        fab_favourite.setOnClickListener(this);
        fab_favourite.setImageResource(R.drawable.love);
        int count = 0;
        for (Service favService : favServiceList) {
           if(current_service.getId().equalsIgnoreCase(favService.getId())){
               fab_favourite.setImageResource(R.drawable.love_fill);

               fab_make_fav.setImageResource(R.drawable.ic_favorite_white_fill);
               fab_make_fav.setLabelText("Remove from favourite");

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

            /*Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{current_service.getCompany().getEmail()});
            i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            i.putExtra(Intent.EXTRA_TEXT   , "body of email");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }*/


            Intent testIntent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + "subject" + "&body=" + "This is body" + "&to=" + current_service.getCompany().getEmail());
            testIntent.setData(data);
            startActivity(testIntent);

           /* try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.putExtra("jid", current_service.getCompany().getPhone() + "@s.whatsapp.net");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            } catch(Exception e) {
                Toast.makeText(this, "Error\n" + e.toString(), Toast.LENGTH_SHORT).show();
            }*/

            /*PackageManager pm=getPackageManager();
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
            }*/

        }
        if (id == R.id.fab_share_link) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            //String textToShare = "<h1>"+ current_service.getTitle() +"</h1><br><br><p>LongLife: https://play.google.com/store/apps/details?id=com.creative.longlife</p>";
            sendIntent.putExtra(Intent.EXTRA_TEXT, current_service.getTitle() + " (" + current_service.getCompany().getName() + ")" + "\n\nLongLife: https://play.google.com/store/apps/details?id=" + getPackageName());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Choose media"));
        }
        if (id == R.id.fab_copy_number) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", current_service.getCompany().getPhone());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this,"Successfully contact number copied.", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.fab_favourite || id == R.id.fab_make_fav) {

            if(favServicePosition != -1){
                fab_favourite.setImageResource(R.drawable.love);
                fab_make_fav.setImageResource(R.drawable.ic_favorite_border_white);
                fab_make_fav.setLabelText("Make favourite");


                favServiceList.remove(favServicePosition);
                MydApplication.getInstance().getPrefManger().setFavServices(favServiceList);
                favServicePosition = -1;
                Toast.makeText(this,"Successfully removed from favourite!",Toast.LENGTH_SHORT).show();
            }else{
                fab_favourite.setImageResource(R.drawable.love_fill);
                fab_make_fav.setImageResource(R.drawable.ic_favorite_white_fill);
                fab_make_fav.setLabelText("Remove from favourite");

                favServicePosition = favServiceList.size();
                favServiceList.add(current_service);
                MydApplication.getInstance().getPrefManger().setFavServices(favServiceList);
                Toast.makeText(this,"Successfully added to favourite!",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
