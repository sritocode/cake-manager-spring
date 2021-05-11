package com.waracle.cakemgr.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CakeRepository extends CrudRepository<CakeEntity, Long> {
    List<CakeEntity> findByTitle(String title);
}