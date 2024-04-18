package edu.northeastern.moodtide.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;

import edu.northeastern.moodtide.repository.StreakRepository;

public class StreakViewModel extends ViewModel {
    private String uid;
    private MutableLiveData<String> streakLiveData = new MutableLiveData<>();
    private StreakRepository streakRepository;

    public StreakViewModel(String uid) {
        this.uid = uid;
    }

    public LiveData<String> getStreak() {
        loadEntries();  // Trigger data loading
        return streakLiveData;
    }

    private void loadEntries() {
        streakRepository = new StreakRepository(uid);
        streakRepository.getStreak(new StreakRepository.DataStatus() {
            @Override
            public void DataIsLoaded(String streak) {
                streakLiveData.postValue(streak);
            }
//            @Override
//            public void DataIsInserted() { }
//
//            @Override
//            public void DataIsUpdated() { }
//
//            @Override
//            public void DataIsDeleted() { }
        });
    }



}
