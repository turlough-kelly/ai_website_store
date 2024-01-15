package org.comp30860.groupproject.ant.security;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class AccountDetails implements UserDetails {

    private AccountModel account;

    private Collection<SimpleGrantedAuthority> authorities = new HashSet<>();

    public AccountDetails(AccountModel account) {
        this.account = account;
        AccountModel.Role[] roles = AccountModel.Role.values();
        for (int i = account.getRole().ordinal(); i < roles.length; i++) {
            this.authorities.add(new SimpleGrantedAuthority(roles[i].toString()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public long id() {
        return this.account.getId();
    }

    @Override
    public String getPassword() {
        return this.account.getPassword();
    }

    @Override
    public String getUsername() {
        return this.account.getUsername();
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

    public String getFullName() {
        return account.getFirstname() + " " + account.getSurname();
    }

}
