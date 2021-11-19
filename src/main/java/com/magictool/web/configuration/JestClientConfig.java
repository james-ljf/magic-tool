package com.magictool.web.configuration;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * es搜索引擎连接配置类
 * @Author ljf
 * @Date 2021/11/19 18:36
 */
@Configuration
public class JestClientConfig {

    /**
     * 配置es连接信息，对应properties或者yml文件里的配置
     * 按自己需要选择，本地使用jextclient即可，可以删去ali的和new的bean和配置
     */
    @Value("${spring.elasticsearch.jest.uris}")
    private String uris;
    @Value("${spring.elasticsearch.jest.username:}")
    private String username;
    @Value("${spring.elasticsearch.jest.password:}")
    private String password;
    @Value("${spring.elasticsearch.jest.ali.uris:}")
    private String aliUris;
    @Value("${spring.elasticsearch.jest.ali.username:}")
    private String aliUsername;
    @Value("${spring.elasticsearch.jest.ali.password:}")
    private String aliPassword;

    @Value("${spring.elasticsearch.jest.new.uris:}")
    private String newUris;
    @Value("${spring.elasticsearch.jest.new.username:}")
    private String newUsername;
    @Value("${spring.elasticsearch.jest.new.password:}")
    private String newPassword;

    @Bean
    @Primary
    public JestClient jestClient() {
        HttpClientConfig.Builder builder = createHttpClientConfig(uris, username, password);
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(builder.build());
        return factory.getObject();
    }

    @Bean("aliJestClient")
    public JestClient aliJestClient() {
        HttpClientConfig.Builder builder = createHttpClientConfig(aliUris, aliUsername, aliPassword);
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(builder.build());
        return factory.getObject();
    }

    @Bean("newJestClient")
    public JestClient newJestClient() {
        HttpClientConfig.Builder builder = createHttpClientConfig(newUris, newUsername, newPassword);
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(builder.build());
        return factory.getObject();
    }

    /**
     * 连接配置（核心）
     */
    private HttpClientConfig.Builder createHttpClientConfig(String uris, String username, String password) {
        HttpClientConfig.Builder builder = new HttpClientConfig.Builder(uris)
                .multiThreaded(true)
                .requestCompressionEnabled(true)
                .connTimeout(3000)
                .readTimeout(3000)
                .maxTotalConnection(400)
                .defaultMaxTotalConnectionPerRoute(400)
                .gson(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .registerTypeAdapter(GeoPoint.class, (JsonDeserializer<GeoPoint>) (jsonElement, type, context) -> {
                            if (jsonElement.isJsonObject()) {
                                return new Gson().fromJson(jsonElement, type);
                            } else if (jsonElement.isJsonArray()) {
                                List<Double> dst = new Gson().fromJson(jsonElement, new TypeToken<List<Double>>() {
                                }.getType());
                                if (null != dst && dst.size() == 2) {
                                    return new GeoPoint(dst.get(1), dst.get(0));
                                }
                                throw new RuntimeException("GeoPoint array format must be [lat, lon]");
                            } else if (jsonElement.isJsonPrimitive()) {
                                String[] segments = StringUtils.split(jsonElement.getAsString(), ",");
                                if (null != segments && segments.length == 2) {
                                    return new GeoPoint(Double.parseDouble(segments[0]), Double.parseDouble(segments[1]));
                                }
                                throw new RuntimeException("GeoPoint string format must be lat,lon");
                            }
                            throw new RuntimeException("unknown GeoPoint format: " + jsonElement.getAsString());
                        })
                        .create());
        if (StringUtils.hasText(username)) {
            builder.defaultCredentials(username, password);
        }
        return builder;
    }

}
