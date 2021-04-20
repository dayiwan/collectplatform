package com.collectplatform.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.collectplatform.project.entity.TagEntity;
import com.collectplatform.project.dao.TagDao;
import com.collectplatform.project.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.collectplatform.project.util.StringTools;
import com.collectplatform.project.vo.TagVo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Clc
 * @since 2021-04-14
 */
@Service
public class TaglServiceImpl extends ServiceImpl<TagDao, TagEntity> implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public String add(AddVo addVo){
        TagEntity labelInfo = new TagEntity();
        labelInfo.setParentId(addVo.getParentId());
        labelInfo.setName(addVo.getName());
        tagDao.insert(labelInfo);
        return labelInfo.getId();
    }

    @Override
    public String delete(DeleteVo deleteVo){
        // 新建list用于存放需要删除的标签id
        List<String> ids = new ArrayList<>();
        //  判断需要删除的Id是否是父级id, 如果是父级Id, 则遍历删除所有子级id

        if(deleteVo.getParentId().equals("0")) {
            List<ListOutVo> childrenInfoList = labelInfoByParentId(deleteVo.getId());
            if(!StringTools.isNullOrEmpty(childrenInfoList)){
                for (ListOutVo childrenInfo: childrenInfoList) {
                    ids.add(childrenInfo.getId());
                }
            }
        }
        //  不管是否是父级id, 最后都需要将传递的Id加入要删除的列表中
        ids.add(deleteVo.getId());

        tagDao.deleteBatchIds(ids);
        return deleteVo.getId();
    }

    public ListOutVo labelInfoById(String id) {
        QueryWrapper<ListOutVo> wrapper = new QueryWrapper<>();
        if(!StringTools.isNullOrEmpty(id)){
            wrapper.eq("id", id);
        }
        return tagDao.labelInfoById(wrapper);
    }

    public List<ListOutVo> labelInfoByParentId(String id) {
        QueryWrapper<ListOutVo> wrapper = new QueryWrapper<>();
        if(!StringTools.isNullOrEmpty(id)){
            wrapper.eq("parent_id", id);
        }
        return tagDao.labelInfoByParentId(wrapper);
    }

    @Override
    public String update(TagEntity tagEntity){
        tagDao.updateById(tagEntity);
        return tagEntity.getId();
    }

    @Override
    public IPage<ListOutVo> listPage(ListInVo listInVo){
        Page<ListOutVo> page = new Page<>(listInVo.getPage(), listInVo.getSize());
        QueryWrapper<ListOutVo> queryWrapper = new QueryWrapper<>();
        if(!StringTools.isNullOrEmpty(listInVo.getName())){
            queryWrapper.like("name", listInVo.getName());
        }
        return tagDao.listPage(page, queryWrapper);
    }

    @Override
    public List<ListOutVo> listAll(String name){
        QueryWrapper<ListOutVo> wrapper = new QueryWrapper<>();
        if(!StringTools.isNullOrEmpty(name)){
            wrapper.like("name", name);
        }
        return tagDao.listAll(wrapper);
    }

}