package com.example.twitter_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Twitter_Users extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayList tUsers;
    private ArrayAdapter adapter;
    private String followedUser = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter__users);


        FancyToast.makeText(this, "Welcome " + ParseUser.getCurrentUser().getUsername(),
                Toast.LENGTH_LONG,FancyToast.INFO,true).show();

        listView = findViewById(R.id.ListView);
        tUsers= new ArrayList<>();
        adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_checked,tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        try{

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects.size()>0 && e ==null){
                        for(ParseUser twitterUser: objects){
                            tUsers.add(twitterUser.getUsername());
                        }
                        listView.setAdapter(adapter);
                        for(Object twitterUser:tUsers){
                            if(ParseUser.getCurrentUser().getList("fanOf") !=null){
                                if(ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)){
                                    followedUser = followedUser + twitterUser + "\n";
                                    listView.setItemChecked(tUsers.indexOf(twitterUser), true);
                                    FancyToast.makeText(Twitter_Users.this, ParseUser.getCurrentUser().getUsername() +
                                            " is following " + followedUser,Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                                }
                            }

                        }
                    }
                }
            });


        }catch(Exception e){
            e.getMessage();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.Logout_Item:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent (Twitter_Users.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            case R.id.sendTweetItem:
                Intent intent = new Intent (Twitter_Users.this, Send_Tweet.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()){
            FancyToast.makeText(Twitter_Users.this, tUsers.get(i) +" is now followed!",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().add("fanOf", tUsers.get(i));
        }else{
            FancyToast.makeText(Twitter_Users.this, tUsers.get(i) +" is not checked!",Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(i));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOfList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(Twitter_Users.this, "Saved",
                            Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
            }
        });
    }
}