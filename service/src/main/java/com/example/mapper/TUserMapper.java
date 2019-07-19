package com.example.mapper;

import com.example.entity.TUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author jinrun.xie
 * @date 2019/7/18
 **/
@Repository
public interface TUserMapper {

    TUser getUserByName(@Param("name") String name);
}
