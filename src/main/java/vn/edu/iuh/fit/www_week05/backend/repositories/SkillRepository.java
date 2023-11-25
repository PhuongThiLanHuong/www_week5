package vn.edu.iuh.fit.www_week05.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.iuh.fit.www_week05.backend.models.Skill;

public interface SkillRepository extends JpaRepository<Skill,Long> {
}
