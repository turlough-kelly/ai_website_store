package org.comp30860.groupproject.ant.persistence.repository;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {

    public AccountModel findByUsername(String username);

    public AccountModel findByEmailAddress(String emailAddress);

}