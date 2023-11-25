package vn.edu.iuh.fit.www_week05.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.iuh.fit.www_week05.backend.models.Skill;

public interface SkillRepository extends JpaRepository<Skill,Long> {
    @Query("SELECT s FROM Skill s JOIN s.jobSkills js WHERE s.id NOT IN (SELECT cs.skill.id FROM CandidateSkill cs WHERE cs.candidate.id = :candidateId) group by s order by count(s) desc")
    Page<Skill> suggestForCandidate(long candidateId, Pageable pageable);
}
