//package com.x.backend.services.media.video;
//
//import com.x.backend.exceptions.image.FailedToUploadImageException;
//import com.x.backend.models.media.Video;
//import com.x.backend.repositories.VideoRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.Objects;
//
//public abstract class VideoUploadService {
//
//    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
//
//    private static final List<String> SUPPORTED_EXTENSIONS = List.of(
//            ".mp4", ".webm", ".mov", ".avi"
//    );
//
//    protected final VideoRepository videoRepository;
//
//    @Value("${video.upload.dir}")
//    private String baseVideoUploadDir;
//
//    public VideoUploadService(VideoRepository videoRepository) {
//        this.videoRepository = videoRepository;
//    }
//
//    protected Video uploadVideo(MultipartFile file) {
//        try {
//
//
//        } catch (Exception e) {
//            throw new FailedToUploadImageException("Video upload failed: " + e.getMessage());
//        }
//    }
//
//    private void validateFile(MultipartFile file) {
//        if (file == null || file.isEmpty())
//            throw new FailedToUploadImageException("Uploaded video is empty.");
//
//        if (file.getSize() > MAX_FILE_SIZE)
//            throw new FailedToUploadImageException("Video size exceeds 50MB limit.");
//
//        String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase());
//
//    }
//
//    private String getFileExtension(String filename) {
//        int index = Objects.requireNonNull(filename).lastIndexOf('.');
//        return (index >= 0) ? filename.substring(index) : "";
//    }
//
//}
