package com.x.backend.services.media.image;

import com.x.backend.dto.image.response.ImageResponse;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.media.Image;
import com.x.backend.models.media.enums.ImageType;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.repositories.ImageRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.ImageResponseBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserImageServiceImpl extends ImageUploadService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserService userService;
    private final ImageResponseBuilder imageResponseBuilder;

    public UserImageServiceImpl(final ImageRepository imageRepository,
                                final ApplicationUserRepository applicationUserRepository,
                                final UserService userService,
                                final ImageResponseBuilder imageResponseBuilder
    ) {
        super(imageRepository);
        this.applicationUserRepository = applicationUserRepository;
        this.userService = userService;
        this.imageResponseBuilder = imageResponseBuilder;
    }

    public BaseApiResponse<ImageResponse> uploadProfileImage(MultipartFile file, String username) {
        Image image = storeImage(file, ImageType.PROFILE_PICTURE);
        ApplicationUser user = userService.getUserByUsername(username);

        user.setProfilePicture(image);
        applicationUserRepository.save(user);

        ImageResponse imageRes = imageResponseBuilder.buildImageResponse(image);
        return BaseApiResponse.success(imageRes, "Profile image uploaded successfully.");
    }

    public BaseApiResponse<ImageResponse> uploadBannerImage(MultipartFile file, String username) {
        Image image = storeImage(file, ImageType.BANNER_PICTURE);
        ApplicationUser user = userService.getUserByUsername(username);

        user.setBannerPicture(image);
        applicationUserRepository.save(user);

        ImageResponse imageRes = imageResponseBuilder.buildImageResponse(image);
        return BaseApiResponse.success(imageRes, "Banner image uploaded successfully.");
    }

}
