package nanodegree.regi.spotifystreamer;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    ArtistsPager artistsPager;
    MyAdapter myAdapter;
    SpotifyApi api;
    SpotifyService service;
    List<Artist> items;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listView = (ListView) findViewById(R.id.lvArtists);
        items = new ArrayList<>();

        myAdapter = new MyAdapter(getApplicationContext(),items);
        listView.setAdapter(myAdapter);

        api = new SpotifyApi();
        service = api.getService();

        EditText et = (EditText) findViewById(R.id.etSearch);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearching(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void startSearching(String seachParam){
        items.clear();
        service.searchArtists(seachParam, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager ap, Response response) {
                artistsPager = ap;
                Pager<kaaes.spotify.webapi.android.models.Artist> artists = artistsPager.artists;

                for (int i = 0; i < artists.items.size(); i ++){
                    kaaes.spotify.webapi.android.models.Artist tempArtist = artists.items.get(i);
                    List<kaaes.spotify.webapi.android.models.Image> tempImg = tempArtist.images;
                    Artist tempAr = new Artist();
                    tempAr.setName(artists.items.get(i).name);
                    tempAr.setId(artists.items.get(i).id);
                    if(tempImg.size() > 0){
                        tempAr.setImgURL(tempImg.get(0).url);
                    }
                    items.add(tempAr);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.wtf("Error","Error " + error.getMessage());
            }
        });
    }



    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mLayoutInflater;
        private List<Artist> mData;

        public MyAdapter(Context context,List<Artist> data){
            mData = data;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Artist getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = mLayoutInflater.inflate(R.layout.list_artist_item, parent, false);

            TextView tvName = (TextView) vi.findViewById(R.id.tvArtist);
            ImageView tvImg = (ImageView) vi.findViewById(R.id.imgArtist);

            Artist artistItem = getItem(position);

            tvName.setText(artistItem.getName());

            if(artistItem.getImgURL() == null){
                tvImg.setImageResource(R.drawable.ic_error);
            }else{
                Picasso.with(getApplicationContext()).load(artistItem.getImgURL()).placeholder(R.drawable.ic_place_holder_light).into(tvImg);
            }
            return vi;
        }
    }

}























