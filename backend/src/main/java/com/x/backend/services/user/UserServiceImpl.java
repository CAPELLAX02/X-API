package com.x.backend.services.user;

import com.x.backend.dto.auth.response.UserResponse;
import com.x.backend.dto.user.request.ChangeNicknameRequest;
import com.x.backend.dto.user.request.SetNicknameRequest;
import com.x.backend.exceptions.user.NicknameAlreadyInUseException;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.UserResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserResponseBuilder userResponseBuilder;

    public UserServiceImpl(
            ApplicationUserRepository applicationUserRepository,
            UserResponseBuilder userResponseBuilder
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.userResponseBuilder = userResponseBuilder;
    }

    // Inherited from "UserDetailsService" interface, overridden by us.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    @Override
    public ApplicationUser getUserByUsername(String username) {
        return applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }


    @Override
    public BaseApiResponse<UserResponse> getCurrentUser(String username) {
        ApplicationUser user = getUserByUsername(username);
        UserResponse userRes = userResponseBuilder.buildUserResponse(user);
        return BaseApiResponse.success(
                userRes,
                "Current user retrieved successfully."
        );
    }

    @Override
    public BaseApiResponse<String> setNickname(String username, SetNicknameRequest req) {
        ApplicationUser user = getUserByUsername(username);
        String reqNickname = req.nickname();
        reqNickname = reqNickname.trim();
        if (applicationUserRepository.existsByNickname(reqNickname)) {
            throw new NicknameAlreadyInUseException(reqNickname);
        }
        user.setNickname(reqNickname);
        applicationUserRepository.save(user);
        return BaseApiResponse.success("Nickname set as " + reqNickname + " successfully.");
    }

    @Override
    public BaseApiResponse<String> changeNickname(String username, ChangeNicknameRequest req) {
        ApplicationUser user = getUserByUsername(username);
        String oldNickname = user.getNickname();
        String newNickname = req.newNickname();
        if (oldNickname.equals(newNickname)) {
            return BaseApiResponse.error("New nickname cannot be the same as the present one.", HttpStatus.CONFLICT);
        }
        if (applicationUserRepository.existsByNickname(newNickname)) {
            throw new NicknameAlreadyInUseException(newNickname);
        }
        user.setNickname(newNickname);
        applicationUserRepository.save(user);
        return BaseApiResponse.success("Nickname changed as " + newNickname + " successfully.");
    }

    @Override
    public BaseApiResponse<UserResponse> getUserByNickname(String nickname) {
        return applicationUserRepository.findByNickname(nickname)
                .map(user -> {
                    UserResponse userRes = userResponseBuilder.buildUserResponse(user);
                    return BaseApiResponse.success(userRes, "User retrieved successfully.");
                })
                .orElseGet(() -> BaseApiResponse.error("User not found.", HttpStatus.NOT_FOUND));
    }

    @Override
    public BaseApiResponse<List<UserResponse>> getAllUsersByNickname(String nickname) {
        if (nickname == null || nickname.trim().length() < 3) {
            return BaseApiResponse.error("Search term must be at least 3 characters.", HttpStatus.BAD_REQUEST);
        }

        List<ApplicationUser> users = applicationUserRepository.findAllByNicknameContainingIgnoreCase(nickname);

        if (users.isEmpty()) {
            return BaseApiResponse.success(List.of(), "No users found matching nickname.");
        }

        List<UserResponse> responseList = userResponseBuilder.buildUserResponses(users);
        return BaseApiResponse.success(responseList, "Users retrieved successfully.");
    }


}
