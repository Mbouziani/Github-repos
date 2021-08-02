package com.example.github_repos;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listrow;
    private RequestQueue RQ;
    int page=33,day_number =30 ;
    static String JSONURL ;

    Button next , back;
    List<String> List_nam = new ArrayList<String>();
    List<String> List_discription = new ArrayList<String>();
    List<String>List_owner = new ArrayList<String>();
    List<String> List_rating = new ArrayList<String>();
    List<String>List_avatar = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next = (Button)findViewById(R.id.next_btn) ;
        back = (Button)findViewById(R.id.back_btn) ;
        listrow = (ListView)findViewById(R.id.list_repos) ;
        loaddata( 1,day_number);




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 List_nam.clear();
                 List_discription.clear();
                 List_owner.clear();
                 List_rating.clear();
                 List_avatar.clear();

                 if(page < 35)
                {
                    page++;
                    loaddata( page,day_number);
                }
                else {
                    page=1;
                    day_number--;
                    loaddata( page,day_number);

                }


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 List_nam.clear();
                 List_discription.clear();
                 List_owner.clear();
                 List_rating.clear();
                 List_avatar.clear();
                if(page != 1)
                {
                    page--;
                    loaddata(page,day_number);
                }
                else {
                    if(day_number==30)
                    {
                        Toast.makeText(MainActivity.this,"Your are in the first page",Toast.LENGTH_LONG).show();
                    }
                    else {
                        page=1;
                        day_number++;
                        loaddata( page,day_number);
                    }





                }
            }
        });







    }

    public void loaddata(Integer nbr,Integer days)
    {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        Date result = cal.getTime();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        String strDate = sdfDate.format(result);
        Toast.makeText(MainActivity.this,strDate,Toast.LENGTH_LONG).show();


        JSONURL ="https://api.github.com/search/repositories?q=created:%3E"+strDate+"&sort=stars&order=desc&page="+String.valueOf(nbr);
        RQ = Volley.newRequestQueue(this);
        JsonObjectRequest jsonO = new JsonObjectRequest(Request.Method.GET, JSONURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray JA = response.getJSONArray("items");
                    for(int i=0;i<JA.length();i++)
                    {
                        JSONObject Item =  JA.getJSONObject(i);
                        List_nam.add(Item.getString("name"));
                        List_discription.add(Item.getString("description"));
                         List_rating.add(Item.getString("stargazers_count"));
                        JSONObject JA2 = Item.getJSONObject("owner");
                         for(int j=0; j<JA2.length(); j++)
                         {
                            List_owner.add(JA2.getString("login"));
                            List_avatar.add(JA2.getString("avatar_url"));
                            break;
                         }
                    }
                    myadapter md = new myadapter(List_nam,List_discription,List_rating,List_owner,List_avatar);
                    listrow.setAdapter(md);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error Response",Toast.LENGTH_LONG).show();

            }
        }) ;
        RQ.add(jsonO);
    }





    class myadapter extends BaseAdapter {

        List<String> Names = new ArrayList<String>();
        List<String> description = new ArrayList<String>();
        List<String> rati = new ArrayList<String>();
        List<String> owner = new ArrayList<String>();
        List<String> avatr = new ArrayList<String>();
        public myadapter(List<String> N,List<String> d,List<String> r,List<String> o,List<String> a){
            Names = N;
            description = d;
            rati = r;
            owner = o;
            avatr = a;

        }
        @Override
        public int getCount() { return List_nam.size(); }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) { return 0; }
        @Override
        public View getView(int i, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.row_explained,null);
            TextView repo_name  = (TextView)view.findViewById(R.id.repo_name);
            TextView repo_description  = (TextView)view.findViewById(R.id.repo_description);
            TextView repo_owner_name  = (TextView)view.findViewById(R.id.repo_owner_name);
            TextView repo_rating_number  = (TextView)view.findViewById(R.id.repo_rating_number);
            ImageView repo_avatar  = (ImageView)view.findViewById(R.id.repo_avatar);
            ImageView mystaar  = (ImageView)view.findViewById(R.id.mystaar);
            repo_owner_name.setText(Names.get(i).toString());
            repo_description.setText(description.get(i).toString());
            repo_name.setText(owner.get(i).toString());
            repo_rating_number.setText(rati.get(i).toString());
            //mystaar.setBackgroundResource(R.drawable.unnamed);

        Picasso picasso = new Picasso.Builder(getApplicationContext()).listener(new Picasso.Listener() {
        @Override
        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
          exception.printStackTrace();
                     }
                  }).build();
        picasso.load(avatr.get(i).toString()).into(repo_avatar);



            return view;
        }
    }

}