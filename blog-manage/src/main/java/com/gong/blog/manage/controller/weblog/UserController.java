package com.gong.blog.manage.controller.weblog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.entity.User;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.service.UserService;
import com.gong.blog.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weblog/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 新增
     */
    @Log(title = "新增论坛用户", businessType = BusinessType.INSERT)
    @PostMapping
    public  Result<String> save(@RequestBody User user) {
        userService.save(user);
        return Result.success("添加成功");
    }

    /**
     * 修改
     */
    @Log(title = "修改weblog用户", businessType = BusinessType.EDIT)
    @PutMapping
    public Result<String> update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success("更新成功");
    }

    /**
     * 查询
     */
    @GetMapping("/list")
    public Result<IPage<User>> list(PageParams params) {
        IPage<User> page = new Page<>(params.getPageNum(), params.getPageSize());
        userService.page(page);
        return Result.success(page);
    }

    /**
     * id查询
     */
    @GetMapping("/{id}")
    public  Result<User> one(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 删除
     */
    @Log(title = "删除weblog用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<String> remove(@PathVariable List<Long> ids) {
        userService.removeByIds(ids);
        return Result.success("删除成功");
    }
}
