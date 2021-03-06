package com.lifeplus.lifeplus.repository;

import com.lifeplus.lifeplus.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>
{
    List<Category> findAll();
}
