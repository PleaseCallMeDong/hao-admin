package com.example.modules.sys.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author: dj
 * @create: 2020-03-04 15:47
 * @description: 用户登录后返回结果
 */
@Data
public class SysUserLoginDTO implements Serializable {

    private String token;

    private String username;

    private String name;

    private List<SysMenuDTO> menuList;

    private Set<String> permList;

//    private List<AddressInfoDTO> addressList;

    private boolean updateAddressInfo;

}
