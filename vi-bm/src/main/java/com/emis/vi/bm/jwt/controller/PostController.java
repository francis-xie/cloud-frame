package com.emis.vi.bm.jwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.emis.vi.bm.jwt.annotation.CurrentUser;
import com.emis.vi.bm.jwt.annotation.LoginRequired;
import com.emis.vi.bm.jwt.model.Post;
import com.emis.vi.bm.jwt.model.User;
import com.emis.vi.bm.jwt.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章controller
 */
@RestController
@RequestMapping("/api/post")
public class PostController {
    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 新增文章
     * @param post
     * @param user
     * @return
     */
    @PostMapping("")
    @LoginRequired //@LoginRequired 注解进行登录拦截和 @CurrentUser 注解获取当前登录用户。
    public Post add(@RequestBody Post post, @CurrentUser User user) {
        post.setAuthorId(user.getId()); // 添加作者信息
        post = postService.add(post);
        return post;
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable int id) {
        Post post = postService.findById(id);
        return postService.findById(id);
    }

    @GetMapping("")
    public List<Post> all() {
        return postService.all();
    }

    /**
     * 更新文章，需要登录
     *
     * @param post        需要修改的内容
     * @param id          文章 id
     * @param currentUser 当前用户
     * @return 更新之后的文章
     */
    @LoginRequired
    @PutMapping("/{id}")
    public Post update(@RequestBody Post post, @PathVariable int id, @CurrentUser User currentUser) {
        post.setId(id);
        return postService.update(post, currentUser);
    }

    /**
     * 删除文章，需要登录
     *
     * @param id          文章 id
     * @param currentUser 当前登录用户
     * @return 提示信息
     */
    @LoginRequired
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable int id, @CurrentUser User currentUser) {
        postService.delete(id, currentUser);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "删除成功");
        return jsonObject;
    }
}
