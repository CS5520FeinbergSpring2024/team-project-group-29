package edu.northeastern.moodtide.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import edu.northeastern.moodtide.object.Trigger;
import edu.northeastern.moodtide.repository.TriggerRepository;

public class TriggerViewModel extends ViewModel {
    String uid;
    private MutableLiveData<List<Trigger>> triggersLiveData = new MutableLiveData<>();
    private TriggerRepository repository;

    public TriggerViewModel(String uid) {
        this.uid = uid;
        repository = new TriggerRepository(uid);
        loadTriggers();
    }

    private void loadTriggers() {
        repository.getTriggers(new TriggerRepository.DataStatus() {
            @Override
            public void DataIsLoaded(List<Trigger> triggers) {
                triggersLiveData.postValue(triggers);
            }

            @Override
            public void DataIsFailed(Exception e) {
                // Log error or handle failed case
            }
        });
    }

    public LiveData<List<Trigger>> getTriggers() {
        return triggersLiveData;
    }
}

