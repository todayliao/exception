package com.exception.qms.domain.enhancement;

import com.exception.qms.domain.entity.QuestionTagRel;
import com.exception.qms.domain.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/26
 * @time 下午1:50
 * @discription auth 验证 user
 **/
@Data
public class AuthUser extends User implements UserDetails {

    private List<GrantedAuthority> grantedAuthorityList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorityList;
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
