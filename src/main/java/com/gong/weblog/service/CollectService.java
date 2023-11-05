package com.gong.weblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.weblog.dto.CollectForm;
import com.gong.weblog.dto.CollectParams;
import com.gong.weblog.entity.Collect;
import com.gong.weblog.vo.Favorites;

import java.util.List;

/**
* @author asus
* @description 针对表【collect】的数据库操作Service
* @createDate 2023-11-01 10:32:28
*/
public interface CollectService extends IService<Collect> {
    void addItem(CollectForm form);

    void itemModify(CollectForm form);

    void favoritesModify(CollectForm form);

    void addFavorites(CollectForm form);

    void removeItem(CollectForm form);

    void removeFavorites(Long favoritesId);

    Favorites getFavorites(CollectParams params);

    List<Favorites> getFavoritesList(CollectParams params);

    List<String> queryIsCollect(CollectParams params);
}
