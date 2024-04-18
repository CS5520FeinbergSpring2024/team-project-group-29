package edu.northeastern.moodtide.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.repository.EntryOfMonthRepository;

public class EntryOfMonthViewModel extends ViewModel {
    private MutableLiveData<List<Entry>> entriesLiveData = new MutableLiveData<>();
    private EntryOfMonthRepository repository;

    public EntryOfMonthViewModel(String uid, int selectedMonth) {
        repository = new EntryOfMonthRepository(uid, selectedMonth);
        loadEntries();
    }

    private void loadEntries() {
        repository.getEntries(new EntryOfMonthRepository.DataStatus() {
            @Override
            public void DataIsLoaded(List<Entry> entryList) {
                entriesLiveData.postValue(entryList);
            }
        });
    }

    public LiveData<List<Entry>> getEntries() {
        return entriesLiveData;
    }
}

