package ktpm17ctt.g6.product.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${app.cloudinary.cloud-name}")
    private String CLOUD_NAME;

    @Value("${app.cloudinary.api-key}")
    private String API_KEY;

    @Value("${app.cloudinary.api-secret}")
    private String API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "folder", "ShoesShopAppV2"
        ));
    }

//    @Bean
//    public Cloudinary cloudinary() {
//        return new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "dr7uxdi9o",
//                "api_key", "677286347142467",
//                "api_secret", "4BTe3f82O9HtS66g1OBYpeYWeCU",
//                "folder", "ShoesShopAppV2"
//        ));
//    }
}
