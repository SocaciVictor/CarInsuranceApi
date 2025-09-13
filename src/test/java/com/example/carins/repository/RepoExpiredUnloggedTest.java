package com.example.carins.repository;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.model.Owner;
import com.example.carins.repo.InsurancePolicyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(statements = {
        "DELETE FROM insurancepolicy",
        "DELETE FROM car",
        "DELETE FROM owner"
})
class RepoExpiredUnloggedTest {

    @Autowired
    InsurancePolicyRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void returnsOnlyExpiredAndUnlogged() {
        Owner owner = new Owner("Ana", "ana@example.com");
        em.persist(owner);

        Car car = new Car("VIN1234", "Dacia", "Logan", 2018, owner);
        em.persist(car);

        LocalDate today = LocalDate.of(2025, 1, 1);

        InsurancePolicy p1 = new InsurancePolicy(car, "Allianz", today.minusYears(1), today.minusDays(1));
        InsurancePolicy p2 = new InsurancePolicy(car, "Groupama", today.minusYears(2), today);
        InsurancePolicy p3 = new InsurancePolicy(car, "Allianz", today.minusYears(3), today.minusYears(2));
        p3.setExpiredLogged(true);

        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.flush();

        var list = repo.findExpiredUnlogged(today);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(p1.getId());
    }
}
