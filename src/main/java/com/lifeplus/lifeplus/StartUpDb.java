package com.lifeplus.lifeplus;

import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.repository.*;
import com.lifeplus.lifeplus.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class StartUpDb {

    private S3Service s3Service;
    private UserRepository userRepository;
    private TeamRepository teamRepository;
    private TeamUserRepository teamUserRepository;
    private PatientRepository patientRepository;
    private ExerciseRepository exerciseRepository;
    private AssignedExerciseRepository assignedExerciseRepository;
    private RoutineDivisionRepository routineDivisionRepository;
    private RoutineRepository routineRepository;
    private PlanRepository planRepository;
    private PlanTemplateRepository planTemplateRepository;
    private CategoryRepository categoryRepository;
    private WeightHistoryRepository weightHistoryRepository;
    private SensorDataRepository sensorDataRepository;
    private ActivityExerciseRepository activityExerciseRepository;
    private ActivityRoutineRepository activityRoutineRepository;

    @Autowired
    public StartUpDb(S3Service s3Service, UserRepository userRepository, TeamRepository teamRepository, TeamUserRepository teamUserRepository, PatientRepository patientRepository, ExerciseRepository exerciseRepository, AssignedExerciseRepository assignedExerciseRepository, RoutineDivisionRepository routineDivisionRepository, RoutineRepository routineRepository, PlanRepository planRepository, PlanTemplateRepository planTemplateRepository, CategoryRepository categoryRepository, WeightHistoryRepository weightHistoryRepository, SensorDataRepository sensorDataRepository, ActivityExerciseRepository activityExerciseRepository, ActivityRoutineRepository activityRoutineRepository) {
        this.s3Service = s3Service;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.teamUserRepository = teamUserRepository;
        this.patientRepository = patientRepository;
        this.exerciseRepository = exerciseRepository;
        this.assignedExerciseRepository = assignedExerciseRepository;
        this.routineDivisionRepository = routineDivisionRepository;
        this.routineRepository = routineRepository;
        this.planRepository = planRepository;
        this.planTemplateRepository = planTemplateRepository;
        this.categoryRepository = categoryRepository;
        this.weightHistoryRepository = weightHistoryRepository;
        this.sensorDataRepository = sensorDataRepository;
        this.activityExerciseRepository = activityExerciseRepository;
        this.activityRoutineRepository = activityRoutineRepository;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        Boolean init = event.getApplicationContext().getEnvironment().getProperty("initdb", boolean.class);
        if (init == null || init) {
            try {
                // users
                User admin1 = new User("Mariano", "Lopez", "admin", "+54155554266", "admin@gmail.com", BCrypt.hashpw("pass", BCrypt.gensalt()), true, UserType.ADMIN);
                User patient1 = new User("Juan", "Mirra", "juanmirra", "+54955535221", "juanMirra@gmail.com", BCrypt.hashpw("juamir", BCrypt.gensalt()), true, UserType.USER);
                User patient2 = new User("Andrés", "Dominguez", "andresdominguez", "+54155556840", "andresdominguez@gmail.com", BCrypt.hashpw("anddom", BCrypt.gensalt()), true, UserType.USER);
                User patient3 = new User("Luis", "Cano", "luiscano", "+54155550946", "luiscano@gmail.com", BCrypt.hashpw("luican", BCrypt.gensalt()), true, UserType.USER);
                User patient4 = new User("Franco", "Hasashi", "francohasashi", "+54955583459" , "francohasashi@gmail.com", BCrypt.hashpw("frahas", BCrypt.gensalt()), true, UserType.USER);
                User patient5 = new User("Angeles", "Alonso", "aalonso", "+54955507833", "aalonso@gmail.com", BCrypt.hashpw("alonsan", BCrypt.gensalt()), true, UserType.USER);
                User medic1 = new User("Jose", "Sierra", "josesierra", "+54955513630", "josesierra@gmail.com", BCrypt.hashpw("jossie", BCrypt.gensalt()), true, UserType.MEDIC);
                User medic2 = new User("Tomas", "Bulacio", "tomasbulacio", "+54955513630", "tomasbulacio@gmail.com", BCrypt.hashpw("tombul", BCrypt.gensalt()), true, UserType.MEDIC);
                User medic3 = new User("Miguel", "Sanchez", "miguelsanchez", "+54955004859", "miguelsanchez@gmail.com", BCrypt.hashpw("migsan", BCrypt.gensalt()), true, UserType.MEDIC);
                User medic4 = new User("Romeo", "Santos", "romeosantos", "+54955098034", "romeosantos@gmail.com", BCrypt.hashpw("romsan", BCrypt.gensalt()), true, UserType.MEDIC);
                User medic5 = new User("Ayelen", "Mar", "aymar", "+54955012390", "aymar@gmail.com", BCrypt.hashpw("ayumar423", BCrypt.gensalt()), true, UserType.MEDIC);
                User medic6 = new User("Tomas", "Iturralde", "titu", "+54955938042", "tomas.iturralde@ing.austral.edu.ar", BCrypt.hashpw("contra", BCrypt.gensalt()), true, UserType.MEDIC);
                User kin1 = new User("Antonio", "Riera", "antonioriera", "+54955581421", "antonioriera@gmail.com", BCrypt.hashpw("antrie", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
                User kin2 = new User("John", "Ferrer", "johnferrer", "+54155552467", "johnferrer@gmail.com", BCrypt.hashpw("johfer", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
                User kin3 = new User("David", "Bisbal", "davidbisbal", "+54155593853", "davidbisbal@gmail.com", BCrypt.hashpw("davbis", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
                User kin4 = new User("Maria", "Keane", "maru", "+54155559285", "mari.keane@gmail.com", BCrypt.hashpw("mariaa413", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);
                User kin5 = new User("Florencia", "Flores", "florkeane", "+541555012942", "florencia.flores@gmail.com", BCrypt.hashpw("fflores123", BCrypt.gensalt()), true, UserType.KINESIOLOGIST);

                userRepository.save(admin1);
                userRepository.save(patient1);
                userRepository.save(patient2);
                userRepository.save(patient3);
                userRepository.save(patient4);
                userRepository.save(patient5);
                userRepository.save(medic1);
                userRepository.save(medic2);
                userRepository.save(medic3);
                userRepository.save(medic4);
                userRepository.save(medic5);
                userRepository.save(medic6);
                userRepository.save(kin1);
                userRepository.save(kin2);
                userRepository.save(kin3);
                userRepository.save(kin4);
                userRepository.save(kin5);

                // teams
                Team team1 = new Team(patient1);
                Team team2 = new Team(patient2);
                Team team3 = new Team(patient3);
                Team team4 = new Team(patient4);
                Team team5 = new Team(patient5);
                teamRepository.save(team1);
                teamRepository.save(team2);
                teamRepository.save(team3);
                teamRepository.save(team4);
                teamRepository.save(team5);
                // teamUsers
                TeamUser teamUserMedic1a = new TeamUser(medic1, TeamRole.LEADER, team1);
                TeamUser teamUserMedic1b = new TeamUser(medic2, TeamRole.LEADER, team1);
                TeamUser teamUserKin1a = new TeamUser(kin1, TeamRole.MODIFY, team1);
                TeamUser teamUserKin1b = new TeamUser(kin2, TeamRole.VIEW, team1);
                TeamUser teamUserMedic2 = new TeamUser(medic3, TeamRole.LEADER, team2);
                TeamUser teamUserMedic2a = new TeamUser(medic1, TeamRole.MODIFY, team2);
                TeamUser teamUserKin2 = new TeamUser(kin3, TeamRole.MODIFY, team2);
                TeamUser teamUserMedic3 = new TeamUser(medic4, TeamRole.LEADER, team3);
                TeamUser teamUserMedic3a = new TeamUser(medic1, TeamRole.VIEW, team3);
                TeamUser teamUserMedic4 = new TeamUser(medic5, TeamRole.LEADER, team4);
                TeamUser teamUserMedic5 = new TeamUser(medic5, TeamRole.LEADER, team5);
                TeamUser teamUserKin5a = new TeamUser(kin3, TeamRole.MODIFY, team5);
                TeamUser teamUserKin5b = new TeamUser(kin4, TeamRole.MODIFY, team5);
                TeamUser teamUserKin5c = new TeamUser(kin5, TeamRole.VIEW, team5);
                teamUserRepository.save(teamUserMedic1a);
                teamUserRepository.save(teamUserMedic1b);
                teamUserRepository.save(teamUserKin1a);
                teamUserRepository.save(teamUserKin1b);
                teamUserRepository.save(teamUserMedic2);
                teamUserRepository.save(teamUserMedic2a);
                teamUserRepository.save(teamUserKin2);
                teamUserRepository.save(teamUserMedic3);
                teamUserRepository.save(teamUserMedic3a);
                teamUserRepository.save(teamUserMedic4);
                teamUserRepository.save(teamUserMedic5);
                teamUserRepository.save(teamUserKin5a);
                teamUserRepository.save(teamUserKin5b);
                teamUserRepository.save(teamUserKin5c);

                // patients
                Patient pat1 = new Patient(patient1, LocalDate.of(1962, 4, 23), 181, 78, 70, 7, LocalDateTime.now(), team1);
                Patient pat2 = new Patient(patient2, LocalDate.of(1977, 3, 12), 200, 85, 105, 15, LocalDateTime.now(), team2);
                Patient pat3 = new Patient(patient3, LocalDate.of(1965, 11, 11), 175, 80, 83, 30, LocalDateTime.now(), team3);
                Patient pat4 = new Patient(patient4, LocalDate.of(1970, 1, 30), 183, 70, 93, 30, LocalDateTime.now(), team4);
                Patient pat5 = new Patient(patient5, LocalDate.of(1975, 6, 23), 160, 50, 63, 30, LocalDateTime.now(), team5);

                patientRepository.save(pat1);
                patientRepository.save(pat2);
                patientRepository.save(pat3);
                patientRepository.save(pat4);
                patientRepository.save(pat5);

                // weight entries
                WeightEntry weightEntry1a = new WeightEntry(pat1, LocalDateTime.now().minusDays(50), 102);
                WeightEntry weightEntry1b = new WeightEntry(pat1, LocalDateTime.now().minusDays(40), 95);
                WeightEntry weightEntry1c = new WeightEntry(pat1, LocalDateTime.now().minusDays(30), 93);
                WeightEntry weightEntry1d = new WeightEntry(pat1, LocalDateTime.now().minusDays(20), 82);
                WeightEntry weightEntry1e = new WeightEntry(pat1, pat1.getWeightLastUpdated(), pat1.getWeight());
                WeightEntry weightEntry2a  = new WeightEntry(pat2, LocalDateTime.now().minusDays(30), pat2.getWeight());
                WeightEntry weightEntry2b  = new WeightEntry(pat2, LocalDateTime.now().minusDays(20), 107);
                WeightEntry weightEntry2c  = new WeightEntry(pat2, LocalDateTime.now().minusDays(9), 107);
                WeightEntry weightEntry2d  = new WeightEntry(pat2, pat2.getWeightLastUpdated(), pat2.getWeight());
                WeightEntry weightEntry3a = new WeightEntry(pat3, LocalDateTime.now().minusDays(20), 81);
                WeightEntry weightEntry3b = new WeightEntry(pat3, LocalDateTime.now().minusDays(9), 82);
                WeightEntry weightEntry3c = new WeightEntry(pat3, pat3.getWeightLastUpdated(), pat3.getWeight());
                WeightEntry weightEntry4a = new WeightEntry(pat4, LocalDateTime.now().minusDays(20), 87);
                WeightEntry weightEntry4b = new WeightEntry(pat4, pat4.getWeightLastUpdated(), pat4.getWeight());
                WeightEntry weightEntry5a = new WeightEntry(pat5, pat5.getWeightLastUpdated(), pat5.getWeight());

                weightHistoryRepository.save(weightEntry1a);
                weightHistoryRepository.save(weightEntry1b);
                weightHistoryRepository.save(weightEntry1c);
                weightHistoryRepository.save(weightEntry1d);
                weightHistoryRepository.save(weightEntry1e);
                weightHistoryRepository.save(weightEntry2a);
                weightHistoryRepository.save(weightEntry2b);
                weightHistoryRepository.save(weightEntry2c);
                weightHistoryRepository.save(weightEntry2d);
                weightHistoryRepository.save(weightEntry3a);
                weightHistoryRepository.save(weightEntry3b);
                weightHistoryRepository.save(weightEntry3c);
                weightHistoryRepository.save(weightEntry4a);
                weightHistoryRepository.save(weightEntry4b);
                weightHistoryRepository.save(weightEntry5a);

                // Load default exercises guide
                Category category1 = new Category("Calentamiento");
                Category category2 = new Category("Brazos");
                Category category3 = new Category("Colchoneta");
                Category category4 = new Category("Marcha y ciclismo");

                categoryRepository.save(category1);
                categoryRepository.save(category2);
                categoryRepository.save(category3);
                categoryRepository.save(category4);

                File warmUpExercisesFile = new File("src/main/resources/exercises-description/exercises-guide-1/warm-up-description.txt");
                File armExercisesFile = new File("src/main/resources/exercises-description/exercises-guide-1/arm-exercise-description.txt");
                File onMattressExercisesFile = new File("src/main/resources/exercises-description/exercises-guide-1/exercises-on-mattress-description.txt");
                File runningAndCyclingExercisesFile = new File("src/main/resources/exercises-description/exercises-guide-1/running-and-cycling-description.txt");

                BufferedReader warmUpExercisesBr = new BufferedReader(new FileReader(warmUpExercisesFile));
                BufferedReader armExercisesBr = new BufferedReader(new FileReader(armExercisesFile));
                BufferedReader onMattressExercisesBr = new BufferedReader(new FileReader(onMattressExercisesFile));
                BufferedReader runningAndCyclingExercisesBr = new BufferedReader(new FileReader(runningAndCyclingExercisesFile));

                List<Exercise> warmExercises = loadExercises(warmUpExercisesBr, category1, false, null);
                List<Exercise> armExercises = loadExercises(armExercisesBr, category2, false, null);
                List<Exercise> matExercises = loadExercises(onMattressExercisesBr, category3, false, null);
                List<Exercise> runExercises = loadExercises(runningAndCyclingExercisesBr, category4, false, null);

                // exercises -> routines -> template -> plan
                assert warmExercises != null;
                assert armExercises != null;
                assert matExercises != null;
                assert runExercises != null;
                // routine 1
                AssignedExercise assignedExercise1 = new AssignedExercise(warmExercises.get(0), 0, null, 8, 2);
                AssignedExercise assignedExercise2 = new AssignedExercise(warmExercises.get(1), 1, null, 6, 3);
                AssignedExercise assignedExercise3 = new AssignedExercise(warmExercises.get(2), 2, null, 5, 2);
                AssignedExercise assignedExercise4 = new AssignedExercise(warmExercises.get(3), 3, null, 15, 1);
                AssignedExercise assignedExercise5 = new AssignedExercise(armExercises.get(1), 4, null, 3, 4);
                AssignedExercise assignedExercise6 = new AssignedExercise(armExercises.get(2), 5, null, 4, 4);
                AssignedExercise assignedExercise7 = new AssignedExercise(matExercises.get(1), 6, null, 10, 2);
                AssignedExercise assignedExercise8 = new AssignedExercise(matExercises.get(3), 7, null, 10, 2);
                AssignedExercise assignedExercise9 = new AssignedExercise(runExercises.get(0), 8, 15, null, null);
                //routine 2
                AssignedExercise assignedExercise11 = new AssignedExercise(warmExercises.get(1), 0, null, 5, 3);
                AssignedExercise assignedExercise12 = new AssignedExercise(warmExercises.get(2), 1, null, 4, 2);
                AssignedExercise assignedExercise13 = new AssignedExercise(warmExercises.get(3), 2, null, 5, 3);
                AssignedExercise assignedExercise14 = new AssignedExercise(warmExercises.get(4), 3, null, 4, 1);
                AssignedExercise assignedExercise15 = new AssignedExercise(armExercises.get(3), 4, null, 2, 3);
                AssignedExercise assignedExercise16 = new AssignedExercise(armExercises.get(4), 5, null, 6, 3);
                AssignedExercise assignedExercise17 = new AssignedExercise(matExercises.get(2), 6, null, 8, 2);
                AssignedExercise assignedExercise18 = new AssignedExercise(matExercises.get(4), 7, null, 10, 1);
                AssignedExercise assignedExercise19 = new AssignedExercise(runExercises.get(0), 8, 20, null, null);
                //template routine
                AssignedExercise tempAssignedExercise1 = new AssignedExercise(warmExercises.get(0), 0, null, 8, 2);
                AssignedExercise tempAssignedExercise2 = new AssignedExercise(warmExercises.get(1), 1, null, 6, 3);
                AssignedExercise tempAssignedExercise3 = new AssignedExercise(warmExercises.get(2), 2, null, 5, 2);
                AssignedExercise tempAssignedExercise4 = new AssignedExercise(warmExercises.get(3), 3, null, 15, 1);
                AssignedExercise tempAssignedExercise5 = new AssignedExercise(armExercises.get(1), 4, null, 3, 4);
                AssignedExercise tempAssignedExercise6 = new AssignedExercise(armExercises.get(2), 5, null, 4, 4);
                AssignedExercise tempAssignedExercise7 = new AssignedExercise(matExercises.get(1), 6, null, 10, 2);
                AssignedExercise tempAssignedExercise8 = new AssignedExercise(matExercises.get(3), 7, null, 10, 2);
                AssignedExercise tempAssignedExercise9 = new AssignedExercise(runExercises.get(0), 8, 15, null, null);
                assignedExerciseRepository.save(assignedExercise1);
                assignedExerciseRepository.save(assignedExercise2);
                assignedExerciseRepository.save(assignedExercise3);
                assignedExerciseRepository.save(assignedExercise4);
                assignedExerciseRepository.save(assignedExercise5);
                assignedExerciseRepository.save(assignedExercise6);
                assignedExerciseRepository.save(assignedExercise7);
                assignedExerciseRepository.save(assignedExercise8);
                assignedExerciseRepository.save(assignedExercise9);
                assignedExerciseRepository.save(assignedExercise11);
                assignedExerciseRepository.save(assignedExercise12);
                assignedExerciseRepository.save(assignedExercise13);
                assignedExerciseRepository.save(assignedExercise14);
                assignedExerciseRepository.save(assignedExercise15);
                assignedExerciseRepository.save(assignedExercise16);
                assignedExerciseRepository.save(assignedExercise17);
                assignedExerciseRepository.save(assignedExercise18);
                assignedExerciseRepository.save(assignedExercise19);
                assignedExerciseRepository.save(tempAssignedExercise1);
                assignedExerciseRepository.save(tempAssignedExercise2);
                assignedExerciseRepository.save(tempAssignedExercise3);
                assignedExerciseRepository.save(tempAssignedExercise4);
                assignedExerciseRepository.save(tempAssignedExercise5);
                assignedExerciseRepository.save(tempAssignedExercise6);
                assignedExerciseRepository.save(tempAssignedExercise7);
                assignedExerciseRepository.save(tempAssignedExercise8);
                assignedExerciseRepository.save(tempAssignedExercise9);
                List<AssignedExercise> assignedExercises1 = new ArrayList<>();
                assignedExercises1.add(assignedExercise1);
                assignedExercises1.add(assignedExercise2);
                assignedExercises1.add(assignedExercise3);
                assignedExercises1.add(assignedExercise4);
                List<AssignedExercise> assignedExercises2 = new ArrayList<>();
                assignedExercises2.add(assignedExercise5);
                assignedExercises2.add(assignedExercise6);
                List<AssignedExercise> assignedExercises3 = new ArrayList<>();
                assignedExercises3.add(assignedExercise7);
                assignedExercises3.add(assignedExercise8);
                List<AssignedExercise> assignedExercises4 = new ArrayList<>();
                assignedExercises4.add(assignedExercise9);

                List<AssignedExercise> assignedExercises5 = new ArrayList<>();
                assignedExercises5.add(assignedExercise11);
                assignedExercises5.add(assignedExercise12);
                assignedExercises5.add(assignedExercise13);
                assignedExercises5.add(assignedExercise14);
                List<AssignedExercise> assignedExercises6 = new ArrayList<>();
                assignedExercises6.add(assignedExercise15);
                assignedExercises6.add(assignedExercise16);
                List<AssignedExercise> assignedExercises7 = new ArrayList<>();
                assignedExercises7.add(assignedExercise17);
                assignedExercises7.add(assignedExercise18);
                List<AssignedExercise> assignedExercises8 = new ArrayList<>();
                assignedExercises8.add(assignedExercise19);

                List<AssignedExercise> tempAssignedExercises1 = new ArrayList<>();
                tempAssignedExercises1.add(tempAssignedExercise1);
                tempAssignedExercises1.add(tempAssignedExercise2);
                tempAssignedExercises1.add(tempAssignedExercise3);
                tempAssignedExercises1.add(tempAssignedExercise4);
                List<AssignedExercise> tempAssignedExercises2 = new ArrayList<>();
                tempAssignedExercises2.add(tempAssignedExercise5);
                tempAssignedExercises2.add(tempAssignedExercise6);
                List<AssignedExercise> tempAssignedExercises3 = new ArrayList<>();
                tempAssignedExercises3.add(tempAssignedExercise7);
                tempAssignedExercises3.add(tempAssignedExercise8);
                List<AssignedExercise> tempAssignedExercises4 = new ArrayList<>();
                tempAssignedExercises4.add(tempAssignedExercise9);

                RoutineDivision routineDivision1 = new RoutineDivision("Calentamiento", 0, assignedExercises1);
                RoutineDivision routineDivision2 = new RoutineDivision("Brazos", 1, assignedExercises2);
                RoutineDivision routineDivision3 = new RoutineDivision("Colchoneta", 2, assignedExercises3);
                RoutineDivision routineDivision4 = new RoutineDivision("Marcha y ciclismo", 3, assignedExercises4);
                List<RoutineDivision> divisions1 = new ArrayList<>();
                divisions1.add(routineDivision1);
                divisions1.add(routineDivision2);
                divisions1.add(routineDivision3);
                divisions1.add(routineDivision4);
                routineDivisionRepository.save(routineDivision1);
                routineDivisionRepository.save(routineDivision2);
                routineDivisionRepository.save(routineDivision3);
                routineDivisionRepository.save(routineDivision4);

                RoutineDivision routineDivision5 = new RoutineDivision("Calentamiento", 0, assignedExercises5);
                RoutineDivision routineDivision6 = new RoutineDivision("Brazos", 1, assignedExercises6);
                RoutineDivision routineDivision7 = new RoutineDivision("Colchoneta", 2, assignedExercises7);
                RoutineDivision routineDivision8 = new RoutineDivision("Marcha y ciclismo", 3, assignedExercises8);
                List<RoutineDivision> divisions2 = new ArrayList<>();
                divisions2.add(routineDivision5);
                divisions2.add(routineDivision6);
                divisions2.add(routineDivision7);
                divisions2.add(routineDivision8);
                routineDivisionRepository.save(routineDivision5);
                routineDivisionRepository.save(routineDivision6);
                routineDivisionRepository.save(routineDivision7);
                routineDivisionRepository.save(routineDivision8);

                RoutineDivision tempRoutineDivision1 = new RoutineDivision("Calentamiento", 0, tempAssignedExercises1);
                RoutineDivision tempRoutineDivision2 = new RoutineDivision("Brazos", 1, tempAssignedExercises2);
                RoutineDivision tempRoutineDivision3 = new RoutineDivision("Colchoneta", 2, tempAssignedExercises3);
                RoutineDivision tempRoutineDivision4 = new RoutineDivision("Marcha y ciclismo", 3, tempAssignedExercises4);
                List<RoutineDivision> tempDivisions = new ArrayList<>();
                tempDivisions.add(tempRoutineDivision1);
                tempDivisions.add(tempRoutineDivision2);
                tempDivisions.add(tempRoutineDivision3);
                tempDivisions.add(tempRoutineDivision4);
                routineDivisionRepository.save(tempRoutineDivision1);
                routineDivisionRepository.save(tempRoutineDivision2);
                routineDivisionRepository.save(tempRoutineDivision3);
                routineDivisionRepository.save(tempRoutineDivision4);

                Routine routine1 = new Routine("Ejercicios Generales", "Ejercicios variados para cualquier tipo de recuperación", 4, RoutineFrequencyType.WEEKLY, 6, divisions1);
                routineRepository.save(routine1);
                Routine routine2 = new Routine("Ejercicios Variados", "Variedad de ejercicios, útil para casos generales", 4, RoutineFrequencyType.WEEKLY, 4, divisions2);
                routineRepository.save(routine2);
                Routine tempRoutine = new Routine("Ejercicios Generales", "Ejercicios variados para cualquier tipo de recuperación", 4, RoutineFrequencyType.WEEKLY, 6, tempDivisions);
                routineRepository.save(tempRoutine);
                List<Routine> routines1 = new ArrayList<>();
                List<Routine> routines2 = new ArrayList<>();
                List<Routine> routines3 = new ArrayList<>();
                List<Routine> routines4 = new ArrayList<>();
                List<Routine> routines5 = new ArrayList<>();
                routines1.add(routine1);
                routines3.add(routine2);
                List<Routine> tempRoutines = new ArrayList<>();
                tempRoutines.add(tempRoutine);

                PlanTemplate template1 = new PlanTemplate("Mis rutinas", medic1, tempRoutines);
                planTemplateRepository.save(template1);
                PlanTemplate template2 = new PlanTemplate("Rutinas de otros agentes", medic1);
                planTemplateRepository.save(template2);
                Plan plan1 = new Plan(routines1, pat1);
                planRepository.save(plan1);
                Plan plan2 = new Plan(routines2, pat2);
                planRepository.save(plan2);
                Plan plan3 = new Plan(routines3, pat3);
                planRepository.save(plan3);
                Plan plan4 = new Plan(routines4, pat4);
                planRepository.save(plan4);
                Plan plan5 = new Plan(routines5, pat5);
                planRepository.save(plan5);

                // assigned/activity exercises
                ActivityExercise activityExercise1 = new ActivityExercise(assignedExercise1, null, 6, 3);
                ActivityExercise activityExercise2 = new ActivityExercise(assignedExercise2, null, 4, 3);
                ActivityExercise activityExercise3 = new ActivityExercise(assignedExercise3, null, 5, 3);
                ActivityExercise activityExercise4 = new ActivityExercise(assignedExercise4, null, 5, 3);
                ActivityExercise activityExercise5 = new ActivityExercise(assignedExercise5, null, 3, 3);
                ActivityExercise activityExercise6 = new ActivityExercise(assignedExercise6, null, 4, 3);
                ActivityExercise activityExercise7 = new ActivityExercise(assignedExercise7, null, 8, 3);
                ActivityExercise activityExercise8 = new ActivityExercise(assignedExercise8, null, 8, 3);
                ActivityExercise activityExercise9 = new ActivityExercise(assignedExercise9, 15, null, null);
                activityExerciseRepository.save(activityExercise1);
                activityExerciseRepository.save(activityExercise2);
                activityExerciseRepository.save(activityExercise3);
                activityExerciseRepository.save(activityExercise4);
                activityExerciseRepository.save(activityExercise5);
                activityExerciseRepository.save(activityExercise6);
                activityExerciseRepository.save(activityExercise7);
                activityExerciseRepository.save(activityExercise8);
                activityExerciseRepository.save(activityExercise9);
                List<ActivityExercise> activityExercises = new ArrayList<>();
                activityExercises.add(activityExercise1);
                activityExercises.add(activityExercise2);
                activityExercises.add(activityExercise3);
                List<ActivityExercise> activityExercises1 = new ArrayList<>();
                activityExercises1.add(activityExercise4);
                activityExercises1.add(activityExercise5);
                activityExercises1.add(activityExercise6);
                List<ActivityExercise> activityExercises2 = new ArrayList<>();
                activityExercises2.add(activityExercise7);
                activityExercises2.add(activityExercise8);
                activityExercises2.add(activityExercise9);

                ActivityRoutine activityRoutine1 = new ActivityRoutine(routine1, pat1, 7, "Muy buena rutina", "Ningún incidente", LocalDateTime.of(2019, 10, 19, 18, 25, 10), activityExercises);
                ActivityRoutine activityRoutine2 = new ActivityRoutine(routine1, pat1, 4, "No me gusto mucho la rutina", null, LocalDateTime.of(2019, 10, 22, 11, 37, 40), activityExercises1);
                ActivityRoutine activityRoutine3 = new ActivityRoutine(routine1, pat1, 9, null, "Me canse mucho corriendo", LocalDateTime.of(2019, 10, 24, 15, 0, 20), activityExercises2);
                activityRoutineRepository.save(activityRoutine1);
                activityRoutineRepository.save(activityRoutine2);
                activityRoutineRepository.save(activityRoutine3);

                SensorData sensorData1 = new SensorData(activityExercise1, 70, LocalDateTime.of(2019, 10, 19, 18, 25, 15));
                SensorData sensorData2 = new SensorData(activityExercise1, 78, LocalDateTime.of(2019, 10, 19, 18, 25, 30));
                SensorData sensorData3 = new SensorData(activityExercise1, 73, LocalDateTime.of(2019, 10, 19, 18, 25, 40));
                SensorData sensorData4 = new SensorData(activityExercise1, 79, LocalDateTime.of(2019, 10, 19, 18, 26, 5));
                SensorData sensorData5 = new SensorData(activityExercise1, 84, LocalDateTime.of(2019, 10, 19, 18, 26, 27));
                SensorData sensorData6 = new SensorData(activityExercise2, 87, LocalDateTime.of(2019, 10, 19, 18, 26, 52));
                SensorData sensorData7 = new SensorData(activityExercise2, 89, LocalDateTime.of(2019, 10, 19, 18, 27, 13));
                SensorData sensorData8 = new SensorData(activityExercise2, 91, LocalDateTime.of(2019, 10, 19, 18, 27, 35));
                SensorData sensorData9 = new SensorData(activityExercise2, 96, LocalDateTime.of(2019, 10, 19, 18, 27, 58));
                SensorData sensorData10 = new SensorData(activityExercise2, 104, LocalDateTime.of(2019, 10, 19, 18, 28, 7));
                SensorData sensorData11 = new SensorData(activityExercise3, 100, LocalDateTime.of(2019, 10, 19, 18, 28, 28));
                SensorData sensorData12 = new SensorData(activityExercise3, 95, LocalDateTime.of(2019, 10, 19, 18, 28, 47));
                SensorData sensorData13 = new SensorData(activityExercise3, 90, LocalDateTime.of(2019, 10, 19, 18, 29, 2));
                SensorData sensorData14 = new SensorData(activityExercise3, 94, LocalDateTime.of(2019, 10, 19, 18, 29, 31));
                SensorData sensorData15 = new SensorData(activityExercise3, 99, LocalDateTime.of(2019, 10, 19, 18, 29, 54));

                SensorData sensorData16 = new SensorData(activityExercise4, 89, LocalDateTime.of(2019, 10, 22, 11, 37, 40));
                SensorData sensorData17 = new SensorData(activityExercise4, 84, LocalDateTime.of(2019, 10, 22, 11, 38, 5));
                SensorData sensorData18 = new SensorData(activityExercise4, 79, LocalDateTime.of(2019, 10, 22, 11, 38, 23));
                SensorData sensorData19 = new SensorData(activityExercise4, 82, LocalDateTime.of(2019, 10, 22, 11, 38, 56));
                SensorData sensorData20 = new SensorData(activityExercise4, 87, LocalDateTime.of(2019, 10, 22, 11, 39, 2));
                SensorData sensorData21 = new SensorData(activityExercise5, 93, LocalDateTime.of(2019, 10, 22, 11, 39, 34));
                SensorData sensorData22 = new SensorData(activityExercise5, 97, LocalDateTime.of(2019, 10, 22, 11, 39, 56));
                SensorData sensorData23 = new SensorData(activityExercise5, 95, LocalDateTime.of(2019, 10, 22, 11, 40, 13));
                SensorData sensorData24 = new SensorData(activityExercise5, 90, LocalDateTime.of(2019, 10, 22, 11, 40, 36));
                SensorData sensorData25 = new SensorData(activityExercise5, 88, LocalDateTime.of(2019, 10, 22, 11, 40, 59));
                SensorData sensorData26 = new SensorData(activityExercise6, 92, LocalDateTime.of(2019, 10, 22, 11, 41, 18));
                SensorData sensorData27 = new SensorData(activityExercise6, 96, LocalDateTime.of(2019, 10, 22, 11, 41, 40));
                SensorData sensorData28 = new SensorData(activityExercise6, 99, LocalDateTime.of(2019, 10, 22, 11, 41, 58));
                SensorData sensorData29 = new SensorData(activityExercise6, 103, LocalDateTime.of(2019, 10, 22, 11, 42, 10));
                SensorData sensorData30 = new SensorData(activityExercise6, 105, LocalDateTime.of(2019, 10, 22, 11, 42, 26));

                SensorData sensorData31 = new SensorData(activityExercise7, 89, LocalDateTime.of(2019, 10, 24, 15, 0, 25));
                SensorData sensorData32 = new SensorData(activityExercise7, 90, LocalDateTime.of(2019, 10, 24, 15, 0, 45));
                SensorData sensorData33 = new SensorData(activityExercise7, 88, LocalDateTime.of(2019, 10, 24, 15, 1, 5));
                SensorData sensorData34 = new SensorData(activityExercise7, 92, LocalDateTime.of(2019, 10, 24, 15, 1, 25));
                SensorData sensorData35 = new SensorData(activityExercise7, 93, LocalDateTime.of(2019, 10, 24, 15, 1, 45));
                SensorData sensorData36 = new SensorData(activityExercise8, 94, LocalDateTime.of(2019, 10, 24, 15, 2, 5));
                SensorData sensorData37 = new SensorData(activityExercise8, 90, LocalDateTime.of(2019, 10, 24, 15, 2, 25));
                SensorData sensorData38 = new SensorData(activityExercise8, 95, LocalDateTime.of(2019, 10, 24, 15, 2, 45));
                SensorData sensorData39 = new SensorData(activityExercise8, 92, LocalDateTime.of(2019, 10, 24, 15, 3, 5));
                SensorData sensorData40 = new SensorData(activityExercise8, 97, LocalDateTime.of(2019, 10, 24, 15, 3, 25));
                SensorData sensorData41 = new SensorData(activityExercise9, 105, LocalDateTime.of(2019, 10, 24, 15, 4, 14));
                SensorData sensorData42 = new SensorData(activityExercise9, 114, LocalDateTime.of(2019, 10, 24, 15, 8, 38));
                SensorData sensorData43 = new SensorData(activityExercise9, 128, LocalDateTime.of(2019, 10, 24, 15, 12, 6));
                SensorData sensorData44 = new SensorData(activityExercise9, 129, LocalDateTime.of(2019, 10, 24, 15, 16, 42));
                SensorData sensorData45 = new SensorData(activityExercise9, 132, LocalDateTime.of(2019, 10, 24, 15, 18, 29));
                sensorDataRepository.save(sensorData1);
                sensorDataRepository.save(sensorData2);
                sensorDataRepository.save(sensorData3);
                sensorDataRepository.save(sensorData4);
                sensorDataRepository.save(sensorData5);
                sensorDataRepository.save(sensorData6);
                sensorDataRepository.save(sensorData7);
                sensorDataRepository.save(sensorData8);
                sensorDataRepository.save(sensorData9);
                sensorDataRepository.save(sensorData10);
                sensorDataRepository.save(sensorData11);
                sensorDataRepository.save(sensorData12);
                sensorDataRepository.save(sensorData13);
                sensorDataRepository.save(sensorData14);
                sensorDataRepository.save(sensorData15);
                sensorDataRepository.save(sensorData16);
                sensorDataRepository.save(sensorData17);
                sensorDataRepository.save(sensorData18);
                sensorDataRepository.save(sensorData19);
                sensorDataRepository.save(sensorData20);
                sensorDataRepository.save(sensorData21);
                sensorDataRepository.save(sensorData22);
                sensorDataRepository.save(sensorData23);
                sensorDataRepository.save(sensorData24);
                sensorDataRepository.save(sensorData25);
                sensorDataRepository.save(sensorData26);
                sensorDataRepository.save(sensorData27);
                sensorDataRepository.save(sensorData28);
                sensorDataRepository.save(sensorData29);
                sensorDataRepository.save(sensorData30);
                sensorDataRepository.save(sensorData31);
                sensorDataRepository.save(sensorData32);
                sensorDataRepository.save(sensorData33);
                sensorDataRepository.save(sensorData34);
                sensorDataRepository.save(sensorData35);
                sensorDataRepository.save(sensorData36);
                sensorDataRepository.save(sensorData37);
                sensorDataRepository.save(sensorData38);
                sensorDataRepository.save(sensorData39);
                sensorDataRepository.save(sensorData40);
                sensorDataRepository.save(sensorData41);
                sensorDataRepository.save(sensorData42);
                sensorDataRepository.save(sensorData43);
                sensorDataRepository.save(sensorData44);
                sensorDataRepository.save(sensorData45);

            } catch (Exception ignored) {
            }
        }

    }

    /**
     * This method given a buffer reader, it loads all the exercises to the data base.
     */
    private List<Exercise> loadExercises(BufferedReader bufferedReader, Category category, boolean assignExercise, Patient patientToAssign) {
        String exercise;
        try {
            List<Exercise> result = new ArrayList<>();
            while ((exercise = bufferedReader.readLine()) != null) {
                String[] exerciseInfo = exercise.split("-/");
                String name = exerciseInfo[0];
                String[] descriptionSteps = exerciseInfo[1].split("-");
                String setsAndRepetition = exerciseInfo[2].split("-")[0];
                int sets = getExerciseSet(setsAndRepetition);
                int repetition = getExerciseRepetition(setsAndRepetition);
                //String routine = exerciseInfo[2].split("-")[1];
                String imageUrl = s3Service.getUrl("exercises/" + getImageName(name) + ".png");
                Exercise e = new Exercise(name, getExerciseDescription(descriptionSteps), imageUrl, category, null, repetition, sets);
                result.add(e);
                exerciseRepository.save(e);
                /*
                if(assignExercise){
                    AssignedExercise assignedE = new AssignedExercise(patientToAssign, e, 0, repetition, sets, 10);
                    assignedExerciseRepository.save(assignedE);
                }
                */
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method return an exercise description as a single string. This must be change
     * when exercise model have all the required fields.
     */
    private String getExerciseDescription(String[] steps) {
        StringBuilder description = null;

        for (String step : steps) {
            if (description == null) {
                description = new StringBuilder(step + " ");
            } else {
                description.append(step).append(" ");
            }
        }
        return description == null ? "" : description.toString();
    }


    /**
     * This method return a valid image name for s3 url. It replaces the spaces with '-' and strip all accents.
     */
    private String getImageName(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll(" ", "-");
    }

    /**
     * This method return an int set given a string containing exercise sets and repetitions
     */
    private int getExerciseSet(String setsAndRepetition) {
        for (int i = 0; i < setsAndRepetition.length(); i++) {
            if (setsAndRepetition.charAt(i) != ' ' && Character.isDigit(setsAndRepetition.charAt(i)))
                return Integer.parseInt(setsAndRepetition.substring(i, i + 1));
        }
        return -1;
    }

    /**
     * This method return an int repetition given a string containing exercise sets and repetitions
     */
    private int getExerciseRepetition(String setsAndRepetition) {
        boolean first = true;
        for (int i = 0; i < setsAndRepetition.length(); i++) {
            if (setsAndRepetition.charAt(i) != ' ' && Character.isDigit(setsAndRepetition.charAt(i))) {
                if (first) {
                    first = false;
                } else return Integer.parseInt(setsAndRepetition.substring(i, i + 1));
            }
        }
        return -1;
    }
}
