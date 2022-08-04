package example.armeria.server.blog;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Post;


import com.linecorp.armeria.server.annotation.RequestConverter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlogService {
    private final Map<Integer, BlogPost> blogPosts = new ConcurrentHashMap<>();

    @Post("/blogs")
    @RequestConverter(BlogPostRequestConverter.class)
    public HttpResponse createBlogPost(BlogPost blogPost) {
        blogPosts.put(blogPost.getId(), blogPost);
        return HttpResponse.ofJson(blogPost);
    }
}
