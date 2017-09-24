package androidbeing.info.firebasetutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import androidbeing.info.firebasetutorial.adapter.PlayerAdapter;
import androidbeing.info.firebasetutorial.listeners.OnCardClickListener;
import androidbeing.info.firebasetutorial.listeners.RequestCompleteListener;
import androidbeing.info.firebasetutorial.model.Player;
import androidbeing.info.firebasetutorial.data.FirebaseHelper;
import androidbeing.info.firebasetutorial.utils.Constants;

public class MainActivity extends AppCompatActivity implements RequestCompleteListener, OnCardClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private List<Player> players;
    private Player selectedPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseHelper = FirebaseHelper.getInstance();
        firebaseHelper.init();
        firebaseHelper.initializePlayerList(this);


        players = firebaseHelper.getPlayers();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new PlayerAdapter(players, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                gotoPlayerEditActivity(AddPlayerActivity.TASK_ADD, players.size());
            }
        });
    }

    private void gotoPlayerEditActivity(int taskType, int key) {
        Intent intent = new Intent(MainActivity.this, AddPlayerActivity.class);
        if (taskType == AddPlayerActivity.TASK_UPDATE) {
            intent.putExtra(AddPlayerActivity.TAG_PLAYER, selectedPlayer);
        }
        intent.putExtra(AddPlayerActivity.TASK_TYPE, taskType);
        intent.putExtra(AddPlayerActivity.NODE_KEY, key);
        startActivityForResult(intent, Constants.ADD_PLAYER_REQ_CODE);
    }

    private void updateList(List<Player> players) {
        adapter.setPlayers(players);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("MainActivity.onActivityResult");
        if (requestCode == Constants.ADD_PLAYER_REQ_CODE && requestCode == RESULT_OK) {
            Toast.makeText(MainActivity.this, getString(R.string.saved_message), Toast.LENGTH_LONG).show();
            System.out.println("MainActivity.onActivityResult" + getString(R.string.saved_message));
            players = firebaseHelper.getPlayers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestSuccess(List result) {
        updateList(result);
    }

    @Override
    public void onRequestFailed(DatabaseError databaseError) {
        Log.d(TAG, "Error: " + databaseError.getMessage());
    }

    @Override
    public void onCardClicked(int position) {
        selectedPlayer = players.get(position);
        gotoPlayerEditActivity(AddPlayerActivity.TASK_UPDATE, position);
    }
}
