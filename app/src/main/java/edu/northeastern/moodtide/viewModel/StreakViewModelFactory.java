package edu.northeastern.moodtide.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;

public class StreakViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public StreakViewModelFactory(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StreakViewModel.class)) {
            return (T) new StreakViewModel(uid);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
