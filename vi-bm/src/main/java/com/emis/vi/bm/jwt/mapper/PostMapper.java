package com.emis.vi.bm.jwt.mapper;

import com.emis.vi.bm.jwt.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PostMapper {
    int add(Post post);

    Post findOne(Post param);

    List<Post> all();

    void update(Post post);

    void delete(int id);
}
