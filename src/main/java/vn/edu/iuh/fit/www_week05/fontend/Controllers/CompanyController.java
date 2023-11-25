package vn.edu.iuh.fit.www_week05.fontend.Controllers;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.www_week05.backend.models.Address;
import vn.edu.iuh.fit.www_week05.backend.models.Candidate;
import vn.edu.iuh.fit.www_week05.backend.models.Company;
import vn.edu.iuh.fit.www_week05.backend.repositories.AddressRepository;
import vn.edu.iuh.fit.www_week05.backend.repositories.CandidateRepository;
import vn.edu.iuh.fit.www_week05.backend.repositories.CompanyRepository;
import vn.edu.iuh.fit.www_week05.backend.services.CompanyService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyRepository companyRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyService companyService;
    private final AddressRepository addressRepository;

    public CompanyController(CompanyRepository companyRepository,
                             CandidateRepository candidateRepository, CompanyService companyService, AddressRepository addressRepository) {
        this.companyRepository = companyRepository;
        this.candidateRepository = candidateRepository;
        this.companyService = companyService;
        this.addressRepository = addressRepository;
    }


    @GetMapping("/listCompany")
    public String showCandidateList(Model model) {
        model.addAttribute("companies",companyRepository.findAll());
        return "companies/listCompany";
    }
    @GetMapping("/show-add-form")
    public ModelAndView add(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Company company=new Company();
        company.setAddress(new Address());
        modelAndView.addObject("company", company);
        modelAndView.addObject("address", company.getAddress());
        modelAndView.addObject("countries", CountryCode.values());
        modelAndView.setViewName("companies/add");
        return modelAndView;
    }

    @PostMapping("/companies/add")
    public String addCompany(
            @ModelAttribute("company") Company company,
            @ModelAttribute("address") Address address,
            BindingResult result, Model model) {
        addressRepository.save(address);
        company.setAddress(address);
       companyRepository.save(company);
        return "/companies/listCompany";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            companyRepository.delete(optionalCompany.get());
        }
        return "/companies/listCompany";
    }

}
