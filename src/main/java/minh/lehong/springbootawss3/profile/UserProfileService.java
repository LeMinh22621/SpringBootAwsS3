package minh.lehong.springbootawss3.profile;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.simplesystemsmanagement.model.InvalidInstanceIdException;
import minh.lehong.springbootawss3.bucket.BucketName;
import minh.lehong.springbootawss3.filestore.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. Check if image is not empty
        if(file.isEmpty())
        {
            throw new IllegalStateException("Cannot upload file empty file [" + file.getSize() + "]");
        }
        // 2. If file is an image
        if(!Arrays.asList(
                ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType()
        ).contains(file.getContentType())){
            throw new IllegalStateException("File must be an image");
        }
        // 3. The user exists in your database

        UserProfile user = getUserProfileOrThrow(userProfileId);

        // 4. Grab some metadata from file if any
        Map<String, String> metaData = new HashMap<>();
        metaData.put("Content-Type", file.getContentType());
        metaData.put("Content-Length",String.valueOf(file.getSize()));

        // 5. Store the image in s3 and update database with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try{
            fileStore.save(path, fileName, Optional.of(metaData), file.getInputStream());
            user.setUserProfileImageLink(fileName);
        }
        catch(IOException e)
        {
            throw new IllegalStateException(e);
        }

    }

//    public byte[] downloadUserProfileImage(UUID userProfileId) {
//        UserProfile user = getUserProfileOrThrow(userProfileId);
//    }

    private UserProfile getUserProfileOrThrow(UUID userProfileId){
        return userProfileDataAccessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(()->new IllegalStateException(String.format("User profile %s not found", userProfileId)));
    }
}
