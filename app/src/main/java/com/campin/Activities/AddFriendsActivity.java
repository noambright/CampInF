package com.campin.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.campin.DB.Model;
import com.campin.R;
import com.campin.Adapters.CustomAdapter;
import com.campin.Utils.PlannedTrip;
import com.campin.Utils.RecommendedTripForUser;
import com.campin.Utils.Trip;
import com.campin.Utils.User;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends AppCompatActivity {

    final String FriendsNum = "FriendsNum";
    ArrayList<String> friends_names = new ArrayList<String>();
    ArrayList<String> friends_id = new ArrayList<String>();
    PlannedTrip newPlannedTrip;
    int friendsNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        TextView firstDate = (TextView) findViewById(R.id.firstOptionDate);
        firstDate.setText(getIntent().getStringExtra("firstDate"));
        //TextView secDate = (TextView) findViewById(R.id.secOptionDate);
        //secDate.setText(getIntent().getStringExtra("secDate"));

        TextView tripHead = (TextView) findViewById(R.id.trip_detail_head);
        tripHead.setText("פרטי הטיול ל" + getIntent().getStringExtra("area"));

        Trip t = new Trip();

        newPlannedTrip = new PlannedTrip(User.getInstance().getId(), -1,
                        getIntent().getStringExtra("firstDate").toString(),getIntent().getStringExtra("secDate").toString());
        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        // initiate a ListView
        final ListView listView = (ListView) findViewById(R.id.lsvFriendList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_friends);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("החברים שלי");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUserFriends();

        // set the adapter to fill the data in ListView
        final CustomAdapter customAdapter = new CustomAdapter(this, friends_names,friends_id,null,null);
        listView.setAdapter(customAdapter);

        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_friends);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Trip newTrip = new Trip();
                newPlannedTrip.setFriends(customAdapter._friends);
                showSuccessDialog();
               // final Intent intent = new Intent(v.getContext(), MainActivity.class);
               // startActivity(intent);
            }
        });
    }

    private void setUserFriends()
    {
        friends_id = new ArrayList<String>();
        friends_names = new ArrayList<String>();

        User.getInstance().setShowFriends(true);
        try {

            JSONArray jsonFriends = new JSONArray(User.getInstance().getFriends());

            for (int i = 0; i < jsonFriends.length(); i++) {

                JSONObject friend = (JSONObject) jsonFriends.get(i);
                friends_names.add(friend.getString("name"));
                friends_id.add(friend.getString("id"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showSuccessDialog() {
        // adding to the db
        RecommendedTripForUser.recommendedTripForUsers(newPlannedTrip, new Model.GetAllTripsListener()
        {
            @Override
            public void onComplete(List<Trip> tripsList, int currentMaxKey)
            {

                newPlannedTrip.setTripId(tripsList.get(0).getId());

                Model.instance().addPlannedTrip(newPlannedTrip, new Model.SuccessListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {

                            // Return to the list activity
                            friendsNum = newPlannedTrip.getFriends().size();
                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                            intent.putExtra(DetailActivity.EXTRA_POSITION, String.valueOf(newPlannedTrip.getTripId()));
                            intent.putExtra(DetailActivity.FRIENDS_NUM, friendsNum);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);



                        } else {
                            // Display message
                            Toast.makeText(getApplicationContext(),
                                    "התרחשה שגיאה",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("AddFriends", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }
}
