
package com.example.uloha2.controller;

import com.example.uloha2.entity.Employee;
import com.example.uloha2.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Value("${job.titles}")
    private String jobTitlesConfig;

    @Value("${employment.types}")
    private String employmentTypesConfig;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ModelAttribute("jobTitles")
    public List<String> jobTitles() {
        return Arrays.stream(jobTitlesConfig.split(","))
                .map(String::trim)
                .toList();
    }

    @ModelAttribute("employmentTypes")
    public List<String> employmentTypes() {
        return Arrays.stream(employmentTypesConfig.split(","))
                .map(String::trim)
                .toList();
    }

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/list";
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Integer id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "employees/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/form";
    }

    @PostMapping
    public String createEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result) {
        employee.setId(null);
        if (!result.hasFieldErrors("email")
                && employeeService.emailExistsForOther(
                employee.getEmail(), null)) {
            result.rejectValue("email", "duplicate",
                    "Tento email už používa iný zamestnanec");
        }
        if (result.hasErrors()) {
            return "employees/form";
        }
        employeeService.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "employees/form";
    }

    @PostMapping("/{id}")
    public String updateEmployee(
            @PathVariable Integer id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result) {
        employeeService.findById(id);
        employee.setId(id);
        if (!result.hasFieldErrors("email")
                && employeeService.emailExistsForOther(
                employee.getEmail(), id)) {
            result.rejectValue("email", "duplicate",
                    "Tento email už používa iný zamestnanec");
        }
        if (result.hasErrors()) {
            return "employees/form";
        }

        employeeService.save(employee);
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }
}
