package bankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface BankAccountRepository extends JpaRepository<BankAccountModel, Integer> {

	BankAccountModel findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	@Modifying
	@Transactional
	@Query("delete from BankAccountModel b where b.email = ?1")
	void deleteByEmail(String email);
}