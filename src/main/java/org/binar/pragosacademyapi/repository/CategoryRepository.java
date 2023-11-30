package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Category;
import org.binar.pragosacademyapi.entity.dto.CategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "select new org.binar.pragosacademyapi.entity.dto.CategoryDto(c.id,c.name, c.image) from Category c")
    List<CategoryDto> selectAll();
}
