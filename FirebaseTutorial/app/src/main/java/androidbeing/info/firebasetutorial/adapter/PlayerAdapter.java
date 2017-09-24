package androidbeing.info.firebasetutorial.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidbeing.info.firebasetutorial.R;
import androidbeing.info.firebasetutorial.listeners.OnCardClickListener;
import androidbeing.info.firebasetutorial.model.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private List<Player> players;
    private OnCardClickListener cardClickListener;

    public PlayerAdapter(List<Player> players, OnCardClickListener listener) {
        this.players = players;
        this.cardClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView fullName;
        public TextView dob;
        public TextView email;
        public TextView mobile;
        public TextView address;
        public TextView game;
        public TextView award;
        public LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            fullName = (TextView) view.findViewById(R.id.textName);
            dob = (TextView) view.findViewById(R.id.textDob);
            email = (TextView) view.findViewById(R.id.textEmail);
            mobile = (TextView) view.findViewById(R.id.textMob);
            address = (TextView) view.findViewById(R.id.textAddress);
            game = (TextView) view.findViewById(R.id.textGame);
            award = (TextView) view.findViewById(R.id.textAwards);

            layout = (LinearLayout) view.findViewById(R.id.player_layout);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            cardClickListener.onCardClicked(getAdapterPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.fullName.setText(player.getFirstName().concat(" " + player.getLastName()));
        holder.dob.setText(player.getDob());
        holder.email.setText(player.getEmail());
        holder.mobile.setText(player.getMobile());
        holder.address.setText(player.getAddress());
        holder.game.setText(player.getGame());
        holder.award.setText(player.getAwards());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setPlayers(List<Player> players){
        this.players = players;
        this.notifyDataSetChanged();
    }

}


