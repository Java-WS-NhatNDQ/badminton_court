package com.re.badminton_court.model.dto.cloudinary;

import lombok.*;

@Data @Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ImageResponse {
    private String publicId;
    private String url;
    private String status;
}
