package com.desafiobackend.gestoreventosapi.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceResolver resolver = new ReactResourceResolver();
        registry.addResourceHandler("/**")
                .resourceChain(true)
                .addResolver(resolver);

        // Can try to play with
        // registry.addResourceHandler("/**")
        //        .addResourceLocations("classpath:/static/");
        // But this option can't map every path to index.html
        // Can try https://stackoverflow.com/a/42998817/1032167
        // to resolve this, but then you loose /api/** => rest
        // and to be honest it is some regex madness, so
        // it was easier for me to setup custom resource resolver


    }

    public static class ReactResourceResolver implements ResourceResolver {
        // root dir of react files
        // example REACT_DIR/index.html
        private static final String REACT_DIR = "/static/react-webapp/";

        // this is directory inside REACT_DIR for react static files
        // example REACT_DIR/REACT_STATIC_DIR/js/
        // example REACT_DIR/REACT_STATIC_DIR/css/
        private static final String REACT_STATIC_DIR = "static";

        private final Resource index = new ClassPathResource(REACT_DIR + "index.html");
        private final List<String> rootStaticFiles = Arrays.asList("favicon.io",
                "asset-manifest.json", "manifest.json", "service-worker.js");

        @Override
        public Resource resolveResource(
                HttpServletRequest request, @NotNull String requestPath,
                @NotNull List<? extends Resource> locations, @NotNull ResourceResolverChain chain) {

            return resolve(requestPath);
        }

        @Override
        public String resolveUrlPath(
                @NotNull String resourcePath, @NotNull List<? extends Resource> locations,
                @NotNull ResourceResolverChain chain) {

            Resource resolvedResource = resolve(resourcePath);
            if (resolvedResource == null) {
                return null;
            }
            try {
                return resolvedResource.getURL().toString();
            } catch (IOException e) {
                return resolvedResource.getFilename();
            }
        }

        private Resource resolve(String requestPath) {

            if (requestPath == null) return null;

            if (rootStaticFiles.contains(requestPath)
                    || requestPath.startsWith(REACT_STATIC_DIR)) {
                return new ClassPathResource(REACT_DIR + requestPath);
            } else
                return index;
        }

    }
}
