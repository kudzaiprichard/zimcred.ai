package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.Coordinator;
import com.intela.zimcredai.Repositories.CoordinatorRepository;
import com.intela.zimcredai.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static com.intela.zimcredai.Util.Util.getNullPropertyNames;
import static com.intela.zimcredai.Util.Util.getUserByToken;

@Service
@RequiredArgsConstructor
public class CoordinatorService {

    private final CoordinatorRepository coordinatorRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Create a new coordinator
    public Coordinator create(Coordinator coordinator, HttpServletRequest request) {
        coordinator.setUser(getUserByToken(request, jwtService, userRepository)); // set user
        return this.coordinatorRepository.save(coordinator);
    }

    // Fetch a coordinator's profile by ID
    public Coordinator fetchProfile(Integer id) throws NotFoundException {
        return this.coordinatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinator profile not found with ID: " + id));
    }

    // Update an existing coordinator's profile
    public Coordinator updateProfile(Integer id, Coordinator coordinator) throws NotFoundException {
        Coordinator dbCoordinator = this.fetchProfile(id);

        // Copy non-null properties from coordinator to dbCoordinator
        BeanUtils.copyProperties(coordinator, dbCoordinator, getNullPropertyNames(coordinator));

        return this.coordinatorRepository.save(dbCoordinator);
    }

    // Delete a coordinator by ID
    public void deleteProfile(Integer id) throws NotFoundException {
        Coordinator dbCoordinator = this.fetchProfile(id);
        this.coordinatorRepository.delete(dbCoordinator);
    }

    // Fetch all coordinators
    public List<Coordinator> fetchAll() {
        return this.coordinatorRepository.findAll();
    }
}
