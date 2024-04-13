package edu.northeastern.moodtide.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TriggerViewModelFactory implements ViewModelProvider.Factory {
    private final String uid;

    public TriggerViewModelFactory(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TriggerViewModel.class)) {
            return (T) new TriggerViewModel(uid);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
