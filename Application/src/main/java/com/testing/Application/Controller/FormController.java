package com.testing.Application.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.testing.Application.model.FormData;
import com.testing.Application.service.FormService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    // Get all form submissions
    @GetMapping
    public String home(Model model) {
        FormData user = new FormData();
        model.addAttribute("user", user);
        model.addAttribute("message", "welcome");
        model.addAttribute("pageTitle", "Home");
        return "index";
    }

    @PostMapping("/submit")
    public String submitForm(@Valid @ModelAttribute("formData") FormData formData, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "index";
        }
        formService.saveForm(formData);
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String listForms(Model model) {
        var forms = formService.getAllForms();
        model.addAttribute("forms", forms);
        return "form_list";
    }
    
    // Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        FormData formData = formService.getFormById(id);
        model.addAttribute("user", formData);
        return "edit_form";
    }
    @PostMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, 
                             @Valid @ModelAttribute("user") FormData formData, 
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", formData);
            return "edit_form";  // Stay on edit page if validation fails
        }
    
        FormData existingForm = formService.getFormById(id);
        if (existingForm == null) {
            return "redirect:/list";  // Redirect if form not found
        }
    
        // ✅ Update all fields
        existingForm.setName(formData.getName());
        existingForm.setEmail(formData.getEmail());
        existingForm.setDateOfBirth(formData.getDateOfBirth());
        existingForm.setDepartment(formData.getDepartment());
        existingForm.setGender(formData.getGender()); // ✅ Added gender update
    
        formService.saveForm(existingForm);  // Save the updated record
        
        return "redirect:/list";  // 🔄 Redirect back to form list after updating
    }
    
    
    // Delete Form Entry
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteForm(@PathVariable Long id) {
        // formService.deleteForm(id);
        // return "redirect:/list";
        try {
            formService.deleteForm(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting record.");
        }
    }
 
}
