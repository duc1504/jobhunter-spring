package vn.developer.jobhunter.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Đăng ký DateTimeFormatterRegistrar để sử dụng định dạng ISO cho ngày giờ
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true); // Sử dụng định dạng ISO 8601 cho ngày giờ
        registrar.registerFormatters(registry); // Đăng ký formatter vào registry
    }
}
