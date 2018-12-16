package ch.epfl.sweng.eventmanager.ui.user;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.UserRepository;
import dagger.android.support.AndroidSupportInjection;

public class UserModel extends ViewModel {
    private UserRepository repository;

    @Inject
    public UserModel(UserRepository repository) {
        this.repository = repository;
    }
}
