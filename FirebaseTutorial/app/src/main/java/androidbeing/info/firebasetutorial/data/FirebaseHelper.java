package androidbeing.info.firebasetutorial.data;

import android.os.AsyncTask;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidbeing.info.firebasetutorial.listeners.RequestCompleteListener;
import androidbeing.info.firebasetutorial.model.Player;
import androidbeing.info.firebasetutorial.utils.Constants;

public class FirebaseHelper {
    private static FirebaseHelper INSTANCE = null;
    private DatabaseReference databaseReference;
    private List<Player> playerList;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseHelper() {
    }

    /**
     * Gets the singletone firebase helper instance.
     *
     * @return FirebaseHelper instance
     */
    public static FirebaseHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseHelper();
        }
        return INSTANCE;
    }

    private FirebaseDatabase getDB() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            return firebaseDatabase;
        } else {
            return firebaseDatabase;
        }
    }

    /**
     * Initializes the database with required flags. Preferably call from application level.
     */
    public void init() {
        FirebaseDatabase database = getDB();
        databaseReference = database.getReference(Constants.PLAYERS_NODE);
        databaseReference.keepSynced(true);
    }

    /**
     * Request for list of segments available in the firebase real time db.
     * It will query in the local db if available else will fetch from server.
     *
     * @param receiver result receiver.
     */
    public void initializePlayerList(final RequestCompleteListener receiver) {
        playerList = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getPlayers(dataSnapshot, receiver);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                receiver.onRequestFailed(databaseError);
            }
        });
    }

    public void  updatePlayer(Player player,  String key) {
        //String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(player);
    }

    private void getPlayers(final DataSnapshot dataSnapshot, final RequestCompleteListener receiver) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Player player = child.getValue(Player.class);
                    System.out.println("XXX " + player.getAddress() + "\n" +
                            player.getAwards() + "\n " + player.getDob() + "\n " + player.getEmail()
                            + " \n " + player.getFirstName() + " \n " + player.getLastName()
                            + " \n " + player.getGame());
                    playerList.add(player);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                addtoChildListener();
                receiver.onRequestSuccess(playerList);
            }
        }.execute();
    }

    public void addtoChildListener() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                playerList.clear();
                Player player = dataSnapshot.getValue(Player.class);
                playerList.add(player);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    playerList.remove(Integer.valueOf(snap.getKey()));
                }*/
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List<Player> getPlayers() {
        return playerList;
    }

}
