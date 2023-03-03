package minh.lehong.springbootawss3.bucket;

public enum BucketName {
    PROFILE_IMAGE("minhlehong-springbootawss3");
    private final String bucketName;
    public String getBucketName() {
        return bucketName;
    }
    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

}
