package com.gong.weblog.controller;

import com.gong.weblog.annotation.Log;
import com.gong.weblog.dto.CollectForm;
import com.gong.weblog.dto.CollectParams;
import com.gong.weblog.enums.BusinessType;
import com.gong.weblog.service.CollectService;
import com.gong.weblog.vo.Favorites;
import com.gong.weblog.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "收藏夹")
@RestController
@RequestMapping("/favorites")
public class CollectController {

    @Autowired
    private CollectService collectService;

    /**
     * 新建收藏夹，返回收藏夹的id
     * @param form
     * @return
     */
    @PostMapping("/creat")
    @ApiOperation(value = "新增收藏夹", notes = "")
    @Log(title = "新建收藏夹", businessType = BusinessType.INSERT, onlyError = true)
    public Result<String> addF(@Validated @RequestBody CollectForm form) {
        return Result.success(Long.toString(collectService.addFavorites(form)));
    }

    /**
     * 修改收藏夹
     * @param form
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value = "修改收藏夹", notes = "")
    @Log(title = "修该收藏夹", businessType = BusinessType.EDIT, onlyError = true)
    public Result<String> modifyF(@Validated @RequestBody CollectForm form) {
        collectService.FavoritesModify(form);
        return Result.success("操作成功");
    }

    /**
     * 新增收藏，批量删除收藏
     * @param form
     * @return
     */
    @PostMapping
    @ApiOperation(value = "修改收藏项", notes = "")
    @Log(title = "修改收藏项", businessType = BusinessType.EDIT, onlyError = true)
    public Result<String> modify(@Validated @RequestBody CollectForm form) {
        collectService.itemModify(form);
        return Result.success("操作成功");
    }

    /**
     * 删除文件夹
     * @param favoritesId
     * @return
     */
    @DeleteMapping("/{favoritesId}")
    @ApiOperation(value = "删除收藏夹", notes = "")
    @Log(title = "删除收藏夹", businessType = BusinessType.DELETE, onlyError = true)
    public Result<String> removeF(@PathVariable("favoritesId") Long favoritesId) {
        collectService.removeFavorites(favoritesId);
        return Result.success("操作成功");
    }

    @GetMapping("/list")
    public Result<Favorites> list(CollectParams params) {
        return Result.success(collectService.getFavorites(params));
    }

}
