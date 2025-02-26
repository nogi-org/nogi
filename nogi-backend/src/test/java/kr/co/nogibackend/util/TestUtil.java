package kr.co.nogibackend.util;

import java.io.IOException;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import org.springframework.core.io.ClassPathResource;

public class TestUtil {

  /**
   * 📌 `resources/image` 폴더의 이미지를 읽고 Base64로 변환하는 유틸 메서드
   */
  public static String encodeImageToBase64(String imagePath) throws IOException {
    ClassPathResource resource = new ClassPathResource(imagePath);
    byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
    return Base64.getEncoder().encodeToString(imageBytes);
  }

  public static String getNowDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    return OffsetDateTime.now().format(formatter);
  }
}
