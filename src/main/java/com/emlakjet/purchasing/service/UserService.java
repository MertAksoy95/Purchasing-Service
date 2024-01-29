package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.dto.BaseResponse;
import com.emlakjet.purchasing.dto.UserDto;
import com.emlakjet.purchasing.entity.User;
import com.emlakjet.purchasing.repository.UserRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This is the class where User related operations are performed.
 */
@Slf4j
@Service
public class UserService {

    private final ModelMapper mapper;
    private final UserRepository userRepo;

    private final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
    private final IMap<String, User> sessionMap = instance.getMap("SessionIdMappingInstance");

    public UserService(ModelMapper mapper, UserRepository userRepo) {
        this.mapper = mapper;
        this.userRepo = userRepo;
    }


    /**
     * This lists all users.
     *
     * @param page    The pagination page
     * @param size    The page size
     * @param sortDir The pagination sort direction
     * @param sort    The pagination sorting parameter
     */
    public ResponseEntity<BaseResponse> list(int page, int size, String sortDir, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", userRepo.findAll(pageRequest)));
    }

    /**
     * This returns the user based on the given id parameter.
     *
     * @param id User id
     */
    public ResponseEntity<BaseResponse> get(UUID id) {
        User existingUser = userRepo.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No user found for this id: " + id));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", existingUser));
    }

    /**
     * This creates a new user.
     * User emails are unique.
     * If there is a user with the same email, it returns a conflict response.
     *
     * @param userDto There are email, password, firstName and lastName fields for the user.
     */
    public ResponseEntity<BaseResponse> create(UserDto userDto) {
        User existingUser = userRepo.findByEmail(userDto.getEmail());
        if (existingUser != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse("This email address is already in use: " + userDto.getEmail()));
        }

        User user = mapper.map(userDto, User.class);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", userRepo.save(user)));
    }

    /**
     * This updates the existing user.
     * User emails are unique.
     * If there is a user with the same email, it returns a conflict response.
     *
     * @param id      User id
     * @param userDto There are email, password, firstName and lastName fields for the user.
     */
    public ResponseEntity<BaseResponse> update(UUID id, UserDto userDto) {
        User existingUser = userRepo.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No user found for this id: " + id));
        }

        if (!existingUser.getEmail().equalsIgnoreCase(userDto.getEmail())) {
            User user = userRepo.findByEmail(userDto.getEmail());
            if (user != null) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new BaseResponse("This email address is already in use: " + userDto.getEmail()));
            }
        }

        existingUser.setEmail(userDto.getEmail());
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());

        sessionMap.remove(existingUser.getEmail());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", userRepo.save(existingUser)));
    }

    /**
     * It soft deletes the user belonging to the given user id.
     *
     * @param id User id
     */
    public ResponseEntity<BaseResponse> delete(UUID id) {
        User existingUser = userRepo.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No user found for this id: " + id));
        }

        UUID randomId = UUID.randomUUID();
        existingUser.setEmail(existingUser.getEmail() + randomId);
        existingUser.setDeleted(true);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", userRepo.save(existingUser)));
    }

}
