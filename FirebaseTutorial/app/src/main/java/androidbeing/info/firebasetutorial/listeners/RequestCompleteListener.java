package androidbeing.info.firebasetutorial.listeners;


import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface RequestCompleteListener<T> {

    void onRequestSuccess(List<T> result);

    void onRequestFailed(DatabaseError databaseError);
}
