package com.sipuang.xianyitong.system.service.impl;

import com.sipuang.xianyitong.system.bo.RoleBO;
import com.sipuang.xianyitong.system.mapper.RoleMapper;
import com.sipuang.xianyitong.system.model.Resource;
import com.sipuang.xianyitong.system.model.Role;
import com.sipuang.xianyitong.system.service.ResourceService;
import com.sipuang.xianyitong.system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author lijun
 * @date 2018-04-20.
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ResourceService resourceService;

    @Override
    public List<RoleBO> getByUserId(Integer userId) {
        //查询用户的角色
        Set<Role> roles = roleMapper.selectByUserId(userId);
        return convertToBO(roles);
    }

    @Override
    public List<RoleBO> getAll() {
        List<Role> roles = roleMapper.selectAll();
        return convertToBO(roles);
    }

    @Override
    public void add(Role role) {
        roleMapper.insertSelective(role);
    }

    @Override
    public void edit(Role role) {
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public void del(Integer id) {
        roleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Role get(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void editResources(Integer roleId, List<Integer> resourceIds) {
        roleMapper.deleteResourceByRoleId(roleId);
        roleMapper.insertResources(roleId, resourceIds);
    }

    /**
     * 转换成BO对象
     *
     * @param roles
     * @return
     */
    private List<RoleBO> convertToBO(Collection<Role> roles) {
        List<RoleBO> bos = new ArrayList<>(roles.size());
        RoleBO bo;
        //根据角色查询资源
        for (Role role : roles) {
            bo = new RoleBO();
            BeanUtils.copyProperties(role, bo);
            Set<Resource> resources = resourceService.getByRoleId(role.getId());
            bo.setResources(resources);
            bos.add(bo);
        }
        return bos;
    }
}
