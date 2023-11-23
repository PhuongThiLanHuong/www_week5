package vn.edu.iuh.fit.www_week05.fontend.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.iuh.fit.www_week05.backend.models.Candidate;
import vn.edu.iuh.fit.www_week05.backend.models.Company;
import vn.edu.iuh.fit.www_week05.backend.repositories.CandidateRepository;
import vn.edu.iuh.fit.www_week05.backend.repositories.CompanyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CompanyController {
    private final CompanyRepository companyRepository;
    private final CandidateRepository candidateRepository;

    public CompanyController(CompanyRepository companyRepository,
                             CandidateRepository candidateRepository) {
        this.companyRepository = companyRepository;
        this.candidateRepository = candidateRepository;
    }

    @GetMapping("/suggest-candidate")
    public String home(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, @RequestParam("company-id") Optional<Long> companyId, Model model) {
        int sizeI = size.orElse(10);
        List<Company> companies = companyRepository.findAll();

        if (companyId.isPresent()) {
            PageRequest pageRequest = PageRequest.of(page.orElse(1) - 1, sizeI, Sort.by("can_id"));

            Page<Candidate> candidates = candidateRepository.suggestCandidate(companyId.get(), pageRequest);

            model.addAttribute("candidates", candidates);
            model.addAttribute("companyId", companyId.get());
            model.addAttribute("pages", IntStream.rangeClosed(1, candidates.getTotalPages()).boxed().collect(Collectors.toList()));
        }
        model.addAttribute("companies", companies);

        return "companies/suggest-candidate";
    }
}
