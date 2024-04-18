package edu.northeastern.moodtide.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
//to generate view momdel with one argument

public class TodayCountViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public TodayCountViewModelFactory(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StreakViewModel.class)) {
            return (T) new TodayCountViewModel(uid);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
