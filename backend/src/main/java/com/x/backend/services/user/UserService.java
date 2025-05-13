package com.x.backend.services.user;

import com.x.backend.dto.user.response.UserResponse;
import com.x.backend.dto.user.request.ChangeNicknameRequest;
import com.x.backend.dto.user.request.SetNicknameRequest;
import com.x.backend.exceptions.user.NicknameAlreadyInUseException;
import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.function.Consumer;

/**
 * UserService defines the contract for managing platform users,
 * including lookup, nickname operations, and profile-level updates.
 * <p>
 * It provides abstraction over ApplicationUser persistence operations
 * and supports user-centric DTO transformations for API responses.
 * </p>
 */

public interface UserService {

    /**
     * Retrieves the internal ApplicationUser entity by its unique username.
     *
     * @param username the exact username of the user
     * @return the corresponding ApplicationUser entity
     * @throws UsernameNotFoundException if no user exists with the given username
     */
    ApplicationUser getUserByUsername(String username);

    /**
     * Fetches the current user's profile information as a DTO.
     *
     * @param username the username of the logged-in user
     * @return user profile data wrapped in {@link UserResponse}
     */
    BaseApiResponse<UserResponse> getCurrentUser(String username);

    /**
     * Sets the initial nickname for the user.
     *
     * @param username the username of the user
     * @param req contains:
     *             - {@code nickname}: the nickname to assign
     * @return confirmation message
     * @throws NicknameAlreadyInUseException if the nickname is already taken
     */
    BaseApiResponse<String> setNickname(String username, SetNicknameRequest req);

    /**
     * Changes the nickname of the user, ensuring uniqueness and non-redundancy.
     *
     * @param username the user's username
     * @param req contains:
     *             - {@code newNickname}: the new nickname to apply
     * @return success or error response depending on validation
     * @throws NicknameAlreadyInUseException if the new nickname is already in use
     */
    BaseApiResponse<String> changeNickname(String username, ChangeNicknameRequest req);

    /**
     * Fetches a single user based on an exact nickname match.
     *
     * @param nickname the unique nickname to search for
     * @return the user matching the nickname or NOT_FOUND error
     */
    BaseApiResponse<UserResponse> getUserByNickname(String nickname);

    /**
     * Searches for users whose nickname contains the given term (case-insensitive).
     * Only executed if the search term is at least 3 characters.
     *
     * @param nickname the partial nickname to search
     * @return list of matching users or empty list if none found
     */
    BaseApiResponse<List<UserResponse>> getAllUsersByNickname(String nickname);

    /**
     * Applies a consumer-style updater to modify the user's internal model.
     *
     * @param username the username of the target user
     * @param updater the mutator function to apply on ApplicationUser
     * @return updated user profile
     */
    BaseApiResponse<UserResponse> updateUser(String username, Consumer<ApplicationUser> updater);

}
