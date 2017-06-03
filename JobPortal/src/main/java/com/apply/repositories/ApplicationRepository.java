package com.apply.repositories;

import com.apply.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Vivek Agarwal on 5/12/2017.
 */
public interface ApplicationRepository extends JpaRepository<Application,Integer>{
}
