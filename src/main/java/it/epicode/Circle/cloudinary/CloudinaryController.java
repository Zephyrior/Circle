package it.epicode.Circle.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class CloudinaryController {
    private final Cloudinary cloudinary;

    @PostMapping(path="/uploadme", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public uploadResponse upload(
            @RequestPart("file")
            MultipartFile file) {

        try {

            String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Map result = cloudinary.uploader().upload(file.getBytes(),
                    Cloudinary.asMap("folder", "CIRCLE", "public_id", uniqueName));

            String url = result.get("secure_url").toString();

            return new uploadResponse(url);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}