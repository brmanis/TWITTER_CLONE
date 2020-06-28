package com.example.twitter_clone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Send_Tweet extends AppCompatActivity implements View.OnClickListener{
    private EditText edtTweet;
    private Button btnSend;
    private ListView tweetsListView;
    private Button btnViewTweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__tweet);
        edtTweet=findViewById(R.id.edtSendTweet);
        tweetsListView=findViewById(R.id.ListView01);
        btnSend=findViewById(R.id.btnSendTweet);
        btnViewTweets=findViewById(R.id.btnViewTweets);
        btnViewTweets.setOnClickListener(this);
        HashMap<String, Integer> numbers = new HashMap<>();
        numbers.put("Number1",1);
        numbers.put("Number2",2);
        //numbers.get("Number1");

        FancyToast.makeText(this,numbers.get("Number1")+"",Toast.LENGTH_LONG,FancyToast.WARNING,true).show();
        //The difference between array and hashmap, the hashmap is a key/value pair.
        // An array is an ordered collection of value.

    }

    public void SendToIt(View view){

        ParseObject parseObject = new ParseObject("MyTweet");
        parseObject.put("Tweet",edtTweet.getText().toString());
        parseObject.put("User", ParseUser.getCurrentUser().getUsername());
        final ProgressDialog progDial=new ProgressDialog(this);
        progDial.setMessage("Loading...");
        progDial.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(Send_Tweet.this, ParseUser.getCurrentUser().getUsername() +
                            " 's tweet (" +edtTweet.getText().toString() + ") is saved!", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }else{
                    FancyToast.makeText(Send_Tweet.this, e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
                progDial.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        final ArrayList<HashMap<String, String>> tweetList= new ArrayList<>();
        final SimpleAdapter simpleAdapter = new SimpleAdapter(Send_Tweet.this,tweetList, android.R.layout.simple_list_item_2, new String[] {"tweetUserName","tweetValue"},new int[]{android.R.id.text1,android.R.id.text2});
        try{
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("User",ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size() >0 && e == null){
                        for(ParseObject tweetObject:objects){
                            HashMap<String, String>userTweet =new HashMap<>();
                            userTweet.put("tweetUserName", tweetObject.getString("User"));
                            userTweet.put("tweetValue",tweetObject.getString("Tweet"));
                            tweetList.add(userTweet);
                        }
                        tweetsListView.setAdapter(simpleAdapter);
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }



    }
}