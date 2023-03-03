package minh.lehong.springbootawss3.datastore;

import minh.lehong.springbootawss3.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {
    private  static  final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static{
        USER_PROFILES.add(new UserProfile(UUID.fromString("36dc71f7-0f3d-4365-b68a-f97f6be4e3eb"), "CAT", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("db662156-bbc3-44fd-b779-47c3060d6e47"), "DOG", null));
    }

    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }
}
