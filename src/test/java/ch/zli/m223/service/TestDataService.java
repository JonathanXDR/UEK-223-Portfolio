package ch.zli.m223.service;

import java.time.LocalDate;

import javax.inject.Inject;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Booking;
import ch.zli.m223.model.RoleEnum;
import ch.zli.m223.model.StatusEnum;
import ch.zli.m223.model.TimeFrameEnum;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@IfBuildProfile("test")
@ApplicationScoped
public class TestDataService {

  @Inject
  EntityManager entityManager;

  @Transactional
  public void generateTestData(@Observes StartupEvent event) {
    clearData();

    var ApplicationUserA = new ApplicationUser("application-user-a@user.com", "User A first name", "User A last name",
        "ApplicationUserA",
        RoleEnum.ADMIN);
    entityManager.persist(ApplicationUserA);

    var ApplicationUserB = new ApplicationUser("application-user-b@user.com", "User B first name", "User B last name",
        "ApplicationUserB",
        RoleEnum.MEMBER);
    entityManager.persist(ApplicationUserB);

    var ApplicationUserC = new ApplicationUser("application-user-c@user.com", "User C first name", "User C last name",
        "ApplicationUserC",
        RoleEnum.MEMBER);

    var BookingA = new Booking(LocalDate.now(), TimeFrameEnum.MORNING, StatusEnum.CONFIRMED, ApplicationUserB);
    entityManager.persist(BookingA);

    var BookingB = new Booking(LocalDate.now().plusDays(3), TimeFrameEnum.AFTERNOON,
        StatusEnum.PENDING, ApplicationUserB);
    entityManager.persist(BookingB);

    var BookingC = new Booking(LocalDate.now().plusDays(5), TimeFrameEnum.FULL_DAY,
        StatusEnum.PENDING, ApplicationUserC);
    entityManager.persist(BookingC);
  }

  @Transactional
  public void clearData() {
    entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    entityManager.createNativeQuery("TRUNCATE TABLE Booking RESTART IDENTITY").executeUpdate();
    entityManager.createNativeQuery("TRUNCATE TABLE ApplicationUser RESTART IDENTITY")
        .executeUpdate();
    entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
  }

}
