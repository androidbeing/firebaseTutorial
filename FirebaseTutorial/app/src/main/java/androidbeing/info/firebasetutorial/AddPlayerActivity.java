package androidbeing.info.firebasetutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidbeing.info.firebasetutorial.model.Player;
import androidbeing.info.firebasetutorial.data.FirebaseHelper;


public class AddPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseHelper firebaseHelper;
    private EditText firstName;
    private EditText lastName;
    private EditText doB;
    private EditText email;
    private EditText mobile;
    private EditText address;
    private EditText game;
    private EditText awards;
    private Button btnSubmit;

    public static final String TASK_TYPE = "taskType";
    public static final String NODE_KEY = "key";
    public static final String TAG_PLAYER = "player";
    public static final int TASK_ADD = 200;
    public static final int TASK_UPDATE = 201;
    private Player player;
    int taskType;
    int key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        taskType = (Integer) getIntent().getExtras().get(TASK_TYPE);
        key = (Integer) getIntent().getExtras().get(NODE_KEY);

        firebaseHelper = FirebaseHelper.getInstance();

        firstName = (EditText) findViewById(R.id.editText2);
        lastName = (EditText) findViewById(R.id.editText);
        doB = (EditText) findViewById(R.id.editText3);
        email = (EditText) findViewById(R.id.editText4);
        mobile = (EditText) findViewById(R.id.editText5);
        address = (EditText) findViewById(R.id.editText6);
        game = (EditText) findViewById(R.id.editText7);
        awards = (EditText) findViewById(R.id.editText8);
        btnSubmit = (Button) findViewById(R.id.btn_add_player);

        if (taskType == TASK_UPDATE) {
            player = (Player) getIntent().getExtras().get(TAG_PLAYER);
            updateViews();
        }

        btnSubmit.setOnClickListener(this);

    }

    private void updateViews() {
        firstName.setText(player.getFirstName());
        lastName.setText(player.getLastName());
        mobile.setText(player.getMobile());
        email.setText(player.getEmail());
        doB.setText(player.getDob());
        game.setText(player.getGame());
        address.setText(player.getAddress());
        awards.setText(player.getAwards());
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (isValid()) {
            Player player = createPlayer();
            if (viewId == R.id.btn_add_player) {
                firebaseHelper.updatePlayer(player, String.valueOf(key));
            }
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.fill_empty_field), Toast.LENGTH_LONG).show();
        }
    }

    private Player createPlayer() {
        Player player = new Player();
        String nameFirst = firstName.getText().toString().trim();
        String namelast = lastName.getText().toString().trim();
        String Dob = doB.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String playerMobile = mobile.getText().toString().trim();
        String playerAddress = address.getText().toString().trim();
        String playerGame = game.getText().toString().trim();
        String playerAward = awards.getText().toString().trim();

        player.setFirstName(nameFirst);
        player.setLastName(namelast);
        player.setDob(Dob);
        player.setEmail(Email);
        player.setMobile(playerMobile);
        player.setAddress(playerAddress);
        player.setGame(playerGame);
        player.setAwards(playerAward);

        return player;
    }

    private boolean isValid() {
        if (!TextUtils.isEmpty(firstName.getText()) && !TextUtils.isEmpty(lastName.getText()) &&
                !TextUtils.isEmpty(doB.getText()) && !TextUtils.isEmpty(email.getText()) &&
                !TextUtils.isEmpty(mobile.getText()) && !TextUtils.isEmpty(address.getText()) &&
                !TextUtils.isEmpty(game.getText()) && !TextUtils.isEmpty(awards.getText())) {
            return true;
        }
        return false;

    }
}
