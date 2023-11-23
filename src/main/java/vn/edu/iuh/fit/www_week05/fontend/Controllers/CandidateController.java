package vn.edu.iuh.fit.www_week05.fontend.Controllers;


import com.neovisionaries.i18n.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
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
import vn.edu.iuh.fit.www_week05.backend.models.Skill;
import vn.edu.iuh.fit.www_week05.backend.repositories.AddressRepository;
import vn.edu.iuh.fit.www_week05.backend.repositories.CandidateRepository;
import vn.edu.iuh.fit.www_week05.backend.repositories.SkillRepository;
import vn.edu.iuh.fit.www_week05.backend.services.CandidateServices;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CandidateController {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidateServices candidateServices;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private SkillRepository skillRepository;

    @GetMapping("/list")
    public String showCandidateList(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "candidates/list_no_paging";
    }

    @GetMapping("/list-paging")
    public String showCandidateListPaging(Model model,
                                          @RequestParam("page") Optional<Integer> page,
                                          @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        /*Page<Candidate> candidatePage= candidateServices.findPaginated(
                PageRequest.of(currentPage - 1, pageSize)
        );*/
        Page<Candidate> candidatePage = candidateServices.findAll(currentPage - 1,
                pageSize, "id", "asc");

        model.addAttribute("candidatePage", candidatePage);

        int totalPages = candidatePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "candidates/list";
    }

    @GetMapping("/show-add-form")
    public ModelAndView add(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Candidate candidate = new Candidate();
        candidate.setAddress(new Address());
        modelAndView.addObject("candidate", candidate);
        modelAndView.addObject("address", candidate.getAddress());
        modelAndView.addObject("countries", CountryCode.values());
        modelAndView.setViewName("candidates/add");
        return modelAndView;
    }

    @PostMapping("/candidates/add")
    public String addCandidate(
            @ModelAttribute("candidate") Candidate candidate,
            @ModelAttribute("address") Address address,
            BindingResult result, Model model) {
        addressRepository.save(address);
        candidate.setAddress(address);
        candidateRepository.save(candidate);
        return "redirect:/list";
    }

    @GetMapping("/show-edit-form/{id}")
    public ModelAndView edit(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Candidate> opt = candidateRepository.findById(id);
        if (opt.isPresent()) {
            Candidate candidate = opt.get();
            modelAndView.addObject("candidate", candidate);
            modelAndView.addObject("address", candidate.getAddress());
            modelAndView.addObject("countries", CountryCode.values());
            modelAndView.setViewName("candidates/update");
        }
        return modelAndView;
    }

    @PostMapping("/candidates/update/{id}")
    public String update(
            @ModelAttribute("candidate") Candidate candidate,
            @ModelAttribute("address") Address address) {
        addressRepository.save(address);
//        candidate.setAddress(address);
        candidateRepository.save(candidate);
        return "redirect:/candidates/update";
    }

    @GetMapping("/candidates/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);
        if (optionalCandidate.isPresent()) {
            candidateRepository.delete(optionalCandidate.get());
        }
        return "redirect:/list";
    }

    @GetMapping("/suggest-skill")
    public String home(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, @RequestParam("candidate-id") Optional<Long> candidateId, Model model) {
        int sizeI = size.orElse(10);
        List<Candidate> candidates = candidateRepository.findAll();

        if (candidateId.isPresent()) {
            PageRequest pageRequest = PageRequest.of(page.orElse(1) - 1, sizeI, Sort.by("id"));

            Page<Skill> skills = skillRepository.suggestForCandidate(candidateId.get(), pageRequest);

            model.addAttribute("skills", skills);
            model.addAttribute("candidateId", candidateId.get());
            model.addAttribute("pages", IntStream.rangeClosed(1, skills.getTotalPages()).boxed().collect(Collectors.toList()));
        }
        model.addAttribute("candidates", candidates);

        return "candidates/suggest-skill";
    }
}
