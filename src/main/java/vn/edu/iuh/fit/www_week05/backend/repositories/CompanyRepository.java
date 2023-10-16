package vn.edu.iuh.fit.www_week05.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.www_week05.backend.models.Company;

public interface CompanyRepository extends JpaRepository<Company,Long> {
}
