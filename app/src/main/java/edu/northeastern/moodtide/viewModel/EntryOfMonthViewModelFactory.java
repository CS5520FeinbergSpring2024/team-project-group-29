package edu.northeastern.moodtide.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EntryOfMonthViewModelFactory implements ViewModelProvider.Factory {
    private final String uid;
    private final int selectedMonth;

    public EntryOfMonthViewModelFactory(String uid, int selectedMonth) {
        this.uid = uid;
        this.selectedMonth = selectedMonth;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EntryOfMonthViewModel.class)) {
            return (T) new EntryOfMonthViewModel(uid, selectedMonth);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

