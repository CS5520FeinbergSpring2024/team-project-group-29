package edu.northeastern.moodtide.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.northeastern.moodtide.repository.StreakRepository;
import edu.northeastern.moodtide.repository.TodayCountRepository;

public class TodayCountViewModel extends ViewModel {
    private String uid;
    private MutableLiveData<String> todayCountLiveData = new MutableLiveData<>();
    private TodayCountRepository todayCountRepository;

    public TodayCountViewModel(String uid) {
        this.uid = uid;
    }

    public LiveData<String> getTodayCount() {
        loadEntries();  // Trigger data loading
        return todayCountLiveData;
    }

    private void loadEntries() {
        todayCountRepository = new TodayCountRepository(uid);
        todayCountRepository.getTodayCount(new TodayCountRepository.DataStatus() {
            @Override
            public void DataIsLoaded(String todayCount) {
                todayCountLiveData.postValue(todayCount);
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
