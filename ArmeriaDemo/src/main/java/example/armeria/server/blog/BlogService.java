package example.armeria.server.blog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlogService {
    private final Map<Integer, BlogPost> blogPosts = new ConcurrentHashMap<>();


}
