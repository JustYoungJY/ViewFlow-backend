package app.viewflowbackend.services;


import app.viewflowbackend.DTO.category.CategoryCardResponseDTO;
import app.viewflowbackend.DTO.category.CategoryResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final ObjectMapper objectMapper;

    @Autowired
    public CategoryService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public List<CategoryCardResponseDTO> getCategories() {
        try {
            ClassPathResource resource = new ClassPathResource("categories.json");

            return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public CategoryResponseDTO getCategory(Integer id) {
        try {
            ClassPathResource resource = new ClassPathResource("categories.json");

            List<CategoryResponseDTO> dto = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {
                    }
            );

            return dto.stream().filter(c -> id.equals(c.getId())).findFirst().orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
