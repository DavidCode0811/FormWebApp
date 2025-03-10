package com.testing.Application.service;

import com.testing.Application.model.FormData;
import com.testing.Application.repository.FormRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    // Retrieve all forms
    public List<FormData> getAllForms() {
        return formRepository.findAll(); // ✅ FIXED: Removed invalid findAllById()
    }

    // Retrieve a single form by ID
    public FormData getFormById(Long id) {
        return formRepository.findById(id).orElse(null); // ✅ FIXED: Now properly fetches form
    }

    // Save or update a form
    public FormData saveForm(FormData formData) {
        return formRepository.save(formData);
    }

    // Delete a form by ID
    public void deleteForm(Long id) {
        if (formRepository.existsById(id)) {
            formRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Form with ID " + id + " does not exist");
        }
    }
}
