package com.apply.repositories;

import com.apply.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Vivek Agarwal on 5/10/2017.
 */
public interface CompanyRepository extends JpaRepository<Company,String>{

    Company findByHashValue(String hashValue);
}
