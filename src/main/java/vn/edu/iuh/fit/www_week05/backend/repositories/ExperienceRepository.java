package vn.edu.iuh.fit.www_week05.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.www_week05.backend.models.Experience;

public interface ExperienceRepository extends JpaRepository<Experience,Long> {
}
