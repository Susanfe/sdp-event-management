package ch.epfl.sweng.eventmanager.ui.event.interaction.models;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.UserRepository;

public class UserModel extends ViewModel {
    private UserRepository repository;

    @Inject
    public UserModel(UserRepository repository) {
        this.repository = repository;
    }
}
