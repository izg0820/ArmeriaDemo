package example.armeria.server.blog;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.*;


import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class BlogService {
    private final Map<Integer, BlogPost> blogPosts = new ConcurrentHashMap<>();

    @Post("/blogs")
    @RequestConverter(BlogPostRequestConverter.class)
    public HttpResponse createBlogPost(BlogPost blogPost) {
        blogPosts.put(blogPost.getId(), blogPost);
        return HttpResponse.ofJson(blogPost);
    }

    @Get("/blogs/:id") //query parameter or path variable를 사용하기 위해서 :id와 같이 추가한다.
    public HttpResponse getBlogPost(@Param int id) {
        BlogPost blogPost = blogPosts.get(id);
        return HttpResponse.ofJson(blogPost);
    }

    @Get("/blogs")
    @ProducesJson //Convert list of object to Json
    public Iterable<BlogPost> getBlogPosts(@Param @Default("true") boolean descending) {
        if(descending) {
            return blogPosts.entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getKey)))
                    .map(Map.Entry::getValue).collect(Collectors.toList());
        }
        return blogPosts.values().stream().collect(Collectors.toList());
    }

    @Put("/blogs/:id")
    public HttpResponse updateBlogPost(@Param int id, @RequestObject BlogPost blogPost) {
        final BlogPost oldBlogPost = blogPosts.get(id);
        if (oldBlogPost == null) {
            HttpResponse.of(HttpStatus.NOT_FOUND);
        }
        BlogPost newBlogPost = new BlogPost(id, blogPost.getTitle(),
                blogPost.getContent(),
                oldBlogPost.getCreatedAt(),
                blogPost.getCreatedAt());
        blogPosts.put(id, newBlogPost);
        return HttpResponse.ofJson(newBlogPost);
    }

}
